platonus [![BuildStatus](https://travis-ci.org/ForNeVeR/platonus.png?branch=develop)](https://travis-ci.org/ForNeVeR/platonus) [ ![Download](https://api.bintray.com/packages/fornever/maven/platonus/images/download.svg) ](https://bintray.com/fornever/maven/platonus/_latestVersion)
========
platonus is a simple Markov network management tool.

## Library

### Artifacts

To use platonus, add the following to your `build.sbt` file (if using sbt 0.13.6+):

    resolvers += Resolver.jcenterRepo
    
    libraryDependencies += "me.fornever" %% "platonus" % "0.2.1"

For older versions of sbt, use the direct personal repository link instead of `Resolver.jcenterRepo`:

    resolvers += "bintray-fornever-maven" at "http://dl.bintray.com/fornever/maven"
    