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
package simplenlg.orthography.spanish;

import simplenlg.features.InternalFeature;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;

import java.util.List;

/**
 * <p>
 * This processing module deals with punctuation when applied to
 * <code>DocumentElement</code>s. The punctuation currently handled by this
 * processor includes the following (as of version 4.0):
 * <ul>
 * <li>Capitalisation of the first letter in sentences.</li>
 * <li>Termination of sentences with a period if not interrogative.</li>
 * <li>Termination of sentences with a question mark if they are interrogative.</li>
 * <li>Replacement of multiple conjunctions with a comma. For example,
 * <em>John and Peter and Simon</em> becomes <em>John, Peter and Simon</em>.</li>
 * </ul>
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public class OrthographyProcessor extends simplenlg.orthography.OrthographyProcessor {

    /**
     * Performs the realisation on a sentence. This includes adding the
     * terminator and capitalising the first letter.
     *
     * @param components the <code>List</code> of <code>NLGElement</code>s representing
     *                   the components that make up the sentence.
     * @param element    the <code>NLGElement</code> representing the sentence.
     * @return the realised element as an <code>NLGElement</code>.
     */
    @Override
    protected NLGElement realiseSentence(List<NLGElement> components, NLGElement element) {

        NLGElement realisedElement = null;
        if (components != null && components.size() > 0) {
            StringBuffer realisation = new StringBuffer();
            realiseList(realisation, components, "");

            stripLeadingCommas(realisation);
            capitaliseFirstLetter(realisation);
            startSentence(realisation, element.getFeatureAsBoolean(InternalFeature.INTERROGATIVE));
            terminateSentence(realisation, element.getFeatureAsBoolean(InternalFeature.INTERROGATIVE));

            ((DocumentElement) element).clearComponents();
            // realisation.append(' ');
            element.setRealisation(realisation.toString());
            realisedElement = element;
        }

        return realisedElement;
    }

    private void startSentence(StringBuffer realisation, boolean interrogative) {
        if (interrogative) {
            realisation.insert(0, '¿');
        }
    }
}
