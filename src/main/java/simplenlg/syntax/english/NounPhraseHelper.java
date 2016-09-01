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
package simplenlg.syntax.english;

import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.syntax.SyntaxProcessor;

/**
 * <p>
 * This class contains static methods to help the syntax processor realise noun
 * phrases.
 * </p>
 * 
 * @author E. Reiter and D. Westwater, University of Aberdeen.
 * @version 4.0
 */
class NounPhraseHelper extends simplenlg.syntax.NounPhraseHelper {

    public NounPhraseHelper() {
        super(new PhraseHelper());
    }

    @Override
    protected void realisePostModifiers(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement) {

    }

    /**
     * Creates the appropriate pronoun if the subject of the noun phrase is
     * pronominal.
     *
     * @param parent the parent <code>SyntaxProcessor</code> that will do the
     *               realisation of the complementiser.
     * @param phrase the <code>PhraseElement</code> representing this noun phrase.
     * @return the <code>NLGElement</code> representing the pronominal.
     */
    @Override
    protected NLGElement createPronoun(SyntaxProcessor parent,
                                       PhraseElement phrase) {

        String pronoun = "it"; //$NON-NLS-1$
        NLGFactory phraseFactory = phrase.getFactory();
        Object personValue = phrase.getFeature(Feature.PERSON);

        if (Person.FIRST.equals(personValue)) {
            pronoun = "I"; //$NON-NLS-1$
        } else if (Person.SECOND.equals(personValue)) {
            pronoun = "you"; //$NON-NLS-1$
        } else {
            Object genderValue = phrase.getFeature(LexicalFeature.GENDER);
            if (Gender.FEMININE.equals(genderValue)) {
                pronoun = "she"; //$NON-NLS-1$
            } else if (Gender.MASCULINE.equals(genderValue)) {
                pronoun = "he"; //$NON-NLS-1$
            }
        }
        // AG: createWord now returns WordElement; so we embed it in an
        // inflected word element here
        NLGElement element;
        NLGElement proElement = phraseFactory.createWord(pronoun,
                LexicalCategory.PRONOUN);

        if (proElement instanceof WordElement) {
            element = new InflectedWordElement((WordElement) proElement);
            element.setFeature(LexicalFeature.GENDER, ((WordElement) proElement).getFeature(LexicalFeature.GENDER));
            // Ehud - also copy over person
            element.setFeature(Feature.PERSON, ((WordElement) proElement).getFeature(Feature.PERSON));
        } else {
            element = proElement;
        }

        element.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                DiscourseFunction.SPECIFIER);
        element.setFeature(Feature.POSSESSIVE, phrase
                .getFeature(Feature.POSSESSIVE));
        element
                .setFeature(Feature.NUMBER, phrase.getFeature(Feature.NUMBER));


        if (phrase.hasFeature(InternalFeature.DISCOURSE_FUNCTION)) {
            element.setFeature(InternalFeature.DISCOURSE_FUNCTION, phrase
                    .getFeature(InternalFeature.DISCOURSE_FUNCTION));
        }

        return element;
    }
}
