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
import simplenlg.features.Feature;
import simplenlg.format.english.TextFormatter;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

import java.util.Arrays;

public class OrthographyFormatTest extends SimpleNLG4Test {

    private DocumentElement list1, list2;
    private DocumentElement listItem1, listItem2, listItem3;
    private String list1Realisation = new StringBuffer("* en la habitación")
            .append("\n* tras la cortina").append("\n").toString();
    private String list2Realisation = new StringBuffer("* en la roca")
            .append("\n* ").append(list1Realisation).append("\n").toString();

    public OrthographyFormatTest(String name) {
        super(name);
    }

    @Before
    public void setUp() {
        super.setUp();

        // need to set formatter for realiser (set to null in the test
        // superclass)
        this.realiser.setFormatter(new TextFormatter());

        // a couple phrases as list items
        this.listItem1 = this.phraseFactory.createListItem(this.inTheRoom);
        this.listItem2 = this.phraseFactory
                .createListItem(this.behindTheCurtain);
        this.listItem3 = this.phraseFactory.createListItem(this.onTheRock);

        // a simple depth-1 list of phrases
        this.list1 = this.phraseFactory
                .createList(Arrays.asList(new DocumentElement[]{
                        this.listItem1, this.listItem2}));

        // a list consisting of one phrase (depth-1) + a list )(depth-2)
        this.list2 = this.phraseFactory.createList(Arrays
                .asList(new DocumentElement[]{this.listItem3,
                        this.phraseFactory.createListItem(this.list1)}));
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        this.list1 = null;
        this.list2 = null;
        this.listItem1 = null;
        this.listItem2 = null;
        this.listItem3 = null;
        this.list1Realisation = null;
        list2Realisation = null;
    }

    /**
     * Test the realisation of a simple list
     */
    @Test
    public void testSimpleListOrthography() {
        NLGElement realised = this.realiser.realise(this.list1);
        Assert.assertEquals(this.list1Realisation, realised.getRealisation());
    }

    /**
     * Test the realisation of a list with an embedded list
     */
    @Test
    public void testEmbeddedListOrthography() {
        NLGElement realised = this.realiser.realise(this.list2);
        Assert.assertEquals(this.list2Realisation, realised.getRealisation());
    }

    /**
     * Test the realisation of appositive pre-modifiers with commas around them.
     */
    @Test
    public void testAppositivePreModifiers() {
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("yo");
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("una bolsa");

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object);

        // add a PP complement
        PPPhraseSpec pp = this.phraseFactory.createPrepositionPhrase("en",
                this.phraseFactory.createNounPhrase("la mayoría de los", "martes"));
        _s1.addPreModifier(pp);

        //without appositive feature on pp
        Assert.assertEquals(
                "yo en la mayoría de los martes llevo una bolsa", this.realiser
                        .realise(_s1).getRealisation());

        //with appositive feature
        pp.setFeature(Feature.APPOSITIVE, true);
        Assert.assertEquals(
                "yo, en la mayoría de los martes, llevo una bolsa", this.realiser
                        .realise(_s1).getRealisation());
    }


    /**
     * Test the realisation of appositive pre-modifiers with commas around them.
     */
    @Test
    public void testCommaSeparatedFrontModifiers() {
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("yo");
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("una bolsa");

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object);

        // add a PP complement
        PPPhraseSpec pp1 = this.phraseFactory.createPrepositionPhrase("en",
                this.phraseFactory.createNounPhrase("la mayoría de los", "martes"));
        _s1.addFrontModifier(pp1);

        PPPhraseSpec pp2 = this.phraseFactory.createPrepositionPhrase("desde",
                this.phraseFactory.createNounPhrase("1991"));
        _s1.addFrontModifier(pp2);
        pp1.setFeature(Feature.APPOSITIVE, true);
        pp2.setFeature(Feature.APPOSITIVE, true);

        //without setCommaSepCuephrase
        Assert.assertEquals(
                "en la mayoría de los martes desde 1991 yo llevo una bolsa", this.realiser
                        .realise(_s1).getRealisation());

        //with setCommaSepCuephrase
        this.realiser.setCommaSepCuephrase(true);
        Assert.assertEquals(
                "en la mayoría de los martes, desde 1991, yo llevo una bolsa", this.realiser
                        .realise(_s1).getRealisation());
    }

    /**
     * Ensure we don't end up with doubled commas.
     */
    @Test
    public void testNoDoubledCommas() {
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("yo");
        NPPhraseSpec object = this.phraseFactory.createNounPhrase("una bolsa");

        SPhraseSpec _s1 = this.phraseFactory.createClause(subject,
                "llevar", object);

        PPPhraseSpec pp1 = this.phraseFactory.createPrepositionPhrase("en",
                this.phraseFactory.createNounPhrase("la mayoría de los", "martes"));
        _s1.addFrontModifier(pp1);

        PPPhraseSpec pp2 = this.phraseFactory.createPrepositionPhrase("desde",
                this.phraseFactory.createNounPhrase("1991"));
        PPPhraseSpec pp3 = this.phraseFactory.createPrepositionPhrase("excepto",
                this.phraseFactory.createNounPhrase("ayer"));

        pp2.setFeature(Feature.APPOSITIVE, true);
        pp3.setFeature(Feature.APPOSITIVE, true);

        pp1.addPostModifier(pp2);
        pp1.addPostModifier(pp3);

        this.realiser.setCommaSepCuephrase(true);

        Assert.assertEquals(
                "en la mayoría de los martes, desde 1991, excepto ayer, yo llevo una bolsa", this.realiser
                        .realise(_s1).getRealisation());
        // without my fix (that we're testing here), you'd end up with
        // "on most Tuesdays, since 1991,, except yesterday, I carry a bag"
    }

// <[on most Tuesdays, since 1991, except yesterday, ]I carry a bag> but was:<[]I carry a bag>


}
