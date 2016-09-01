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
package simplenlg.syntax.english;

import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.syntax.SyntaxProcessor;

import java.util.Stack;

/**
 * <p>
 * This class contains static methods to help the syntax processor realise verb
 * phrases. It adds auxiliary verbs into the element tree as required.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
class VerbPhraseHelper extends simplenlg.syntax.VerbPhraseHelper {

    public VerbPhraseHelper() {
        super(new PhraseHelper());
    }

    /**
     * Realises the complements of this phrase.
     *
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param phrase          the <code>PhraseElement</code> representing this noun phrase.
     * @param realisedElement the current realisation of the noun phrase.
     */
    protected void realiseComplements(simplenlg.syntax.SyntaxProcessor parent, PhraseElement phrase, ListElement realisedElement) {

        ListElement indirects = new ListElement();
        ListElement directs = new ListElement();
        ListElement unknowns = new ListElement();
        Object discourseValue = null;
        NLGElement currentElement = null;

        for (NLGElement complement : phrase
                .getFeatureAsElementList(InternalFeature.COMPLEMENTS)) {

            discourseValue = complement
                    .getFeature(InternalFeature.DISCOURSE_FUNCTION);
            currentElement = parent.realise(complement);
            if (currentElement != null) {
                currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                        DiscourseFunction.COMPLEMENT);

                if (DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {
                    indirects.addComponent(currentElement);
                } else if (DiscourseFunction.OBJECT.equals(discourseValue)) {
                    directs.addComponent(currentElement);
                } else {
                    unknowns.addComponent(currentElement);
                }
            }
        }
        if (!InterrogativeType.isIndirectObject(phrase
                .getFeature(Feature.INTERROGATIVE_TYPE))) {
            realisedElement.addComponents(indirects.getChildren());
        }
        if (!phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
            if (!InterrogativeType.isObject(phrase
                    .getFeature(Feature.INTERROGATIVE_TYPE))) {
                realisedElement.addComponents(directs.getChildren());
            }
            realisedElement.addComponents(unknowns.getChildren());
        }
    }

    /**
     * Splits the stack of verb components into two sections. One being the verb
     * associated with the main verb group, the other being associated with the
     * auxiliary verb group.
     *
     * @param vgComponents         the stack of verb components in the verb group.
     * @param mainVerbRealisation  the main group of verbs.
     * @param auxiliaryRealisation the auxiliary group of verbs.
     */
    protected void splitVerbGroup(Stack<NLGElement> vgComponents,
                                  Stack<NLGElement> mainVerbRealisation,
                                  Stack<NLGElement> auxiliaryRealisation) {

        boolean mainVerbSeen = false;

        for (NLGElement word : vgComponents) {
            if (!mainVerbSeen) {
                mainVerbRealisation.push(word);
                if (!word.equals("not")) { //$NON-NLS-1$
                    mainVerbSeen = true;
                }
            } else {
                auxiliaryRealisation.push(word);
            }
        }
    }

    /**
     * Creates a stack of verbs for the verb phrase. Additional auxiliary verbs
     * are added as required based on the features of the verb phrase.
     *
     * @param parent the parent <code>SyntaxProcessor</code> that will do the
     *               realisation of the complementiser.
     * @param phrase the <code>PhraseElement</code> representing this noun phrase.
     * @return the verb group as a <code>Stack</code> of <code>NLGElement</code>
     * s.
     */
    protected Stack<NLGElement> createVerbGroup(SyntaxProcessor parent, PhraseElement phrase) {

        String actualModal = null;
        Object formValue = phrase.getFeature(Feature.FORM);
        Tense tenseValue = (Tense) phrase.getFeature(Feature.TENSE);
        String modal = phrase.getFeatureAsString(Feature.MODAL);
        boolean modalPast = false;
        Stack<NLGElement> vgComponents = new Stack<NLGElement>();
        boolean interrogative = phrase.hasFeature(Feature.INTERROGATIVE_TYPE);

        if (Form.GERUND.equals(formValue) || Form.INFINITIVE.equals(formValue)) {
            tenseValue = Tense.PRESENT;
        }

        if (Form.INFINITIVE.equals(formValue)) {
            actualModal = "to"; //$NON-NLS-1$

        } else if (formValue == null || Form.NORMAL.equals(formValue)) {
            if (Tense.FUTURE.equals(tenseValue)
                    && modal == null
                    && ((!(phrase.getHead() instanceof CoordinatedPhraseElement)) || (phrase
                    .getHead() instanceof CoordinatedPhraseElement && interrogative))) {

                actualModal = "will"; //$NON-NLS-1$

            } else if (modal != null) {
                actualModal = modal;

                if (Tense.PAST.equals(tenseValue)) {
                    modalPast = true;
                }
            }
        }

        pushParticles(phrase, parent, vgComponents);
        NLGElement frontVG = grabHeadVerb(phrase, tenseValue, modal != null);
        checkImperativeInfinitive(formValue, frontVG);

        if (phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
            frontVG = addBe(frontVG, vgComponents, Form.PAST_PARTICIPLE);
        }

        if (phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue()) {
            frontVG = addBe(frontVG, vgComponents, Form.PRESENT_PARTICIPLE);
        }

        if (phrase.getFeatureAsBoolean(Feature.PERFECT).booleanValue()
                || modalPast) {
            frontVG = addHave(frontVG, vgComponents, modal, tenseValue);
        }

        frontVG = pushIfModal(actualModal != null, phrase, frontVG,
                vgComponents);
        frontVG = createNot(phrase, vgComponents, frontVG, modal != null);

        if (frontVG != null) {
            pushFrontVerb(phrase, vgComponents, frontVG, formValue,
                    interrogative);
        }

        pushModal(actualModal, phrase, vgComponents);
        return vgComponents;
    }

    /**
     * Adds <em>not</em> to the stack if the phrase is negated.
     *
     * @param phrase       the <code>PhraseElement</code> representing this noun phrase.
     * @param vgComponents the stack of verb components in the verb group.
     * @param frontVG      the first verb in the verb group.
     * @param hasModal     the phrase has a modal
     * @return the new element for the front of the group.
     */
    private NLGElement createNot(PhraseElement phrase,
                                 Stack<NLGElement> vgComponents, NLGElement frontVG, boolean hasModal) {
        NLGElement newFront = frontVG;

        if (phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue()) {
            NLGFactory factory = phrase.getFactory();

            // before adding "do", check if this is an object WH
            // interrogative
            // in which case, don't add anything as it's already done by
            // ClauseHelper
            Object interrType = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
            boolean addDo = !(InterrogativeType.WHAT_OBJECT.equals(interrType) || InterrogativeType.WHO_OBJECT
                    .equals(interrType));

            if (!vgComponents.empty() || frontVG != null && isCopular(frontVG)) {
                vgComponents.push(new InflectedWordElement(
                        "not", LexicalCategory.ADVERB)); //$NON-NLS-1$
            } else {
                if (frontVG != null && !hasModal) {
                    frontVG.setFeature(Feature.NEGATED, true);
                    vgComponents.push(frontVG);
                }

                vgComponents.push(new InflectedWordElement(
                        "not", LexicalCategory.ADVERB)); //$NON-NLS-1$

                if (addDo) {
                    if (factory != null) {
                        newFront = factory.createInflectedWord("do",
                                LexicalCategory.VERB);

                    } else {
                        newFront = new InflectedWordElement(
                                "do", LexicalCategory.VERB); //$NON-NLS-1$
                    }
                }
            }
        }

        return newFront;
    }

    /**
     * Adds <em>have</em> to the stack.
     *
     * @param frontVG      the first verb in the verb group.
     * @param vgComponents the stack of verb components in the verb group.
     * @param modal        the modal to be used.
     * @param tenseValue   the <code>Tense</code> of the phrase.
     * @return the new element for the front of the group.
     */
    private NLGElement addHave(NLGElement frontVG,
                               Stack<NLGElement> vgComponents, String modal, Tense tenseValue) {
        NLGElement newFront = frontVG;

        if (frontVG != null) {
            frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
            vgComponents.push(frontVG);
        }
        newFront = new InflectedWordElement("have", LexicalCategory.VERB); //$NON-NLS-1$
        newFront.setFeature(Feature.TENSE, tenseValue);
        if (modal != null) {
            newFront.setFeature(InternalFeature.NON_MORPH, true);
        }
        return newFront;
    }

    /**
     * Pushes the front verb onto the stack of verb components.
     *
     * @param phrase        the <code>PhraseElement</code> representing this noun phrase.
     * @param vgComponents  the stack of verb components in the verb group.
     * @param frontVG       the first verb in the verb group.
     * @param formValue     the <code>Form</code> of the phrase.
     * @param interrogative <code>true</code> if the phrase is interrogative.
     */
    private void pushFrontVerb(PhraseElement phrase,
                               Stack<NLGElement> vgComponents, NLGElement frontVG,
                               Object formValue, boolean interrogative) {
        Object interrogType = phrase.getFeature(Feature.INTERROGATIVE_TYPE);

        if (Form.GERUND.equals(formValue)) {
            frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
            vgComponents.push(frontVG);

        } else if (Form.PAST_PARTICIPLE.equals(formValue)) {
            frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
            vgComponents.push(frontVG);

        } else if (Form.PRESENT_PARTICIPLE.equals(formValue)) {
            frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
            vgComponents.push(frontVG);

        } else if ((!(formValue == null || Form.NORMAL.equals(formValue)) || interrogative)
                && !isCopular(phrase.getHead()) && vgComponents.isEmpty()) {

            // AG: fix below: if interrogative, only set non-morph feature in
            // case it's not WHO_SUBJECT OR WHAT_SUBJECT
            if (!(InterrogativeType.WHO_SUBJECT.equals(interrogType) || InterrogativeType.WHAT_SUBJECT
                    .equals(interrogType))) {
                frontVG.setFeature(InternalFeature.NON_MORPH, true);
            }

            vgComponents.push(frontVG);

        } else {
            NumberAgreement numToUse = determineNumber(phrase.getParent(),
                    phrase);
            frontVG.setFeature(Feature.TENSE, phrase.getFeature(Feature.TENSE));
            frontVG.setFeature(Feature.PERSON, phrase
                    .getFeature(Feature.PERSON));
            frontVG.setFeature(Feature.NUMBER, numToUse);

            //don't push the front VG if it's a negated interrogative WH object question
            if (!(phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue() && (InterrogativeType.WHO_OBJECT
                    .equals(interrogType) || InterrogativeType.WHAT_OBJECT
                    .equals(interrogType)))) {
                vgComponents.push(frontVG);
            }
        }
    }

    /**
     * Adds the <em>be</em> verb to the front of the group.
     *
     * @param frontVG      the first verb in the verb group.
     * @param vgComponents the stack of verb components in the verb group.
     * @param frontForm    the form the current front verb is to take.
     * @return the new element for the front of the group.
     */
    private NLGElement addBe(NLGElement frontVG,
                             Stack<NLGElement> vgComponents, Form frontForm) {

        if (frontVG != null) {
            frontVG.setFeature(Feature.FORM, frontForm);
            vgComponents.push(frontVG);
        }
        return new InflectedWordElement("be", LexicalCategory.VERB); //$NON-NLS-1$
    }
}
