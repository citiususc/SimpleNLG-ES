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
import simplenlg.framework.DocumentElement;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

import java.util.Arrays;

/**
 * Tests for the DocumentElement class.
 *
 * @author ereiter
 */
public class DocumentElementTest extends SimpleNLG4Test {

    private SPhraseSpec p1, p2, p3;

    /**
     * Instantiates a new document element test.
     *
     * @param name the name
     */
    public DocumentElementTest(String name) {
        super(name);
    }

    @Before
    public void setUp() {
        super.setUp();
        p1 = this.phraseFactory.createClause("tú", "ser", "feliz");
        p2 = this.phraseFactory.createClause("yo", "estar", "triste");
        p3 = this.phraseFactory.createClause("ellos", "estar", this.phraseFactory.createAdjectivePhrase("nervioso"));
        p3.getObject().setPlural(true);
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        this.p1 = null;
        this.p2 = null;
        this.p3 = null;
    }

    /**
     * Basic tests.
     */
    @Test
    public void testBasics() {
        DocumentElement s1 = this.phraseFactory.createSentence(p1);
        DocumentElement s2 = this.phraseFactory.createSentence(p2);
        DocumentElement s3 = this.phraseFactory.createSentence(p3);

        DocumentElement par1 = this.phraseFactory.createParagraph(Arrays
                .asList(s1, s2, s3));

        Assert.assertEquals("Tú eres feliz. Yo estoy triste. Ellos están nerviosos.\n\n",
                this.realiser.realise(par1).getRealisation());

    }

    /**
     * Ensure that no extra whitespace is inserted into a realisation if a
     * constituent is empty. (This is to check for a bug fix for addition of
     * spurious whitespace).
     */
    public void testExtraWhitespace() {
        NPPhraseSpec np1 = this.phraseFactory.createNounPhrase("un", "buque");

        // empty coordinate as premod
        np1.setPreModifier(this.phraseFactory.createCoordinatedPhrase());
        Assert.assertEquals("un buque", this.realiser.realise(np1)
                .getRealisation());

        // empty adjP as premod
        np1.setPreModifier(this.phraseFactory.createAdjectivePhrase());
        Assert.assertEquals("un buque", this.realiser.realise(np1)
                .getRealisation());

        // empty string
        np1.setPreModifier("");
        Assert.assertEquals("un buque", this.realiser.realise(np1)
                .getRealisation());

    }

    /**
     * test whether sents can be embedded in a section without intervening paras
     */
    @Test
    public void testEmbedding() {
        DocumentElement sent = phraseFactory.createSentence("Esto es una prueba");
        DocumentElement sent2 = phraseFactory.createSentence(phraseFactory
                .createClause("John", "estar", "desaparecido"));
        DocumentElement section = phraseFactory.createSection("TÍTULO DE SECCION");
        section.addComponent(sent);
        section.addComponent(sent2);

        Assert.assertEquals(
                "TÍTULO DE SECCION\nEsto es una prueba.\n\nJohn está desaparecido.\n\n",
                this.realiser.realise(section).getRealisation());
    }

    @Test
    public void testSections() {
        // doc which contains a section, and two paras
        DocumentElement doc = this.phraseFactory
                .createDocument("Documento de Prueba");

        DocumentElement section = this.phraseFactory
                .createSection("Sección de Prueba");
        doc.addComponent(section);

        DocumentElement para1 = this.phraseFactory.createParagraph();
        DocumentElement sent1 = this.phraseFactory
                .createSentence("Este es el primer párrafo de prueba");
        para1.addComponent(sent1);
        section.addComponent(para1);

        DocumentElement para2 = this.phraseFactory.createParagraph();
        DocumentElement sent2 = this.phraseFactory
                .createSentence("Este es el segundo párrafo de prueba");
        para2.addComponent(sent2);
        section.addComponent(para2);

        Assert
                .assertEquals(
                        "Documento de Prueba\n\nSección de Prueba\nEste es el primer párrafo de prueba.\n\nEste es el segundo párrafo de prueba.\n\n",
                        this.realiser.realise(doc).getRealisation());
        //
        // Realiser htmlRealiser = new Realiser();
        // htmlRealiser.setHTML(true);
        // Assert
        // .assertEquals(
        // "<BODY><H1>Test Document</H1>\r\n<H2>Test Section</H2>\r\n<H3>Test Subsection</H3>\r\n<UL><LI>This is the first test paragraph.</LI>\r\n<LI>This is the second test paragraph.</LI>\r\n</UL>\r\n</BODY>\r\n",
        // htmlRealiser.realise(doc));
        //
        // // now lets try a doc with a header, header-less section and
        // subsection,
        // // and 2 paras (no list)
        // doc = new TextSpec();
        // doc.setDocument();
        // doc.setHeading("Test Document2");
        //
        // section = new TextSpec();
        // section.setDocStructure(DocStructure.SECTION);
        // ;
        // doc.addSpec(section);
        //
        // subsection = new TextSpec();
        // subsection.setDocStructure(DocStructure.SUBSECTION);
        // section.addSpec(subsection);
        //
        // // use list from above, with indent
        // subsection.addChild(list);
        // list.setIndentedList(false);
        //
        // Assert
        // .assertEquals(
        // "Test Document2\r\n\r\nThis is the first test paragraph.\r\n\r\nThis is the second test paragraph.\r\n",
        // this.realiser.realise(doc));
        //
        // Assert
        // .assertEquals(
        // "<BODY><H1>Test Document2</H1>\r\n<P>This is the first test paragraph.</P>\r\n<P>This is the second test paragraph.</P>\r\n</BODY>\r\n",
        // htmlRealiser.realise(doc));

    }

    /**
     * Tests for lists and embedded lists
     */
    public void testListItems() {
        DocumentElement list = this.phraseFactory.createList();
        list.addComponent(this.phraseFactory.createListItem(p1));
        list.addComponent(this.phraseFactory.createListItem(p2));
        list.addComponent(this.phraseFactory.createListItem(this.phraseFactory
                .createCoordinatedPhrase(p1, p2)));
        String realisation = this.realiser.realise(list).getRealisation();
        Assert.assertEquals(
                "* tú eres feliz\n* yo estoy triste\n* tú eres feliz y yo estoy triste\n",
                realisation);
    }
}
