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

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class contains static methods to help the syntax processor realise noun
 * phrases.
 * </p>
 *
 * @author E. Reiter and D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public abstract class NounPhraseHelper {

    /**
     * The qualitative position for ordering premodifiers.
     */
    private static final int QUALITATIVE_POSITION = 1;
    /**
     * The colour position for ordering premodifiers.
     */
    private static final int COLOUR_POSITION = 2;
    /**
     * The classifying position for ordering premodifiers.
     */
    private static final int CLASSIFYING_POSITION = 3;
    /**
     * The noun position for ordering premodifiers.
     */
    private static final int NOUN_POSITION = 4;
    protected PhraseHelper phraseHelper;

    public NounPhraseHelper(PhraseHelper phraseHelper) {
        this.phraseHelper = phraseHelper;
    }

    /**
     * The main method for realising noun phrases.
     *
     * @param parent the <code>SyntaxProcessor</code> that called this method.
     * @param phrase the <code>PhraseElement</code> to be realised.
     * @return the realised <code>NLGElement</code>.
     */
    public NLGElement realise(SyntaxProcessor parent, PhraseElement phrase) {
        ListElement realisedElement = null;

        if (phrase != null
                && !phrase.getFeatureAsBoolean(Feature.ELIDED).booleanValue()) {
            realisedElement = new ListElement();

            if (phrase.getFeatureAsBoolean(Feature.PRONOMINAL).booleanValue()) {
                realisedElement.addComponent(createPronoun(parent, phrase));

            } else {
                realiseSpecifier(phrase, parent, realisedElement);
                realisePreModifiers(phrase, parent, realisedElement);
                realisePostModifiers(phrase, parent, realisedElement);
                realiseHeadNoun(phrase, parent, realisedElement);
                phraseHelper.realiseList(parent, realisedElement, phrase
                                .getFeatureAsElementList(InternalFeature.COMPLEMENTS),
                        DiscourseFunction.COMPLEMENT);

                phraseHelper.realiseList(parent, realisedElement, phrase
                        .getPostModifiers(), DiscourseFunction.POST_MODIFIER);
            }
        }

        return realisedElement;
    }

    /**
     * Realises the head noun of the noun phrase.
     *
     * @param phrase          the <code>PhraseElement</code> representing this noun phrase.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the noun phrase.
     */
    private void realiseHeadNoun(PhraseElement phrase,
                                 SyntaxProcessor parent, ListElement realisedElement) {
        NLGElement headElement = phrase.getHead();

        if (headElement != null) {
            headElement.setFeature(Feature.ELIDED, phrase
                    .getFeature(Feature.ELIDED));
            headElement.setFeature(LexicalFeature.GENDER, phrase
                    .getFeature(LexicalFeature.GENDER));
            headElement.setFeature(InternalFeature.ACRONYM, phrase
                    .getFeature(InternalFeature.ACRONYM));
            headElement.setFeature(Feature.NUMBER, phrase
                    .getFeature(Feature.NUMBER));
            headElement.setFeature(Feature.PERSON, phrase
                    .getFeature(Feature.PERSON));
            headElement.setFeature(Feature.POSSESSIVE, phrase
                    .getFeature(Feature.POSSESSIVE));
            headElement.setFeature(Feature.PASSIVE, phrase
                    .getFeature(Feature.PASSIVE));
            NLGElement currentElement = parent.realise(headElement);
            currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                    DiscourseFunction.SUBJECT);
            realisedElement.addComponent(currentElement);
        }
    }

    /**
     * Realises the pre-modifiers of the noun phrase. Before being realised,
     * pre-modifiers undergo some basic sorting based on adjective ordering.
     *
     * @param phrase          the <code>PhraseElement</code> representing this noun phrase.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the noun phrase.
     */
    private void realisePreModifiers(PhraseElement phrase,
                                     SyntaxProcessor parent, ListElement realisedElement) {

        List<NLGElement> preModifiers = phrase.getPreModifiers();
        if (phrase.getFeatureAsBoolean(Feature.ADJECTIVE_ORDERING)
                .booleanValue()) {
            preModifiers = sortNPPreModifiers(preModifiers);
        }
        phraseHelper.realiseList(parent, realisedElement, preModifiers,
                DiscourseFunction.PRE_MODIFIER);
    }

    /**
     * Realises the post-modifiers of the noun phrase.
     *
     * @param phrase          the <code>PhraseElement</code> representing this noun phrase.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the noun phrase.
     */
    protected abstract void realisePostModifiers(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement);

    /**
     * Realises the specifier of the noun phrase.
     *
     * @param phrase          the <code>PhraseElement</code> representing this noun phrase.
     * @param parent          the parent <code>SyntaxProcessor</code> that will do the
     *                        realisation of the complementiser.
     * @param realisedElement the current realisation of the noun phrase.
     */
    private void realiseSpecifier(PhraseElement phrase,
                                  SyntaxProcessor parent, ListElement realisedElement) {
        List<NLGElement> specifierElements = phrase
                .getFeatureAsElementList(InternalFeature.SPECIFIER);

        for (NLGElement specifierElement : specifierElements) {
            if (specifierElement != null
                    && !phrase.getFeatureAsBoolean(InternalFeature.RAISED)
                    .booleanValue() && !phrase.getFeatureAsBoolean(Feature.ELIDED).booleanValue()) {
                if (!specifierElement.isA(LexicalCategory.PRONOUN) && specifierElement.getCategory() != PhraseCategory.NOUN_PHRASE) {
                    specifierElement.setFeature(Feature.NUMBER, phrase
                            .getFeature(Feature.NUMBER));
                }

                NLGElement currentElement = parent.realise(specifierElement);

                if (currentElement != null) {
                    currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                            DiscourseFunction.SPECIFIER);
                    realisedElement.addComponent(currentElement);
                }
            }
        }
    }

    /**
     * Sort the list of premodifiers for this noun phrase using adjective
     * ordering (ie, "big" comes before "red")
     *
     * @param originalModifiers the original listing of the premodifiers.
     * @return the sorted <code>List</code> of premodifiers.
     */
    private List<NLGElement> sortNPPreModifiers(
            List<NLGElement> originalModifiers) {

        List<NLGElement> orderedModifiers = null;

        if (originalModifiers == null || originalModifiers.size() <= 1) {
            orderedModifiers = originalModifiers;
        } else {
            orderedModifiers = new ArrayList<NLGElement>(originalModifiers);
            boolean changesMade = false;
            do {
                changesMade = false;
                for (int i = 0; i < orderedModifiers.size() - 1; i++) {
                    if (getMinPos(orderedModifiers.get(i)) > getMaxPos(orderedModifiers
                            .get(i + 1))) {
                        NLGElement temp = orderedModifiers.get(i);
                        orderedModifiers.set(i, orderedModifiers.get(i + 1));
                        orderedModifiers.set(i + 1, temp);
                        changesMade = true;
                    }
                }
            } while (changesMade == true);
        }
        return orderedModifiers;
    }

    /**
     * Determines the minimim position at which this modifier can occur.
     *
     * @param modifier the modifier to be checked.
     * @return the minimum position for this modifier.
     */
    private int getMinPos(NLGElement modifier) {
        int position = QUALITATIVE_POSITION;

        if (modifier.isA(LexicalCategory.NOUN)
                || modifier.isA(PhraseCategory.NOUN_PHRASE)) {

            position = NOUN_POSITION;
        } else if (modifier.isA(LexicalCategory.ADJECTIVE)
                || modifier.isA(PhraseCategory.ADJECTIVE_PHRASE)) {
            WordElement adjective = getHeadWordElement(modifier);

            if (adjective.getFeatureAsBoolean(LexicalFeature.QUALITATIVE)
                    .booleanValue()) {
                position = QUALITATIVE_POSITION;
            } else if (adjective.getFeatureAsBoolean(LexicalFeature.COLOUR)
                    .booleanValue()) {
                position = COLOUR_POSITION;
            } else if (adjective
                    .getFeatureAsBoolean(LexicalFeature.CLASSIFYING)
                    .booleanValue()) {
                position = CLASSIFYING_POSITION;
            }
        }
        return position;
    }

    /**
     * Determines the maximim position at which this modifier can occur.
     *
     * @param modifier the modifier to be checked.
     * @return the maximum position for this modifier.
     */
    private int getMaxPos(NLGElement modifier) {
        int position = NOUN_POSITION;

        if (modifier.isA(LexicalCategory.ADJECTIVE)
                || modifier.isA(PhraseCategory.ADJECTIVE_PHRASE)) {
            WordElement adjective = getHeadWordElement(modifier);

            if (adjective.getFeatureAsBoolean(LexicalFeature.CLASSIFYING)
                    .booleanValue()) {
                position = CLASSIFYING_POSITION;
            } else if (adjective.getFeatureAsBoolean(LexicalFeature.COLOUR)
                    .booleanValue()) {
                position = COLOUR_POSITION;
            } else if (adjective
                    .getFeatureAsBoolean(LexicalFeature.QUALITATIVE)
                    .booleanValue()) {
                position = QUALITATIVE_POSITION;
            } else {
                position = CLASSIFYING_POSITION;
            }
        }
        return position;
    }

    /**
     * Retrieves the correct representation of the word from the element. This
     * method will find the <code>WordElement</code>, if it exists, for the
     * given phrase or inflected word.
     *
     * @param element the <code>NLGElement</code> from which the head is required.
     * @return the <code>WordElement</code>
     */
    private WordElement getHeadWordElement(NLGElement element) {
        WordElement head = null;

        if (element instanceof WordElement)
            head = (WordElement) element;
        else if (element instanceof InflectedWordElement) {
            head = (WordElement) element.getFeature(InternalFeature.BASE_WORD);
        } else if (element instanceof PhraseElement) {
            head = getHeadWordElement(((PhraseElement) element).getHead());
        }

        return head;
    }

    /**
     * Creates the appropriate pronoun if the subject of the noun phrase is
     * pronominal.
     *
     * @param parent the parent <code>SyntaxProcessor</code> that will do the
     *               realisation of the complementiser.
     * @param phrase the <code>PhraseElement</code> representing this noun phrase.
     * @return the <code>NLGElement</code> representing the pronominal.
     */
    protected abstract NLGElement createPronoun(SyntaxProcessor parent,
                                                PhraseElement phrase);
}
