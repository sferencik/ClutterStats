package sferencik.teamcity.clutterstats;

import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ClutterStatsBuildFeature extends BuildFeature {
    private final PluginDescriptor descriptor;

    public ClutterStatsBuildFeature(@NotNull final PluginDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return Names.FEATURE_NAME;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Disk-space statistics (ClutterStats)";
    }

    @Nullable
    @Override
    public String getEditParametersUrl() {
        return descriptor.getPluginResourcesPath("clutterStatsSettings.jsp");
    }

    @Override
    public boolean isMultipleFeaturesPerBuildTypeAllowed() {
        return true;
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> params) {
        final Names names = new Names();
        final String parameterName = params.get(names.getParameterNameParameterName());
        final String directoryPath = params.get(names.getDirectoryPathParameterName());
        if (directoryPath == null)
            return "ERROR: directory path unspecified";
        else if (parameterName == null)
            return "Measure and log the final size of " + directoryPath;
        else
            return "Store the final size of " +
                    directoryPath +
                    " in the '" +
                    parameterName +
                    "' parameter and build statistic";
    }
}
