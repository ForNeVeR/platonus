package me.fornever.platonus

import scala.util.Random

object Network {
  def apply(): Network = Network(1)
  def apply(depth: Int): Network = Network(depth, Map[Vector[Word], Map[Word, Int]]())
  def apply(depth: Int, data: Map[Vector[Word], Map[Word, Int]]) = new Network(depth, data)

  def mergeNetworkMaps(map1: Map[Vector[Word], Map[Word, Int]],
                map2: Map[Vector[Word], Map[Word, Int]]): Map[Vector[Word], Map[Word, Int]] = {
    mergeMaps(map1, map2, mergeWordMaps)
  }

  private def mergeMaps[TKey, TValue, TMap <: Map[TKey, TValue]]
  (map1: TMap, map2: TMap, mergeFunction: (TValue, TValue) => TValue) = {
    val intersection = map1.keySet.intersect(map2.keySet)
    val merged = intersection.map(key => key -> mergeFunction(map1(key), map2(key)))
    map1 ++ map2 ++ merged
  }

  private def mergeWordMaps(map1: Map[Word, Int], map2: Map[Word, Int]): Map[Word, Int] = {
    mergeMaps(map1, map2, (v1: Int, v2: Int) => v1 + v2)
  }
}

class Network(val depth: Int, val data: Map[Vector[Word], Map[Word, Int]]) {
  def size = data.size

  def add(phrase: Vector[String]): Network = {
    val words = Stream.concat(
      Stream(PhraseBegin()),
      phrase.toStream.map(OrdinarWord),
      Stream(PhraseEnd())
    ).toList

    // initialData generates a "begin -> 1", "begin 1 -> 2" etc. from a phrase "1 2 3". It helps at the initial phrase
    // generation stage.
    val initialData: Map[Vector[Word], Map[Word, Int]] = (1 to depth - 1).map(
      length => {
        val key = words.take(length)
        val value = words.drop(length).take(1).toList
        key.toVector -> List(value)
      }
    ).toMap.map(prepareWordValue)

    val phraseData: Map[Vector[Word], Map[Word, Int]] = words
      .sliding(depth + 1)
      .toList
      .groupBy(getWordsKey)
      .map(prepareWordValue)

    Network(depth, Network.mergeNetworkMaps(Network.mergeNetworkMaps(data, initialData), phraseData))
  }

  def generate(): Stream[String] = {
    def generate(init: Vector[Word]): Vector[Word] = {
      val key = init.takeRight(depth)
      val nextWordMap = data.getOrElse(key, Map())
      val nextWord = if (nextWordMap.isEmpty) {
        None
      } else {
        val sum = nextWordMap.values.sum
        val position = Random.nextInt(sum)

        def getter(index: Int, iterator: Iterator[(Word, Int)]): Word = {
          iterator.next() match {
            case (word, value) =>
              val nextValue = index + value
              if (nextValue > position) {
                word
              } else {
                getter(nextValue, iterator)
              }
          }
        }

        Some(getter(0, nextWordMap.toIterator))
      }

      nextWord match {
        case Some(word @ OrdinarWord(_)) => generate(init :+ word)
        case _ => init :+ PhraseEnd()
      }
    }

    val phrase = generate(Vector(PhraseBegin()))
    phrase.toStream.filter(_.isInstanceOf[OrdinarWord]).map {
      case OrdinarWord(word) => word
      case _ => throw new Exception("Impossible")
    }
  }

  private def getWordsKey(words: List[Word]) = words.take(depth).toVector
  private def prepareWordValue(item: (Vector[Word], List[List[Word]])): (Vector[Word], Map[Word, Int]) = {
    val key = item._1
    val finalWords = item._2.map(_.last)
    val countMap = finalWords.groupBy(identity).mapValues(_.length)
    (key, countMap)
  }
}
