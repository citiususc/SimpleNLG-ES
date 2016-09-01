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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.lexicon.spanish;

import junit.framework.Assert;
import simplenlg.features.Inflection;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;

/**
 * @author Dave Westwater, Data2Text Ltd
 */
public class SharedLexiconTests {

    public void doBasicTests(Lexicon lexicon) {
        // test getWords. Should be 2 "poder" (of any cat), 1 noun tree, 0 adj
        // trees
        Assert.assertEquals(2, lexicon.getWords("poder").size());
        Assert.assertEquals(1, lexicon.getWords("poder", LexicalCategory.NOUN).size());
        Assert.assertEquals(0, lexicon.getWords("poder", LexicalCategory.ADJECTIVE).size());

        // below test removed as standard morph variants no longer recorded in
        // lexicon
        // WordElement early = lexicon.getWord("early",
        // LexicalCategory.ADJECTIVE);
        // Assert.assertEquals("earlier",
        // early.getFeatureAsString(Feature.COMPARATIVE));

        // test getWord. Comparative of ADJ "good" is "better", superlative is
        // "best", this is a qualitative and predicative adjective
        WordElement bueno = lexicon.getWord("bueno", LexicalCategory.ADJECTIVE);
        Assert.assertEquals("mejor", bueno
                .getFeatureAsString(LexicalFeature.COMPARATIVE));
        Assert.assertEquals("buenísimo", bueno
                .getFeatureAsString(LexicalFeature.SUPERLATIVE));
        Assert.assertEquals(true, bueno.getFeatureAsBoolean(
                LexicalFeature.QUALITATIVE).booleanValue());
        Assert.assertEquals(true, bueno.getFeatureAsBoolean(
                LexicalFeature.PREDICATIVE).booleanValue());
        Assert.assertEquals(false, bueno.getFeatureAsBoolean(
                LexicalFeature.COLOUR).booleanValue());
        Assert.assertEquals(false, bueno.getFeatureAsBoolean(
                LexicalFeature.CLASSIFYING).booleanValue());

        // test getWord. There is only one "woman", and its plural is "mujeres".
        // It is not an acronym, not proper, and countable
        WordElement mujer = lexicon.getWord("woman");

        Assert.assertEquals("mujeres", mujer
                .getFeatureAsString(LexicalFeature.PLURAL));
        Assert.assertEquals(null, mujer
                .getFeatureAsString(LexicalFeature.ACRONYM_OF));
        Assert.assertEquals(false, mujer.getFeatureAsBoolean(
                LexicalFeature.PROPER).booleanValue());
        Assert.assertFalse(mujer.hasInflectionalVariant(Inflection.UNCOUNT));

        // NB: This fails if the lexicon is XMLLexicon. No idea why.
        // Assert.assertEquals("irreg",
        // woman.getFeatureAsString(LexicalFeature.DEFAULT_INFL));

        // test getWord. Noun "sand" is non-count
        WordElement arena = lexicon.getWord("arena", LexicalCategory.NOUN);
        Assert.assertEquals(true, arena.hasInflectionalVariant(Inflection.UNCOUNT));
        Assert.assertEquals(Inflection.UNCOUNT, arena.getDefaultInflectionalVariant());

        // test hasWord
        Assert.assertEquals(true, lexicon.hasWord("árbol")); // "tree" exists
        Assert.assertEquals(false, lexicon.hasWord("árbol",
                LexicalCategory.ADVERB)); // but not as an adverb

        // test getWordByID; quickly, also check that this is a verb_modifier
        WordElement rapidamente = lexicon.getWordByID("E0051632");
        Assert.assertEquals("rápidamente", rapidamente.getBaseForm());
        Assert.assertEquals(LexicalCategory.ADVERB, rapidamente.getCategory());
        Assert.assertEquals(true, rapidamente.getFeatureAsBoolean(
                LexicalFeature.VERB_MODIFIER).booleanValue());
        Assert.assertEquals(false, rapidamente.getFeatureAsBoolean(
                LexicalFeature.SENTENCE_MODIFIER).booleanValue());
        Assert.assertEquals(false, rapidamente.getFeatureAsBoolean(
                LexicalFeature.INTENSIFIER).booleanValue());

        // test getWordFromVariant, verb type (tran or intran, not ditran)
        WordElement comer = lexicon.getWordFromVariant("comiendo");
        Assert.assertEquals("comer", comer.getBaseForm());
        Assert.assertEquals(LexicalCategory.VERB, comer.getCategory());
        Assert.assertEquals(true, comer.getFeatureAsBoolean(
                LexicalFeature.INTRANSITIVE).booleanValue());
        Assert.assertEquals(true, comer.getFeatureAsBoolean(
                LexicalFeature.TRANSITIVE).booleanValue());
        Assert.assertEquals(false, comer.getFeatureAsBoolean(
                LexicalFeature.DITRANSITIVE).booleanValue());

        // test BE is handled OK
        Assert.assertEquals("sido", lexicon.getWordFromVariant("ser",
                LexicalCategory.VERB).getFeatureAsString(
                LexicalFeature.PAST_PARTICIPLE));

        // test modal
        // WordElement can = lexicon.getWord("can", LexicalCategory.MODAL);
        // Assert.assertEquals("could", can
        //		.getFeatureAsString(LexicalFeature.PAST));

        // test non-existent word
        Assert.assertEquals(0, lexicon.getWords("akjmchsgk").size());

        // test lookup word method
        Assert.assertEquals(lexicon.lookupWord("say", LexicalCategory.VERB)
                .getBaseForm(), "say");
        Assert.assertEquals(lexicon.lookupWord("dicho", LexicalCategory.VERB)
                .getBaseForm(), "say");
        Assert.assertEquals(lexicon
                        .lookupWord("E0054448", LexicalCategory.VERB).getBaseForm(),
                "say");
    }

}
