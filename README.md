# ClutterStats: measure your clutter footprint [![Build Status](https://travis-ci.org/sferencik/ClutterStats.svg?branch=master)](https://travis-ci.org/sferencik/ClutterStats)

ClutterStats is a TeamCity plugin that measures the disk space consumption.

As your build starts, or as it finishes, ClutterStats measures the size of the directory (or directories) you specify, and for each, it

1. logs the size into the build log
2. stores the size into a build parameter

You can measure multiple directories by adding the build feature multiple times.

## Configuration
Add the "Disk-space metrics (ClutterStats)" build feature to your build configuration. To measure the disk-space of your workspace, set it up as follows:

![Build feature dialog](/images/BuildFeatureDialog.PNG)

Much the same can be achieved by writing a simple build step that would measure the directory size in the language of
your choice (e.g. bash or Powershell) and set the parameter using a
[service message](https://confluence.jetbrains.com/display/TCD10/Build+Script+Interaction+with+TeamCity#BuildScriptInteractionwithTeamCity-changingBuildParameterAddingorChangingaBuildParameterfromaBuildStepAddingorChangingaBuildParameter).
In fact, the plugin uses service messages to achieve this. However, an explicit step clutters your build configuration.
Also, it's impossible to measure the disk-space consumption *before* the build using a build step, since even the first
build step takes place only after the sources have been updated.

## Technical notes
If the directory does not exist, no parameter is set, and no statistic is published. (As opposed to setting these to 0.)

If you omit the parameter name in the build feature dialog, the size of the directory is only logged and no parameter is
set.

## Development notes
To build, test, and package the plugin, run `mvn package` from the root directory.

To deploy the plugin, copy `clutter-stats.zip` into the TeamCity plugin directory and restart the server. If you've built locally, get the zip file from `target/clutter-stats.zip`; otherwise grab the [latest released version](https://github.com/sferencik/ClutterStats/releases).

## Ideas for improvement
Allow measuring the size of a file (as opposed to a directory).

Allow file masks, or lists of files/directories.
