package sferencik.teamcity.clutterstats;

import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.util.EventDispatcher;
import jetbrains.buildServer.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildStatusListener {
    public BuildStatusListener(EventDispatcher<AgentLifeCycleListener> listener)
    {
        listener.addListener(new AgentLifeCycleAdapter()
        {
            @Override
            public void buildStarted(@NotNull AgentRunningBuild build) {
                super.buildStarted(build);
                measureAndSet(build.getBuildLogger(),
                        build.getBuildFeaturesOfType(Names.FEATURE_NAME)
                                .stream()
                                .filter(feature -> Feature.isMeasureBeforeBuild(feature.getParameters()))
                                .collect(Collectors.toList()));
            }

            @Override
            public void beforeBuildFinish(@NotNull AgentRunningBuild build, @NotNull BuildFinishedStatus buildStatus) {
                super.beforeBuildFinish(build, buildStatus);
                measureAndSet(build.getBuildLogger(),
                        build.getBuildFeaturesOfType(Names.FEATURE_NAME)
                                .stream()
                                .filter(feature -> Feature.isMeasureAfterBuild(feature.getParameters()) || Feature.isWhenToMeasureUnset(feature.getParameters()))
                                .collect(Collectors.toList()));
            }

            private void measureAndSet(BuildProgressLogger buildLogger, Collection<AgentBuildFeature> features) {
                if (features.isEmpty())
                    return;

                final Names names = new Names();

                BuildProgressLogger logger = buildLogger;
                logger.activityStarted("ClutterStats", "CUSTOM_CLUTTER_STATS");
                for (AgentBuildFeature feature : features) {

                    final Map<String, String> parameters = feature.getParameters();

                    final String directoryPath = parameters.get(names.getDirectoryPathParameterName());
                    if (StringUtil.isEmpty(directoryPath)) {
                        // should not happen, thanks to ClutterStatsBuildFeature.getParametersProcessor()
                        logger.error("Directory path unset; cannot measure");
                        continue;
                    }

                    File directory = new File(directoryPath);
                    if (!directory.exists()) {
                        logger.error(directory + " does not exist; cannot measure");
                        continue;
                    }

                    final String parameterName = parameters.get(names.getParameterNameParameterName());
                    long dirSize = FileUtils.sizeOfDirectory(directory);
                    if (StringUtil.isEmpty(parameterName)) {
                        logger.message(directory + " has " + dirSize + " bytes");
                    }
                    else {
                        logger.message(directory + " has " + dirSize + " bytes; setting " + parameterName);
                        logger.message("##teamcity[setParameter name='" + parameterName + "' value='" + dirSize + "']");
                    }
                }
                logger.activityFinished("ClutterStats", "CUSTOM_CLUTTER_STATS");
            }
        });
    }
}

