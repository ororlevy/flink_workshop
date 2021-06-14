package StageThree

object MagicLock {

  val initState: Seq[Int] = (1 to 9) :+ 0

  val getSum: Seq[Int] => Int = s => s.zipWithIndex.map { case (n, i) => n * i }.sum + s.head

  def validate(move: String): (Char, Int) =
    move.toCharArray match {
      case Array(d, c) if d.isLetter & c.isDigit => d -> c.asDigit
      case Array(d, c1, c2) if d.isLetter & c1.isDigit & c2.isDigit => d -> Array(c1, c2).mkString.toInt
      case _ => 'x' -> 0
    }


  def rotate(move: String, state: Seq[Int]): Seq[Int] = {
    val (d, c) = validate(move)
    d match {
      case 'l' | 'L' => state.map(i => math.abs(i + c) % 10)
      case 'r' | 'R' => state.map(i => (i - c + 10) % 10)
      case 'x' => state
    }
  }

  def solve(moves: Seq[String]): String = {
    val sol = moves.foldLeft[(Seq[Int], Int)](initState, 0){
      case ((state, checkSum), move) =>
        val next = rotate(move, state)
        next -> ( checkSum + getSum(next))
    }
    sol._2.toString
  }

}
