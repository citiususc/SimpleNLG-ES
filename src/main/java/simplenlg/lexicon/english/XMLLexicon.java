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
package simplenlg.lexicon.english;

import simplenlg.features.*;
import simplenlg.framework.*;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class loads words from an XML lexicon. All features specified in the
 * lexicon are loaded
 *
 * @author ereiter
 */
public class XMLLexicon extends simplenlg.lexicon.XMLLexicon {

    /**
     * The list of English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> PRONOUNS = Arrays.asList("I",
            "you",
            "he",
            "she",
            "it",
            "me",
            "you",
            "him",
            "her",
            "it",
            "myself",
            "yourself",
            "himself",
            "herself",
            "itself",
            "mine",
            "yours",
            "his",
            "hers",
            "its",
            "we",
            "you",
            "they",
            "they",
            "they",
            "us",
            "you",
            "them",
            "them",
            "them",
            "ourselves",
            "yourselves",
            "themselves",
            "themselves",
            "themselves",
            "ours",
            "yours",
            "theirs",
            "theirs",
            "theirs",
            "there");

    /**
     * The list of first-person English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> FIRST_PRONOUNS = Arrays.asList("I",
            "me",
            "myself",
            "we",
            "us",
            "ourselves",
            "mine",
            "my",
            "ours",
            "our");

    /**
     * The list of second person English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> SECOND_PRONOUNS = Arrays.asList("you",
            "yourself",
            "yourselves",
            "yours",
            "your");

    /**
     * The list of reflexive English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> REFLEXIVE_PRONOUNS = Arrays.asList("myself",
            "yourself",
            "himself",
            "herself",
            "itself",
            "ourselves",
            "yourselves",
            "themselves");

    /**
     * The list of masculine English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> MASCULINE_PRONOUNS = Arrays.asList("he", "him", "himself", "his");

    /**
     * The list of feminine English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> FEMININE_PRONOUNS = Arrays.asList("she", "her", "herself", "hers");

    /**
     * The list of possessive English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> POSSESSIVE_PRONOUNS = Arrays.asList("mine",
            "ours",
            "yours",
            "his",
            "hers",
            "its",
            "theirs",
            "my",
            "our",
            "your",
            "her",
            "their");

    /**
     * The list of plural English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> PLURAL_PRONOUNS = Arrays.asList("we",
            "us",
            "ourselves",
            "ours",
            "our",
            "they",
            "them",
            "theirs",
            "their");

    /**
     * The list of English pronouns that can be singular or plural.
     */
    @SuppressWarnings("nls")
    private static final List<String> EITHER_NUMBER_PRONOUNS = Arrays.asList("there");

    /**
     * The list of expletive English pronouns.
     */
    @SuppressWarnings("nls")
    private static final List<String> EXPLETIVE_PRONOUNS = Arrays.asList("there");

    /**********************************************************************/
    // constructors
    /**********************************************************************/

    /**
     * Load an XML Lexicon from a named file
     *
     * @param filename
     */
    public XMLLexicon(String filename) {

        super(filename, Language.ENGLISH);
        this.PLURAL_COORDINATORS.add("and");
    }

    /**
     * Load an XML Lexicon from a File
     *
     * @param file
     */
    public XMLLexicon(File file) {

        super(file, Language.ENGLISH);
        this.PLURAL_COORDINATORS.add("and");
    }

    /**
     * Load an XML Lexicon from a URI
     *
     * @param lexiconURI
     */
    public XMLLexicon(URI lexiconURI) {

        super(lexiconURI, Language.ENGLISH);
        this.PLURAL_COORDINATORS.add("and");
    }

    public XMLLexicon() {

        super(Language.ENGLISH);
        this.PLURAL_COORDINATORS.add("and");
    }

    /**
     * add special cases to lexicon
     */
    protected void addSpecialCases() {
        // add variants of "be"
        WordElement be = getWord("be", LexicalCategory.VERB);
        if (be != null) {
            updateIndex(be, "is", indexByVariant);
            updateIndex(be, "am", indexByVariant);
            updateIndex(be, "are", indexByVariant);
            updateIndex(be, "was", indexByVariant);
            updateIndex(be, "were", indexByVariant);
        }
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
                    break;

                case ADJECTIVE:
                    variants
                            .add(getVariant(word, LexicalFeature.COMPARATIVE, "er"));
                    variants
                            .add(getVariant(word, LexicalFeature.SUPERLATIVE, "est"));
                    break;

                case VERB:
                    variants.add(getVariant(word, LexicalFeature.PRESENT3S, "s"));
                    variants.add(getVariant(word, LexicalFeature.PAST, "ed"));
                    variants.add(getVariant(word, LexicalFeature.PAST_PARTICIPLE,
                            "ed"));
                    variants.add(getVariant(word,
                            LexicalFeature.PRESENT_PARTICIPLE, "ing"));
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

        // rule 1 - convert final "y" to "ie" if suffix does not start with "i"
        // eg, cry + s = cries , not crys
        if (base.endsWith("y") && !suffix.startsWith("i"))
            base = base.substring(0, base.length() - 1) + "ie";

        // rule 2 - drop final "e" if suffix starts with "e" or "i"
        // eg, like+ed = liked, not likeed
        if (base.endsWith("e")
                && (suffix.startsWith("e") || suffix.startsWith("i")))
            base = base.substring(0, base.length() - 1);

        // rule 3 - insert "e" if suffix is "s" and base ends in s, x, z, ch, sh
        // eg, watch+s -> watches, not watchs
        if (suffix.startsWith("s")
                && (base.endsWith("s") || base.endsWith("x")
                || base.endsWith("z") || base.endsWith("ch") || base
                .endsWith("sh")))
            base = base + "e";

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
        if (PRONOUNS.contains(baseForm)) {
            setPronounFeatures(wordElement, baseForm);
        }
        return wordElement;
    }

    /**
     * A helper method to set the features on newly created pronoun words.
     *
     * @param wordElement the created element representing the pronoun.
     * @param word        the base word for the pronoun.
     */
    private void setPronounFeatures(NLGElement wordElement, String word) {
        wordElement.setCategory(LexicalCategory.PRONOUN);
        if (FIRST_PRONOUNS.contains(word)) {
            wordElement.setFeature(Feature.PERSON, Person.FIRST);
        } else if (SECOND_PRONOUNS.contains(word)) {
            wordElement.setFeature(Feature.PERSON, Person.SECOND);

            if ("yourself".equalsIgnoreCase(word)) { //$NON-NLS-1$
                wordElement.setPlural(false);
            } else if ("yourselves".equalsIgnoreCase(word)) { //$NON-NLS-1$
                wordElement.setPlural(true);
            } else {
                wordElement.setFeature(Feature.NUMBER, NumberAgreement.BOTH);
            }
        } else {
            wordElement.setFeature(Feature.PERSON, Person.THIRD);
        }
        if (REFLEXIVE_PRONOUNS.contains(word)) {
            wordElement.setFeature(LexicalFeature.REFLEXIVE, true);
        } else {
            wordElement.setFeature(LexicalFeature.REFLEXIVE, false);
        }
        if (MASCULINE_PRONOUNS.contains(word)) {
            wordElement.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
        } else if (FEMININE_PRONOUNS.contains(word)) {
            wordElement.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        } else {
            wordElement.setFeature(LexicalFeature.GENDER, Gender.NEUTER);
        }

        if (POSSESSIVE_PRONOUNS.contains(word)) {
            wordElement.setFeature(Feature.POSSESSIVE, true);
        } else {
            wordElement.setFeature(Feature.POSSESSIVE, false);
        }

        if (PLURAL_PRONOUNS.contains(word) && !SECOND_PRONOUNS.contains(word)) {
            wordElement.setPlural(true);
        } else if (!EITHER_NUMBER_PRONOUNS.contains(word)) {
            wordElement.setPlural(false);
        }

        if (EXPLETIVE_PRONOUNS.contains(word)) {
            wordElement.setFeature(InternalFeature.NON_MORPH, true);
            wordElement.setFeature(LexicalFeature.EXPLETIVE_SUBJECT, true);
        }
    }

    /**
     * Get the coordination conjunction used for addition in this lexicon.
     *
     * @return the coordination conjunction used for addition in this lexicon
     */
    public WordElement getAdditionCoordConjunction() {
        return lookupWord("and", LexicalCategory.CONJUNCTION);
    }

    /**
     * Get the default complementiser for clauses.
     *
     * @return the default complementiser for clauses in this lexicon
     */
    public WordElement getDefaultComplementiser() {
        return lookupWord("that", LexicalCategory.COMPLEMENTISER);
    }

    /**
     * Get the preposition used for passive subjects.
     *
     * @return the default complementiser for clauses in this lexicon
     */
    @Override
    public WordElement getPassivePreposition() {
        return lookupWord("by", LexicalCategory.PREPOSITION);
    }

    @Override
    public String getInterrogativeTypeString(InterrogativeType type) {
        String s = "";

        switch (type) {
            case HOW:
            case HOW_PREDICATE:
                s = "how";
                break;
            case WHAT_OBJECT:
            case WHAT_SUBJECT:
                s = "what";
                break;
            case WHERE:
                s = "where";
                break;
            case WHO_INDIRECT_OBJECT:
            case WHO_OBJECT:
            case WHO_SUBJECT:
                s = "who";
                break;
            case WHY:
                s = "why";
                break;
            case HOW_MANY:
                s = "how many";
                break;
            case YES_NO:
                s = "yes/no";
                break;
        }

        return s;
    }
}
