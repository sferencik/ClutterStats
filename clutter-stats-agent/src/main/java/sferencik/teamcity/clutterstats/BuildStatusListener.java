package sferencik.teamcity.clutterstats;

import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.util.EventDispatcher;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public class BuildStatusListener {
    public BuildStatusListener(EventDispatcher<AgentLifeCycleListener> listener)
    {
        listener.addListener(new AgentLifeCycleAdapter()
        {
            @Override
            public void beforeBuildFinish(@NotNull AgentRunningBuild build, @NotNull BuildFinishedStatus buildStatus) {
                super.beforeBuildFinish(build, buildStatus);

                final Collection<AgentBuildFeature> features = build.getBuildFeaturesOfType(Names.FEATURE_NAME);

                if (features.isEmpty())
                    return;

                BuildProgressLogger logger = build.getBuildLogger();
                logger.activityStarted("ClutterStats", "CUSTOM_CLUTTER_STATS");
                for (AgentBuildFeature feature : features) {
                    final Names names = new Names();

                    final String directoryPath = feature.getParameters().get(names.getDirectoryPathParameterName());
                    if (directoryPath == null) {
                        // should not happen, thanks to ClutterStatsBuildFeature.getParametersProcessor()
                        logger.error("Directory path unset; cannot measure");
                        continue;
                    }

                    File directory = new File(directoryPath);
                    if (!directory.exists()) {
                        logger.error(directory + " does not exist; cannot measure");
                        continue;
                    }

                    final String parameterName = feature.getParameters().get(names.getParameterNameParameterName());
                    long dirSize = FileUtils.sizeOfDirectory(directory);
                    if (parameterName == null) {
                        logger.message(directory + " has " + dirSize + " bytes");
                    }
                    else {
                        logger.message(directory + " has " + dirSize + " bytes; setting " + parameterName);
                        logger.message("##teamcity[setParameter name='" + parameterName + "' value='" + dirSize + "']");
                        logger.message("##teamcity[buildStatisticValue key='" + parameterName + "' value='" + dirSize + "']");
                    }
                }
                logger.activityFinished("ClutterStats", "CUSTOM_CLUTTER_STATS");
            }
        });
    }
}

