platonus
========
platonus is a simple Markov network management tool.

## Library

### Artifacts

To use platonus, add the following to your `build.sbt` file:

    resolvers ++= Seq(
      "codingteam" at "http://fornever.me:18080/repository/codingteam",
      "codingteam-snapshots" at "http://fornever.me:18080/repository/codingteam-snapshots"
    )

    libraryDependencies ++= Seq(
      "me.fornever" %% "platonus" % "0.2"
    )

Note that the `SNAPSHOT` releases may be not up to date. Please prefer stable releases (the ones without the `SNAPSHOT`
postfix).
