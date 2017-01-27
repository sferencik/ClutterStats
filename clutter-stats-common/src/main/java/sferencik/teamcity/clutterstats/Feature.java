package sferencik.teamcity.clutterstats;

import java.util.Map;

public class Feature {
    static boolean isMeasureBeforeBuild(Map<String, String> params) {
        Names names = new Names();
        return params.get(names.getRbWhenToMeasure()).equals(names.getBeforeBuild());
    }

    static boolean isMeasureAfterBuild(Map<String, String> params) {
        Names names = new Names();
        return params.get(names.getRbWhenToMeasure()).equals(names.getAfterBuild());
    }
}
