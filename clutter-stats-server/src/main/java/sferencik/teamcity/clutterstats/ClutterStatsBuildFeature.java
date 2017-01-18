package sferencik.teamcity.clutterstats;

import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
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
        if (StringUtil.isEmpty(directoryPath)) {
            // should not happen, thanks to getParametersProcessor()
            return "ERROR: directory path unspecified";
        }
        else if (StringUtil.isEmpty(parameterName)) {
            return "Measure and log the final size of " + directoryPath;
        }
        else {
            return "Store the final size of " +
                    directoryPath +
                    " in the '" +
                    parameterName +
                    "' parameter";
        }
    }

    @Nullable
    @Override
    public PropertiesProcessor getParametersProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> params) {
                final Names names = new Names();
                final String directoryPathParameterName = names.getDirectoryPathParameterName();
                final String directoryPath = params.get(directoryPathParameterName);
                if (StringUtil.isEmpty(directoryPath)) {
                    return Arrays.asList(new InvalidProperty(directoryPathParameterName, "Please specify the directory path"));
                }
                else {
                    return null;
                }
            }
        };
    }
}
