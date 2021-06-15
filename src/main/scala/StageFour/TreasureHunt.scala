package StageFour

import StageFour.Room.size

case class Pos(row: Int, column: Int)

case class Room(maze: Array[Array[Int]], startPoint: Pos, val lastPoint: Pos, blocked: List[(Int, Int)]) {

  def getPossible(pos: Pos): List[(Int, Int, Char)] = {
    val row = pos.row
    val colmun = pos.column
    List((row - 1, colmun, 'w'), (row + 1, colmun, 's'), (row, colmun - 1, 'a'), (row, colmun + 1, 'd')).filter {
      case (i, j, _) =>
        i >= 0 && i < size && j >= 0 && j < size && !blocked.contains((i, j))
    }
  }

  def define(next: Pos, currentPos: Pos): (String, Pos) = {
    val near = getPossible(currentPos)
    val onlyCells = near.map(t => t._1 -> t._2)
    val d = near.map(_._3)
    if (onlyCells.contains(next.row, next.column)) {
      "You have progressed" -> next
    } else {
      s"You have encounter wall, try to move $d" -> currentPos
    }
  }

  def progress(direction: Char, currentPos: Pos): (String, Pos) = {
    direction match {
      case 'w' | 'W' =>
        val next = currentPos.copy(row = currentPos.row - 1)
        define(next, currentPos)
      case 's' | 'S' =>
        val next = currentPos.copy(row = currentPos.row + 1)
        define(next, currentPos)
      case 'a' | 'A' =>
        val next = currentPos.copy(column = currentPos.column - 1)
        define(next, currentPos)
      case 'd' | 'D' =>
        val next = currentPos.copy(column = currentPos.column + 1)
        define(next, currentPos)
    }
  }

}

object Room {
  val size = 3

  def apply(s: Pos, blocked: List[(Int, Int)], l: Pos): Room = {
    val maze = Array.ofDim[Int](size, size)
    blocked.foreach { pos =>
      val (row, column) = pos
      maze(row)(column) = 1
    }
    new Room(maze, s, l, blocked)
  }
}

object TreasureHunt {

  val startingPos = Pos(2, 1)
  val mars = Room(startingPos, List((1, 1), (2, 2)), Pos(1, 2))
  val ceres = Room(startingPos, List((0, 1), (0, 2), (1, 2), (2, 0), (2, 0), (2, 2)), Pos(0, 0))
  val jupiter = Room(startingPos, List((0, 0), (0, 1), (1, 0), (2, 2), (2, 0)), Pos(0, 2))
  val venus = Room(startingPos, List((0, 0), (0, 2), (1, 0), (2, 0), (1, 2), (2, 2)), Pos(0, 1))

  val mazes = Map('m' -> mars, 'c' -> ceres, 'j' -> jupiter, 'v' -> venus)

}

