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
package simplenlg.morphology;

import simplenlg.framework.*;

/**
 * <p>
 * This abstract class contains a number of rules for doing simple inflection.
 * </p>
 * <p>
 * <p>
 * As a matter of course, the processor will first use any user-defined
 * inflection for the world. If no inflection is provided then the lexicon, if
 * it exists, will be examined for the correct inflection. Failing this a set of
 * very basic rules will be examined to inflect the word.
 * </p>
 * <p>
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 * <p>
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0 16-Mar-2011 modified to use correct base form (ER)
 */
public abstract class MorphologyRules {

    /**
     * This method performs the morphology for nouns.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public abstract StringElement doNounMorphology(InflectedWordElement element, WordElement baseWord);

    /**
     * This method performs the morphology for verbs.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public abstract NLGElement doVerbMorphology(InflectedWordElement element, WordElement baseWord);

    /**
     * This method performs the morphology for adjectives.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public abstract NLGElement doAdjectiveMorphology(InflectedWordElement element, WordElement baseWord);

    /**
     * This method performs the morphology for adverbs.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public abstract NLGElement doAdverbMorphology(InflectedWordElement element, WordElement baseWord);

    /**
     * This method performs the morphology for pronouns.
     *
     * @param element the <code>InflectedWordElement</code>.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public abstract NLGElement doPronounMorphology(InflectedWordElement element);

    /**
     * This method performs the morphology for determiners.
     *
     * @param determiner  the <code>InflectedWordElement</code>.
     * @param realisation the current realisation of the determiner.
     */
    public abstract void doDeterminerMorphology(NLGElement determiner, String realisation);

    /**
     * This method performs the morphology for determiners.
     *
     * @param element the <code>InflectedWordElement</code>.
     */
    public abstract NLGElement doDeterminerMorphology(InflectedWordElement element);

    /**
     * return the base form of a word
     *
     * @param element
     * @param baseWord
     * @return
     */
    protected String getBaseForm(InflectedWordElement element, WordElement baseWord) {
        // unclear what the right behaviour should be
        // for now, prefer baseWord.getBaseForm() to element.getBaseForm() for
        // verbs (ie, "is" mapped to "be")
        // but prefer element.getBaseForm() to baseWord.getBaseForm() for other
        // words (ie, "children" not mapped to "child")

        // AG: changed this to get the default spelling variant
        // needed to preserve spelling changes in the VP

        if (LexicalCategory.VERB == element.getCategory()) {
            if (baseWord != null && baseWord.getDefaultSpellingVariant() != null)
                return baseWord.getDefaultSpellingVariant();
            else
                return element.getBaseForm();
        } else {
            if (element.getBaseForm() != null)
                return element.getBaseForm();
            else if (baseWord == null)
                return null;
            else
                return baseWord.getDefaultSpellingVariant();
        }

        // if (LexicalCategory.VERB == element.getCategory()) {
        // if (baseWord != null && baseWord.getBaseForm() != null)
        // return baseWord.getBaseForm();
        // else
        // return element.getBaseForm();
        // } else {
        // if (element.getBaseForm() != null)
        // return element.getBaseForm();
        // else if (baseWord == null)
        // return null;
        // else
        // return baseWord.getBaseForm();
        // }
    }
}
