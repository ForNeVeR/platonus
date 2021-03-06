package me.fornever.platonus

import scala.util.Random
import scala.collection.mutable

object Network {
  def apply(): Network = Network(1)
  def apply(depth: Int): Network = Network(depth, mutable.Map[Vector[Word], mutable.Map[Word, Int]]())
  def apply(depth: Int, data: mutable.Map[Vector[Word], mutable.Map[Word, Int]]) = new Network(depth, data)
}

class Network(val depth: Int,
              val data: mutable.Map[Vector[Word], mutable.Map[Word, Int]]) {
  def size = data.size

  def add(phrase: Vector[String]) = {
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

    for (pair <- Stream.concat(initialData, phraseData)) {
      pair match {
        case (keys, valueMap) =>
          val values = data.getOrElse(keys, scala.collection.mutable.Map[Word, Int]())
          data.update(keys, values)
          for (valueCount <- valueMap) {
            valueCount match {
              case (word, count) =>
                values.update(word, count + values.getOrElse(word, 0))
            }
          }
      }
    }

    this
  }

  def generate(limit: Option[Int]): Stream[String] = {
    def generate(init: Vector[Word]): Vector[Word] = {
      if (limit.isDefined && init.length > limit.get) {
        init
      } else {
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
          case Some(word@OrdinarWord(_)) => generate(init :+ word)
          case _ => init :+ PhraseEnd()
        }
      }
    }

    val phrase = generate(Vector(PhraseBegin()))
    phrase.toStream.filter(_.isInstanceOf[OrdinarWord]).map {
      case OrdinarWord(word) => word
      case _ => throw new Exception("Impossible")
    }
  }

  def generate(): Stream[String] = generate(None)
  def generate(limit: Int): Stream[String] = generate(Some(limit))

  private def getWordsKey(words: List[Word]) = words.take(depth).toVector
  private def prepareWordValue(item: (Vector[Word], List[List[Word]])): (Vector[Word], Map[Word, Int]) = {
    val key = item._1
    val finalWords = item._2.map(_.last)
    val countMap = finalWords.groupBy(identity).mapValues(_.length)
    (key, countMap)
  }
}
