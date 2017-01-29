package sferencik.teamcity.clutterstats;

import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

        String adjective = Feature.isMeasureBeforeBuild(params)
                ? "initial" : "final";

        if (StringUtil.isEmpty(parameterName)) {
            return "Measure and log the " + adjective + " size of " + directoryPath;
        }

        return "Store the " +
                adjective +
                " size of " +
                directoryPath +
                " in the '" +
                parameterName +
                "' parameter";
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultParameters() {
        Map<String, String> defaultParameters = new HashMap<String, String>();
        final Names names = new Names();
        defaultParameters.put(names.getRbWhenToMeasure(), names.getAfterBuild());
        return defaultParameters;
    }

    @Nullable
    @Override
    public PropertiesProcessor getParametersProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> params) {
                Collection<InvalidProperty> errors = new ArrayList<InvalidProperty>();
                final Names names = new Names();

                final String directoryPathParameterName = names.getDirectoryPathParameterName();
                final String directoryPath = params.get(directoryPathParameterName);
                if (StringUtil.isEmpty(directoryPath)) {
                    errors.add(new InvalidProperty(directoryPathParameterName, "Please specify the directory path"));
                }

                if (Feature.isWhenToMeasureUnset(params)) {
                    errors.add(new InvalidProperty(names.getRbWhenToMeasure(), "Please specify when to measure the directory size"));
                }

                if (errors.isEmpty()) {
                    return null;
                }
                else {
                    return errors;
                }
            }
        };
    }

}
