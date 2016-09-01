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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */

package simplenlg.syntax.spanish;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.spanish.XMLLexicon;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

/**
 * {@link PremodifierTest} contains a series of JUnit test cases for Premodifiers.
 *
 * @author Saad Mahamood
 */
public class PremodifierTest {

    private Lexicon lexicon = null;
    private NLGFactory phraseFactory = null;
    private Realiser realiser = null;

    @Before
    public void setup() {
        lexicon = new XMLLexicon();
        phraseFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
    }


    /**
     * Test change from "a" to "an" in the presence of a premodifier with a
     * vowel
     */
    @Test
    public void indefiniteWithPremodifierTest() {
        SPhraseSpec s = this.phraseFactory.createClause(null, "haber");
        s.setFeature(Feature.TENSE, Tense.PRESENT);
        NPPhraseSpec np = this.phraseFactory.createNounPhrase("una", "estenosis");
        s.setObject(np);

        // check without modifiers -- article should be "a"
        Assert.assertEquals("hay una estenosis", this.realiser.realise(s)
                .getRealisation());

        // add a single modifier -- should turn article to "an"
        np.addPreModifier(this.phraseFactory.createAdjectivePhrase("excéntrico"));
        Assert.assertEquals("hay una excéntrica estenosis", this.realiser
                .realise(s).getRealisation());
    }

    /**
     * Test for comma separation between premodifers
     */
    @Test
    public void multipleAdjPremodifiersTest() {
        NPPhraseSpec np = this.phraseFactory.createNounPhrase("una", "estenosis");
        np.addPreModifier(this.phraseFactory.createAdjectivePhrase("excéntrico"));
        np.addPreModifier(this.phraseFactory.createAdjectivePhrase("discreto"));
        Assert.assertEquals("una excéntrica, discreta estenosis", this.realiser
                .realise(np).getRealisation());
    }

    /**
     * Test for comma separation between verb premodifiers
     */
    @Test
    public void multipleAdvPremodifiersTest() {
        AdvPhraseSpec adv1 = this.phraseFactory.createAdverbPhrase("lentamente");
        AdvPhraseSpec adv2 = this.phraseFactory
                .createAdverbPhrase("discretamente");

        // case 1: concatenated premods: should have comma
        VPPhraseSpec vp = this.phraseFactory.createVerbPhrase("correr");
        vp.addPreModifier(adv1);
        vp.addPreModifier(adv2);
        Assert.assertEquals("lentamente, discretamente corre", this.realiser
                .realise(vp).getRealisation());

        // case 2: coordinated premods: no comma
        VPPhraseSpec vp2 = this.phraseFactory.createVerbPhrase("comer");
        vp2.addPreModifier(this.phraseFactory.createCoordinatedPhrase(adv1,
                adv2));
        Assert.assertEquals("lentamente y discretamente come", this.realiser
                .realise(vp2).getRealisation());
    }

}
