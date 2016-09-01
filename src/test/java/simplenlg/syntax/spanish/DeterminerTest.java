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
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

/**
 * Some determiner tests -- in particular for indefinite articles like "a" or "an".
 *
 * @author Saad Mahamood, Data2Text Limited.
 */
public class DeterminerTest {

    private final String DB_FILENAME = "src/test/resources/NIHLexicon/lexAccess2011.data";
    /**
     * The realiser.
     */
    private Realiser realiser;
    private NLGFactory phraseFactory;
    private Lexicon lexicon;

    /**
     * Set up the variables we'll need for this simplenlg.test to run (Called
     * automatically by JUnit)
     */
    @Before
    public void setUp() {
        this.lexicon = new simplenlg.lexicon.spanish.XMLLexicon();  // built in lexicon
        this.phraseFactory = new NLGFactory(this.lexicon);
        this.realiser = new Realiser(this.lexicon);
    }

    @After
    public void tearDown() {
        this.realiser = null;

        this.phraseFactory = null;

        if (null != lexicon) {
            lexicon = null;
        }
    }

    /**
     * testLowercaseConstant - Test for when there is a lower case constant
     */
    @Test
    public void testLowercaseConstant() {

        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "dog");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un dog.", output);
    }

    /**
     * testLowercaseVowel - Test for "an" as a specifier.
     */
    @Test
    public void testLowercaseVowel() {
        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "buho");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un buho.", output);
    }

    /**
     * testUppercaseConstant - Test for when there is a upper case constant
     */
    @Test
    public void testUppercaseConstant() {

        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "Gato");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un Gato.", output);
    }

    /**
     * testUppercaseVowel - Test for "an" as a specifier for upper subjects.
     */
    @Test
    public void testUppercaseVowel() {
        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "Emu");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un Emu.", output);
    }

    /**
     * testNumericA - Test for "a" specifier with a numeric subject
     */
    @Test
    public void testNumericA() {
        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "7");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un 7.", output);
    }

    /**
     * testNumericAn - Test for "an" specifier with a numeric subject
     */
    @Test
    public void testNumericAn() {
        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "11");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un 11.", output);
    }

    /**
     * testIrregularSubjects - Test irregular subjects that don't conform to the
     * vowel vs. constant divide.
     */
    @Test
    public void testIrregularSubjects() {
        SPhraseSpec sentence = this.phraseFactory.createClause();

        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("un", "uno");
        sentence.setSubject(subject);

        String output = this.realiser.realiseSentence(sentence);

        Assert.assertEquals("Un uno.", output);
    }

    /**
     * testSingluarThisDeterminerNPObject - Test for "this" when used in the singular form as a determiner in a NP Object
     */
    @Test
    public void testSingluarThisDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("este", "mono");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Este mono.", this.realiser.realiseSentence(sentence_1));
    }

    /**
     * testPluralThisDeterminerNPObject - Test for "this" when used in the plural form as a determiner in a NP Object
     */
    @Test
    public void testPluralThisDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(true);
        nounPhrase_1.setDeterminer("este");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Estos monos.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testSingluarThatDeterminerNPObject - Test for "that" when used in the singular form as a determiner in a NP Object
     */
    @Test
    public void testSingluarThatDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("ese", "mono");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Ese mono.", this.realiser.realiseSentence(sentence_1));
    }

    /**
     * testPluralThatDeterminerNPObject - Test for "that" when used in the plural form as a determiner in a NP Object
     */
    @Test
    public void testPluralThatDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(true);
        nounPhrase_1.setDeterminer("ese");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Esos monos.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testSingularThoseDeterminerNPObject - Test for "those" when used in the singular form as a determiner in a NP Object
     */
    @Test
    public void testSingularThoseDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(false);
        nounPhrase_1.setDeterminer("esos");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Ese mono.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testSingularTheseDeterminerNPObject - Test for "these" when used in the singular form as a determiner in a NP Object
     */
    @Test
    public void testSingularTheseDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(false);
        nounPhrase_1.setDeterminer("estos");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Este mono.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testPluralThoseDeterminerNPObject - Test for "those" when used in the plural form as a determiner in a NP Object
     */
    @Test
    public void testPluralThoseDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(true);
        nounPhrase_1.setDeterminer("esos");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Esos monos.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testPluralTheseDeterminerNPObject - Test for "these" when used in the plural form as a determiner in a NP Object
     */
    @Test
    public void testPluralTheseDeterminerNPObject() {
        SPhraseSpec sentence_1 = this.phraseFactory.createClause();

        NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("mono");
        nounPhrase_1.setPlural(true);
        nounPhrase_1.setDeterminer("estos");
        sentence_1.setObject(nounPhrase_1);

        Assert.assertEquals("Estos monos.", this.realiser.realiseSentence(sentence_1));

    }

    /**
     * testSingularTheseDeterminerNPObject - Test for "these" when used in the singular form as a determiner in a NP Object
     *                                       using the NIHDB Lexicon.
     */
//	@Test
//	public void testSingularTheseDeterminerNPObject_NIHDBLexicon() {
//		this.lexicon = new NIHDBLexicon(DB_FILENAME);
//		this.phraseFactory = new NLGFactory(this.lexicon);
//		this.realiser = new Realiser(this.lexicon);
//
//		SPhraseSpec sentence_1 = this.phraseFactory.createClause();
//
//		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
//		nounPhrase_1.setPlural(false);
//		nounPhrase_1.setDeterminer("these");
//		sentence_1.setObject(nounPhrase_1);
//
//		Assert.assertEquals("This monkey.", this.realiser.realiseSentence(sentence_1));
//
//	}

    /**
     * testSingularThoseDeterminerNPObject - Test for "those" when used in the singular form as a determiner in a NP Object
     *                                       using the NIHDB Lexicon
     */
//	@Test
//	public void testSingularThoseDeterminerNPObject_NIHDBLexicon() {
//		this.lexicon = new NIHDBLexicon(DB_FILENAME);
//		this.phraseFactory = new NLGFactory(this.lexicon);
//		this.realiser = new Realiser(this.lexicon);
//
//		SPhraseSpec sentence_1 = this.phraseFactory.createClause();
//
//		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
//		nounPhrase_1.setPlural(false);
//		nounPhrase_1.setDeterminer("those");
//		sentence_1.setObject(nounPhrase_1);
//
//		Assert.assertEquals("That monkey.", this.realiser.realiseSentence(sentence_1));
//
//	}

    /**
     * testPluralThatDeterminerNPObject - Test for "that" when used in the plural form as a determiner in a NP Object
     *                                    using the NIHDB Lexicon.
     */
//	@Test
//	public void testPluralThatDeterminerNPObject_NIHDBLexicon() {
//		this.lexicon = new NIHDBLexicon(DB_FILENAME);
//		this.phraseFactory = new NLGFactory(this.lexicon);
//		this.realiser = new Realiser(this.lexicon);
//
//		SPhraseSpec sentence_1 = this.phraseFactory.createClause();
//
//		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
//		nounPhrase_1.setPlural(true);
//		nounPhrase_1.setDeterminer("that");
//		sentence_1.setObject(nounPhrase_1);
//
//		Assert.assertEquals("Those monkeys.", this.realiser.realiseSentence(sentence_1));
//
//	}

    /**
     * testPluralThisDeterminerNPObject - Test for "this" when used in the plural form as a determiner in a NP Object
     *                                    using the NIHDBLexicon.
     */
//	@Test
//	public void testPluralThisDeterminerNPObject_NIHDBLexicon() {
//		this.lexicon = new NIHDBLexicon(DB_FILENAME);
//		this.phraseFactory = new NLGFactory(this.lexicon);
//		this.realiser = new Realiser(this.lexicon);
//
//		SPhraseSpec sentence_1 = this.phraseFactory.createClause();
//
//		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
//		nounPhrase_1.setPlural(true);
//		nounPhrase_1.setDeterminer("this");
//		sentence_1.setObject(nounPhrase_1);
//
//		Assert.assertEquals("These monkeys.", this.realiser.realiseSentence(sentence_1));
//
//	}

}
