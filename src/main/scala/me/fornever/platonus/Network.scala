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
    ).toList

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

    // TODO: use a special map merge function (for summing up the word count).
    Network(depth, data ++ initialData ++ phraseData)
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

  private def getWordsKey(words: List[Word]) = words.take(depth).toVector
  private def prepareWordValue(item: (Vector[Word], List[List[Word]])): (Vector[Word], Map[Word, Int]) = {
    val key = item._1
    val finalWords = item._2.map(_.last)
    val countMap = finalWords.groupBy(w => w).mapValues(_.length)
    (key, countMap)
  }
}
