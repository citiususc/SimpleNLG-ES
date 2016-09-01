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
package simplenlg.external.spanish;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.spanish.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests from third parties
 *
 * @author ereiter
 */
public class ExternalTest {


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
     * Basic tests
     */
    @Test
    public void forcherTest() {
        // Bjorn Forcher's tests
        this.phraseFactory.setLexicon(this.lexicon);
        PhraseElement s1 = this.phraseFactory.createClause(null, "asociar",
                "Marie");
        ((SPhraseSpec) s1).getObject().setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        s1.setFeature(Feature.PASSIVE, true);
        PhraseElement pp1 = this.phraseFactory.createPrepositionPhrase("con"); //$NON-NLS-1$
        pp1.addComplement("Peter"); //$NON-NLS-1$
        pp1.addComplement("Paul"); //$NON-NLS-1$
        s1.addPostModifier(pp1);

        Assert.assertEquals("Marie es asociada con Peter y Paul", //$NON-NLS-1$
                this.realiser.realise(s1).getRealisation());
        SPhraseSpec s2 = this.phraseFactory.createClause();
        s2.setSubject(this.phraseFactory
                .createNounPhrase("Peter")); //$NON-NLS-1$
        s2.setVerb("tener"); //$NON-NLS-1$
        s2.setObject("algo que ver"); //$NON-NLS-1$
        s2.addPostModifier(this.phraseFactory.createPrepositionPhrase(
                "con", "Paul")); //$NON-NLS-1$ //$NON-NLS-2$


        Assert.assertEquals("Peter tiene algo que ver con Paul", //$NON-NLS-1$
                this.realiser.realise(s2).getRealisation());
    }

    @Test
    public void luTest() {
        // Xin Lu's test
        this.phraseFactory.setLexicon(this.lexicon);
        PhraseElement s1 = this.phraseFactory.createClause("nosotros", //$NON-NLS-1$
                "considerar", //$NON-NLS-1$
                "a John"); //$NON-NLS-1$
        s1.addPostModifier("un amigo"); //$NON-NLS-1$

        Assert.assertEquals("nosotros consideramos a John un amigo", this.realiser //$NON-NLS-1$
                .realise(s1).getRealisation());
    }

    @Test
    public void dwightTest() {
        // Rachel Dwight's test
        this.phraseFactory.setLexicon(this.lexicon);

        NPPhraseSpec noun4 = this.phraseFactory
                .createNounPhrase("gen FGFR3 en todas las células"); //$NON-NLS-1$

        noun4.setSpecifier("el");

        PhraseElement prep1 = this.phraseFactory.createPrepositionPhrase(
                "de", noun4); //$NON-NLS-1$

        PhraseElement noun1 = this.phraseFactory.createNounPhrase(
                "la", "madre del paciente"); //$NON-NLS-1$ //$NON-NLS-2$
        noun1.setFeature(LexicalFeature.GENDER, Gender.FEMININE);

        PhraseElement noun2 = this.phraseFactory.createNounPhrase(
                "el", "padre del paciente"); //$NON-NLS-1$ //$NON-NLS-2$

        PhraseElement noun3 = this.phraseFactory
                .createNounPhrase("copia cambiada"); //$NON-NLS-1$
        noun3.addPreModifier("una"); //$NON-NLS-1$
        noun3.addComplement(prep1);

        CoordinatedPhraseElement coordNoun1 = new CoordinatedPhraseElement(
                noun1, noun2);
        coordNoun1.setConjunction("o"); //$NON-NLS-1$

        PhraseElement verbPhrase1 = this.phraseFactory.createVerbPhrase("tener"); //$NON-NLS-1$
        verbPhrase1.setFeature(Feature.TENSE, Tense.PRESENT);

        PhraseElement sentence1 = this.phraseFactory.createClause(coordNoun1,
                verbPhrase1, noun3);

        realiser.setDebugMode(true);
        Assert
                .assertEquals(
                        "la madre del paciente o el padre del paciente tiene una copia cambiada del gen FGFR3 en todas las células", //$NON-NLS-1$
                        this.realiser.realise(sentence1).getRealisation());

        // Rachel's second test
        noun3 = this.phraseFactory.createNounPhrase("un", "test de genes"); //$NON-NLS-1$ //$NON-NLS-2$
        noun2 = this.phraseFactory.createNounPhrase("un", "test de LDL"); //$NON-NLS-1$ //$NON-NLS-2$
        noun1 = this.phraseFactory.createNounPhrase("el", "clínico"); //$NON-NLS-1$ //$NON-NLS-2$
        verbPhrase1 = this.phraseFactory.createVerbPhrase("realizar"); //$NON-NLS-1$

        CoordinatedPhraseElement coord1 = this.phraseFactory.createCoordinatedPhrase(noun2,
                noun3);
        sentence1 = this.phraseFactory.createClause(noun1, verbPhrase1, coord1);
        sentence1.setFeature(Feature.TENSE, Tense.PAST);

        Assert
                .assertEquals(
                        "el clínico realizó un test de LDL y un test de genes", this.realiser //$NON-NLS-1$
                                .realise(sentence1).getRealisation());
    }

    @Test
    public void novelliTest() {
        // Nicole Novelli's test
        PhraseElement p = this.phraseFactory.createClause(
                "Mary", "perseguir", "a George"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        PhraseElement pp = this.phraseFactory.createPrepositionPhrase(
                "en", "el parque"); //$NON-NLS-1$ //$NON-NLS-2$
        p.addPostModifier(pp);

        Assert.assertEquals("Mary persigue a George en el parque", this.realiser //$NON-NLS-1$
                .realise(p).getRealisation());

        // another question from Nicole
        SPhraseSpec run = this.phraseFactory.createClause(
                "tú", "ir", "a correr"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        run.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        run.addFrontModifier("realmente"); //$NON-NLS-1$
        SPhraseSpec think = this.phraseFactory.createClause("yo", "creer"); //$NON-NLS-1$ //$NON-NLS-2$
        think.setObject(run);

        String text = this.realiser.realise(think).getRealisation();
        Assert.assertEquals("yo creo que realmente tú debes ir a correr", text); //$NON-NLS-1$
    }

    @Test
    public void piotrekTest() {
        // Piotrek Smulikowski's test
        this.phraseFactory.setLexicon(this.lexicon);
        PhraseElement sent = this.phraseFactory.createClause(
                "yo", "disparar", "al pato"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        sent.setFeature(Feature.TENSE, Tense.PAST);

        PhraseElement loc = this.phraseFactory.createPrepositionPhrase(
                "en", "el campo de tiro"); //$NON-NLS-1$ //$NON-NLS-2$
        sent.addPostModifier(loc);
        sent.setFeature(Feature.CUE_PHRASE, "entonces"); //$NON-NLS-1$

        Assert.assertEquals("entonces yo disparé al pato en el campo de tiro", //$NON-NLS-1$
                this.realiser.realise(sent).getRealisation());
    }

    @Test
    public void prescottTest() {
        // Michael Prescott's test
        this.phraseFactory.setLexicon(this.lexicon);
        PPPhraseSpec embedded = this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createClause(
                "Jill", "pinchar", "a Spot")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        PhraseElement sent = this.phraseFactory.createClause(
                "Jack", "ver", embedded); //$NON-NLS-1$ //$NON-NLS-2$
        embedded.getObject().setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
        embedded.getObject().setFeature(Feature.FORM, Form.BARE_INFINITIVE);

        Assert.assertEquals("Jack ve a Jill pinchar a Spot", this.realiser //$NON-NLS-1$
                .realise(sent).getRealisation());
    }

    @Test
    public void wissnerTest() {
        // Michael Wissner's text
        PhraseElement p = this.phraseFactory.createClause("un lobo", "comer"); //$NON-NLS-1$ //$NON-NLS-2$
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("qué come un lobo", this.realiser.realise(p) //$NON-NLS-1$
                .getRealisation());

    }

    @Test
    public void phanTest() {
        // Thomas Phan's text
        PhraseElement subjectElement = this.phraseFactory.createNounPhrase("yo");
        PhraseElement verbElement = this.phraseFactory.createVerbPhrase("correr");

        PhraseElement prepPhrase = this.phraseFactory.createPrepositionPhrase("desde");
        prepPhrase.addComplement("casa");

        verbElement.addComplement(prepPhrase);
        SPhraseSpec newSentence = this.phraseFactory.createClause();
        newSentence.setSubject(subjectElement);
        newSentence.setVerbPhrase(verbElement);

        Assert.assertEquals("yo corro desde casa", this.realiser.realise(newSentence) //$NON-NLS-1$
                .getRealisation());

    }

    @Test
    public void kerberTest() {
        // Frederic Kerber's tests
        SPhraseSpec sp = this.phraseFactory.createClause("él", "necesitar");
        SPhraseSpec secondSp = this.phraseFactory.createClause();
        secondSp.setVerb("contruir");
        secondSp.setObject("una casa");
        secondSp.setFeature(Feature.FORM, Form.INFINITIVE);
        secondSp.setPreModifier("para");
        sp.setObject("piedra");
        sp.addComplement(secondSp);
        Assert.assertEquals("él necesita piedra para contruir una casa", this.realiser.realise(sp).getRealisation());

        SPhraseSpec sp2 = this.phraseFactory.createClause("él", "dar");
        sp2.setIndirectObject("yo");
        sp2.setObject("el libro");
        Assert.assertEquals("él me da el libro", this.realiser.realise(sp2).getRealisation());

    }

    @Test
    public void stephensonTest() {
        // Bruce Stephenson's test
        SPhraseSpec qs2 = this.phraseFactory.createClause();
        qs2 = this.phraseFactory.createClause();
        qs2.setSubject("moles de oro");
        qs2.setVerb("ser");
        qs2.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        qs2.setFeature(Feature.PASSIVE, false);
        qs2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW_MANY);
        qs2.setObject("una muestra de 2.50 g de oro puro");
        DocumentElement sentence = this.phraseFactory.createSentence(qs2);
        Assert.assertEquals("¿Cuántos moles de oro son una muestra de 2.50 g de oro puro?", this.realiser.realise(sentence).getRealisation());
    }

    @Test
    public void pierreTest() {
        // John Pierre's test
        SPhraseSpec p = this.phraseFactory.createClause("Mary", "perseguir", "a George");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("¿Qué persigue Mary?", realiser.realiseSentence(p));

        p = this.phraseFactory.createClause("Mary", "perseguir", "a George");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("¿Persigue Mary a George?", realiser.realiseSentence(p));

        p = this.phraseFactory.createClause("Mary", "perseguir", "a George");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("¿Dónde persigue Mary a George?", realiser.realiseSentence(p));

        p = this.phraseFactory.createClause("Mary", "perseguir", "a George");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("¿Por qué persigue Mary a George?", realiser.realiseSentence(p));

        p = this.phraseFactory.createClause("Mary", "perseguir", "a George");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        Assert.assertEquals("¿Cómo persigue Mary a George?", realiser.realiseSentence(p));


    }

    @Test
    public void data2TextTest() {
        // Data2Text tests
        // test OK to have number at end of sentence
        SPhraseSpec p = this.phraseFactory.createClause("el perro", "pesar", "12");
        Assert.assertEquals("El perro pesa 12.", realiser.realiseSentence(p));

        // test OK to have "there be" sentence with "there" as a StringElement
        NLGElement dataDropout2 = this.phraseFactory.createNLGElement("pérdidas de datos");
        dataDropout2.setPlural(true);
        SPhraseSpec sentence2 = this.phraseFactory.createClause();
//        sentence2.setSubject(this.phraseFactory.createStringElement("there"));
        sentence2.setVerb("haber");
        sentence2.setObject(dataDropout2);
        Assert.assertEquals("Hay pérdidas de datos.", realiser.realiseSentence(sentence2));

        // test OK to have gerund form verb
        SPhraseSpec weather1 = this.phraseFactory.createClause("SE 10-15", "virar", "a S 15-20");
        weather1.setFeature(Feature.FORM, Form.GERUND);
        Assert.assertEquals("SE 10-15 virando a S 15-20.", realiser.realiseSentence(weather1));

        // test OK to have subject only
        SPhraseSpec weather2 = this.phraseFactory.createClause("nublado y con niebla", "estar", "XXX");
        weather2.getVerbPhrase().setFeature(Feature.ELIDED, true);
        Assert.assertEquals("Nublado y con niebla.", realiser.realiseSentence(weather2));

        // test OK to have VP only
        SPhraseSpec weather3 = this.phraseFactory.createClause("S 15-20", "incrementar", "a 20-25");
        weather3.setFeature(Feature.FORM, Form.GERUND);
        weather3.getSubject().setFeature(Feature.ELIDED, true);
        Assert.assertEquals("Incrementando a 20-25.", realiser.realiseSentence(weather3));

        // conjoined test
        SPhraseSpec weather4 = this.phraseFactory.createClause("S 20-25", "retroceder", "a SSE");
        weather4.setFeature(Feature.FORM, Form.GERUND);
        weather4.getSubject().setFeature(Feature.ELIDED, true);

        CoordinatedPhraseElement coord = new CoordinatedPhraseElement();
        coord.addCoordinate(weather1);
        coord.addCoordinate(weather3);
        coord.addCoordinate(weather4);
        coord.setConjunction("y");
        Assert.assertEquals("SE 10-15 virando a S 15-20, incrementando a 20-25 y retrocediendo a SSE.", realiser.realiseSentence(coord));


        // no verb
        SPhraseSpec weather5 = this.phraseFactory.createClause("lluvia", null, "probable");
        Assert.assertEquals("Lluvia probable.", realiser.realiseSentence(weather5));

    }

    @Test
    public void rafaelTest() {
        // Rafael Valle's tests
        List<NLGElement> ss = new ArrayList<NLGElement>();
        ClauseCoordinationRule coord = new ClauseCoordinationRule();
        coord.setFactory(this.phraseFactory);

        ss.add(this.agreePhrase("John Lennon")); // john lennon agreed with it
        ss.add(this.disagreePhrase("Geri Halliwell")); // Geri Halliwell disagreed with it
        ss.add(this.commentPhrase("Melanie B")); // Mealnie B commented on it
        ss.add(this.agreePhrase("tú")); // you agreed with it
        ss.add(this.commentPhrase("Emma Bunton")); //Emma Bunton commented on it

        List<NLGElement> results = coord.apply(ss);
        List<String> ret = this.realizeAll(results);
        Assert.assertEquals("[John Lennon y tú coincidisteis con eso, Geri Halliwell discrepó con eso, Melanie B y Emma Bunton comentaron sobre eso]", ret.toString());
    }

    private NLGElement commentPhrase(String name) {  // used by testRafael
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setSubject(this.phraseFactory.createNounPhrase(name));
        s.setVerbPhrase(this.phraseFactory.createVerbPhrase("comentar sobre"));
        s.setObject("eso");
        s.setFeature(Feature.TENSE, Tense.PAST);
        return s;
    }

    private NLGElement agreePhrase(String name) {  // used by testRafael
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setSubject(this.phraseFactory.createNounPhrase(name));
        s.setVerbPhrase(this.phraseFactory.createVerbPhrase("coincidir con"));
        s.setObject("eso");
        s.setFeature(Feature.TENSE, Tense.PAST);
        return s;
    }

    private NLGElement disagreePhrase(String name) {  // used by testRafael
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setSubject(this.phraseFactory.createNounPhrase(name));
        s.setVerbPhrase(this.phraseFactory.createVerbPhrase("discrepar con"));
        s.setObject("eso");
        s.setFeature(Feature.TENSE, Tense.PAST);
        return s;
    }

    private ArrayList<String> realizeAll(List<NLGElement> results) { // used by testRafael
        ArrayList<String> ret = new ArrayList<String>();
        for (NLGElement e : results) {
            String r = this.realiser.realise(e).getRealisation();
            ret.add(r);
        }
        return ret;
    }

    @Test
    public void wikipediaTest() {
        // test code fragments in wikipedia
        // realisation
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("la", "mujer");
        subject.setPlural(true);
        SPhraseSpec sentence = this.phraseFactory.createClause(subject, "fumar");
        sentence.setFeature(Feature.NEGATED, true);
        Assert.assertEquals("Las mujeres no fuman.", realiser.realiseSentence(sentence));

        // aggregation
        SPhraseSpec s1 = this.phraseFactory.createClause("el hombre", "estar", "hambriento");
        SPhraseSpec s2 = this.phraseFactory.createClause("el hombre", "comprar", "una manzana");
        ClauseCoordinationRule ccr = new ClauseCoordinationRule();
        ccr.setFactory(this.phraseFactory);
        NLGElement result = ccr.apply(s1, s2);
        Assert.assertEquals("El hombre está hambriento y compra una manzana.", realiser.realiseSentence(result));

    }

    @Test
    public void leanTest() {
        // A Lean's test
        SPhraseSpec sentence = this.phraseFactory.createClause();
        sentence.setVerb("ser");
        sentence.setObject("una pelota");
        sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("¿Qué es una pelota?", realiser.realiseSentence(sentence));

        sentence = this.phraseFactory.createClause();
        sentence.setVerb("ser");
        sentence.setPlural(true);
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("ejemplo");
        object.setPlural(true);
        object.addModifier("de trabajos");
        sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        sentence.setObject(object);
        Assert.assertEquals("¿Qué son ejemplos de trabajos?", realiser.realiseSentence(sentence));

        SPhraseSpec p = this.phraseFactory.createClause();
        NPPhraseSpec sub1 = this.phraseFactory.createNounPhrase("Mary");

        sub1.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        sub1.setFeature(Feature.PRONOMINAL, true);
        sub1.setFeature(Feature.PERSON, Person.FIRST);
        p.setSubject(sub1);
        p.setVerb("perseguir");
        p.setObject("el mono");


        String output2 = this.realiser.realiseSentence(p); // Realiser created earlier. 
        Assert.assertEquals("Yo persigo el mono.", output2);

        SPhraseSpec test = this.phraseFactory.createClause();
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("Mary");

        subject.setFeature(Feature.PRONOMINAL, true);
        subject.setFeature(Feature.PERSON, Person.SECOND);
        test.setSubject(subject);
        test.setVerb("llorar");

        test.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        test.setFeature(Feature.TENSE, Tense.PRESENT);
        Assert.assertEquals("¿Por qué lloras tú?", realiser.realiseSentence(test));

        test = this.phraseFactory.createClause();
        subject = this.phraseFactory.createNounPhrase("Mary");

        subject.setFeature(Feature.PRONOMINAL, true);
        subject.setFeature(Feature.PERSON, Person.SECOND);
        test.setSubject(subject);
        test.setVerb("estar");
        test.setObject("llorando");

        test.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        test.setFeature(Feature.TENSE, Tense.PRESENT);
        Assert.assertEquals("¿Por qué estás tú llorando?", realiser.realiseSentence(test));


    }

    @Test
    public void kalijurandTest() {
        // K Kalijurand's test
        String lemma = "caminar";


        WordElement word = this.lexicon.lookupWord(lemma, LexicalCategory.VERB);
        InflectedWordElement inflectedWord = new InflectedWordElement(word);

        inflectedWord.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
        String form = realiser.realise(inflectedWord).getRealisation();
        Assert.assertEquals("caminado", form);


        inflectedWord = new InflectedWordElement(word);

        inflectedWord.setFeature(Feature.PERSON, Person.THIRD);
        form = realiser.realise(inflectedWord).getRealisation();
        Assert.assertEquals("camina", form);


    }

    @Test
    public void layTest() {
        // Richard Lay's test
        String lemma = "abofetear";

        WordElement word = this.lexicon.lookupWord(lemma, LexicalCategory.VERB);
        InflectedWordElement inflectedWord = new InflectedWordElement(word);
        inflectedWord.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
        String form = realiser.realise(inflectedWord).getRealisation();
        Assert.assertEquals("abofeteando", form);


        VPPhraseSpec v = this.phraseFactory.createVerbPhrase("abofetear");
        v.setFeature(Feature.PROGRESSIVE, true);
        String progressive = this.realiser.realise(v).getRealisation();
        Assert.assertEquals("está abofeteando", progressive);
    }
}
