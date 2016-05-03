platonus [![Status Aquana][status-aquana]][andivionian-status-classifier] [![Build Status][build-status-travis]][build-travis] [![Download][status-bintray]][download-bintray]
========

platonus is a simple Markov network management tool.

## Library

### Artifacts

To use platonus, add the following to your `build.sbt` file (if using sbt
0.13.6+):

    resolvers += Resolver.jcenterRepo

    libraryDependencies += "me.fornever" %% "platonus" % "0.2.1"

For older versions of sbt, use the direct personal repository link instead of
`Resolver.jcenterRepo`:

    resolvers += "bintray-fornever-maven" at "http://dl.bintray.com/fornever/maven"

[andivionian-status-classifier]: https://github.com/ForNeVeR/andivionian-status-classifier
[build-travis]: https://travis-ci.org/ForNeVeR/platonus
[download-bintray]: https://bintray.com/fornever/maven/platonus/_latestVersion

[build-status-travis]: https://travis-ci.org/ForNeVeR/platonus.png?branch=develop
[status-aquana]: https://img.shields.io/badge/status-aquana-yellowgreen.svg
[status-bintray]: https://api.bintray.com/packages/fornever/maven/platonus/images/download.svg
