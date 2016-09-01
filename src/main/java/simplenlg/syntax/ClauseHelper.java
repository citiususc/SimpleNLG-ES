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
package simplenlg.syntax;

import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.phrasespec.VPPhraseSpec;

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
public abstract class ClauseHelper {

    protected PhraseHelper phraseHelper;
    protected VerbPhraseHelper verbPhraseHelper;

    public ClauseHelper(VerbPhraseHelper verbPhraseHelper, PhraseHelper phraseHelper) {
        this.verbPhraseHelper = verbPhraseHelper;
        this.phraseHelper = phraseHelper;
    }

    /**
     * The main method for controlling the syntax realisation of clauses.
     *
     * @param parent the parent <code>SyntaxProcessor</code> that called this
     *               method.
     * @param phrase the <code>PhraseElement</code> representation of the clause.
     * @return the <code>NLGElement</code> representing the realised clause.
     */
    public abstract NLGElement realise(SyntaxProcessor parent, PhraseElement phrase);

    /**
     * Adds the front modifiers to the end of the clause when dealing with
     * interrogatives.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     */
    protected void addInterrogativeFrontModifiers(PhraseElement phrase,
                                                  SyntaxProcessor parent,
                                                  ListElement realisedElement) {
        NLGElement currentElement = null;
        if (phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
            for (NLGElement subject : phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS)) {
                currentElement = parent.realise(subject);
                if (currentElement != null) {
                    currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.FRONT_MODIFIER);

                    realisedElement.addComponent(currentElement);
                }
            }
        }
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
    protected abstract void addPassiveSubjects(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement, NLGFactory phraseFactory);

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
    protected abstract void realiseVerb(PhraseElement phrase,
                                        SyntaxProcessor parent,
                                        ListElement realisedElement,
                                        NLGElement splitVerb,
                                        NLGElement verbElement,
                                        boolean whObj);

    /**
     * Ensures that the verb inherits the features from the clause.
     *
     * @param phrase      the <code>PhraseElement</code> representing this clause.
     * @param verbElement the <code>NLGElement</code> representing the verb phrase for
     *                    this clause.
     */
    protected void setVerbFeatures(PhraseElement phrase, NLGElement verbElement) {
        // this routine copies features from the clause to the VP.
        // it is disabled, as this copying is now done automatically
        // when features are set in SPhraseSpec
        // if (verbElement != null) {
        // verbElement.setFeature(Feature.INTERROGATIVE_TYPE, phrase
        // .getFeature(Feature.INTERROGATIVE_TYPE));
        // verbElement.setFeature(InternalFeature.COMPLEMENTS, phrase
        // .getFeature(InternalFeature.COMPLEMENTS));
        // verbElement.setFeature(InternalFeature.PREMODIFIERS, phrase
        // .getFeature(InternalFeature.PREMODIFIERS));
        // verbElement.setFeature(Feature.FORM, phrase
        // .getFeature(Feature.FORM));
        // verbElement.setFeature(Feature.MODAL, phrase
        // .getFeature(Feature.MODAL));
        // verbElement.setNegated(phrase.isNegated());
        // verbElement.setFeature(Feature.PASSIVE, phrase
        // .getFeature(Feature.PASSIVE));
        // verbElement.setFeature(Feature.PERFECT, phrase
        // .getFeature(Feature.PERFECT));
        // verbElement.setFeature(Feature.PROGRESSIVE, phrase
        // .getFeature(Feature.PROGRESSIVE));
        // verbElement.setTense(phrase.getTense());
        // verbElement.setFeature(Feature.FORM, phrase
        // .getFeature(Feature.FORM));
        // verbElement.setFeature(LexicalFeature.GENDER, phrase
        // .getFeature(LexicalFeature.GENDER));
        // }
    }

    /**
     * Realises the complements of passive clauses; also sets number, person for
     * passive
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param verbElement     the <code>NLGElement</code> representing the verb phrase for
     *                        this clause.
     */
    protected NLGElement addPassiveComplementsNumberPerson(PhraseElement phrase,
                                                           SyntaxProcessor parent,
                                                           ListElement realisedElement,
                                                           NLGElement verbElement) {
        Object passiveNumber = null;
        Object passivePerson = null;
        NLGElement currentElement = null;
        NLGElement splitVerb = null;
        NLGElement verbPhrase = phrase.getFeatureAsElement(InternalFeature.VERB_PHRASE);

        // count complements to set plural feature if more than one
        int numComps = 0;
        boolean coordSubj = false;

        if (phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue() && verbPhrase != null
                && !InterrogativeType.WHAT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {

            // complements of a clause are stored in the VPPhraseSpec
            for (NLGElement subject : verbPhrase.getFeatureAsElementList(InternalFeature.COMPLEMENTS)) {

                // AG: complement needn't be an NP
                // subject.isA(PhraseCategory.NOUN_PHRASE) &&
                if (DiscourseFunction.OBJECT.equals(subject.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {
                    subject.setFeature(Feature.PASSIVE, true);
                    numComps++;
                    currentElement = parent.realise(subject);

                    if (currentElement != null) {
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);

                        if (phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
                            splitVerb = currentElement;
                        } else {
                            realisedElement.addComponent(currentElement);
                        }
                    }

                    // flag if passive subject is coordinated with an "and"
                    if (!coordSubj && subject instanceof CoordinatedPhraseElement) {
                        String conj = ((CoordinatedPhraseElement) subject).getConjunction();
                        coordSubj = (conj != null && conj.equals("and"));
                    }

                    if (passiveNumber == null) {
                        passiveNumber = subject.getFeature(Feature.NUMBER);
                    } else {
                        passiveNumber = NumberAgreement.PLURAL;
                    }

                    if (Person.FIRST.equals(subject.getFeature(Feature.PERSON))) {
                        passivePerson = Person.FIRST;
                    } else if (Person.SECOND.equals(subject.getFeature(Feature.PERSON))
                            && !Person.FIRST.equals(passivePerson)) {
                        passivePerson = Person.SECOND;
                    } else if (passivePerson == null) {
                        passivePerson = Person.THIRD;
                    }

                    if (Form.GERUND.equals(phrase.getFeature(Feature.FORM))
                            && !phrase.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue()) {
                        subject.setFeature(Feature.POSSESSIVE, true);
                    }
                }
            }
        }

        if (verbElement != null) {
            if (passivePerson != null) {
                verbElement.setFeature(Feature.PERSON, passivePerson);
                // below commented out. for non-passive, number and person set
                // by checkSubjectNumberPerson
                // } else {
                // verbElement.setFeature(Feature.PERSON, phrase
                // .getFeature(Feature.PERSON));
            }

            if (numComps > 1 || coordSubj) {
                verbElement.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
            } else if (passiveNumber != null) {
                verbElement.setFeature(Feature.NUMBER, passiveNumber);
            }
        }
        return splitVerb;
    }

    /**
     * Adds the subjects to the beginning of the clause unless the clause is
     * infinitive, imperative or passive, or the subjects split the verb.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param splitVerb       an <code>NLGElement</code> representing the subjects that
     *                        should split the verb
     */
    protected void addSubjectsToFront(PhraseElement phrase,
                                      SyntaxProcessor parent,
                                      ListElement realisedElement,
                                      NLGElement splitVerb) {
        if (!Form.INFINITIVE.equals(phrase.getFeature(Feature.FORM))
                && !Form.IMPERATIVE.equals(phrase.getFeature(Feature.FORM))
                && !phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue() && splitVerb == null) {
            realisedElement.addComponents(realiseSubjects(phrase, parent).getChildren());
        }
    }

    /**
     * Realises the subjects for the clause.
     *
     * @param phrase the <code>PhraseElement</code> representing this clause.
     * @param parent the parent <code>SyntaxProcessor</code> that will do the
     *               realisation of the complementiser.
     */
    protected ListElement realiseSubjects(PhraseElement phrase, SyntaxProcessor parent) {

        NLGElement currentElement = null;
        ListElement realisedElement = new ListElement();

        for (NLGElement subject : phrase.getFeatureAsElementList(InternalFeature.SUBJECTS)) {

            subject.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.SUBJECT);
            if (Form.GERUND.equals(phrase.getFeature(Feature.FORM))
                    && !phrase.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue()) {
                subject.setFeature(Feature.POSSESSIVE, true);
            }
            currentElement = parent.realise(subject);
            if (currentElement != null) {
                realisedElement.addComponent(currentElement);
            }
        }
        return realisedElement;
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
    protected abstract NLGElement realiseInterrogative(PhraseElement phrase,
                                                       SyntaxProcessor parent,
                                                       ListElement realisedElement,
                                                       NLGFactory phraseFactory,
                                                       NLGElement verbElement);

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
    protected abstract NLGElement realiseObjectWHInterrogative(String keyword,
                                                               PhraseElement phrase,
                                                               SyntaxProcessor parent,
                                                               ListElement realisedElement,
                                                               NLGFactory phraseFactory);

    /**
     * Adds a <em>do</em> verb to the realisation of this clause.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     */
    protected void addDoAuxiliary(PhraseElement phrase,
                                  SyntaxProcessor parent,
                                  NLGFactory phraseFactory,
                                  ListElement realisedElement) {

        PhraseElement doPhrase = phraseFactory.createVerbPhrase("do"); //$NON-NLS-1$
        doPhrase.setFeature(Feature.TENSE, phrase.getFeature(Feature.TENSE));
        doPhrase.setFeature(Feature.PERSON, phrase.getFeature(Feature.PERSON));
        doPhrase.setFeature(Feature.NUMBER, phrase.getFeature(Feature.NUMBER));
        realisedElement.addComponent(parent.realise(doPhrase));
    }

    /**
     * Realises the key word of the interrogative. For example, <em>who</em>,
     * <em>what</em>
     *
     * @param keyWord         the key word of the interrogative.
     * @param cat             the category (usually pronoun, but not in the case of
     *                        "how many")
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     * @param phraseFactory   the phrase factory to be used.
     */
    protected void realiseInterrogativeKeyWord(String keyWord,
                                               LexicalCategory cat,
                                               PhraseElement phrase,
                                               SyntaxProcessor parent,
                                               ListElement realisedElement,
                                               NLGFactory phraseFactory) {

        if (keyWord != null) {
            NLGElement question = phraseFactory.createWord(keyWord, cat);
            VPPhraseSpec verbPhrase = (VPPhraseSpec) phrase.getFeature(InternalFeature.VERB_PHRASE);
            if (verbPhrase != null) {
                if (verbPhrase.hasFeature(Feature.NUMBER)) {
                    question.setFeature(Feature.NUMBER, verbPhrase.getFeature(Feature.NUMBER));
                }
                if (verbPhrase.hasFeature(LexicalFeature.GENDER)) {
                    question.setFeature(LexicalFeature.GENDER, verbPhrase.getFeature(LexicalFeature.GENDER));
                }
            }
            NLGElement currentElement = parent.realise(question);

            if (currentElement != null) {
                realisedElement.addComponent(currentElement);
            }
        }
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
    protected abstract NLGElement realiseYesNo(PhraseElement phrase,
                                               SyntaxProcessor parent,
                                               NLGElement verbElement,
                                               NLGFactory phraseFactory,
                                               ListElement realisedElement);

    /**
     * Realises the cue phrase for the clause if it exists.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     */
    protected void addCuePhrase(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement) {

        NLGElement currentElement = parent.realise(phrase.getFeatureAsElement(Feature.CUE_PHRASE));

        if (currentElement != null) {
            currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.CUE_PHRASE);
            realisedElement.addComponent(currentElement);
        }
    }

    /**
     * Checks to see if this clause is a subordinate clause. If it is then the
     * complementiser is added as a component to the realised element
     * <b>unless</b> the complementiser has been suppressed.
     *
     * @param phrase          the <code>PhraseElement</code> representing this clause.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the clause.
     */
    protected void addComplementiser(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement) {

        NLGElement currentElement;

        if ((ClauseStatus.SUBORDINATE.equals(phrase.getFeature(InternalFeature.CLAUSE_STATUS)) ||
                DiscourseFunction.SUBJECT.equals(phrase.getFeature(InternalFeature.DISCOURSE_FUNCTION)))
                && !phrase.getFeatureAsBoolean(Feature.SUPRESSED_COMPLEMENTISER).booleanValue()) {

            currentElement = parent.realise(phrase.getFeatureAsElement(Feature.COMPLEMENTISER));

            if (currentElement != null) {
                realisedElement.addComponent(currentElement);
            }
        }
    }

    /**
     * Copies the front modifiers of the clause to the list of post-modifiers of
     * the verb only if the phrase has infinitive form.
     *
     * @param phrase      the <code>PhraseElement</code> representing this clause.
     * @param verbElement the <code>NLGElement</code> representing the verb phrase for
     *                    this clause.
     */
    protected void copyFrontModifiers(PhraseElement phrase, NLGElement verbElement) {
        List<NLGElement> frontModifiers = phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS);
        Object clauseForm = phrase.getFeature(Feature.FORM);

        // bug fix by Chris Howell (Agfa) -- do not overwrite existing post-mods
        // in the VP
        if (verbElement != null) {
            List<NLGElement> phrasePostModifiers = phrase.getFeatureAsElementList(InternalFeature.POSTMODIFIERS);

            if (verbElement instanceof PhraseElement) {
                List<NLGElement> verbPostModifiers = verbElement.getFeatureAsElementList(InternalFeature.POSTMODIFIERS);

                for (NLGElement eachModifier : phrasePostModifiers) {

                    // need to check that VP doesn't already contain the
                    // post-modifier
                    // this only happens if the phrase has already been realised
                    // and later modified, with realiser called again. In that
                    // case, postmods will be copied over twice
                    if (!verbPostModifiers.contains(eachModifier)) {
                        ((PhraseElement) verbElement).addPostModifier(eachModifier);
                    }
                }
            }
        }

        // if (verbElement != null) {
        // verbElement.setFeature(InternalFeature.POSTMODIFIERS, phrase
        // .getFeature(InternalFeature.POSTMODIFIERS));
        // }

        if (Form.INFINITIVE.equals(clauseForm) || Form.SUBJUNCTIVE.equals(clauseForm)) {
            if (Form.INFINITIVE.equals(clauseForm))
                phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);

            for (NLGElement eachModifier : frontModifiers) {
                if (verbElement instanceof PhraseElement) {
                    ((PhraseElement) verbElement).addPostModifier(eachModifier);
                }
            }
            phrase.removeFeature(InternalFeature.FRONT_MODIFIERS);
            if (verbElement != null) {
                verbElement.setFeature(InternalFeature.NON_MORPH, true);
            }
        }
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
    protected abstract void checkDiscourseFunction(PhraseElement phrase);

    /**
     * Checks the subjects of the phrase to determine if there is more than one
     * subject. This ensures that the verb phrase is correctly set. Also set
     * person correctly
     *
     * @param phrase      the <code>PhraseElement</code> representing this clause.
     * @param verbElement the <code>NLGElement</code> representing the verb phrase for
     *                    this clause.
     */
    protected abstract void checkSubjectNumberPerson(PhraseElement phrase, NLGElement verbElement);
}
