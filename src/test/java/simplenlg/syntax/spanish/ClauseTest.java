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
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.phrasespec.*;

// TODO: Auto-generated Javadoc

/**
 * The Class STest.
 */
public class ClauseTest extends SimpleNLG4Test {

    // set up a few more fixtures
    /**
     * The s4.
     */
    SPhraseSpec s1, s2, s3, s4;

    /**
     * Instantiates a new s test.
     *
     * @param name the name
     */
    public ClauseTest(String name) {
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

        // the woman kisses the man
        this.s1 = this.phraseFactory.createClause();
        this.s1.setSubject(this.woman);
        this.s1.setVerbPhrase(this.kiss);
        this.s1.setObject(this.phraseFactory.createPrepositionPhrase("a", this.man));

        // there is the dog on the rock
        this.s2 = this.phraseFactory.createClause();
        this.s2.setVerb("haber"); //$NON-NLS-1$
        this.s2.setObject(this.dog);
        this.s2.addPostModifier(this.onTheRock);

        // the man gives the woman John's flower
        this.s3 = this.phraseFactory.createClause();
        this.s3.setSubject(this.man);
        this.s3.setVerbPhrase(this.give);

        NPPhraseSpec flor = this.phraseFactory.createNounPhrase("la", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flor.setPostModifier(deJohn);
        this.s3.setObject(flor);
        this.s3.setIndirectObject(this.woman);

        this.s4 = this.phraseFactory.createClause();
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$

        CoordinatedPhraseElement subject = this.phraseFactory
                .createCoordinatedPhrase(this.phraseFactory
                        .createNounPhrase("Jane"), this.phraseFactory //$NON-NLS-1$
                        .createNounPhrase("Andrew")); //$NON-NLS-1$

        this.s4.setSubject(subject);

        PhraseElement coger = this.phraseFactory.createVerbPhrase("coger"); //$NON-NLS-1$
        this.s4.setVerbPhrase(coger);
        this.s4.setObject("las pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
    }

    @After
    public void tearDown() {
        super.tearDown();

        this.s1 = null;
        this.s2 = null;
        this.s3 = null;
        this.s4 = null;
    }


    /**
     * Initial test for basic sentences.
     */
    @Test
    public void testBasic() {
        Assert.assertEquals("la mujer besa al hombre", this.realiser //$NON-NLS-1$
                .realise(this.s1).getRealisation());
        Assert.assertEquals("hay el perro en la roca", this.realiser //$NON-NLS-1$
                .realise(this.s2).getRealisation());

        setUp();
        Assert.assertEquals("el hombre da a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());
        Assert.assertEquals(
                "sin embargo mañana Jane y Andrew cogerán las pelotas en la tienda", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Test did not
     */
    @Test
    public void testDidNot() {
        PhraseElement s = phraseFactory.createClause("John", "comer");
        s.setFeature(Feature.TENSE, Tense.PAST);
        s.setFeature(Feature.NEGATED, true);

        Assert.assertEquals("John no comió", //$NON-NLS-1$
                this.realiser.realise(s).getRealisation());

    }

    /**
     * Test did not
     */
    @Test
    public void testVPNegation() {
        // negate the VP
        PhraseElement vp = phraseFactory.createVerbPhrase("descansar");
        vp.setFeature(Feature.TENSE, Tense.PAST);
        vp.setFeature(Feature.NEGATED, true);
        PhraseElement compl = phraseFactory.createVerbPhrase("eterizar");
        compl.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
        vp.setComplement(compl);

        SPhraseSpec s = phraseFactory.createClause(phraseFactory
                .createNounPhrase("el", "paciente"), vp);

        Assert.assertEquals("el paciente no descansó eterizado", //$NON-NLS-1$
                this.realiser.realise(s).getRealisation());

    }

    /**
     * Test that pronominal args are being correctly cast as NPs.
     */
//    @Test
//    public void testPronounArguments() {
//        // the subject of s2 should have been cast into a pronominal NP
//        NLGElement subj = this.s2.getFeatureAsElementList(
//                InternalFeature.SUBJECTS).get(0);
//        Assert.assertTrue(subj.isA(PhraseCategory.NOUN_PHRASE));
//        // Assert.assertTrue(LexicalCategory.PRONOUN.equals(((PhraseElement)
//        // subj)
//        // .getCategory()));
//    }

    /**
     * Tests for setting tense, aspect and passive from the sentence interface.
     */
    @Test
    public void testTenses() {
        // simple past
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("el hombre dio a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // perfect
        this.s3.setFeature(Feature.PERFECT, true);
        Assert.assertEquals("el hombre ha dado a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // negation
        this.s3.setFeature(Feature.NEGATED, true);
        Assert.assertEquals("el hombre no ha dado a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        this.s3.setFeature(Feature.PROGRESSIVE, true);
        Assert.assertEquals(
                "el hombre no ha estado dando a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // passivisation with direct and indirect object
        this.s3.setFeature(Feature.PASSIVE, true);
        // Assert.assertEquals(
        //				"John's flower had not been being given the woman by the man", //$NON-NLS-1$
        // this.realiser.realise(this.s3).getRealisation());
    }

    /**
     * Test what happens when a sentence is subordinated as complement of a
     * verb.
     */
    @Test
    public void testSubordination() {

        // subordinate sentence by setting it as complement of a verb
        this.say.addComplement(this.s3);

        // check the getter
        Assert.assertEquals(ClauseStatus.SUBORDINATE, this.s3
                .getFeature(InternalFeature.CLAUSE_STATUS));

        // check realisation
        Assert.assertEquals("dice que el hombre da a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());
    }

    /**
     * Test the various forms of a sentence, including subordinates.
     */
    /**
     *
     */
    @Test
    public void testForm() {

        // check the getter method
        Assert.assertEquals(Form.NORMAL, this.s1.getFeatureAsElement(
                InternalFeature.VERB_PHRASE).getFeature(Feature.FORM));

        // infinitive
        this.s1.setFeature(Feature.FORM, Form.INFINITIVE);
        Assert
                .assertEquals(
                        "besar al hombre", this.realiser.realise(this.s1).getRealisation()); //$NON-NLS-1$

        // gerund with "there"
//        this.s2.setFeature(Feature.FORM, Form.GERUND);
//        Assert.assertEquals("there being the dog on the rock", this.realiser //$NON-NLS-1$
//                .realise(this.s2).getRealisation());

        // gerund with possessive
        this.s3.setFeature(Feature.PROGRESSIVE, true);
        Assert.assertEquals("el hombre está dando a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());
        this.s3.setFeature(Feature.PROGRESSIVE, false);

        // imperative
        this.s3.setFeature(Feature.FORM, Form.IMPERATIVE);

        Assert.assertEquals("dé a la mujer la flor de John", this.realiser //$NON-NLS-1$
                .realise(this.s3).getRealisation());

        // subordinating the imperative to a verb should turn it to subjunctive
        this.say.addComplement(this.s3);

        Assert.assertEquals("dice que el hombre dé a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.say).getRealisation());

        // imperative -- case II
        this.s4.setFeature(Feature.FORM, Form.IMPERATIVE);
        Assert.assertEquals("sin embargo mañana cojan las pelotas en la tienda", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // infinitive -- case II
        this.s4 = this.phraseFactory.createClause();
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$

        CoordinatedPhraseElement subject = new CoordinatedPhraseElement(
                this.phraseFactory.createNounPhrase("Jane"), this.phraseFactory //$NON-NLS-1$
                .createNounPhrase("Andrew")); //$NON-NLS-1$

        this.s4.setFeature(InternalFeature.SUBJECTS, subject);

        PhraseElement pick = this.phraseFactory.createVerbPhrase("coger"); //$NON-NLS-1$
        this.s4.setFeature(InternalFeature.VERB_PHRASE, pick);
        this.s4.setObject("las pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.FORM, Form.INFINITIVE);
        Assert.assertEquals(
                "sin embargo coger las pelotas en la tienda mañana", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Slightly more complex tests for forms.
     */
    @Test
    public void testForm2() {
        // set s4 as subject of a new sentence
        SPhraseSpec temp = this.phraseFactory.createClause(this.s4, "ser", //$NON-NLS-1$
                "recomendado"); //$NON-NLS-1$

        Assert.assertEquals(
                "sin embargo que Jane y Andrew cojan las " + //$NON-NLS-1$
                        "pelotas en la tienda mañana es recomendado", //$NON-NLS-1$
                this.realiser.realise(temp).getRealisation());

        // compose this with a new sentence
        // ER - switched direct and indirect object in sentence
        SPhraseSpec temp2 = this.phraseFactory.createClause("yo", "contar", temp); //$NON-NLS-1$ //$NON-NLS-2$
        temp2.setFeature(Feature.TENSE, Tense.FUTURE);

        PhraseElement indirectObject = this.phraseFactory
                .createNounPhrase("John"); //$NON-NLS-1$

        temp2.setIndirectObject(indirectObject);

        Assert.assertEquals("yo contaré a John que sin embargo que Jane y " + //$NON-NLS-1$
                        "Andrew cojan las pelotas en la tienda mañana es " + //$NON-NLS-1$
                        "recomendado", //$NON-NLS-1$
                this.realiser.realise(temp2).getRealisation());

        // turn s4 to imperative and put it in indirect object position

        this.s4 = this.phraseFactory.createClause();
        this.s4.setFeature(Feature.CUE_PHRASE, "sin embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañana"); //$NON-NLS-1$

        CoordinatedPhraseElement subject = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), this.phraseFactory //$NON-NLS-1$
                        .createNounPhrase("Andrew")); //$NON-NLS-1$

        this.s4.setSubject(subject);

        PhraseElement pick = this.phraseFactory.createVerbPhrase("coger"); //$NON-NLS-1$
        this.s4.setVerbPhrase(pick);
        this.s4.setObject("las pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("en la tienda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.FORM, Form.IMPERATIVE);

        temp2 = this.phraseFactory.createClause("yo", "contar", this.s4); //$NON-NLS-1$ //$NON-NLS-2$
        indirectObject = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        temp2.setIndirectObject(indirectObject);
        temp2.setFeature(Feature.TENSE, Tense.FUTURE);

        Assert.assertEquals("yo contaré a John que sin embargo Jane y Andrew cojan las pelotas " //$NON-NLS-1$
                + "en la tienda mañana", this.realiser.realise(temp2) //$NON-NLS-1$
                .getRealisation());
    }

    /**
     * Tests for gerund forms and genitive subjects.
     */
    @Test
    public void testGerundsubject() {

        // the man's giving the woman John's flower upset Peter
        SPhraseSpec _s4 = this.phraseFactory.createClause();
        _s4.setVerbPhrase(this.phraseFactory.createVerbPhrase("enfadar")); //$NON-NLS-1$
        _s4.setFeature(Feature.TENSE, Tense.PAST);
        _s4.setIndirectObject(this.phraseFactory.createNounPhrase("Peter")); //$NON-NLS-1$
        this.s3.setFeature(Feature.PERFECT, true);

        // set the sentence as subject of another: makes it a gerund
        _s4.setSubject(this.s3);

        // suppress the genitive realisation of the NP subject in gerund
        // sentences
        this.s3.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, true);

        // check the realisation: subject should not be genitive
        Assert.assertEquals(
                "que el hombre haya dado a la mujer la flor de John enfadó a Peter", //$NON-NLS-1$
                this.realiser.realise(_s4).getRealisation());

    }

    /**
     * Some tests for multiple embedded sentences.
     */
    @Test
    public void testComplexSentence1() {
        setUp();
        // the man's giving the woman John's flower upset Peter
        SPhraseSpec complexS = this.phraseFactory.createClause();
        complexS.setVerbPhrase(this.phraseFactory.createVerbPhrase("enfadar")); //$NON-NLS-1$
        complexS.setFeature(Feature.TENSE, Tense.PAST);
        complexS.setIndirectObject(this.phraseFactory.createNounPhrase("Peter")); //$NON-NLS-1$
        this.s3.setFeature(Feature.PERFECT, true);
        complexS.setSubject(this.s3);

        // check the realisation: subject should be genitive
        Assert.assertEquals(
                "que el hombre haya dado a la mujer la flor de John enfadó a Peter", //$NON-NLS-1$
                this.realiser.realise(complexS).getRealisation());

        setUp();
        // coordinate sentences in subject position
        SPhraseSpec s5 = this.phraseFactory.createClause();
        s5.setSubject(this.phraseFactory.createNounPhrase("alguna", "persona")); //$NON-NLS-1$ //$NON-NLS-2$
        s5.setVerbPhrase(this.phraseFactory.createVerbPhrase("acariciar")); //$NON-NLS-1$
        s5.setObject(this.phraseFactory.createNounPhrase("el", "gato")); //$NON-NLS-1$ //$NON-NLS-2$

        CoordinatedPhraseElement coord = this.phraseFactory.createCoordinatedPhrase(this.s3,
                s5);
        complexS = this.phraseFactory.createClause();
        complexS.setVerbPhrase(this.phraseFactory.createVerbPhrase("enfadar")); //$NON-NLS-1$
        complexS.setFeature(Feature.TENSE, Tense.PAST);
        complexS.setIndirectObject(this.phraseFactory.createNounPhrase("Peter")); //$NON-NLS-1$
        complexS.setSubject(coord);
        this.s3.setFeature(Feature.PERFECT, true);

        Assert.assertEquals("que el hombre haya dado a la mujer la flor de John " //$NON-NLS-1$
                        + "y que alguna persona acaricie el gato enfadaron a Peter", //$NON-NLS-1$
                this.realiser.realise(complexS).getRealisation());

        setUp();
        // now subordinate the complex sentence
        // coord.setClauseStatus(SPhraseSpec.ClauseType.MAIN);
        SPhraseSpec s6 = this.phraseFactory.createClause();
        s6.setVerbPhrase(this.phraseFactory.createVerbPhrase("contar")); //$NON-NLS-1$
        s6.setFeature(Feature.TENSE, Tense.PAST);
        s6.setSubject(this.phraseFactory.createNounPhrase("el", "niño")); //$NON-NLS-1$ //$NON-NLS-2$
        // ER - switched indirect and direct object
        NPPhraseSpec indirect = this.phraseFactory.createNounPhrase("todas", //$NON-NLS-1$
                "niñas"); //$NON-NLS-1$
        indirect.addSpecifier("las");
        indirect.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        indirect.setPlural(true);
        s6.setIndirectObject(indirect);
        complexS = this.phraseFactory.createClause();
        complexS.setVerbPhrase(this.phraseFactory.createVerbPhrase("enfadar")); //$NON-NLS-1$
        complexS.setFeature(Feature.TENSE, Tense.PAST);
        complexS.setIndirectObject(this.phraseFactory.createNounPhrase("Peter")); //$NON-NLS-1$
        s6.setObject(complexS);
        coord = this.phraseFactory.createCoordinatedPhrase(this.s3, s5);
        complexS.setSubject(coord);
        this.s3.setFeature(Feature.PERFECT, true);
        Assert.assertEquals(
                "el niño contó a todas las niñas que que el hombre haya dado a la mujer " //$NON-NLS-1$
                        + "la flor de John y que alguna persona acaricie el gato " //$NON-NLS-1$
                        + "enfadaron a Peter", //$NON-NLS-1$
                this.realiser.realise(s6).getRealisation());

    }

    /**
     * More coordination tests.
     */
    @Test
    public void testComplexSentence3() {
        setUp();

        this.s1 = this.phraseFactory.createClause();
        this.s1.setSubject(this.woman);
        this.s1.setVerb("besar");
        this.s1.setObject(this.phraseFactory.createPrepositionPhrase("a", this.man));

        PhraseElement _man = this.phraseFactory.createNounPhrase("el", "hombre"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause();
        this.s3.setSubject(_man);
        this.s3.setVerb("dar");

        NPPhraseSpec flower = this.phraseFactory.createNounPhrase("la", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flower.setPostModifier(deJohn);
        this.s3.setObject(flower);

        PhraseElement _woman = this.phraseFactory.createNounPhrase(
                "la", "mujer"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3.setIndirectObject(_woman);

        // the coordinate sentence allows us to raise and lower complementiser
        CoordinatedPhraseElement coord2 = this.phraseFactory.createCoordinatedPhrase(this.s1,
                this.s3);
        coord2.setFeature(Feature.TENSE, Tense.PAST);

        Assert
                .assertEquals(
                        "la mujer besó al hombre y el hombre dio a la mujer la flor de John", //$NON-NLS-1$
                        this.realiser.realise(coord2).getRealisation());
    }

    // /**
    // * Sentence with clausal subject with verb "be" and a progressive feature
    // */
    // public void testComplexSentence2() {
    // SPhraseSpec subject = this.phraseFactory.createClause(
    // this.phraseFactory.createNounPhrase("the", "child"),
    // this.phraseFactory.createVerbPhrase("be"), this.phraseFactory
    // .createWord("difficult", LexicalCategory.ADJECTIVE));
    // subject.setFeature(Feature.PROGRESSIVE, true);
    // }

    /**
     * Tests recogition of strings in API.
     */
    @Test
    public void testStringRecognition() {

        // test recognition of forms of "be"
        PhraseElement _s1 = this.phraseFactory.createClause(
                "mi gato", "estar", "triste"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Assert.assertEquals(
                "mi gato está triste", this.realiser.realise(_s1).getRealisation()); //$NON-NLS-1$

        // test recognition of pronoun for afreement
        PhraseElement _s2 = this.phraseFactory
                .createClause("yo", "querer", "a Mary"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        Assert.assertEquals(
                "yo quiero a Mary", this.realiser.realise(_s2).getRealisation()); //$NON-NLS-1$

        // test recognition of pronoun for correct form
        PhraseElement subject = this.phraseFactory.createNounPhrase("perro"); //$NON-NLS-1$
        subject.setFeature(InternalFeature.SPECIFIER, "un"); //$NON-NLS-1$
        subject.addPostModifier("de al lado"); //$NON-NLS-1$
        PhraseElement object = this.phraseFactory.createNounPhrase("yo"); //$NON-NLS-1$
        PhraseElement s = this.phraseFactory.createClause(subject,
                "perseguir", object); //$NON-NLS-1$
        s.setFeature(Feature.PROGRESSIVE, true);
        realiser.setDebugMode(true);
        Assert.assertEquals("un perro de al lado me está persiguiendo", //$NON-NLS-1$
                this.realiser.realise(s).getRealisation());
    }

    /**
     * Tests complex agreement.
     */
    @Test
    public void testAgreement() {

        // basic agreement
        NPPhraseSpec np = this.phraseFactory.createNounPhrase("perro"); //$NON-NLS-1$
        np.setSpecifier("el"); //$NON-NLS-1$
        np.addPostModifier("enfadado"); //$NON-NLS-1$
        PhraseElement _s1 = this.phraseFactory
                .createClause(np, "perseguir", "a John"); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("el perro enfadado persigue a John", this.realiser //$NON-NLS-1$
                .realise(_s1).getRealisation());

        // plural
        np = this.phraseFactory.createNounPhrase("perro"); //$NON-NLS-1$
        np.setSpecifier("el"); //$NON-NLS-1$
        np.addPostModifier("enfadado"); //$NON-NLS-1$
        np.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        _s1 = this.phraseFactory.createClause(np, "perseguir", "a John"); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("los perros enfadados persiguen a John", this.realiser //$NON-NLS-1$
                .realise(_s1).getRealisation());

        // test agreement with "there is"
        np = this.phraseFactory.createNounPhrase("perro"); //$NON-NLS-1$
        np.addPostModifier("enfadado"); //$NON-NLS-1$
        np.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        np.setSpecifier("un"); //$NON-NLS-1$
        PhraseElement _s2 = this.phraseFactory.createClause(null, "haber", np); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("hay un perro enfadado", this.realiser //$NON-NLS-1$
                .realise(_s2).getRealisation());

        // plural with "there"
        np = this.phraseFactory.createNounPhrase("perro"); //$NON-NLS-1$
        np.addPostModifier("enfadado"); //$NON-NLS-1$
        np.setSpecifier("un"); //$NON-NLS-1$
        np.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        _s2 = this.phraseFactory.createClause(null, "haber", np); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("hay unos perros enfadados", this.realiser //$NON-NLS-1$
                .realise(_s2).getRealisation());
    }

    /**
     * Tests passive.
     */
    @Test
    public void testPassive() {
        // passive with just complement
        SPhraseSpec _s1 = this.phraseFactory.createClause(null,
                "intubar", this.phraseFactory.createNounPhrase("el", "bebé")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        _s1.setFeature(Feature.PASSIVE, true);

        Assert.assertEquals("el bebé es intubado", this.realiser //$NON-NLS-1$
                .realise(_s1).getRealisation());

        // passive with subject and complement
        _s1 = this.phraseFactory.createClause(null,
                "intubar", this.phraseFactory.createNounPhrase("el", "bebé")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        _s1.setSubject(this.phraseFactory.createNounPhrase("la enfermera")); //$NON-NLS-1$
        _s1.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("el bebé es intubado por la enfermera", //$NON-NLS-1$
                this.realiser.realise(_s1).getRealisation());

        // passive with subject and indirect object
        SPhraseSpec _s2 = this.phraseFactory.createClause(null, "dar", //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("50ug de morfina")); //$NON-NLS-1$ //$NON-NLS-2$
        _s2.getObject().setPlural(true);

        PhraseElement morphine = this.phraseFactory
                .createNounPhrase("el", "bebé"); //$NON-NLS-1$
        _s2.setIndirectObject(morphine);
        _s2.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("50ug de morfina son dados al bebé", //$NON-NLS-1$
                this.realiser.realise(_s2).getRealisation());

        // passive with subject, complement and indirect object
        _s2 = this.phraseFactory.createClause(this.phraseFactory
                        .createNounPhrase("la", "enfermera"), "dar", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                this.phraseFactory.createNounPhrase("50ug de morfina")); //$NON-NLS-1$ //$NON-NLS-2$
        _s2.getObject().setPlural(true);
        _s2.getSubject().setFeature(LexicalFeature.GENDER, Gender.FEMININE);

        morphine = this.phraseFactory.createNounPhrase("el", "bebé"); //$NON-NLS-1$
        _s2.setIndirectObject(morphine);
        _s2.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("50ug de morfina son dados al bebé por la enfermera", //$NON-NLS-1$
                this.realiser.realise(_s2).getRealisation());

        // test agreement in passive
        PhraseElement _s3 = this.phraseFactory.createClause(
                this.phraseFactory.createCoordinatedPhrase("mi perro", "tu gato"), "perseguir", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "a George"); //$NON-NLS-1$
        _s3.setFeature(Feature.TENSE, Tense.PAST);
        _s3.addFrontModifier("ayer"); //$NON-NLS-1$
        Assert.assertEquals("ayer mi perro y tu gato persiguieron a George", //$NON-NLS-1$
                this.realiser.realise(_s3).getRealisation());

        _s3 = this.phraseFactory.createClause(this.phraseFactory.createCoordinatedPhrase(
                "mi perro", "tu gato"), "perseguir", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                this.phraseFactory.createNounPhrase("George")); //$NON-NLS-1$
        _s3.setFeature(Feature.TENSE, Tense.PAST);
        _s3.addFrontModifier("ayer"); //$NON-NLS-1$
        _s3.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals(
                "ayer George fue perseguido por mi perro y tu gato", //$NON-NLS-1$
                this.realiser.realise(_s3).getRealisation());

        // test correct pronoun forms
        PhraseElement _s4 = this.phraseFactory.createClause(this.phraseFactory
                        .createNounPhrase("él"), "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createNounPhrase("yo")); //$NON-NLS-1$
        Assert.assertEquals("él me persigue", this.realiser.realise(_s4) //$NON-NLS-1$
                .getRealisation());
        _s4 = this.phraseFactory
                .createClause(
                        this.phraseFactory.createNounPhrase("él"), "perseguir", this.phraseFactory.createNounPhrase("me")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        _s4.setFeature(Feature.PASSIVE, true);
        Assert
                .assertEquals(
                        "yo soy perseguido por él", this.realiser.realise(_s4).getRealisation()); //$NON-NLS-1$

        // same thing, but giving the S constructor "me". Should recognise
        // correct pro
        // anyway
        PhraseElement _s5 = this.phraseFactory
                .createClause("él", "perseguir", "yo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Assert.assertEquals(
                "él me persigue", this.realiser.realise(_s5).getRealisation()); //$NON-NLS-1$

        _s5 = this.phraseFactory.createClause("él", "perseguir", "yo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        _s5.setFeature(Feature.PASSIVE, true);
        Assert
                .assertEquals(
                        "yo soy perseguido por él", this.realiser.realise(_s5).getRealisation()); //$NON-NLS-1$
    }

    /**
     * Test that complements set within the VP are raised when sentence is
     * passivised.
     */
    @Test
    public void testPassiveWithInternalVPComplement() {
        PhraseElement vp = this.phraseFactory.createVerbPhrase(phraseFactory
                .createWord("enfadar", LexicalCategory.VERB));
        vp.addComplement(phraseFactory.createNounPhrase("el", "hombre"));
        PhraseElement _s6 = this.phraseFactory.createClause(phraseFactory
                .createNounPhrase("el", "niño"), vp);
        _s6.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("el niño enfadó el hombre", this.realiser.realise(
                _s6).getRealisation());

        _s6.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("el hombre fue enfadado por el niño", this.realiser
                .realise(_s6).getRealisation());
    }

    /**
     * Tests tenses with modals.
     */
    @Test
    public void testModal() {

        setUp();
        // simple modal in present tense
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        Assert.assertEquals("el hombre debe dar a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // modal + future -- uses present
        setUp();
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        this.s3.setFeature(Feature.TENSE, Tense.FUTURE);
        Assert.assertEquals("el hombre deberá dar a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // modal + present progressive
        setUp();
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        this.s3.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s3.setFeature(Feature.PROGRESSIVE, true);
        Assert.assertEquals("el hombre deberá estar dando a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // modal + past tense
        setUp();
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals(
                "el hombre debe haber dado a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

        // modal + past progressive
        setUp();
        this.s3.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        this.s3.setFeature(Feature.PROGRESSIVE, true);

        Assert.assertEquals(
                "el hombre debe haber estado dando a la mujer la flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());

    }

    /**
     * Test for passivisation with mdoals
     */
    @Test
    public void testModalWithPassive() {
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("la",
                "pizza");
        AdjPhraseSpec post = this.phraseFactory.createAdjectivePhrase("bueno");
        post.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        AdvPhraseSpec as = this.phraseFactory.createAdverbPhrase("como");
        as.addComplement(post);
        VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("clasificar");
        verb.addPostModifier(as);
        verb.addComplement(object);
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setVerbPhrase(verb);
        s.setFeature(Feature.MODAL, "poder");
        // s.setFeature(Feature.FORM, Form.INFINITIVE);
        s.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("la pizza puede ser clasificada como buena",
                this.realiser.realise(s).getRealisation());
    }

    @Test
    public void testPassiveWithPPCompl() {
        // passive with just complement
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("ola");
        subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("surfero");
        object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // add a PP complement
        PPPhraseSpec pp = this.phraseFactory.createPrepositionPhrase("a",
                this.phraseFactory.createNounPhrase("la", "costa"));
        pp.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.INDIRECT_OBJECT);
        _s1.addComplement(pp);

        _s1.setFeature(Feature.PASSIVE, true);

        Assert.assertEquals(
                "surferos son llevados a la costa por olas", this.realiser //$NON-NLS-1$
                        .realise(_s1).getRealisation());
    }

    @Test
    public void testPassiveWithPPMod() {
        // passive with just complement
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("ola");
        subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("surfero");
        object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // add a PP complement
        PPPhraseSpec pp = this.phraseFactory.createPrepositionPhrase("a",
                this.phraseFactory.createNounPhrase("la", "costa"));
        _s1.addPostModifier(pp);

        _s1.setFeature(Feature.PASSIVE, true);

        Assert.assertEquals(
                "surferos son llevados a la costa por olas", this.realiser //$NON-NLS-1$
                        .realise(_s1).getRealisation());
    }

    @Test
    public void testCuePhrase() {
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("ola");
        subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("surfero");
        object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // add a PP complement
        PPPhraseSpec pp = this.phraseFactory.createPrepositionPhrase("a",
                this.phraseFactory.createNounPhrase("la", "costa"));
        _s1.addPostModifier(pp);

        _s1.setFeature(Feature.PASSIVE, true);

        _s1.addFrontModifier("sin embargo");


        //without comma separation of cue phrase
        Assert.assertEquals(
                "sin embargo surferos son llevados a la costa por olas", this.realiser //$NON-NLS-1$
                        .realise(_s1).getRealisation());

        //with comma separation
        this.realiser.setCommaSepCuephrase(true);
        Assert.assertEquals(
                "sin embargo, surferos son llevados a la costa por olas", this.realiser //$NON-NLS-1$
                        .realise(_s1).getRealisation());
    }


    /**
     * Check that setComplement replaces earlier complements
     */
    @Test
    public void testSetComplement() {
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setSubject("yo");
        s.setVerb("ver");
        s.setObject("un perro");

        Assert.assertEquals("yo veo un perro", this.realiser.realise(s)
                .getRealisation());

        s.setObject("un gato");
        Assert.assertEquals("yo veo un gato", this.realiser.realise(s)
                .getRealisation());

        s.setObject("un lobo");
        Assert.assertEquals("yo veo un lobo", this.realiser.realise(s)
                .getRealisation());

    }


    /**
     * Test for subclauses involving WH-complements Based on a query by Owen
     * Bennett
     */
    @Test
    public void testSubclauses() {
        // Once upon a time, there was an Accountant, called Jeff, who lived in
        // a forest.

        // main sentence
        NPPhraseSpec acct = this.phraseFactory.createNounPhrase("un",
                "contable");

        // first postmodifier of "an accountant"
        VPPhraseSpec sub1 = this.phraseFactory.createVerbPhrase("llamar");
        sub1.addComplement("Jeff");
        sub1.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
        // this is an appositive modifier, which makes simplenlg put it between
        // commas
        sub1.setFeature(Feature.APPOSITIVE, true);
        acct.addPostModifier(sub1);

        // second postmodifier of "an accountant" is "who lived in a forest"
        SPhraseSpec sub2 = this.phraseFactory.createClause();
        VPPhraseSpec subvp = this.phraseFactory.createVerbPhrase("vivir");
        subvp.setFeature(Feature.TENSE, Tense.PAST);
        subvp.setComplement(this.phraseFactory.createPrepositionPhrase("en",
                this.phraseFactory.createNounPhrase("un", "bosque")));
        sub2.setVerbPhrase(subvp);
        // simplenlg can't yet handle wh-clauses in NPs, so we need to hack it
        // by setting the subject to "who"
        sub2.setSubject("que");
        acct.addPostModifier(sub2);

        // main sentence
        SPhraseSpec s = this.phraseFactory.createClause(null, "haber", acct);
        s.setFeature(Feature.TENSE, Tense.PAST);

        // add front modifier "once upon a time"
        s.addFrontModifier("una vez");

        Assert.assertEquals(
                "una vez hubo un contable, llamado Jeff, que vivió en un bosque",
                this.realiser.realise(s).getRealisation());

    }


}
