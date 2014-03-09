package me.fornever.platonus

sealed trait Word
case class PhraseBegin() extends Word
case class PhraseEnd() extends Word
case class OrdinarWord(word: String) extends Word
