package me.fornever.platonus.tests

import org.scalatest.{Matchers, FlatSpec}
import me.fornever.platonus.{PhraseEnd, OrdinarWord, PhraseBegin, Network}

class AddSpec extends FlatSpec with Matchers {
  "A Network" should "have size of 0 if default constructed" in {
    Network().size should equal (0)
  }

  it should "have depth of 1 if default constructed" in {
    Network().depth should equal (1)
  }

  it should "have size of 2 after adding the one-word phrase" in {
    val phrase = Vector("word")
    val network = Network().add(phrase)
    network.size should equal (2)
  }

  it should "have a specialized one-layer structure when added a phrase" in {
    val phrase = Vector("word")
    val network = Network().add(phrase)
    val structure = Map(
      Vector(PhraseBegin()) -> Map(OrdinarWord("word") -> 1),
      Vector(OrdinarWord("word")) -> Map(PhraseEnd() -> 1)
    )

    network.data should equal (structure)
  }

  it should "have a specialized structure of depth 2 when added a phrase" in {
    val phrase = Vector("1", "2")
    val network = Network(2).add(phrase)
    val structure = Map(
      Vector(PhraseBegin(), OrdinarWord("1")) -> Map(OrdinarWord("2") -> 1),
      Vector(OrdinarWord("1"), OrdinarWord("2")) -> Map(PhraseEnd() -> 1)
    )

    network.data should equal (structure)
  }
}
