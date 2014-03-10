package me.fornever.platonus

object Network {
  def apply(): Network = Network(1)
  def apply(depth: Int): Network = Network(depth, Map[Vector[Word], Map[Word, Int]]())
  def apply(depth: Int, data: Map[Vector[Word], Map[Word, Int]]) = new Network(depth, data)
}

class Network(val depth: Int, val data: Map[Vector[Word], Map[Word, Int]]) {
  def size = data.size

  def add(phrase: Vector[String]): Network = {
    val words = Stream.concat(
      Stream(PhraseBegin()),
      phrase.toStream.map(OrdinarWord),
      Stream(PhraseEnd())
    )

    val phraseData: Map[Vector[Word], Map[Word, Int]] = words
      .sliding(depth + 1)
      .toList
      .groupBy(getWordsKey)
      .map(prepareWordValue)

    Network(depth, data ++ phraseData)
  }

  def generate(): Stream[String] = {
    def generate(init: Vector[Word]): Vector[Word] = {
      val key = init.takeRight(depth)
      val nextWordMap = data.getOrElse(key, Map())
      val nextWord = if (nextWordMap.isEmpty) {
        None
      } else {
        Some(nextWordMap.maxBy(_._2)._1)
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

  private def getWordsKey(words: Stream[Word]) = words.take(depth).toVector
  private def prepareWordValue(item: (Vector[Word], List[Stream[Word]])): (Vector[Word], Map[Word, Int]) = {
    val key = item._1
    val finalWords = item._2.map(_.last)
    val countMap = finalWords.groupBy(w => w).mapValues(_.length)
    (key, countMap)
  }
}
