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

import org.junit.After;
import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

import java.util.List;

/**
 * These are tests for the verb phrase and coordinate VP classes.
 *
 * @author agatt
 */
public class VerbPhraseTest extends SimpleNLG4Test {

    /**
     * Instantiates a new vP test.
     *
     * @param name the name
     */
    public VerbPhraseTest(String name) {
        super(name);
    }


    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }


    /**
     * Some tests to check for an early bug which resulted in reduplication of
     * verb particles in the past tense e.g. "fall down down" or "creep up up"
     */
    @Test
    public void testVerbParticle() {
        VPPhraseSpec v = this.phraseFactory.createVerbPhrase("caer"); //$NON-NLS-1$

//		assertEquals(
//				"down", v.getFeatureAsString(Feature.PARTICLE)); //$NON-NLS-1$

        assertEquals(
                "caer", ((WordElement) v.getVerb()).getBaseForm()); //$NON-NLS-1$

        v.setFeature(Feature.TENSE, Tense.PAST);
        v.setFeature(Feature.PERSON, Person.THIRD);
        v.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

        assertEquals(
                "cayeron", this.realiser.realise(v).getRealisation()); //$NON-NLS-1$

        v.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
        assertEquals(
                "caídos", this.realiser.realise(v).getRealisation()); //$NON-NLS-1$
    }

    /**
     * Tests for the tense and aspect.
     */
    @Test
    public void testSimplePast() {
        // "fell down"
        this.fallDown.setFeature(Feature.TENSE, Tense.PAST);
        assertEquals(
                "cayó", this.realiser.realise(this.fallDown).getRealisation()); //$NON-NLS-1$

    }

    /**
     * Test tense aspect.
     */
    @Test
    public void testTenseAspect() {
        // had fallen down
        this.realiser.setLexicon(this.lexicon);
        this.fallDown.setFeature(Feature.TENSE, Tense.PAST);
        this.fallDown.setFeature(Feature.PERFECT, true);

        assertEquals("ha caído", this.realiser.realise( //$NON-NLS-1$
                this.fallDown).getRealisation());

        // had been falling down
        this.fallDown.setFeature(Feature.PROGRESSIVE, true);
        assertEquals("ha estado cayendo", this.realiser.realise( //$NON-NLS-1$
                this.fallDown).getRealisation());

        // will have been kicked
        this.kick.setFeature(Feature.PASSIVE, true);
        this.kick.setFeature(Feature.PERFECT, true);
        this.kick.setFeature(Feature.TENSE, Tense.FUTURE);
        assertEquals("habrá sido golpeado", this.realiser.realise( //$NON-NLS-1$
                this.kick).getRealisation());

        // will have been being kicked
        this.kick.setFeature(Feature.PROGRESSIVE, true);
        assertEquals("habrá estado siendo golpeado", this.realiser //$NON-NLS-1$
                .realise(this.kick).getRealisation());

        // will not have been being kicked
        this.kick.setFeature(Feature.NEGATED, true);
        assertEquals("no habrá estado siendo golpeado", this.realiser //$NON-NLS-1$
                .realise(this.kick).getRealisation());

        // passivisation should suppress the complement
        this.kick.clearComplements();
        this.kick.addComplement(this.man);
        assertEquals("no habrá estado siendo golpeado", this.realiser //$NON-NLS-1$
                .realise(this.kick).getRealisation());

        // de-passivisation should now give us "will have been kicking the man"
        this.kick.setFeature(Feature.PASSIVE, false);
        assertEquals("no habrá estado golpeando el hombre", this.realiser //$NON-NLS-1$
                .realise(this.kick).getRealisation());

        // remove the future tense --
        // this is a test of an earlier bug that would still realise "will"
        this.kick.setFeature(Feature.TENSE, Tense.PRESENT);
        assertEquals("no ha estado golpeando el hombre", this.realiser //$NON-NLS-1$
                .realise(this.kick).getRealisation());
    }

    /**
     * Test for realisation of VP complements.
     */
    @Test
    public void testComplementation() {

        // was kissing Mary
        PhraseElement mary = this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("Mary")); //$NON-NLS-1$
        mary.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);
        this.kiss.clearComplements();
        this.kiss.addComplement(mary);
        this.kiss.setFeature(Feature.PROGRESSIVE, true);
        this.kiss.setFeature(Feature.TENSE, Tense.PAST);

        assertEquals("estuvo besando a Mary", this.realiser //$NON-NLS-1$
                .realise(this.kiss).getRealisation());

        CoordinatedPhraseElement mary2 = this.phraseFactory.createCoordinatedPhrase(mary,
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("Susan"))); //$NON-NLS-1$
        // add another complement -- should come out as "Mary and Susan"
        this.kiss.clearComplements();
        this.kiss.addComplement(mary2);
        assertEquals("estuvo besando a Mary y a Susan", this.realiser //$NON-NLS-1$
                .realise(this.kiss).getRealisation());

        // passivise -- should make the direct object complement disappear
        // Note: The verb doesn't come out as plural because agreement
        // is determined by the sentential subjects and this VP isn't inside a
        // sentence
        this.kiss.setFeature(Feature.PASSIVE, true);
        assertEquals("estuvo siendo besado", this.realiser //$NON-NLS-1$
                .realise(this.kiss).getRealisation());

        // make it plural (this is usually taken care of in SPhraseSpec)
        this.kiss.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        assertEquals("estuvieron siendo besados", this.realiser.realise( //$NON-NLS-1$
                this.kiss).getRealisation());

        // depassivise and add post-mod: yields "was kissing Mary in the room"
        this.kiss.addPostModifier(this.inTheRoom);
        this.kiss.setFeature(Feature.PASSIVE, false);
        this.kiss.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        assertEquals("estuvo besando a Mary y a Susan en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.kiss).getRealisation());

        // passivise again: should make direct object disappear, but not postMod
        // ="was being kissed in the room"
        this.kiss.setFeature(Feature.PASSIVE, true);
        this.kiss.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        assertEquals("estuvieron siendo besados en la habitación", this.realiser //$NON-NLS-1$
                .realise(this.kiss).getRealisation());
    }

    /**
     * This tests for the default complement ordering, relative to pre and
     * postmodifiers.
     */
    @Test
    public void testComplementation_2() {
        // give the woman the dog
        this.woman.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.INDIRECT_OBJECT);
        this.dog.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.OBJECT);
        this.give.clearComplements();
        this.give.addComplement(this.dog);
        this.give.addComplement(this.woman);
        assertEquals("da a la mujer el perro", this.realiser.realise( //$NON-NLS-1$
                this.give).getRealisation());

        // add a few premodifiers and postmodifiers
        this.give.addPreModifier("lentamente"); //$NON-NLS-1$
        this.give.addPostModifier(this.behindTheCurtain);
        this.give.addPostModifier(this.inTheRoom);
        assertEquals(
                "lentamente da a la mujer el perro tras la cortina en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.give).getRealisation());

        // reset the arguments
        this.give.clearComplements();
        this.give.addComplement(this.dog);
        CoordinatedPhraseElement womanBoy = this.phraseFactory.createCoordinatedPhrase(
                this.woman, this.boy);
        womanBoy.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.INDIRECT_OBJECT);
        this.give.addComplement(womanBoy);

        // if we unset the passive, we should get the indirect objects
        // they won't be coordinated
        this.give.setFeature(Feature.PASSIVE, false);
        assertEquals(
                "lentamente da a la mujer y al niño el perro tras la cortina en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.give).getRealisation());

        // set them to a coordinate instead
        // set ONLY the complement INDIRECT_OBJECT, leaves OBJECT intact
        this.give.clearComplements();
        this.give.addComplement(womanBoy);
        this.give.addComplement(this.dog);
        List<NLGElement> complements = this.give
                .getFeatureAsElementList(InternalFeature.COMPLEMENTS);

        int indirectCount = 0;
        for (NLGElement eachElement : complements) {
            if (DiscourseFunction.INDIRECT_OBJECT.equals(eachElement
                    .getFeature(InternalFeature.DISCOURSE_FUNCTION))) {
                indirectCount++;
            }
        }
        assertEquals(1, indirectCount); // only one indirect object
        // where
        // there were two before

        assertEquals(
                "lentamente da a la mujer y al niño el perro tras la cortina en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.give).getRealisation());
    }

    /**
     * Test for complements raised in the passive case.
     */
    @Test
    public void testPassiveComplement() {
        // add some arguments
        this.dog.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.OBJECT);
        this.woman.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.INDIRECT_OBJECT);
        this.give.addComplement(this.dog);
        this.give.addComplement(this.woman);
        assertEquals("da a la mujer el perro", this.realiser.realise( //$NON-NLS-1$
                this.give).getRealisation());

        // add a few premodifiers and postmodifiers
        this.give.addPreModifier("lentamente"); //$NON-NLS-1$
        this.give.addPostModifier(this.behindTheCurtain);
        this.give.addPostModifier(this.inTheRoom);
        assertEquals(
                "lentamente da a la mujer el perro tras la cortina en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.give).getRealisation());

        // passivise: This should suppress "the dog"
        this.give.clearComplements();
        this.give.addComplement(this.dog);
        this.give.addComplement(this.woman);
        this.give.setFeature(Feature.PASSIVE, true);

        assertEquals(
                "es lentamente dado a la mujer tras la cortina en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.give).getRealisation());
    }

    /**
     * Test VP with sentential complements. This tests for structures like "said
     * that John was walking"
     */
    @Test
    public void testClausalComplement() {
        this.phraseFactory.setLexicon(this.lexicon);
        SPhraseSpec s = this.phraseFactory.createClause();

        s.setSubject(this.phraseFactory
                .createNounPhrase("John")); //$NON-NLS-1$

        // Create a sentence first
        CoordinatedPhraseElement maryAndSusan = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createPrepositionPhrase("a", "Mary"), //$NON-NLS-1$
                this.phraseFactory.createPrepositionPhrase("a", "Susan")); //$NON-NLS-1$

        this.kiss.clearComplements();
        s.setVerbPhrase(this.kiss);
        s.setObject(maryAndSusan);
        s.setFeature(Feature.PROGRESSIVE, true);
        s.setFeature(Feature.TENSE, Tense.PAST);
        s.addPostModifier(this.inTheRoom);
        assertEquals("John estuvo besando a Mary y a Susan en la habitación", //$NON-NLS-1$
                this.realiser.realise(s).getRealisation());

        // make the main VP past
        this.say.setFeature(Feature.TENSE, Tense.PAST);
        assertEquals("dijo", this.realiser.realise(this.say) //$NON-NLS-1$
                .getRealisation());

        // now add the sentence as complement of "say". Should make the sentence
        // subordinate
        // note that sentential punctuation is suppressed
        this.say.addComplement(s);
        assertEquals(
                "dijo que John estuvo besando a Mary y a Susan en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());

        // add a postModifier to the main VP
        // yields [says [that John was kissing Mary and Susan in the room]
        // [behind the curtain]]
        this.say.addPostModifier(this.behindTheCurtain);
        assertEquals(
                "dijo que John estuvo besando a Mary y a Susan en la habitación tras la cortina", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());

        // create a new sentential complement
        PhraseElement s2 = this.phraseFactory.createClause(this.phraseFactory
                        .createNounPhrase("todo"), //$NON-NLS-1$
                "estar", //$NON-NLS-1$
                this.phraseFactory.createAdjectivePhrase("bien")); //$NON-NLS-1$

        s2.setFeature(Feature.TENSE, Tense.FUTURE);
        assertEquals("todo estará bien", this.realiser.realise(s2) //$NON-NLS-1$
                .getRealisation());

        // add the new complement to the VP
        // yields [said [that John was kissing Mary and Susan in the room and
        // all will be fine] [behind the curtain]]
        CoordinatedPhraseElement s3 = this.phraseFactory.createCoordinatedPhrase(s, s2);
        this.say.clearComplements();
        this.say.addComplement(s3);

        // first with outer complementiser suppressed
        s3.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
        assertEquals(
                "dijo que John estuvo besando a Mary y a Susan en la habitación y todo estará bien tras la cortina", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());

        setUp();
        s = this.phraseFactory.createClause();

        s.setSubject(this.phraseFactory
                .createNounPhrase("John")); //$NON-NLS-1$

        // Create a sentence first
        maryAndSusan = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createPrepositionPhrase("a", "Mary"), //$NON-NLS-1$
                this.phraseFactory.createPrepositionPhrase("a", "Susan")); //$NON-NLS-1$

        s.setVerbPhrase(this.kiss);
        s.setObject(maryAndSusan);
        s.setFeature(Feature.PROGRESSIVE, true);
        s.setFeature(Feature.TENSE, Tense.PAST);
        s.addPostModifier(this.inTheRoom);
        s2 = this.phraseFactory.createClause(this.phraseFactory
                        .createNounPhrase("todo"), //$NON-NLS-1$
                "estar", //$NON-NLS-1$
                this.phraseFactory.createAdjectivePhrase("bien")); //$NON-NLS-1$

        s2.setFeature(Feature.TENSE, Tense.FUTURE);
        // then with complementiser not suppressed and not aggregated
        s3 = this.phraseFactory.createCoordinatedPhrase(s, s2);
        this.say.addComplement(s3);
        this.say.setFeature(Feature.TENSE, Tense.PAST);
        this.say.addPostModifier(this.behindTheCurtain);

        assertEquals(
                "dijo que John estuvo besando a Mary y a Susan en la habitación y que todo estará bien tras la cortina", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());

    }

    /**
     * Test VP coordination and aggregation:
     * <OL>
     * <LI>If the simplenlg.features of a coordinate VP are set, they should be
     * inherited by its daughter VP;</LI>
     * <LI>2. We can aggregate the coordinate VP so it's realised with one
     * wide-scope auxiliary</LI>
     */
    @Test
    public void testCoordination() {
        // simple case
        this.kiss.addComplement(this.dog);
        this.kick.addComplement(this.boy);

        CoordinatedPhraseElement coord1 = this.phraseFactory.createCoordinatedPhrase(
                this.kiss, this.kick);

        coord1.setFeature(Feature.PERSON, Person.THIRD);
        coord1.setFeature(Feature.TENSE, Tense.PAST);
        assertEquals("besó el perro y golpeó el niño", this.realiser //$NON-NLS-1$
                .realise(coord1).getRealisation());

        // with negation: should be inherited by all components
        coord1.setFeature(Feature.NEGATED, true);
        this.realiser.setLexicon(this.lexicon);
        assertEquals("no besó el perro y no golpeó el niño", //$NON-NLS-1$
                this.realiser.realise(coord1).getRealisation());

        // set a modal
        coord1.setFeature(Feature.MODAL, "poder"); //$NON-NLS-1$
        assertEquals(
                "no puede haber besado el perro y no puede haber golpeado el niño", //$NON-NLS-1$
                this.realiser.realise(coord1).getRealisation());

        // set perfect and progressive
        coord1.setFeature(Feature.PERFECT, true);
        coord1.setFeature(Feature.PROGRESSIVE, true);
        assertEquals("no puede haber estado besando el perro y no puede haber estado golpeando el niño", this.realiser.realise( //$NON-NLS-1$
                coord1).getRealisation());

        // now aggregate
        coord1.setFeature(Feature.AGGREGATE_AUXILIARY, true);
        assertEquals(
                "no puede haber estado besando el perro y golpeando el niño", //$NON-NLS-1$
                this.realiser.realise(coord1).getRealisation());
    }
}
