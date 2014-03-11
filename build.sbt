name := "platonus"

organization := "me.fornever"

version := "0.2-SNAPSHOT"

scalaVersion := "2.10.3"

publishTo <<= version { (v: String) =>
  val base = "http://fornever.me:18080/repository"
  val (repoType, repoName) = if (v.trim.endsWith("SNAPSHOT")) {
    ("snapshots", "codingteam-snapshots")
  } else {
    ("releases", "codingteam")
  }
  val url = base + "/" + repoName
  Some(repoType at url)
}

credentials ++= Seq(
  Credentials(Path.userHome / ".ivy2" / ".credentials-codingteam-snapshots"),
  Credentials(Path.userHome / ".ivy2" / ".credentials-codingteam")
)

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"