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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood, Pierre-Luc Vaudry, Julio Janeiro, Alejandro Ramos, Alberto Bugarín.
 */
package simplenlg.lexicon.spanish;

import org.w3c.dom.Node;
import simplenlg.features.*;
import simplenlg.features.spanish.LexicalFeature;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.Language;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class loads words from an XML lexicon. All features specified in the
 * lexicon are loaded
 *
 * @author ereiter
 */
public class XMLLexicon extends simplenlg.lexicon.XMLLexicon {

    /**********************************************************************/
    // constructors
    /**********************************************************************/

    /**
     * Load an XML Lexicon from a named file
     *
     * @param filename
     */
    public XMLLexicon(String filename) {

        super(filename, Language.SPANISH);
        this.PLURAL_COORDINATORS.addAll(Arrays.asList("y", "o"));
    }

    /**
     * Load an XML Lexicon from a File
     *
     * @param file
     */
    public XMLLexicon(File file) {

        super(file, Language.SPANISH);
        this.PLURAL_COORDINATORS.addAll(Arrays.asList("y", "o"));
    }

    /**
     * Load an XML Lexicon from a URI
     *
     * @param lexiconURI
     */
    public XMLLexicon(URI lexiconURI) {

        super(lexiconURI, Language.SPANISH);
        this.PLURAL_COORDINATORS.addAll(Arrays.asList("y", "o"));
    }

    public XMLLexicon() {

        super(Language.SPANISH);
        this.PLURAL_COORDINATORS.addAll(Arrays.asList("y", "o"));
    }

    @Override
    protected WordElement convertNodeToWord(Node wordNode) {
        WordElement word = super.convertNodeToWord(wordNode);
        if (word.hasFeature(LexicalFeature.GENDER)) {
            String value = word.getFeatureAsString(LexicalFeature.GENDER);
            word.setFeature(LexicalFeature.GENDER, Gender.valueOf(value.toUpperCase()));
        }
        if (word.hasFeature(Feature.NUMBER)) {
            String value = word.getFeatureAsString(Feature.NUMBER);
            word.setFeature(Feature.NUMBER, NumberAgreement.valueOf(value.toUpperCase()));
        }
        if (word.hasFeature(Feature.PERSON)) {
            String value = word.getFeatureAsString(Feature.PERSON);
            word.setFeature(Feature.PERSON, Person.valueOf(value.toUpperCase()));
        }
        return word;
    }

    /**
     * add special cases to lexicon
     */
    protected void addSpecialCases() {
    }

    /**
     * quick-and-dirty routine for getting morph variants should be replaced by
     * something better!
     *
     * @param word
     * @return
     */
    protected Set<String> getVariants(WordElement word) {
        Set<String> variants = new HashSet<String>();
        variants.add(word.getBaseForm());
        ElementCategory category = word.getCategory();
        if (category instanceof LexicalCategory) {
            switch ((LexicalCategory) category) {
                case NOUN:
                    variants.add(getVariant(word, LexicalFeature.PLURAL, "s"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_SINGULAR, "a"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_PLURAL, "as"));
                    break;

                case DETERMINER:
                    variants.add(getVariant(word, LexicalFeature.PLURAL, "s"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_SINGULAR, "a"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_PLURAL, "as"));
                    break;

                case PRONOUN:
                    variants.add(getVariant(word, LexicalFeature.PLURAL, ""));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_SINGULAR, ""));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_PLURAL, ""));
                    break;

                case ADJECTIVE:
                    variants.add(getVariant(word, LexicalFeature.PLURAL, "s"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_SINGULAR, "a"));
                    variants.add(getVariant(word, LexicalFeature.FEMININE_PLURAL, "as"));
//                    variants.add(getVariant(word, LexicalFeature.COMPARATIVE, "er"));
                    variants.add(getVariant(word, LexicalFeature.SUPERLATIVE, "ísimo"));
                    variants.add(getVariant(word, LexicalFeature.SUPERLATIVE_FEMININE, "ísima"));
                    variants.add(getVariant(word, LexicalFeature.SUPERLATIVE_FEMININE_PLURAL, "ísimas"));
                    break;

                case VERB:
                    variants.add(getVariant(word, LexicalFeature.PAST_PARTICIPLE, "do"));
                    variants.add(getVariant(word, LexicalFeature.PRESENT_PARTICIPLE, "ndo"));

                    variants.add(getVariant(word, LexicalFeature.PRESENT1S, ""));
                    variants.add(getVariant(word, LexicalFeature.PRESENT2S, ""));
                    variants.add(getVariant(word, LexicalFeature.PRESENT3S, ""));
                    variants.add(getVariant(word, LexicalFeature.PRESENT1P, ""));
                    variants.add(getVariant(word, LexicalFeature.PRESENT2P, ""));
                    variants.add(getVariant(word, LexicalFeature.PRESENT3P, ""));

                    variants.add(getVariant(word, LexicalFeature.PAST1S, ""));
                    variants.add(getVariant(word, LexicalFeature.PAST2S, ""));
                    variants.add(getVariant(word, LexicalFeature.PAST3S, ""));
                    variants.add(getVariant(word, LexicalFeature.PAST1P, ""));
                    variants.add(getVariant(word, LexicalFeature.PAST2P, ""));
                    variants.add(getVariant(word, LexicalFeature.PAST3P, ""));

                    variants.add(getVariant(word, LexicalFeature.IMPERFECT1S, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERFECT2S, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERFECT3S, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERFECT1P, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERFECT2P, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERFECT3P, ""));

                    variants.add(getVariant(word, LexicalFeature.FUTURE1S, ""));
                    variants.add(getVariant(word, LexicalFeature.FUTURE2S, ""));
                    variants.add(getVariant(word, LexicalFeature.FUTURE3S, ""));
                    variants.add(getVariant(word, LexicalFeature.FUTURE1P, ""));
                    variants.add(getVariant(word, LexicalFeature.FUTURE2P, ""));
                    variants.add(getVariant(word, LexicalFeature.FUTURE3P, ""));

                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL1S, ""));
                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL2S, ""));
                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL3S, ""));
                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL1P, ""));
                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL2P, ""));
                    variants.add(getVariant(word, LexicalFeature.CONDITIONAL3P, ""));

                    variants.add(getVariant(word, LexicalFeature.IMPERATIVE2S, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERATIVE3S, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERATIVE1P, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERATIVE2P, ""));
                    variants.add(getVariant(word, LexicalFeature.IMPERATIVE3P, ""));

                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE1S, ""));
                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE2S, ""));
                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE3S, ""));
                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE1P, ""));
                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE2P, ""));
                    variants.add(getVariant(word, LexicalFeature.SUBJUNCTIVE3P, ""));
                    break;

                default:
                    // only base needed for other forms
                    break;
            }
        }
        return variants;
    }

    /**
     * quick-and-dirty routine for computing morph forms Should be replaced by
     * something better!
     *
     * @param word
     * @param feature
     * @param suffix
     * @return
     */
    protected String getVariant(WordElement word, String feature, String suffix) {
        if (word.hasFeature(feature))
            return word.getFeatureAsString(feature);
        else
            return getForm(word.getBaseForm(), suffix);
    }

    /**
     * quick-and-dirty routine for standard orthographic changes Should be
     * replaced by something better!
     *
     * @param base
     * @param suffix
     * @return
     */
    protected String getForm(String base, String suffix) {
        // add a suffix to a base form, with orthographic changes

        // Plural
        if (suffix.startsWith("s") && !((base.endsWith("a") || base.endsWith("e") || base.endsWith("i") || base.endsWith("i") || base.endsWith("u"))))
            base = base + "e";
        // Verbos
        if (base.endsWith("r") && (suffix.startsWith("d") || suffix.startsWith("n")))
            base = base.substring(0, base.length() - 1);
        // Superlativos
        if (suffix.startsWith("í"))
            base = base.substring(0, base.length() - 1);

        // have made changes, now append and return
        return base + suffix; // eg, want + s = wants
    }

    /**
     * General word lookup method, tries base form, variant, ID (in this order)
     * Creates new word if can't find existing word
     *
     * @param baseForm
     * @param category
     * @return word
     */
    @Override
    public WordElement lookupWord(String baseForm, LexicalCategory category) {
        WordElement wordElement = super.lookupWord(baseForm, category);
        if (category.equals(LexicalCategory.NOUN) && (hasWord(baseForm, LexicalCategory.PRONOUN) || hasWordFromVariant(baseForm, LexicalCategory.PRONOUN))) {
            wordElement = super.lookupWord(baseForm, LexicalCategory.PRONOUN);
        }
        return wordElement;
    }

    /**
     * Get the coordination conjunction used for addition in this lexicon.
     *
     * @return the coordination conjunction used for addition in this lexicon
     */
    @Override
    public WordElement getAdditionCoordConjunction() {
        return lookupWord("y", LexicalCategory.CONJUNCTION);
    }

    /**
     * Get the default complementiser for clauses.
     *
     * @return the default complementiser for clauses in this lexicon
     */
    @Override
    public WordElement getDefaultComplementiser() {
        return lookupWord("que", LexicalCategory.CONJUNCTION);
    }

    /**
     * Get the preposition used for passive subjects.
     *
     * @return the default complementiser for clauses in this lexicon
     */
    @Override
    public WordElement getPassivePreposition() {
        return lookupWord("por", LexicalCategory.PREPOSITION);
    }

    @Override
    public String getInterrogativeTypeString(InterrogativeType type) {
        String s = "";

        switch (type) {
            case HOW:
            case HOW_PREDICATE:
                s = "cómo";
                break;
            case WHAT_OBJECT:
            case WHAT_SUBJECT:
                s = "qué";
                break;
            case WHERE:
                s = "dónde";
                break;
            case WHO_INDIRECT_OBJECT:
            case WHO_OBJECT:
            case WHO_SUBJECT:
                s = "quién";
                break;
            case WHY:
                s = "por qué";
                break;
            case HOW_MANY:
                s = "cuántos";
                break;
            case YES_NO:
                s = "si/no";
                break;
        }

        return s;
    }
}
