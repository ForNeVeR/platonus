package me.fornever.platonus.tests

import org.scalatest.{Matchers, FlatSpec}
import me.fornever.platonus.{PhraseEnd, OrdinarWord, PhraseBegin, Network}

class NetworkSpec extends FlatSpec with Matchers {
  "A Network" should "have size of 0 if default constructed" in {
    Network().size should equal (0)
  }

  it should "have depth of 1 if default constructed" in {
    Network().depth should equal (1)
  }

  it should "have size of 2 after adding the one-word phrase" in {
    var network = Network()
    val phrase = Vector("word")
    network = network.add(phrase)
    network.size should equal (2)
  }

  it should "have a specialized one-layer structure when added a phrase" in {
    var network = Network()
    val phrase = Vector("word")
    network = network.add(phrase)
    val structure = Map(
      Vector(PhraseBegin()) -> Map(OrdinarWord("word") -> 1),
      Vector(OrdinarWord("word")) -> Map(PhraseEnd() -> 1)
    )

    network.data should equal (structure)
  }
}
