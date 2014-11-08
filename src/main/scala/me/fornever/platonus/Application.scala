package me.fornever.platonus

import java.io.File
import java.util.{Calendar, Scanner}

object Application extends App {
  val regex = "^\\[.*?\\] \\* (.*?)(?: \\*|:) (.*?)$".r
  val directory = new File(args(0))
  val userName = args(1)

  var network = Network(2)

  val dateTime = Calendar.getInstance
  println(s"Started at $dateTime")
  for (file <- directory.listFiles()) {
    val scanner = new Scanner(file, "UTF-16").useDelimiter("\\r\\n")
    try {
      while (scanner.hasNext) {
        val string = scanner.next()
        string match {
          case regex(nick, message) if nick == userName =>
            network.add(message.split("\\s+").toVector)
          case _ =>
        }
      }
    } finally {
      scanner.close()
    }

    val size = network.size
    println(s"${file.getPath} ok")
    println(s"Network size: $size")
  }

  val endTime = Calendar.getInstance
  println(s"Finished at $endTime")
}
