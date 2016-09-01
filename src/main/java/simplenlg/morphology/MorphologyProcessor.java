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
package simplenlg.morphology;

import simplenlg.framework.*;

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
public abstract class MorphologyProcessor extends NLGModule {

    protected MorphologyRules morphologyRules;

    public MorphologyProcessor(MorphologyRules morphologyRules) {
        this.morphologyRules = morphologyRules;
    }

    @Override
    public NLGElement realise(NLGElement element) {
        NLGElement realisedElement = null;

        if (element instanceof InflectedWordElement) {
            realisedElement = doMorphology((InflectedWordElement) element);

        } else if (element instanceof StringElement) {
            realisedElement = element;

        } else if (element instanceof WordElement) {
            // AG: now retrieves the default spelling variant, not the baseform
            // String baseForm = ((WordElement) element).getBaseForm();
            String defaultSpell = ((WordElement) element).getDefaultSpellingVariant();

            if (defaultSpell != null) {
                realisedElement = new StringElement(defaultSpell);
            }

        } else if (element instanceof DocumentElement) {
            List<NLGElement> children = element.getChildren();
            ((DocumentElement) element).setComponents(realise(children));
            realisedElement = element;

        } else if (element instanceof ListElement) {
            realisedElement = new ListElement();
            ((ListElement) realisedElement).addComponents(realise(element.getChildren()));

        } else if (element instanceof CoordinatedPhraseElement) {
            List<NLGElement> children = element.getChildren();
            ((CoordinatedPhraseElement) element).clearCoordinates();

            if (children != null && children.size() > 0) {
                ((CoordinatedPhraseElement) element).addCoordinate(realise(children.get(0)));

                for (int index = 1; index < children.size(); index++) {
                    ((CoordinatedPhraseElement) element).addCoordinate(realise(children.get(index)));
                }

                realisedElement = element;
            }

        } else if (element != null) {
            realisedElement = element;
        }

        return realisedElement;
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
    protected abstract NLGElement doMorphology(InflectedWordElement element);
}
