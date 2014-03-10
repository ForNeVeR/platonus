package me.fornever.platonus.tests

import org.scalatest.{Matchers, FlatSpec}
import me.fornever.platonus.Network

class GenerateSpec extends FlatSpec with Matchers {
  val samplePhrase = Vector("1", "2", "3")
  "A Network" should "generate the phrase " + samplePhrase + " when got it as input" in {
    val network = Network().add(samplePhrase)
    network.generate() should equal (samplePhrase)
  }

  it should "generate the same phrase with depth of 2" in {
    val network = Network(2).add(samplePhrase)
    network.generate() should equal (samplePhrase)
  }
}
