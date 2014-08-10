package me.fornever.platonus.tests.suffixtree

import me.fornever.platonus.{Word, PhraseEnd, PhraseBegin}
import me.fornever.platonus.suffixtree.SuffixTree
import org.scalatest.{Matchers, FlatSpec}

class SuffixTreeSpec extends FlatSpec with Matchers {

  "A suffix tree" should "have size of 0 if it's empty" in {
    assert(SuffixTree.empty[Nothing].childCount === 0)
  }

  val begin: Word = PhraseBegin()
  val end: Word = PhraseEnd()
  val emptyPhrase = List(begin, end)

  it should "have a size of 1 when added an empty phrase" in {
    val tree = SuffixTree.create(List(emptyPhrase))
    assert(tree.childCount === 1)
  }

  it should "have a simple structure when added an empty phrase" in {
    val tree = SuffixTree.create(List(emptyPhrase))
    assert(tree === SuffixTree(Map(
      begin -> SuffixTree(Map(end -> SuffixTree.empty[Word]))
    )))
  }

  it should "have a simple structure when added a phrase without repeating words" in {
    val phrase = List(1, 2, 3)
    val tree = SuffixTree.create(List(phrase))
    assert(tree === SuffixTree(Map(
      1 -> SuffixTree(Map(2 -> SuffixTree(Map(3 -> SuffixTree.empty[Int])))),
      2 -> SuffixTree(Map(3 -> SuffixTree.empty[Int])),
      3 -> SuffixTree.empty[Int]
    )))
  }

  it should "not change when appended a branch it already have" in {
    val phrase = List(1, 2, 3)
    val tree = SuffixTree.create(List(phrase))
    val branch = SuffixTree.branch(phrase.tail)
    assert(tree.append(2, branch) === tree)
  }

  it should "change when added a new branch" in {
    val phrase1 = List(1)
    val phrase2 = List(2)
    val tree1 = SuffixTree.create(List(phrase1))
    val tree2 = SuffixTree.create(List(phrase2))
    val resultTree1 = tree1.append(2, SuffixTree.empty[Int])
    val resultTree2 = tree2.append(1, SuffixTree.empty[Int])
    assert(tree1 !== tree2)
    assert(resultTree1 === resultTree2)
    assert(resultTree1 === SuffixTree(Map(
      1 -> SuffixTree.empty[Int],
      2 -> SuffixTree.empty[Int]
    )))
  }

  it should "have a complex structure when added a phrase with repeating words" in {
    val phrase = List("a", "b", "c", "b", "a")
    val tree = SuffixTree.create(List(phrase))
    assert(tree === SuffixTree(Map(
      "a" -> SuffixTree(Map("b" -> SuffixTree(Map("c" -> SuffixTree(Map("b" -> SuffixTree(Map("a" -> SuffixTree.empty[String])))))))),
      "b" -> SuffixTree(Map(
        "c" -> SuffixTree(Map("b" -> SuffixTree(Map("a" -> SuffixTree.empty[String])))),
        "a" -> SuffixTree.empty[String])),
      "c" -> SuffixTree(Map("b" -> SuffixTree(Map("a" -> SuffixTree.empty[String]))))
    )))
  }

}
