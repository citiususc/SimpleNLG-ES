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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Pierre-Luc Vaudry.
 */

package simplenlg.features.spanish;

public abstract class LexicalFeature extends simplenlg.features.LexicalFeature {


    /**
     * <p>
     * This feature gives the feminine singular form of a determiner or adjective.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>feminine_singular</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * determiners and adjectives. This feature will be looked at first before
     * any reference to lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Determiners and adjectives.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String FEMININE_SINGULAR = "feminine_singular";

    /**
     * <p>
     * This feature gives the feminine plural form of a determiner or adjective.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>feminine_plural</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * determiners and adjectives. This feature will be looked at first before
     * any reference to lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Adjectives.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String FEMININE_PLURAL = "feminine_plural";

    /**
     * <p>
     * This feature gives the superlative feminine singular form of a adjective.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>feminine_plural</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * determiners and adjectives. This feature will be looked at first before
     * any reference to lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Adjectives.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String SUPERLATIVE_PLURAL = "superlative_plural";

    /**
     * <p>
     * This feature gives the superlative feminine singular form of a adjective.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>feminine_plural</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * determiners and adjectives. This feature will be looked at first before
     * any reference to lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Adjectives.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String SUPERLATIVE_FEMININE = "superlative_feminine";

    /**
     * <p>
     * This feature gives the superlative feminine plural form of a adjective.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>feminine_plural</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * determiners and adjectives. This feature will be looked at first before
     * any reference to lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Adjectives.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String SUPERLATIVE_FEMININE_PLURAL = "superlative_feminine_plural";

    /**
     * <p>
     * This feature gives the past participle tense form of a verb. For many
     * verbs the past participle is exactly the same as the past tense, for
     * example, the verbs <em>talk</em>, <em>walk</em> and <em>say</em> have
     * past tense and past participles of <em>talked</em>, <em>walked</em> and
     * <em>said</em>. Contrast this with the verbs <em>do</em>, <em>eat</em> and
     * <em>sing</em>. The past tense of these verbs is <em>did</em>,
     * <em>ate</em> and <em>sang</em> respectively. while the respective past
     * participles are <em>done</em>, <em>eaten</em> and <em>sung</em>
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>pastParticiple</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs and verb phrases only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String PAST_PARTICIPLE_PLURAL = "pastParticiplePlural";

    /**
     * <p>
     * This feature gives the feminine past participle tense form of a verb. For many
     * verbs the past participle is exactly the same as the past tense, for
     * example, the verbs <em>talk</em>, <em>walk</em> and <em>say</em> have
     * past tense and past participles of <em>talked</em>, <em>walked</em> and
     * <em>said</em>. Contrast this with the verbs <em>do</em>, <em>eat</em> and
     * <em>sing</em>. The past tense of these verbs is <em>did</em>,
     * <em>ate</em> and <em>sang</em> respectively. while the respective past
     * participles are <em>done</em>, <em>eaten</em> and <em>sung</em>
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>pastParticiple</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs and verb phrases only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String PAST_PARTICIPLE_FEMININE_SINGULAR = "pastParticipleFeminineSingular";

    /**
     * <p>
     * This feature gives the feminine plural past participle tense form of a verb. For many
     * verbs the past participle is exactly the same as the past tense, for
     * example, the verbs <em>talk</em>, <em>walk</em> and <em>say</em> have
     * past tense and past participles of <em>talked</em>, <em>walked</em> and
     * <em>said</em>. Contrast this with the verbs <em>do</em>, <em>eat</em> and
     * <em>sing</em>. The past tense of these verbs is <em>did</em>,
     * <em>ate</em> and <em>sang</em> respectively. while the respective past
     * participles are <em>done</em>, <em>eaten</em> and <em>sung</em>
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>pastParticiple</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs and verb phrases only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String PAST_PARTICIPLE_FEMININE_PLURAL = "pastParticipleFemininePlural";

    /**
     * <p>
     * These features give the indicative present form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>present (person) (number)</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String PRESENT1S = "present1s";
    public static final String PRESENT2S = "present2s";
    public static final String PRESENT3S = "present3s";
    public static final String PRESENT1P = "present1p";
    public static final String PRESENT2P = "present2p";
    public static final String PRESENT3P = "present3p";

    /**
     * <p>
     * These features give the indicative past form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>past (person) (number)</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String PAST1S = "past1s";
    public static final String PAST2S = "past2s";
    public static final String PAST3S = "past3s";
    public static final String PAST1P = "past1p";
    public static final String PAST2P = "past2p";
    public static final String PAST3P = "past3p";

    /**
     * <p>
     * These features give the indicative imperfect form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>imperfect (person) (number)</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String IMPERFECT1S = "imperfect1s";
    public static final String IMPERFECT2S = "imperfect2s";
    public static final String IMPERFECT3S = "imperfect3s";
    public static final String IMPERFECT1P = "imperfect1p";
    public static final String IMPERFECT2P = "imperfect2p";
    public static final String IMPERFECT3P = "imperfect3p";

    /**
     * <p>
     * These features give the indicative future form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>future_radical</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String FUTURE1S = "future1s";
    public static final String FUTURE2S = "future2s";
    public static final String FUTURE3S = "future3s";
    public static final String FUTURE1P = "future1p";
    public static final String FUTURE2P = "future2p";
    public static final String FUTURE3P = "future3p";

    /**
     * <p>
     * These features give the indicative conditional form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>future_radical</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String CONDITIONAL1S = "conditional1s";
    public static final String CONDITIONAL2S = "conditional2s";
    public static final String CONDITIONAL3S = "conditional3s";
    public static final String CONDITIONAL1P = "conditional1p";
    public static final String CONDITIONAL2P = "conditional2p";
    public static final String CONDITIONAL3P = "conditional3p";

    /**
     * <p>
     * These features give the imperative present form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>imperative (person) (number)</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String IMPERATIVE2S = "imperative2s";
    public static final String IMPERATIVE3S = "imperative3s";
    public static final String IMPERATIVE1P = "imperative1p";
    public static final String IMPERATIVE2P = "imperative2p";
    public static final String IMPERATIVE3P = "imperative3p";

    /**
     * <p>
     * These features give the subjunctive present form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>subjunctive (person) (number)</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String SUBJUNCTIVE1S = "subjunctive1s";
    public static final String SUBJUNCTIVE2S = "subjunctive2s";
    public static final String SUBJUNCTIVE3S = "subjunctive3s";
    public static final String SUBJUNCTIVE1P = "subjunctive1p";
    public static final String SUBJUNCTIVE2P = "subjunctive2p";
    public static final String SUBJUNCTIVE3P = "subjunctive3p";

    /**
     * <p>
     * These features give the impersonal present form of a verb.
     * </p>
     * <table border="1">
     * <tr>
     * <td><b>Feature name</b></td>
     * <td><em>indicative</em></td>
     * </tr>
     * <tr>
     * <td><b>Expected type</b></td>
     * <td><code>String</code></td>
     * </tr>
     * <tr>
     * <td><b>Created by</b></td>
     * <td>All supporting lexicons but can be set by the user for irregular
     * cases.</td>
     * </tr>
     * <tr>
     * <td><b>Used by</b></td>
     * <td>The morphology processor uses this feature to correctly inflect
     * verbs. This feature will be looked at first before any reference to
     * lexicons or morphology rules.</td>
     * </tr>
     * <tr>
     * <td><b>Applies to</b></td>
     * <td>Verbs only.</td>
     * </tr>
     * <tr>
     * <td><b>Default</b></td>
     * <td><code>null</code>.</td>
     * </tr>
     * </table>
     */
    public static final String IMPERSONAL = "impersonal";
}
