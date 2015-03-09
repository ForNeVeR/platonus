name := "platonus"

organization := "me.fornever"

version := "0.2.1"

scalaVersion := "2.11.5"

publishTo <<= version { (v: String) =>
  val base = "http://archiva.fornever.me/repository"
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

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)