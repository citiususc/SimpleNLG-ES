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
package simplenlg.syntax.spanish;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;

/**
 * This class incorporates a few tests for adjectival phrases. Also tests for
 * adverbial phrase specs, which are very similar
 *
 * @author agatt
 */
public class AdjectivePhraseTest extends SimpleNLG4Test {

    /**
     * Instantiates a new adj p test.
     *
     * @param name the name
     */
    public AdjectivePhraseTest(String name) {
        super(name);
    }


    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }


    /**
     * Test premodification & coordination of Adjective Phrases (Not much else
     * to simplenlg.test)
     */
    @Test
    public void testAdj() {

        // form the adjphrase "increíblemente salacious"
        this.salacious.addPreModifier(this.phraseFactory
                .createAdverbPhrase("increíblemente")); //$NON-NLS-1$
        Assert.assertEquals("increíblemente salaz", this.realiser //$NON-NLS-1$
                .realise(this.salacious).getRealisation());

        // form the adjphrase "sorprendentemente beautiful"
        this.beautiful.addPreModifier("sorprendentemente"); //$NON-NLS-1$
        Assert.assertEquals("sorprendentemente bonito", this.realiser //$NON-NLS-1$
                .realise(this.beautiful).getRealisation());

        // coordinate the two aps
        CoordinatedPhraseElement coordap = this.phraseFactory.createCoordinatedPhrase(
                this.salacious, this.beautiful);
        Assert.assertEquals("increíblemente salaz y sorprendentemente bonito", //$NON-NLS-1$
                this.realiser.realise(coordap).getRealisation());

        // changing the inner conjunction
        coordap.setFeature(Feature.CONJUNCTION, "o"); //$NON-NLS-1$
        Assert.assertEquals("increíblemente salaz o sorprendentemente bonito", //$NON-NLS-1$
                this.realiser.realise(coordap).getRealisation());

        // coordinate this with a new AdjPhraseSpec
        CoordinatedPhraseElement coord2 = this.phraseFactory.createCoordinatedPhrase(coordap,
                this.stunning);
        Assert.assertEquals(
                "increíblemente salaz o sorprendentemente bonito y estupendo", //$NON-NLS-1$
                this.realiser.realise(coord2).getRealisation());

        // add a premodifier the coordinate phrase, yielding
        // "seriamente y innegablemente increíblemente salacious o sorprendentemente beautiful
        // y stunning"
        CoordinatedPhraseElement preMod = this.phraseFactory.createCoordinatedPhrase(
                new StringElement("seriamente"), new StringElement("innegablemente")); //$NON-NLS-1$//$NON-NLS-2$

        coord2.addPreModifier(preMod);
        Assert.assertEquals(
                "seriamente y innegablemente increíblemente salaz o sorprendentemente bonito y estupendo", //$NON-NLS-1$
                this.realiser.realise(coord2).getRealisation());

        // adding a coordinate rather than coordinating should give a different
        // result
        coordap.addCoordinate(this.stunning);
        Assert.assertEquals(
                "increíblemente salaz, sorprendentemente bonito o estupendo", //$NON-NLS-1$
                this.realiser.realise(coordap).getRealisation());

    }

    /**
     * Simple test of adverbials
     */
    @Test
    public void testAdv() {

        PhraseElement sent = this.phraseFactory.createClause("John", "comer"); //$NON-NLS-1$ //$NON-NLS-2$

        PhraseElement adv = this.phraseFactory.createAdverbPhrase("rápido"); //$NON-NLS-1$

        sent.addPostModifier(adv);

        Assert.assertEquals("John come rápido", this.realiser.realise(sent) //$NON-NLS-1$
                .getRealisation());

        adv.addPreModifier("muy"); //$NON-NLS-1$

        Assert.assertEquals("John come muy rápido", this.realiser.realise( //$NON-NLS-1$
                sent).getRealisation());

    }

    /**
     * Test participles as adjectives
     */
    @Test
    public void testParticipleAdj() {
        PhraseElement ap = this.phraseFactory
                .createAdjectivePhrase(this.lexicon.getWord("asociado",
                        LexicalCategory.ADJECTIVE));
        Assert.assertEquals("asociado", this.realiser.realise(ap)
                .getRealisation());
    }

    /**
     * Test for multiple adjective modifiers with comma-separation. Example courtesy of William Bradshaw (Data2Text Ltd).
     */
    @Test
    public void testMultipleModifiers() {
        PhraseElement np = this.phraseFactory
                .createNounPhrase(this.lexicon.getWord("mensaje",
                        LexicalCategory.NOUN));
        np.addPreModifier(this.lexicon.getWord("activo",
                LexicalCategory.ADJECTIVE));
        np.addPreModifier(this.lexicon.getWord("temperatura",
                LexicalCategory.ADJECTIVE));
        Assert.assertEquals("activo, temperatura mensaje", this.realiser.realise(np).getRealisation());

        //now we set the realiser not to separate using commas
        this.realiser.setCommaSepPremodifiers(false);
        Assert.assertEquals("activo temperatura mensaje", this.realiser.realise(np).getRealisation());

    }

}
