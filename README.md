# ClutterStats: measure your clutter footprint [![Build Status](https://travis-ci.org/sferencik/ClutterStats.svg?branch=master)](https://travis-ci.org/sferencik/ClutterStats)

ClutterStats is a TeamCity plugin that helps you measure how much disk space your build consumes.

As your build finishes, ClutterStats measures the size of the directory (or directories) you specify, and for each, it

1. logs the size into the build log
2. stores the size into a build parameter

You can measure multiple directories by adding the build feature multiple times.

## Technical notes
If the directory does not exist, no parameter is set, and no statistic is published. (As opposed to setting these to 0.)

If you omit the parameter name in the build feature dialog, the size of the directory is only logged and no parameter is
set.

## Development notes
To build, test, and package the plugin, run `mvn package` from the root directory.

To deploy the plugin, copy `target/clutter-stats.zip` into the TeamCity plugin directory and restart the server.

## Ideas for improvement
Allow measuring the size of a directory as the build *starts*. This could be useful to compare the starting and final
sizes, e.g. for incremental builds.

Allow measuring the size of a file (as opposed to a directory). Allow file masks, or lists of files/directories.
