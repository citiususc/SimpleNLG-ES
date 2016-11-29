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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.morphology.spanish;

import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is the processor for handling morphology within the SimpleNLG. The
 * processor inflects words form the base form depending on the features applied
 * to the word. For example, <em>kiss</em> is inflected to <em>kissed</em> for
 * past tense, <em>dog</em> is inflected to <em>dogs</em> for pluralisation.
 * </p>
 * <p>
 * <p>
 * As a matter of course, the processor will first use any user-defined
 * inflection for the world. If no inflection is provided then the lexicon, if
 * it exists, will be examined for the correct inflection. Failing this a set of
 * very basic rules will be examined to inflect the word.
 * </p>
 * <p>
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 * <p>
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public class MorphologyProcessor extends simplenlg.morphology.MorphologyProcessor {

    public MorphologyProcessor() {
        super(new MorphologyRules());
    }

    @Override
    public void initialise() {
        // Do nothing
    }

    @Override
    public List<NLGElement> realise(List<NLGElement> elements) {
        List<NLGElement> realisedElements = new ArrayList<NLGElement>();
        NLGElement currentElement = null;
        NLGElement determiner = null;
        NLGElement prevElement = null;

        if (elements != null) {
            for (NLGElement eachElement : elements) {
//                if (DiscourseFunction.SPECIFIER.equals(eachElement.getFeature(InternalFeature.DISCOURSE_FUNCTION))){
//                    int index = elements.indexOf(eachElement);
//                    NLGElement nextElement;
//                    if (elements.size()>index+1){
//                        nextElement=elements.get(index+1);
//                        eachElement.setFeature(LexicalFeature.GENDER, nextElement.getFeature(LexicalFeature.GENDER));
//                        eachElement.setFeature(Feature.NUMBER, nextElement.getFeature(Feature.NUMBER));
//                    }
//                }
                currentElement = realise(eachElement);

                if (currentElement != null) {
                    //pass the discourse function and appositive features -- important for orth processor
                    currentElement.setFeature(Feature.APPOSITIVE, eachElement.getFeature(Feature.APPOSITIVE));
                    Object function = eachElement.getFeature(InternalFeature.DISCOURSE_FUNCTION);

                    if (function != null) {
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, function);
                    }

                    if (prevElement != null && LexicalCategory.PREPOSITION.equals(prevElement.getCategory())) {

                        NLGElement root = elements.get(0);
                        while (root.getParent() != null) {
                            root = root.getParent();
                        }
                        if (root.getFeatureAsBoolean(Feature.PASSIVE) && root.hasFeature(Feature.INTERROGATIVE_TYPE)) {
                            realisedElements.remove(realisedElements.size() - 1);
                        } else {
                            StringElement prevString = (StringElement) realisedElements.get(realisedElements.size() - 1);
                            Boolean startsWithEl = currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("el");

                            if ("a".equals(prevString.toString()) && startsWithEl) {
                                prevString.setRealisation("al");
                                realisedElements.set(realisedElements.size() - 1, prevString);
                                ((ListElement) currentElement).getFirst().setRealisation("");
                            } else if ("de".equals(prevString.toString()) && startsWithEl) {
                                prevString.setRealisation("del");
                                realisedElements.set(realisedElements.size() - 1, prevString);
                                ((ListElement) currentElement).getFirst().setRealisation("");
                            }
                        }
                    }

                    if (prevElement != null && (LexicalCategory.VERB.equals(prevElement.getCategory()) || LexicalCategory.MODAL.equals(prevElement.getCategory())) && LexicalCategory.PRONOUN.equals(eachElement.getCategory())) {
                        int i;
                        for (i = 0; i < elements.size(); i++) {
                            if (LexicalCategory.VERB.equals(elements.get(i).getCategory())) {
                                break;
                            }
                        }
                        realisedElements.add(i, currentElement);
                    } else {
                        realisedElements.add(currentElement);
                    }

//                    if (determiner == null && DiscourseFunction.SPECIFIER.equals(currentElement.getFeature(
//                            InternalFeature.DISCOURSE_FUNCTION))) {
//                        determiner = currentElement;
//                        determiner.setFeature(Feature.NUMBER, eachElement.getFeature(Feature.NUMBER));
//                        // MorphologyRules.doDeterminerMorphology(determiner,
//                        // currentElement.getRealisation());
//
//                    } else if (determiner != null) {
//
//                        if (currentElement instanceof ListElement) {
//                            // list elements: ensure det matches first element
//                            NLGElement firstChild = ((ListElement) currentElement).getChildren().get(0);
//
//                            if (firstChild != null) {
//                                //AG: need to check if child is a coordinate
//                                if (firstChild instanceof CoordinatedPhraseElement) {
//                                    morphologyRules.doDeterminerMorphology(determiner,
//                                            firstChild.getChildren().get(0).getRealisation());
//                                } else {
//                                    morphologyRules.doDeterminerMorphology(determiner, firstChild.getRealisation());
//                                }
//                            }
//
//                        } else {
//                            // everything else: ensure det matches realisation
//                            morphologyRules.doDeterminerMorphology(determiner, currentElement.getRealisation());
//                        }
//
//                        determiner = null;
//                    }
                }
                prevElement = eachElement;
            }
        }

        return realisedElements;
    }

    /**
     * This is the main method for performing the morphology. It effectively
     * examines the lexical category of the element and calls the relevant set
     * of rules from <code>MorphologyRules</em>.
     *
     * @param element the <code>InflectedWordElement</code>
     * @return an <code>NLGElement</code> reflecting the correct inflection for
     * the word.
     */
    @Override
    protected NLGElement doMorphology(InflectedWordElement element) {
        NLGElement realisedElement = null;
        if (element.getFeatureAsBoolean(InternalFeature.NON_MORPH).booleanValue()) {
            realisedElement = new StringElement(element.getBaseForm());
            realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                    element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

        } else {
            NLGElement baseWord = element.getFeatureAsElement(InternalFeature.BASE_WORD);

            ElementCategory category = element.getCategory();

            if ((baseWord == null && this.lexicon != null) || LexicalCategory.MODAL.equals(category)) {
                if (LexicalCategory.MODAL.equals(category)) {
                    baseWord = this.lexicon.lookupWord(element.getBaseForm(), LexicalCategory.VERB);
                } else {
                    baseWord = this.lexicon.lookupWord(element.getBaseForm());
                }
            }

            if (category instanceof LexicalCategory) {
                switch ((LexicalCategory) category) {
                    case PRONOUN:
                        realisedElement = morphologyRules.doPronounMorphology(element);
                        break;

                    case NOUN:
                        realisedElement = morphologyRules.doNounMorphology(element, (WordElement) baseWord);
                        break;

                    case VERB:
                    case MODAL:
                        realisedElement = morphologyRules.doVerbMorphology(element, (WordElement) baseWord);
                        break;

                    case ADJECTIVE:
                        realisedElement = morphologyRules.doAdjectiveMorphology(element, (WordElement) baseWord);
                        break;

                    case ADVERB:
                        realisedElement = morphologyRules.doAdverbMorphology(element, (WordElement) baseWord);
                        break;

                    case DETERMINER:
                        realisedElement = morphologyRules.doDeterminerMorphology(element);
                        break;

                    default:
                        realisedElement = new StringElement(element.getBaseForm());
                        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                                element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
                }
            }
        }
        return realisedElement;
    }

}
