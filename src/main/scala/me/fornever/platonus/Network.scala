package me.fornever.platonus

object Network {
  def apply(depth: Int) = new Network(depth, Map[Vector[Word], Map[Word, Int]]())
}

class Network(val depth: Int, val data: Map[Vector[Word], Map[Word, Int]]) {
  def add(phrase: Vector[String]): Network = {
    val words = Stream.concat(
      Stream(PhraseBegin()),
      phrase.toStream.map(OrdinarWord),
      Stream(PhraseEnd())
    )

    val newData: Map[Vector[Word], Map[Word, Int]] = words
      .sliding(depth + 1)
      .toList
      .groupBy(getWordsKey)
      .map(prepareWordValue)

    new Network(depth, newData)
  }

  private def getWordsKey(words: Stream[Word]) = words.take(depth).toVector
  private def prepareWordValue(item: (Vector[Word], List[Stream[Word]])): (Vector[Word], Map[Word, Int]) = {
    val key = item._1
    val finalWords = item._2.map(_.last)
    val countMap = finalWords.groupBy(w => w).mapValues(_.length)
    (key, countMap)
  }
}
