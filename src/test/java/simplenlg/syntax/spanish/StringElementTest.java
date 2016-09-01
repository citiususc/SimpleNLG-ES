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
import simplenlg.features.Gender;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.spanish.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for string elements as parts of larger phrases
 *
 * @author bertugatt
 */
public class StringElementTest {

    private Lexicon lexicon = null;
    private NLGFactory phraseFactory = null;
    private Realiser realiser = null;

    @Before
    public void setup() {
        lexicon = new XMLLexicon();
        phraseFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
    }

    @After
    public void tearDown() {
        lexicon = null;
        phraseFactory = null;
        realiser = null;
    }

    /**
     * Test that string elements can be used as heads of NP
     */
    @Test
    public void stringElementAsHeadTest() {
        NPPhraseSpec np = this.phraseFactory.createNounPhrase();
        np.setHead(phraseFactory.createStringElement("perros y gatos"));
        np.setSpecifier(phraseFactory.createWord("los", LexicalCategory.DETERMINER));
        np.setPlural(true);
        assertEquals("los perros y gatos", this.realiser.realise(np)
                .getRealisation());
    }

    /**
     * Sentences whose VP is a canned string
     */
    @Test
    public void stringElementAsVPTest() {
        SPhraseSpec s = this.phraseFactory.createClause();
        s.setVerbPhrase(this.phraseFactory.createStringElement("come y bebe"));
        s.setSubject(this.phraseFactory.createStringElement("el hombre grande y gordo"));
        assertEquals("el hombre grande y gordo come y bebe", this.realiser
                .realise(s).getRealisation());
    }

    /**
     * Test for when the SPhraseSpec has a NPSpec added directly after it:
     * "Mary loves NP[the cow]."
     */
    @Test
    public void tailNPStringElementTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement((this.phraseFactory.createStringElement("mary ama")));
        NPPhraseSpec np = this.phraseFactory.createNounPhrase();
        np.setHead("vaca");
        np.setDeterminer("el");
        np.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        senSpec.addComplement(np);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Mary ama la vaca.", this.realiser.realise(completeSen).getRealisation());
    }

    /**
     * Test for a NP followed by a canned text: "NP[A cat] loves a dog".
     */
    @Test
    public void frontNPStringElementTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec np = this.phraseFactory.createNounPhrase();
        np.setHead("gato");
        np.setDeterminer("el");
        senSpec.addComplement(np);
        senSpec.addComplement(this.phraseFactory.createStringElement("ama un perro"));
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("El gato ama un perro.", this.realiser.realise(completeSen).getRealisation());
    }


    /**
     * Test for a StringElement followed by a NP followed by a StringElement
     * "The world loves NP[ABBA] but not a sore loser."
     */
    @Test
    public void mulitpleStringElementsTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement(this.phraseFactory.createStringElement("el mundo ama"));
        NPPhraseSpec np = this.phraseFactory.createNounPhrase();
        np.setHead("ABBA");
        senSpec.addComplement(np);
        senSpec.addComplement(this.phraseFactory.createStringElement("pero no a un mal perdedor"));
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("El mundo ama ABBA pero no a un mal perdedor.", this.realiser.realise(completeSen).getRealisation());
    }

    /**
     * Test for multiple NP phrases with a single StringElement phrase:
     * "NP[John is] a trier NP[for cheese]."
     */
    @Test
    public void mulitpleNPElementsTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec frontNoun = this.phraseFactory.createNounPhrase();
        frontNoun.setHead("john");
        senSpec.addComplement(frontNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement("es un juez"));
        NPPhraseSpec backNoun = this.phraseFactory.createNounPhrase();
        backNoun.setDeterminer("de");
        backNoun.setNoun("queso");
        senSpec.addComplement(backNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("John es un juez de queso.", this.realiser.realise(completeSen).getRealisation());

    }


    /**
     * White space check: Test to see how SNLG deals with additional whitespaces:
     * <p>
     * NP[The Nasdaq] rose steadily during NP[early trading], however it plummeted due to NP[a shock] after NP[IBM] announced poor
     * NP[first quarter results].
     */
    @Test
    public void whiteSpaceNPTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
        firstNoun.setDeterminer("el");
        firstNoun.setNoun("Nasdaq");
        senSpec.addComplement(firstNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement(" subió sostenidamente durante "));
        NPPhraseSpec secondNoun = this.phraseFactory.createNounPhrase();
        secondNoun.setSpecifier("las");
        secondNoun.setPreModifier(phraseFactory.createWord("primera", LexicalCategory.ADJECTIVE));
        secondNoun.setNoun("venta");
        secondNoun.setPlural(true);
        senSpec.addComplement(secondNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement(" , sin embargo se desplomó debido a"));
        NPPhraseSpec thirdNoun = this.phraseFactory.createNounPhrase();
        thirdNoun.setSpecifier("un");
        thirdNoun.setNoun("shock");
        senSpec.addComplement(thirdNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement(" después de que "));
        NPPhraseSpec fourthNoun = this.phraseFactory.createNounPhrase();
        fourthNoun.setNoun("IBM");
        senSpec.addComplement(fourthNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement("anunciara malos    "));
        NPPhraseSpec fifthNoun = this.phraseFactory.createNounPhrase();
        fifthNoun.setPostModifier("el primer trimestre");
        fifthNoun.setNoun("resultados");
        fifthNoun.setPlural(true);
        senSpec.addComplement(fifthNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("El Nasdaq subió sostenidamente durante las primeras ventas, sin embargo se desplomó debido a un shock después de que IBM anunciara malos resultados el primer trimestre.",
                this.realiser.realise(completeSen).getRealisation());
    }

    /**
     * Point absorption test: Check to see if SNLG respects abbreviations at the end of a sentence.
     * "NP[Yahya] was sleeping his own and dreaming etc."
     */
    @Test
    public void pointAbsorptionTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
        firstNoun.setNoun("yaha");
        senSpec.addComplement(firstNoun);
        senSpec.addComplement("estaba durmiendo y soñando etc.");
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Yaha estaba durmiendo y soñando etc.",
                this.realiser.realise(completeSen).getRealisation());


    }

    /**
     * Point absorption test: As above, but with trailing white space.
     * "NP[Yaha] was sleeping his own and dreaming etc.      "
     */
    @Test
    public void pointAbsorptionTrailingWhiteSpaceTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
        firstNoun.setNoun("yaha");
        senSpec.addComplement(firstNoun);
        senSpec.addComplement("estaba durmiendo y soñando etc.      ");
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Yaha estaba durmiendo y soñando etc.",
                this.realiser.realise(completeSen).getRealisation());
    }

    /**
     * Abbreviation test: Check to see how SNLG deals with abbreviations in the middle of a sentence.
     * <p>
     * "NP[Yahya] and friends etc. went to NP[the park] to play."
     */
    @Test
    public void middleAbbreviationTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
        firstNoun.setNoun("yahya");
        senSpec.addComplement(firstNoun);
        senSpec.addComplement(this.phraseFactory.createStringElement("y amigos etc. fueron"));
        NPPhraseSpec secondNoun = this.phraseFactory.createNounPhrase();
        secondNoun.setDeterminer("al");
        secondNoun.setNoun("parque");
        senSpec.addComplement(secondNoun);
        senSpec.addComplement("a jugar");
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Yahya y amigos etc. fueron al parque a jugar.",
                this.realiser.realise(completeSen).getRealisation());
    }


    /**
     * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
     * "I see an NP[elephant]"
     */
    @Test
    public void stringIndefiniteArticleInflectionVowelTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement(this.phraseFactory.createStringElement("yo veo un"));
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("elefante");
        senSpec.addComplement(firstNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Yo veo un elefante.",
                this.realiser.realise(completeSen).getRealisation());

    }

    /**
     * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
     * "I see NP[a elephant]" -->
     */
    @Test
    public void NPIndefiniteArticleInflectionVowelTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement(this.phraseFactory.createStringElement("yo veo"));
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("elefante");
        firstNoun.setDeterminer("un");
        senSpec.addComplement(firstNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        assertEquals("Yo veo un elefante.",
                this.realiser.realise(completeSen).getRealisation());

    }


    /**
     * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
     * "I see an NP[cow]"
     */
    @Test
    public void stringIndefiniteArticleInflectionConsonantTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement(this.phraseFactory.createStringElement("yo veo un"));
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("vaca");
        senSpec.addComplement(firstNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        // Do not attempt "an" -> "a"
        assertNotSame("Yo veo una vaca.",
                this.realiser.realise(completeSen).getRealisation());
    }

    /**
     * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
     * "I see NP[an cow]" -->
     */
    @Test
    public void NPIndefiniteArticleInflectionConsonantTest() {
        SPhraseSpec senSpec = this.phraseFactory.createClause();
        senSpec.addComplement(this.phraseFactory.createStringElement("yo veo"));
        NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("vaca");
        firstNoun.setDeterminer("un");
        senSpec.addComplement(firstNoun);
        DocumentElement completeSen = this.phraseFactory.createSentence();
        completeSen.addComponent(senSpec);
        // Do not attempt "an" -> "a"
        assertEquals("Yo veo una vaca.",
                this.realiser.realise(completeSen).getRealisation());
    }


    /**
     * aggregationStringElementTest: Test to see if we can aggregate two StringElements in a CoordinatedPhraseElement.
     */
    @Test
    public void aggregationStringElementTest() {

        CoordinatedPhraseElement coordinate =
                phraseFactory.createCoordinatedPhrase(new StringElement("John está llendo a Tesco"),
                        new StringElement("Mary está llendo a Sainsburys"));
        SPhraseSpec sentence = phraseFactory.createClause();
        sentence.addComplement(coordinate);

        assertEquals("John está llendo a Tesco y Mary está llendo a Sainsburys.",
                realiser.realiseSentence(sentence));
    }


    /**
     * Tests that no empty space is added when a StringElement is instantiated with an empty string
     * or null object.
     */
    @Test
    public void nullAndEmptyStringElementTest() {

        NLGElement nullStringElement = this.phraseFactory.createStringElement(null);
        NLGElement emptyStringElement = this.phraseFactory.createStringElement("");
        NLGElement beautiful = this.phraseFactory.createStringElement("bonita");
        NLGElement horseLike = this.phraseFactory.createStringElement("horse-like");
        NLGElement creature = this.phraseFactory.createStringElement("criatura");

        // Test1: null or empty at beginning
        SPhraseSpec test1 = this.phraseFactory.createClause("un unicornio", "ser", "considerado como una");
        test1.addPostModifier(emptyStringElement);
        test1.addPostModifier(beautiful);
        test1.addPostModifier(horseLike);
        test1.addPostModifier(creature);
        System.out.println(realiser.realiseSentence(test1));
        Assert.assertEquals("Un unicornio es considerado como una bonita horse-like criatura.",
                realiser.realiseSentence(test1));

        // Test2: empty or null at end
        SPhraseSpec test2 = this.phraseFactory.createClause("un unicornio", "ser", "considerado como una");
        test2.addPostModifier(beautiful);
        test2.addPostModifier(horseLike);
        test2.addPostModifier(creature);
        test2.addPostModifier(nullStringElement);
        System.out.println(realiser.realiseSentence(test2));
        Assert.assertEquals("Un unicornio es considerado como una bonita horse-like criatura.",
                realiser.realiseSentence(test2));

        // Test3: empty or null in the middle
        SPhraseSpec test3 = this.phraseFactory.createClause("un unicornio", "ser", "considerado como una");
        test3.addPostModifier(beautiful);
        test3.addPostModifier("horse-like");
        test3.addPostModifier("");
        test3.addPostModifier("criatura");
        System.out.println(realiser.realiseSentence(test3));
        Assert.assertEquals("Un unicornio es considerado como una bonita horse-like criatura.",
                realiser.realiseSentence(test3));

        // Test4: empty or null in the middle with empty or null at beginning
        SPhraseSpec test4 = this.phraseFactory.createClause("un unicornio", "ser", "considerado como una");
        test4.addPostModifier("");
        test4.addPostModifier(beautiful);
        test4.addPostModifier("horse-like");
        test4.addPostModifier(nullStringElement);
        test4.addPostModifier("criatura");
        System.out.println(realiser.realiseSentence(test4));
        Assert.assertEquals("Un unicornio es considerado como una bonita horse-like criatura.",
                realiser.realiseSentence(test4));

    }

}
