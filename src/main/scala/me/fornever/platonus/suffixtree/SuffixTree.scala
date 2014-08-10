package me.fornever.platonus.suffixtree

object SuffixTree {
  def empty[T]: SuffixTree[T] = SuffixTree[T](Map())

  def create[T](items: Seq[Seq[T]]): SuffixTree[T] = {
    var tree = empty[T]
    for (phrase <- items) {
      val tokens = phrase.toList
      if (tokens.nonEmpty) {
        val miniTree = branch(tokens.tail)
        tree = tree.append(tokens.head, miniTree)
      }
    }

    tree
  }

  def branch[T](items: Seq[T]): SuffixTree[T] = {
    items match {
      case List(head, tail @ _*) => SuffixTree(Map(head -> branch(tail)))
      case Nil => empty[T]
    }
  }
}

case class SuffixTree[T](children: Map[T, SuffixTree[T]]) {

  def append(value: T, node: SuffixTree[T]): SuffixTree[T] = {
    children.get(value) match {
      case None => SuffixTree(children + (value -> node))
      case Some(existingNode) =>
        val children = node.children
        val newNode = children.foldLeft(existingNode) {
          case (tree: SuffixTree[T], (childValue, childNode)) => tree.append(childValue, childNode)
        }

        val newChildren = children.updated(value, newNode)
        SuffixTree(newChildren)
    }
  }

  def childCount = children.size

}
