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
package simplenlg.syntax.spanish;

/**
 * <p>
 * This is the processor for handling syntax within the SimpleNLG. The processor
 * translates phrases into lists of words.
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
public class SyntaxProcessor extends simplenlg.syntax.SyntaxProcessor {

    @Override
    public void initialise() {
        this.clauseHelper = new ClauseHelper();
        this.coordinatedPhraseHelper = new CoordinatedPhraseHelper();
        this.nounPhraseHelper = new NounPhraseHelper();
        this.phraseHelper = new PhraseHelper();
        this.verbPhraseHelper = new VerbPhraseHelper();
    }
}
