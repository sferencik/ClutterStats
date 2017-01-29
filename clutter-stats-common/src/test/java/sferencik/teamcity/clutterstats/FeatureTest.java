package sferencik.teamcity.clutterstats;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FeatureTest {
    private Map<String, String> neitherBeforeNorAfter;
    private Map<String, String> before;
    private Map<String, String> after;

    @Before
    public void SetUp() {
        final Names names = new Names();

        neitherBeforeNorAfter = new HashMap<>();

        before = new HashMap<>();
        before.put(names.getRbWhenToMeasure(), names.getBeforeBuild());

        after = new HashMap<>();
        after.put(names.getRbWhenToMeasure(), names.getAfterBuild());
    }

    @Test
    public void testIsMeasureBeforeBuild() {
        assertThat(Feature.isMeasureBeforeBuild(before), is(true));
        assertThat(Feature.isMeasureBeforeBuild(after), is(false));
        assertThat(Feature.isMeasureBeforeBuild(neitherBeforeNorAfter), is(false));
    }

    @Test
    public void testIsMeasureAfterBuild() {
        assertThat(Feature.isMeasureAfterBuild(before), is(false));
        assertThat(Feature.isMeasureAfterBuild(after), is(true));
        assertThat(Feature.isMeasureAfterBuild(neitherBeforeNorAfter), is(false));
    }

    @Test
    public void testIsWhenToMeasureUnset() {
        assertThat(Feature.isWhenToMeasureUnset(before), is(false));
        assertThat(Feature.isWhenToMeasureUnset(after), is(false));
        assertThat(Feature.isWhenToMeasureUnset(neitherBeforeNorAfter), is(true));
    }
}
