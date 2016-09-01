/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.syntax.spanish;

import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.syntax.SyntaxProcessor;

import java.util.List;

/**
 * <p>
 * This is a helper class containing the main methods for realising the syntax
 * of clauses. It is used exclusively by the <code>SyntaxProcessor</code>.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
class ClauseHelper extends simplenlg.syntax.ClauseHelper {

    public ClauseHelper() {
        super(new VerbPhraseHelper(), new PhraseHelper());
    }

    /**
     * The main method for controlling the syntax realisation of clauses.
     *
     * @param parent the parent <code>SyntaxProcessor</code> that called this
     *               method.
     * @param phrase the <code>PhraseElement</code> representation of the clause.
     * @return the <code>NLGElement</code> representing the realised clause.
     */
    public NLGElement realise(SyntaxProcessor parent, PhraseElement phrase) {
        ListElement realisedElement = null;
        NLGFactory phraseFactory = phrase.getFactory();
        NLGElement splitVerb = null;
        boolean interrogObj = false;

        if (phrase != null) {
            realisedElement = new ListElement();
            NLGElement verbElement = phrase.getFeatureAsElement(InternalFeature.VERB_PHRASE);

            if (verbElement == null) {
                verbElement = phrase.getHead();
            }

            checkSubjectNumberPerson(phrase, verbElement);
            checkDiscourseFunction(phrase);
            copyFrontModifiers(phrase, verbElement);
            if (DiscourseFunction.SUBJECT.equals(phrase.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {
                addCuePhrase(phrase, parent, realisedElement);
                addComplementiser(phrase, parent, realisedElement);
            } else {
                addComplementiser(phrase, parent, realisedElement);
                addCuePhrase(phrase, parent, realisedElement);
            }

            if (phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
                Object inter = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
                interrogObj = (InterrogativeType.WHAT_OBJECT.equals(inter)
                        || InterrogativeType.WHO_OBJECT.equals(inter)
                        || InterrogativeType.HOW_PREDICATE.equals(inter) || InterrogativeType.HOW.equals(inter)
                        || InterrogativeType.WHY.equals(inter) || InterrogativeType.WHERE.equals(inter));
                splitVerb = realiseInterrogative(phrase, parent, realisedElement, phraseFactory, verbElement);
            } else {
                phraseHelper.realiseList(parent,
                        realisedElement,
                        phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS),
                        DiscourseFunction.FRONT_MODIFIER);
            }

            addSubjectsToFront(phrase, parent, realisedElement, splitVerb);

            NLGElement passiveSplitVerb = addPassiveComplementsNumberPerson(phrase,
                    parent,
                    realisedElement,
                    verbElement);

            if (passiveSplitVerb != null) {
                splitVerb = passiveSplitVerb;
            }

            // realise verb needs to know if clause is object interrogative
            realiseVerb(phrase, parent, realisedElement, splitVerb, verbElement, interrogObj);
            addPassiveSubjects(phrase, parent, realisedElement, phraseFactory);
            addInterrogativeFrontModifiers(phrase, parent, realisedElement);
        }
        return realisedElement;
    }

    /**
     * Checks the discourse function of the clause and alters the form of the
     * clause as necessary. The following algorithm is used: <br>
     * <p>
     * <pre>
     * If the clause represents a direct or indirect object then
     *      If form is currently Imperative then
     *           Set form to Infinitive
     *           Suppress the complementiser
     *      If form is currently Gerund and there are no subjects
     *      	 Suppress the complementiser
     * If the clause represents a subject then
     *      Set the form to be Gerund
     *      Suppress the complementiser
     * </pre>
     *
     * @param phrase the <code>PhraseElement</code> representing this clause.
     */
    protected void checkDiscourseFunction(PhraseElement phrase) {
        List<NLGElement> subjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);
        Object clauseForm = phrase.getFeature(Feature.FORM);
        Object discourseValue = phrase.getFeature(InternalFeature.DISCOURSE_FUNCTION);

        if (DiscourseFunction.OBJECT.equals(discourseValue) || DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {

            if (Form.IMPERATIVE.equals(clauseForm)) {
//                phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
//                phrase.setFeature(Feature.FORM, Form.INFINITIVE);
                phrase.setFeature(Feature.FORM, Form.SUBJUNCTIVE);
                phrase.setFeature(Feature.TENSE, Tense.PRESENT);
            } else if (Form.GERUND.equals(clauseForm) && subjects.size() == 0) {
                phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
            }
        } else if (DiscourseFunction.SUBJECT.equals(discourseValue) && !Form.SUBJUNCTIVE.equals(clauseForm)) {
//            phrase.setFeature(Feature.FORM, Form.GERUND);
//            phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
            phrase.setFeature(Feature.FORM, Form.SUBJUNCTIVE);
            phrase.setFeature(Feature.TENSE, Tense.PRESENT);
//            List<NLGElement> modifiers = phrase.getFrontModifiers();
//            modifiers.add(0, phrase.getFactory().getLexicon().getDefaultComplementiser());
//            phrase.setFeature(InternalFeature.FRONT_MODIFIERS, modifiers);
        }
    }

    /**
     * Checks the subjects of the phrase to determine if there is more than one
     * subject. This ensures that the verb phrase is correctly set. Also set
     * person correctly
     *
     * @param phrase      the <code>PhraseElement</code> representing this clause.
     * @param verbElement the <code>NLGElement</code> representing the verb phrase for
     *                    this clause.
     */
    @Override
    protected void checkSubjectNumberPerson(PhraseElement phrase, NLGElement verbElement) {
        NLGElement currentElement = null;
        List<NLGElement> subjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);
        boolean pluralSubjects = false;
        Person person = null;

        if (subjects != null) {
            switch (subjects.size()) {
                case 0:
                    break;

                case 1:
                    currentElement = subjects.get(0);
                    // coordinated NP with "and" are plural (not coordinated NP with
                    // "or")
                    if (currentElement instanceof CoordinatedPhraseElement
                            && ((CoordinatedPhraseElement) currentElement).checkIfPlural())
                        pluralSubjects = true;
                    else if ((currentElement.getFeature(Feature.NUMBER) == NumberAgreement.PLURAL)
                            && !(currentElement instanceof SPhraseSpec)) // ER mod-
                        // clauses
                        // are
                        // singular
                        // as
                        // NPs,
                        // even
                        // if
                        // they
                        // are
                        // plural
                        // internally
                        pluralSubjects = true;
                    if (currentElement.isA(PhraseCategory.NOUN_PHRASE) && !(currentElement instanceof CoordinatedPhraseElement)) {
                        NLGElement currentHead = currentElement.getFeatureAsElement(InternalFeature.HEAD);
                        person = (Person) currentElement.getFeature(Feature.PERSON);

                        if (currentHead == null) {
                            // subject is null and therefore is not gonna be plural
                            pluralSubjects = false;
                        } else if ((currentHead.getFeature(Feature.NUMBER) == NumberAgreement.PLURAL))
                            pluralSubjects = true;
                        else if (currentHead instanceof ListElement) {
                            pluralSubjects = true;
                        /*
                         * } else if (currentElement instanceof
						 * CoordinatedPhraseElement &&
						 * "and".equals(currentElement.getFeatureAsString(
						 * //$NON-NLS-1$ Feature.CONJUNCTION))) { pluralSubjects
						 * = true;
						 */
                        }
                    } else if (currentElement instanceof CoordinatedPhraseElement) {
                        List<NLGElement> children = currentElement.getChildren();
                        for (NLGElement child : children) {
                            Person newPerson = (Person) child.getFeature(Feature.PERSON);
                            if (person != null && newPerson != null) {
                                if (person.ordinal() > newPerson.ordinal()) {
                                    person = newPerson;
                                }
                            } else if (newPerson != null) {
                                person = newPerson;
                            }


                        }
                    }
                    break;

                default:
                    pluralSubjects = true;
                    break;
            }
        }
        if (verbElement != null) {
            verbElement.setFeature(Feature.NUMBER, pluralSubjects ? NumberAgreement.PLURAL
                    : phrase.getFeature(Feature.NUMBER));
            if (person != null)
                verbElement.setFeature(Feature.PERSON, person);
            if (verbElement.getFeatureAsBoolean(Feature.PERFECT)) {
                NLGElement head = (NLGElement) verbElement.getFeature(InternalFeature.HEAD);
                if (head instanceof CoordinatedPhraseElement) {
                    List<NLGElement> coordinates = head.getChildren();
                    for (NLGElement coordinate : coordinates) {
                        head = (NLGElement) coordinate.getFeature(InternalFeature.HEAD);
                        if (head != null) {
                            head.setFeature(Feature.PERFECT, true);
                        }
                    }
                } else {
                    head.setFeature(Feature.PERFECT, true);
                }
            }
        }
        if ((subjects == null || subjects.isEmpty()) && (verbElement.hasFeature(InternalFeature.HEAD) &&
                ((WordElement) verbElement.getFeature(InternalFeature.HEAD)).getBaseForm().equals("haber") &&
                Person.THIRD.equals(verbElement.getFeature(Feature.PERSON)) && Tense.PRESENT.equals(verbElement.getFeature(Feature.TENSE)) &&
                !verbElement.getFeatureAsBoolean(Feature.PERFECT))) {
            verbElement.setFeature(Feature.PERSON, Person.NONE);
        }
    }

    /**
     * Realises the verb part of the clause.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param splitVerb       an <code>NLGElement</code> representing the subjects that
     *                        should split the verb
     * @param verbElement     the <code>NLGElement</code> representing the verb phrase for
     *                        this clause.
     * @param whObj           whether the VP is part of an object WH-interrogative
     */
    @Override
    protected void realiseVerb(PhraseElement phrase,
                               simplenlg.syntax.SyntaxProcessor parent,
                               ListElement realisedElement,
                               NLGElement splitVerb,
                               NLGElement verbElement,
                               boolean whObj) {

        setVerbFeatures(phrase, verbElement);

        NLGElement currentElement = parent.realise(verbElement);
        if (currentElement != null) {
            if (splitVerb == null) {
                currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

                realisedElement.addComponent(currentElement);

            } else {
                if (currentElement instanceof ListElement) {
                    List<NLGElement> children = currentElement.getChildren();
                    Boolean foundVerb = false;
                    int i;
                    for (i = 0; i < children.size(); i++) {
                        currentElement = children.get(i);
                        if (foundVerb && !(LexicalCategory.VERB.equals(currentElement.getCategory()) || LexicalCategory.MODAL.equals(currentElement.getCategory()))) {
                            break;
                        }
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);
                        realisedElement.addComponent(currentElement);
                        if (LexicalCategory.VERB.equals(currentElement.getCategory()) || LexicalCategory.MODAL.equals(currentElement.getCategory())) {
                            foundVerb = true;
                            if (!whObj) {
                                i++;
                                break;
                            }
                        }
                    }
                    realisedElement.addComponent(splitVerb);

                    for (int eachChild = i; eachChild < children.size(); eachChild++) {
                        currentElement = children.get(eachChild);
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);
                        realisedElement.addComponent(currentElement);
                    }
                } else {
                    currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

                    if (whObj) {
                        realisedElement.addComponent(currentElement);
                        realisedElement.addComponent(splitVerb);
                    } else {
                        realisedElement.addComponent(splitVerb);
                        realisedElement.addComponent(currentElement);
                    }
                }
            }
        }
    }

    /**
     * This is the main controlling method for handling interrogative clauses.
     * The actual steps taken are dependent on the type of question being asked.
     * The method also determines if there is a subject that will split the verb
     * group of the clause. For example, the clause
     * <em>the man <b>should give</b> the woman the flower</em> has the verb
     * group indicated in <b>bold</b>. The phrase is rearranged as yes/no
     * question as
     * <em><b>should</b> the man <b>give</b> the woman the flower</em> with the
     * subject <em>the man</em> splitting the verb group.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     * @param verbElement     the <code>NLGElement</code> representing the verb phrase for
     *                        this clause.
     * @return an <code>NLGElement</code> representing a subject that should
     * split the verb
     */
    @Override
    protected NLGElement realiseInterrogative(PhraseElement phrase,
                                              SyntaxProcessor parent,
                                              ListElement realisedElement,
                                              NLGFactory phraseFactory,
                                              NLGElement verbElement) {
        NLGElement splitVerb = null;

        if (phrase.getParent() != null) {
            phrase.getParent().setFeature(InternalFeature.INTERROGATIVE, true);
        }

        Object type = phrase.getFeature(Feature.INTERROGATIVE_TYPE);

        if (type instanceof InterrogativeType) {
            switch ((InterrogativeType) type) {
                case YES_NO:
                    splitVerb = realiseYesNo(phrase, parent, verbElement, phraseFactory, realisedElement);
                    break;

                case WHO_SUBJECT:
                case WHAT_SUBJECT:
                    if (phrase.getFeatureAsBoolean(Feature.PASSIVE)) {
                        realisedElement.addComponent(parent.realise(phraseFactory.getLexicon().getPassivePreposition()));
                    }
                    realiseInterrogativeKeyWord(parent.getLexicon().getInterrogativeTypeString((InterrogativeType) type),
                            LexicalCategory.PRONOUN, phrase, parent, realisedElement, phraseFactory);
                    phrase.removeFeature(InternalFeature.SUBJECTS);
                    break;

                case HOW_MANY:
                    realiseInterrogativeKeyWord(parent.getLexicon().getInterrogativeTypeString((InterrogativeType) type),
                            LexicalCategory.PRONOUN, phrase, parent, realisedElement, phraseFactory);
                    break;

                case HOW:
                case WHY:
                case WHERE:
                case WHO_OBJECT:
                case WHO_INDIRECT_OBJECT:
                case WHAT_OBJECT:
                    splitVerb = realiseObjectWHInterrogative((InterrogativeType) type, phrase, parent, realisedElement, phraseFactory);
                    break;

                case HOW_PREDICATE:
                    splitVerb = realiseObjectWHInterrogative((InterrogativeType) type, phrase, parent, realisedElement, phraseFactory);
                    break;

                default:
                    break;
            }
        }

        return splitVerb;
    }

    /**
     * Performs the realisation for YES/NO types of questions. This may involve
     * adding an optional <em>do</em> auxiliary verb to the beginning of the
     * clause. The method also determines if there is a subject that will split
     * the verb group of the clause. For example, the clause
     * <em>the man <b>should give</b> the woman the flower</em> has the verb
     * group indicated in <b>bold</b>. The phrase is rearranged as yes/no
     * question as
     * <em><b>should</b> the man <b>give</b> the woman the flower</em> with the
     * subject <em>the man</em> splitting the verb group.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     * @param verbElement     the <code>NLGElement</code> representing the verb phrase for
     *                        this clause.
     * @return an <code>NLGElement</code> representing a subject that should
     * split the verb
     */
    @Override
    protected NLGElement realiseYesNo(PhraseElement phrase,
                                      simplenlg.syntax.SyntaxProcessor parent,
                                      NLGElement verbElement,
                                      NLGFactory phraseFactory,
                                      ListElement realisedElement) {

        NLGElement splitVerb = realiseSubjects(phrase, parent);

//        if (!(verbElement instanceof VPPhraseSpec && verbPhraseHelper.isCopular(((VPPhraseSpec) verbElement).getVerb()))
//                && !phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue() && !phrase.hasFeature(Feature.MODAL)
//                && !Tense.FUTURE.equals(phrase.getFeature(Feature.TENSE))
//                && !phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue()
//                && !phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
//            addDoAuxiliary(phrase, parent, phraseFactory, realisedElement);
//        } else {
//            splitVerb = realiseSubjects(phrase, parent);
//        }

//        NLGElement verb = verbElement.getFeatureAsElement(InternalFeature.HEAD);
//        realisedElement.addComponent(parent.realise(verb));
//        verb.setFeature(Feature.ELIDED, true);
        return splitVerb;
    }

    /**
     * Controls the realisation of <em>wh</em> object questions.
     *
     * @param keyword         the wh word
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     * @return an <code>NLGElement</code> representing a subject that should
     * split the verb
     */
    @Override
    protected NLGElement realiseObjectWHInterrogative(String keyword,
                                                      PhraseElement phrase,
                                                      SyntaxProcessor parent,
                                                      ListElement realisedElement,
                                                      NLGFactory phraseFactory) {
        NLGElement splitVerb = null;
        realiseInterrogativeKeyWord(keyword, LexicalCategory.PRONOUN, phrase, parent, realisedElement, //$NON-NLS-1$
                phraseFactory);

        if (!phrase.getFeatureAsBoolean(Feature.PASSIVE)) {
            splitVerb = realiseSubjects(phrase, parent);
        }

        return splitVerb;
    }

    /**
     * Controls the realisation of <em>wh</em> object questions.
     *
     * @param type            the interrogative type
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     * @return an <code>NLGElement</code> representing a subject that should
     * split the verb
     */
    private NLGElement realiseObjectWHInterrogative(InterrogativeType type,
                                                    PhraseElement phrase,
                                                    SyntaxProcessor parent,
                                                    ListElement realisedElement,
                                                    NLGFactory phraseFactory) {

        NLGElement object = null;
        VPPhraseSpec verbPhrase = (VPPhraseSpec) phrase.getFeature(InternalFeature.VERB_PHRASE);
        if (verbPhrase != null) {
            switch (type) {
                case WHAT_OBJECT:
                case WHO_OBJECT:
                    object = verbPhrase.getObject();
                    break;
                case WHO_INDIRECT_OBJECT:
                    object = verbPhrase.getIndirectObject();
                    break;
            }
            if (object != null) {
                if (PhraseCategory.PREPOSITIONAL_PHRASE.equals(object.getCategory())) {
                    NLGElement preposition = ((PPPhraseSpec) object).getPreposition();
                    realisedElement.addComponent(parent.realise(preposition));
                } else if (type == InterrogativeType.WHO_INDIRECT_OBJECT) {
                    NLGElement preposition = phraseFactory.createWord("a", LexicalCategory.PREPOSITION);
                    realisedElement.addComponent(parent.realise(preposition));
                }
            }
        }

        return realiseObjectWHInterrogative(parent.getLexicon().getInterrogativeTypeString(type),
                phrase, parent, realisedElement, phraseFactory);
    }

    /**
     * Realises the subjects of a passive clause.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     */
    @Override
    protected void addPassiveSubjects(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement, NLGFactory phraseFactory) {
        NLGElement currentElement = null;

        if (phrase.getFeatureAsBoolean(Feature.PASSIVE)) {
            List<NLGElement> allSubjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);

            if (allSubjects.size() > 0) {
                realisedElement.addComponent(parent.realise(phraseFactory.createPrepositionPhrase(phraseFactory.getLexicon().getPassivePreposition())));
            }

            for (NLGElement subject : allSubjects) {

                subject.setFeature(Feature.PASSIVE, true);
                if (subject.isA(PhraseCategory.NOUN_PHRASE) || subject instanceof CoordinatedPhraseElement) {
                    currentElement = parent.realise(subject);
                    if (currentElement != null) {
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.SUBJECT);
                        realisedElement.addComponent(currentElement);
                    }
                }
            }
        }
    }
}
