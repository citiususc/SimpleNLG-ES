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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.syntax.spanish;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * Tests that check that realization of different Features against NLGElements.
 *
 * @author François Portet
 */

public class FeatureTest extends SimpleNLG4Test {


    NLGFactory docFactory = new NLGFactory(this.lexicon);

    /**
     * Instantiates a new text spec test.
     *
     * @param name the name
     */
    public FeatureTest(String name) {
        super(name);
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        this.docFactory = null;
    }

    /**
     * Tests use of the Possessive Feature.
     */
    @Test
    public void testPossessiveFeature_PastTense() {
        this.phraseFactory.setLexicon(this.lexicon);
        this.realiser.setLexicon(this.lexicon);

        // Create the pronoun 'she'
        NLGElement she = phraseFactory.createWord("ella", LexicalCategory.PRONOUN);

        // Set possessive on the pronoun to make it 'her'
        she.setFeature(Feature.POSSESSIVE, true);

        // Create a noun phrase with the subject lover and the determiner
        // as she
        PhraseElement herLover = phraseFactory.createNounPhrase(she, "amante");

        // Create a clause to say 'he be her lover'
        PhraseElement clause = phraseFactory.createClause("él", "ser", herLover);

        // Add the cue phrase need the comma as orthography
        // currently doesn't handle this.
        // This could be expanded to be a noun phrase with determiner
        // 'two' and noun 'week', set to plural and with a premodifier of
        // 'after'
        clause.setFeature(Feature.CUE_PHRASE, "después de dos semanas,");

        // Add the 'for a fortnight' as a post modifier. Alternatively
        // this could be added as a prepositional phrase 'for' with a
        // complement of a noun phrase ('a' 'fortnight')
        clause.addPostModifier("durante una quincena");

        // Set 'be' to 'was' as past tense
        clause.setFeature(Feature.TENSE, Tense.PAST);

        // Add the clause to a sentence.
        DocumentElement sentence1 = docFactory.createSentence(clause);

        // Realise the sentence
        NLGElement realised = this.realiser.realise(sentence1);

        Assert.assertEquals("Después de dos semanas, él fue su amante durante una quincena.",
                realised.getRealisation());
    }

    /**
     * Basic tests.
     */
    @Test
    public void testTwoPossessiveFeature_PastTense() {
        this.phraseFactory.setLexicon(this.lexicon);

        // Create the pronoun 'she'
        NLGElement she = phraseFactory.createWord("ella", LexicalCategory.PRONOUN);

        // Set possessive on the pronoun to make it 'her'
        she.setFeature(Feature.POSSESSIVE, true);

        // Create a noun phrase with the subject lover and the determiner
        // as she
        PhraseElement herLover = phraseFactory.createNounPhrase(she, "amante");
        herLover.setPlural(true);

        // Create the pronoun 'he'
        NLGElement he = phraseFactory.createNounPhrase(LexicalCategory.PRONOUN, "él");
        he.setPlural(true);

        // Create a clause to say 'they be her lovers'
        PhraseElement clause = phraseFactory.createClause(he, "ser", herLover);
        clause.setFeature(Feature.POSSESSIVE, true);

        // Add the cue phrase need the comma as orthography
        // currently doesn't handle this.
        // This could be expanded to be a noun phrase with determiner
        // 'two' and noun 'week', set to plural and with a premodifier of
        // 'after'
        clause.setFeature(Feature.CUE_PHRASE, "después de dos semanas,");

        // Add the 'for a fortnight' as a post modifier. Alternatively
        // this could be added as a prepositional phrase 'for' with a
        // complement of a noun phrase ('a' 'fortnight')
        clause.addPostModifier("durante una quincena");

        // Set 'be' to 'was' as past tense
        clause.setFeature(Feature.TENSE, Tense.PAST);

        // Add the clause to a sentence.
        DocumentElement sentence1 = docFactory.createSentence(clause);

        // Realise the sentence
        NLGElement realised = this.realiser.realise(sentence1);

        Assert.assertEquals("Después de dos semanas, ellos fueron sus amantes durante una quincena.", //$NON-NLS-1$
                realised.getRealisation());
    }

    /**
     * Test use of the Complementiser feature by combining two S's using cue phrase and gerund.
     */
    @Test
    public void testComplementiserFeature_PastTense() {
        this.phraseFactory.setLexicon(this.lexicon);

        PhraseElement born = phraseFactory.createClause("Dave Bus", "nacer");
        born.setFeature(Feature.TENSE, Tense.PAST);
        born.setFeature(Feature.COMPLEMENTISER, "en la que");

        PhraseElement theHouse = phraseFactory.createNounPhrase("la", "casa");
        theHouse.addComplement(born);

        PhraseElement clause = phraseFactory.createClause(theHouse, "estar", phraseFactory.createPrepositionPhrase("en", "Edinburgh"));
        DocumentElement sentence = docFactory.createSentence(clause);
        NLGElement realised = realiser.realise(sentence);

        // Retrieve the realisation and dump it to the console
        Assert.assertEquals("La casa en la que Dave Bus nació está en Edinburgh.",
                realised.getRealisation());
    }

    /**
     * Test use of the Complementiser feature in a {@link CoordinatedPhraseElement} by combine two S's using cue phrase and gerund.
     */
    @Test
    public void testComplementiserFeatureInACoordinatePhrase_PastTense() {
        this.phraseFactory.setLexicon(this.lexicon);

        NLGElement dave = phraseFactory.createWord("Dave Bus", LexicalCategory.NOUN);
        NLGElement albert = phraseFactory.createWord("Albert", LexicalCategory.NOUN);

        CoordinatedPhraseElement coord1 = phraseFactory.createCoordinatedPhrase(dave, albert);

        PhraseElement born = phraseFactory.createClause(coord1, "nacer");
        born.setFeature(Feature.TENSE, Tense.PAST);
        born.setFeature(Feature.COMPLEMENTISER, "en la cual");

        PhraseElement theHouse = phraseFactory.createNounPhrase("la", "casa");
        theHouse.addComplement(born);

        PhraseElement clause = phraseFactory.createClause(theHouse, "estar", phraseFactory.createPrepositionPhrase("en", "Edinburgh"));
        DocumentElement sentence = docFactory.createSentence(clause);

        NLGElement realised = realiser.realise(sentence);

        // Retrieve the realisation and dump it to the console
        Assert.assertEquals("La casa en la cual Dave Bus y Albert nacieron está en Edinburgh.",
                realised.getRealisation());
    }

    /**
     * Test the use of the Progressive and Complementiser Features in future tense.
     */
    @Test
    public void testProgressiveAndComplementiserFeatures_FutureTense() {
        this.phraseFactory.setLexicon(this.lexicon);

        // Inner clause is 'I' 'make' 'sentence' 'for'.
        PhraseElement inner = phraseFactory.createClause("yo", "hacer", "sentencia");
        // Inner clause set to progressive.
        inner.setFeature(Feature.PROGRESSIVE, true);

        //Complementiser on inner clause is 'whom'
        inner.setFeature(Feature.COMPLEMENTISER, "para quien");

        // create the engineer and add the inner clause as post modifier
        PhraseElement engineer = phraseFactory.createNounPhrase("el ingeniero");
        engineer.addComplement(inner);

        // Outer clause is: 'the engineer' 'go' (preposition 'to' 'holidays')
        NLGElement vac = phraseFactory.createWord("vacación", LexicalCategory.NOUN);
        vac.setPlural(true);
        PhraseElement outer = phraseFactory.createClause(engineer, "ir", phraseFactory.createPrepositionPhrase("de", vac));
        ((SPhraseSpec) outer).getObject().setPlural(true);

        // Outer clause tense is Future.
        outer.setFeature(Feature.TENSE, Tense.FUTURE);

        // Possibly progressive as well not sure.
        outer.setFeature(Feature.PROGRESSIVE, true);

        //Outer clause postmodifier would be 'tomorrow'
        outer.addPostModifier("mañana");
        DocumentElement sentence = docFactory.createSentence(outer);
        NLGElement realised = realiser.realise(sentence);

        // Retrieve the realisation and dump it to the console
        Assert.assertEquals("El ingeniero para quien yo estoy haciendo sentencia estará yendo de vacaciones mañana.",
                realised.getRealisation());
    }


    /**
     * Tests the use of the Complementiser, Passive, Perfect features in past tense.
     */
    @Test
    public void testComplementiserPassivePerfectFeatures_PastTense() {
        setUp();
        this.realiser.setLexicon(this.lexicon);

        PhraseElement inner = phraseFactory.createClause("yo", "jugar", "poker");
        inner.setFeature(Feature.TENSE, Tense.PAST);
        inner.setFeature(Feature.COMPLEMENTISER, "donde");

        PhraseElement house = phraseFactory.createNounPhrase("la", "casa");
        house.addComplement(inner);

        SPhraseSpec outer = phraseFactory.createClause(null, "abandonar", house);

        outer.addPostModifier("en 1986");

        outer.setFeature(Feature.PASSIVE, true);
        outer.setFeature(Feature.PERFECT, true);

        DocumentElement sentence = docFactory.createSentence(outer);
        NLGElement realised = realiser.realise(sentence);

        // Retrieve the realisation and dump it to the console
        Assert.assertEquals("La casa donde yo jugué poker ha sido abandonada en 1986.",
                realised.getRealisation());
    }

    /**
     * Tests the user of the progressive and complementiser featuers in past tense.
     */
    @Test
    public void testProgressiveComplementiserFeatures_PastTense() {
        this.phraseFactory.setLexicon(this.lexicon);

        NLGElement sandwich = phraseFactory.createNounPhrase(LexicalCategory.NOUN, "sandwich");
        sandwich.setPlural(true);
        //
        PhraseElement first = phraseFactory.createClause("yo", "hacer", sandwich);
        first.setFeature(Feature.TENSE, Tense.IMPERFECT);
        first.setFeature(Feature.PROGRESSIVE, true);
        first.setPlural(false);

        PhraseElement second = phraseFactory.createClause("la mayonesa", "acabar", "ella");
        second.setFeature(Feature.TENSE, Tense.PAST);
        ((SPhraseSpec) second).getObject().setFeature(LexicalFeature.REFLEXIVE, true);
        //
        second.setFeature(Feature.COMPLEMENTISER, "cuando");

        first.addComplement(second);

        DocumentElement sentence = docFactory.createSentence(first);
        NLGElement realised = realiser.realise(sentence);

        // Retrieve the realisation and dump it to the console
        Assert.assertEquals("Yo estaba haciendo sandwiches cuando la mayonesa se acabó.",
                realised.getRealisation());
    }

    /**
     * Test the use of Passive in creating a Passive sentence structure: <Object> + [be] + <verb> + [by] + [Subject].
     */
    @Test
    public void testPassiveFeature() {
        this.realiser.setLexicon(this.lexicon);

        PhraseElement phrase = phraseFactory.createClause("la recesión", "afectar", "el valor");
        phrase.setFeature(Feature.PASSIVE, true);
        DocumentElement sentence = docFactory.createSentence(phrase);
        NLGElement realised = realiser.realise(sentence);

        Assert.assertEquals("El valor es afectado por la recesión.", realised.getRealisation());
    }


    /**
     * Test for repetition of the future auxiliary "will", courtesy of Luxor
     * Vlonjati
     */
    @Test
    public void testFutureTense() {
        SPhraseSpec test = this.phraseFactory.createClause();

        NPPhraseSpec subj = this.phraseFactory.createNounPhrase("yo");

        VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("ir");

        AdvPhraseSpec adverb = this.phraseFactory
                .createAdverbPhrase("mañana");

        test.setSubject(subj);
        test.setVerbPhrase(verb);
        test.setFeature(Feature.TENSE, Tense.FUTURE);
        test.addPostModifier(adverb);
        String sentence = realiser.realiseSentence(test);
        Assert.assertEquals("Yo iré mañana.", sentence);

        SPhraseSpec test2 = this.phraseFactory.createClause();
        NLGElement vb = this.phraseFactory.createWord("ir", LexicalCategory.VERB);
        test2.setSubject(subj);
        test2.setVerb(vb);
        test2.setFeature(Feature.TENSE, Tense.FUTURE);
        test2.addPostModifier(adverb);
        String sentence2 = realiser.realiseSentence(test);
        Assert.assertEquals("Yo iré mañana.", sentence2);

    }


}
