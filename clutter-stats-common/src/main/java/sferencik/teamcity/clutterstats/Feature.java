package sferencik.teamcity.clutterstats;

import java.util.Map;

public class Feature {
    /** Is the feature set up to measure the disk size at the beginning of the build?
     * @param params the parameter map from the build feature
     * @return true/false based on the radio-box selection; false even if none of the radio options is selected (which happens after upgrade from plugin versions before 3.0.0)
     */
    static boolean isMeasureBeforeBuild(Map<String, String> params) {
        Names names = new Names();
        final String whenToMeasure = params.get(names.getRbWhenToMeasure());
        return whenToMeasure == null ? false : whenToMeasure.equals(names.getBeforeBuild());
    }

    /** @see static Feature.isMeasureBeforeBuild
     */
    static boolean isMeasureAfterBuild(Map<String, String> params) {
        Names names = new Names();
        final String whenToMeasure = params.get(names.getRbWhenToMeasure());
        return whenToMeasure == null ? false : whenToMeasure.equals(names.getAfterBuild());
    }

    /** Is the "when to measure" radio box checked one way or another? It may not be if someone set up the ClutterStats build feature before v3.0.0.
     *
     * @param params the parameter map from the build feature
     * @return true if the radio is unchecked
     */
    static boolean isWhenToMeasureUnset(Map<String, String> params) {
        return params.get(new Names().getRbWhenToMeasure()) == null;
    }
}
