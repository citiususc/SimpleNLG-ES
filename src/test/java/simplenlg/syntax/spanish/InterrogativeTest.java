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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.spanish.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

/**
 * JUnit test case for interrogatives.
 *
 * @author agatt
 */
@Ignore
public class InterrogativeTest extends SimpleNLG4Test {

    // set up a few more fixtures
    /**
     * The s5.
     */
    SPhraseSpec s1, s2, s3, s4, s5;

    /**
     * Instantiates a new interrogative test.
     *
     * @param name the name
     */
    public InterrogativeTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see simplenlg.test.SimplenlgTest#setUp()
     */
    @Override
    @Before
    protected void setUp() {
        super.setUp();

        // // the man gives the woman John's flower
        NPPhraseSpec flor = this.phraseFactory.createNounPhrase("la", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flor.setPostModifier(deJohn);
        PhraseElement _woman = this.phraseFactory.createNounPhrase(
                "la", "mujer"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause(this.man, this.give, flor);
        this.s3.setIndirectObject(_woman);

        CoordinatedPhraseElement subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "cojer", //$NON-NLS-1$
                "las pelotas"); //$NON-NLS-1$
        this.s4.getObject().setPlural(true);
        this.s4.getObject().setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        // this.s5 = new SPhraseSpec();
        // this.s5.setSubject(new NPPhraseSpec("the", "dog"));
        // this.s5.setHead("be");
        // this.s5.setComplement(new NPPhraseSpec("the", "rock"),
        // DiscourseFunction.OBJECT);

    }

    /**
     * Tests a couple of fairly simple questions.
     */
    @Test
    public void testSimpleQuestions() {
        setUp();
        this.phraseFactory.setLexicon(this.lexicon);
        this.realiser.setLexicon(this.lexicon);

        // simple present
        this.s1 = this.phraseFactory.createClause(this.woman, this.kiss,
                this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PRESENT);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

        NLGFactory docFactory = new NLGFactory(this.lexicon);
        DocumentElement sent = docFactory.createSentence(this.s1);
        Assert.assertEquals("¿Besa la mujer el hombre?", this.realiser //$NON-NLS-1$
                .realise(sent).getRealisation());

        // simple past
        // sentence: "the woman kissed the man"
        this.s1 = this.phraseFactory.createClause(this.woman, this.kiss,
                this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PAST);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("besó la mujer el hombre", this.realiser //$NON-NLS-1$
                .realise(this.s1).getRealisation());

        // copular/existential: be-fronting
        // sentence = "there is the dog on the rock"
        this.s2 = this.phraseFactory.createClause(null, "haber", this.dog); //$NON-NLS-1$ //$NON-NLS-2$
        this.s2.addPostModifier(this.onTheRock);
        this.s2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("hay el perro en la roca", this.realiser //$NON-NLS-1$
                .realise(this.s2).getRealisation());

        // perfective
        // sentence -- "there has been the dog on the rock"
        this.s2 = this.phraseFactory.createClause(null, "haber", this.dog); //$NON-NLS-1$ //$NON-NLS-2$
        this.s2.addPostModifier(this.onTheRock);
        this.s2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        this.s2.setFeature(Feature.PERFECT, true);
        Assert.assertEquals("ha habido el perro en la roca", //$NON-NLS-1$
                this.realiser.realise(this.s2).getRealisation());

        // progressive
        // sentence: "the man was giving the woman John's flower"
        NPPhraseSpec flower = this.phraseFactory.createNounPhrase("la", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flower.setPostModifier(deJohn);
        PhraseElement _woman = this.phraseFactory.createNounPhrase(
                "la", "mujer"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
        this.s3.setIndirectObject(_woman);
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        this.s3.setFeature(Feature.PROGRESSIVE, true);
        this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        NLGElement realised = this.realiser.realise(this.s3);
        Assert.assertEquals("estuvo el hombre dando a la mujer la flor de John", //$NON-NLS-1$
                realised.getRealisation());

        // modal
        // sentence: "the man should be giving the woman John's flower"
        setUp();
        flower = this.phraseFactory.createNounPhrase("la", "flor"); //$NON-NLS-1$
        john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flower.setPostModifier(deJohn);
        _woman = this.phraseFactory.createNounPhrase("la", "mujer"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
        this.s3.setIndirectObject(_woman);
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        Assert.assertEquals(
                "debe el hombre haber dado a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // complex case with cue phrases
        // sentence: "however, tomorrow, Jane and Andrew will pick up the balls
        // in the shop"
        // this gets the front modifier "tomorrow" shifted to the end
        setUp();
        CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "recoger", //$NON-NLS-1$
                "las pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo,"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals(
                "sin embargo, recogerán Jane and Andrew las pelotas en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Test for sentences with negation.
     */
    @Test
    public void testNegatedQuestions() {
        setUp();
        this.phraseFactory.setLexicon(this.lexicon);
        this.realiser.setLexicon(this.lexicon);

        // sentence: "the woman did not kiss the man"
        this.s1 = this.phraseFactory.createClause(this.woman, "besar", this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PAST);
        this.s1.setFeature(Feature.NEGATED, true);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("no besó la mujer el hombre", this.realiser //$NON-NLS-1$
                .realise(this.s1).getRealisation());

        // sentence: however, tomorrow, Jane and Andrew will not pick up the
        // balls in the shop
        CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "coger", //$NON-NLS-1$
                "las pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo,"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.NEGATED, true);
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals(
                "sin embargo, no cogerán Jane and Andrew las pelotas en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Tests for coordinate VPs in question form.
     */
    @Test
    public void testCoordinateVPQuestions() {

        // create a complex vp: "kiss the dog and walk in the room"
        setUp();
        CoordinatedPhraseElement complex = this.phraseFactory.createCoordinatedPhrase(
                this.kiss, this.walk);
        this.kiss.addComplement(this.dog);
        this.walk.addComplement(this.inTheRoom);

        // sentence: "However, tomorrow, Jane and Andrew will kiss the dog and
        // will walk in the room"
        CoordinatedPhraseElement subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, complex);
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);

        Assert.assertEquals(
                "sin embargo mañana Jane y Andrew besarán el perro y caminarán en la habitación", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // setting to interrogative should automatically give us a single,
        // wide-scope aux
        setUp();
        subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.kiss.addComplement(this.dog);
        this.walk.addComplement(this.inTheRoom);
        complex = this.phraseFactory.createCoordinatedPhrase(this.kiss, this.walk);
        this.s4 = this.phraseFactory.createClause(subjects, complex);
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

        Assert.assertEquals(
                "sin embargo Jane y Andrew besarán el perro y caminarán en la habitación mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // slightly more complex -- perfective
        setUp();
        this.realiser.setLexicon(this.lexicon);
        subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        complex = this.phraseFactory.createCoordinatedPhrase(this.kiss, this.walk);
        this.kiss.addComplement(this.dog);
        this.walk.addComplement(this.inTheRoom);
        this.s4 = this.phraseFactory.createClause(subjects, complex);
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        this.s4.setFeature(Feature.PERFECT, true);

        Assert.assertEquals(
                "sin embargo habrán Jane y Andrew besado el perro y caminado en la habitación mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Test for simple WH questions in present tense.
     */
    @Test
    public void testSimpleQuestions2() {
        setUp();
        this.realiser.setLexicon(this.lexicon);
        PhraseElement s = this.phraseFactory.createClause("la mujer", "besar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("el", "hombre"))); //$NON-NLS-1$

        // try with the simple yes/no type first
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("besa la mujer al hombre", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // now in the passive
        s = this.phraseFactory.createClause("la mujer", "besar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("el", "hombre"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        s.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("es el hombre besado por la mujer", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // // subject interrogative with simple present
        // // sentence: "the woman kisses the man"
        s = this.phraseFactory.createClause("la mujer", "besar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("el", "hombre"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);

        Assert.assertEquals("quién besa al hombre", this.realiser.realise(s) //$NON-NLS-1$
                .getRealisation());

        // object interrogative with simple present
        s = this.phraseFactory.createClause("la mujer", "besar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("el", "hombre"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién besa la mujer", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // subject interrogative with passive
        s = this.phraseFactory.createClause("la mujer", "besar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("el", "hombre"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        s.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("por quién es el hombre besado", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());
    }

    /**
     * Test for wh questions.
     */
    @Test
    public void testWHQuestions() {

        // subject interrogative
        setUp();
        this.realiser.setLexicon(this.lexicon);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals(
                "sin embargo quiénes cojerán las pelotas en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // subject interrogative in passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_SUBJECT);

        Assert.assertEquals(
                "sin embargo por quiénes serán las pelotas cojidas en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // object interrogative
        setUp();
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals(
                "sin embargo qué cojerán Jane y Andrew en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // object interrogative with passive
        setUp();
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        this.s4.setFeature(Feature.PASSIVE, true);

        Assert.assertEquals(
                "sin embargo qué serán cojidas en la tienda por Jane y Andrew mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // how-question + passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        Assert.assertEquals(
                "sin embargo cómo serán cojidas las pelotas en la tienda por Jane y Andrew mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // // why-question + passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals(
                "sin embargo por qué serán cojidas las pelotas en la tienda por Jane y Andrew mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // how question with modal
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        this.s4.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        Assert.assertEquals(
                "sin embargo cómo deberán ser cojidas las pelotas en la tienda por Jane y Andrew mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // indirect object
        setUp();
        this.realiser.setLexicon(this.lexicon);
        this.s3.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_INDIRECT_OBJECT);
        Assert.assertEquals("a quién da el hombre la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());
    }

    /**
     * WH movement in the progressive
     */
    @Test
    public void testProgrssiveWHSubjectQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("la", "tarta"));
        p.setFeature(Feature.PROGRESSIVE, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién está comiendo la tarta", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());
    }

    /**
     * WH movement in the progressive
     */
    @Test
    public void testProgrssiveWHObjectQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("la", "tarta"));
        p.setFeature(Feature.PROGRESSIVE, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("qué está comiendo Mary", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());

        // AG -- need to check this; it doesn't work
        // p.setFeature(Feature.NEGATED, true);
        //		Assert.assertEquals("what is Mary not eating", //$NON-NLS-1$
        // this.realiser.realise(p).getRealisation());

    }

    /**
     * Negation with WH movement for subject
     */
    @Test
    public void testNegatedWHSubjQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("la", "tarta"));
        p.setFeature(Feature.NEGATED, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién no come la tarta", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());
    }

    /**
     * Negation with WH movement for object
     */
    @Test
    public void testNegatedWHObjQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("la", "tarta"));
        p.setFeature(Feature.NEGATED, true);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        NLGElement realisation = this.realiser.realise(p);
        Assert.assertEquals("qué no come Mary", //$NON-NLS-1$
                realisation.getRealisation());
    }

    /**
     * Test questyions in the tutorial.
     */
    @Test
    public void testTutorialQuestions() {
        setUp();
        this.realiser.setLexicon(this.lexicon);

        PhraseElement p = this.phraseFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", "George")); //$NON-NLS-1$
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("persigue Mary a George", this.realiser.realise(p) //$NON-NLS-1$
                .getRealisation());

        p = this.phraseFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", "George")); //$NON-NLS-1$
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién persigue Mary", this.realiser.realise(p) //$NON-NLS-1$
                .getRealisation());

    }

    /**
     * Subject WH Questions with modals
     */
    @Test
    public void testModalWHSubjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("el perro enfadó al hombre", this.realiser.realise(p)
                .getRealisation());

        // first without modal
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién enfadó al hombre", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué enfadó al hombre", this.realiser.realise(p)
                .getRealisation());

        // now with modal auxiliary
        p.setFeature(Feature.MODAL, "poder");

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién puede haber enfadado al hombre", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        Assert.assertEquals("quién podrá enfadar al hombre", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué puede haber enfadado al hombre", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        Assert.assertEquals("qué podrá enfadar al hombre", this.realiser.realise(p)
                .getRealisation());
    }

    /**
     * Subject WH Questions with modals
     */
    @Test
    public void testModalWHObjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);

        Assert.assertEquals("a quién enfadó el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.MODAL, "poder");
        Assert.assertEquals("a quién puede haber enfadado el perro", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a qué puede haber enfadado el perro", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién podrá enfadar el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a qué podrá enfadar el perro", this.realiser.realise(p)
                .getRealisation());
    }

    /**
     * Questions with tenses requiring auxiliaries + subject WH
     */
    @Test
    public void testAuxWHSubjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PRESENT);
        p.setFeature(Feature.PERFECT, true);
        Assert.assertEquals("el perro ha enfadado al hombre",
                this.realiser.realise(p).getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién ha enfadado al hombre", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué ha enfadado al hombre", this.realiser.realise(p)
                .getRealisation());
    }

    /**
     * Questions with tenses requiring auxiliaries + subject WH
     */
    @Test
    public void testAuxWHObjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));

        // first without any aux
        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a qué enfadó el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién enfadó el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.TENSE, Tense.PRESENT);
        p.setFeature(Feature.PERFECT, true);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién ha enfadado el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a qué ha enfadado el perro", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        p.setFeature(Feature.PERFECT, true);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quién habrá enfadado el perro", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a qué habrá enfadado el perro", this.realiser
                .realise(p).getRealisation());

    }

    /**
     * Test for questions with "be"
     */
    @Test
    public void testBeQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("una", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "juguete"));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("qué es una pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("es una pelota un juguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué es un juguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por qué es Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("dónde es Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién es bonita", this.realiser.realise(p2)
                .getRealisation());
    }

    /**
     * Test for questions with "be" in future tense
     */
    @Test
    public void testBeQuestionsFuture() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("una", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "juguete"));
        p.setFeature(Feature.TENSE, Tense.FUTURE);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("qué será una pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("será una pelota un juguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué será un juguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.TENSE, Tense.FUTURE);
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por qué será Mary bonita", this.realiser
                .realise(p2).getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("dónde será Mary bonita", this.realiser
                .realise(p2).getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién será bonita", this.realiser.realise(p2)
                .getRealisation());
    }

    /**
     * Tests for WH questions with be in past tense
     */
    @Test
    public void testBeQuestionsPast() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("una", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "juguete"));
        p.setFeature(Feature.TENSE, Tense.PAST);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("qué fue una pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("fue una pelota un juguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("qué fue un juguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.TENSE, Tense.PAST);
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por qué fue Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("dónde fue Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quién fue bonita", this.realiser.realise(p2)
                .getRealisation());
    }


    /**
     * Test WHERE, HOW and WHY questions, with copular predicate "be"
     */
    public void testSimpleBeWHQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause("yo", "estar");

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("¿Dónde estoy yo?", realiser.realiseSentence(p));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("¿Por qué estoy yo?", realiser.realiseSentence(p));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        Assert.assertEquals("¿Cómo estoy yo?", realiser.realiseSentence(p));

    }

    /**
     * Test a simple "how" question, based on query from Albi Oxa
     */
    @Test
    public void testHowPredicateQuestion() {
        SPhraseSpec test = this.phraseFactory.createClause();
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("tú");

        subject.setFeature(Feature.PRONOMINAL, true);
        subject.setFeature(Feature.PERSON, Person.SECOND);
        test.setSubject(subject);
        test.setVerb("estar");

        test.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.HOW_PREDICATE);
        test.setFeature(Feature.TENSE, Tense.PRESENT);

        String result = realiser.realiseSentence(test);
        Assert.assertEquals("¿Cómo estás tú?", result);

    }

    /**
     * Case 1 checks that "What do you think about John?" can be generated.
     * <p>
     * Case 2 checks that the same clause is generated, even when an object is
     * declared.
     */
    @Test
    public void testWhatObjectInterrogative() {
        Lexicon lexicon = new XMLLexicon();
        NLGFactory nlg = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);

        // Case 1, no object is explicitly given:
        SPhraseSpec clause = nlg.createClause("tú", "pensar");
        PPPhraseSpec aboutJohn = nlg.createPrepositionPhrase("acerca de", "John");
        clause.addPostModifier(aboutJohn);
        clause.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        String realisation = realiser.realiseSentence(clause);
        System.out.println(realisation);
        Assert.assertEquals("¿Qué piensas tú acerca de John?", realisation);

        // Case 2:
        // Add "bad things" as the object so the object doesn't remain null:
        clause.setObject("cosas malas");
        realisation = realiser.realiseSentence(clause);
        System.out.println(realiser.realiseSentence(clause));
        Assert.assertEquals("¿Qué piensas tú acerca de John?", realisation);
    }
}
