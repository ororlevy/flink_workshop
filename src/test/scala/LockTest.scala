import StageThree.MagicLock
import org.scalatest.flatspec.AnyFlatSpec

class LockTest extends AnyFlatSpec {

  "rotate left 1" should "be correct" in {
    val result = MagicLock.rotate("L1", MagicLock.initState)
    val expected = Seq(2, 3, 4, 5, 6, 7, 8, 9, 0, 1)
    assert(result == expected)
  }

  "rotate right 1" should "be correct" in {
    val result = MagicLock.rotate("R1", MagicLock.initState)
    val expected = Seq(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(result == expected)
  }

  "rotate left 7" should "be correct" in {
    val result = MagicLock.rotate("L7", MagicLock.initState)
    val expected = Seq(8, 9, 0, 1, 2, 3, 4, 5, 6, 7)
    assert(result == expected)
  }

  "rotate right 7" should "be correct" in {
    val result = MagicLock.rotate("r7", MagicLock.initState)
    val expected = Seq(4, 5, 6, 7, 8, 9, 0, 1, 2, 3)
    assert(result == expected)
  }

  "rotate left 10" should "be the same" in {
    val result = MagicLock.rotate("L10", MagicLock.initState)
    assert(result == MagicLock.initState)
  }

  "rotate right 10" should "be the same" in {
    val result = MagicLock.rotate("R10", MagicLock.initState)
    assert(result == MagicLock.initState)
  }

  "L1, L1 ,L1 from initState" should "be equal to 559" in {
    val move = "l1"
    val sum = MagicLock.solve(Seq(move, move, move))
    assert(sum.toInt == 559)
  }

  "dd" should "sdf" in {
    val d = "  \\ \\_\\ .----------.     /\n   \\   (  ||MARS||  )   /..\n|\\  \\   ~-||====||-~_  ///\\\\\n| \\  \\    ||    || // /((()))\n \\|  |    ||====|| ~ / \\\\\\///\n     |__  ||    ||   |  `|''\n     | |\\ ||====||___|  _|_\n     | | \\`'    `/ | |  =O=\n   V | |C|_ `   /  | |   ~\n   E | |E||\\___/   | |_ J\n _ N | |R|| ___    | |_]U\n| |U | |E|l/:::\\   | |  P\n|/ S | |S|::::::\\  | |  I\n     | | /:::::::\\ | |  T\n     |_|/:::::::::\\|_|  E\n  /|/|:.:.:.:.:.:.:.:|  R\n | / |.:.:.:.:.:.:.:.|\n |/| /....READ.......\\ |\\\n   |/.......THE.......\\ \\|\n   /. . . . . LOGS . . .\\\n  /. . . . . . . . . . .\\\nYou have unchained the torch and keep walking inside the maze. \nYou see in front of you many paths, and each hallway has a sign: \n 1. Jupiter\n 2. Ceres\n 3. Mars\n 4. Venus\nYou can go through a hallway you choose, and each one holds a mystery.\nSome will have traps, and one of them holds a secret treasure!\nKeep walking in each way by sending the sign's initial letter with the direction. \n\n          [_W_]   \n     [_A_][_S_][_D_]\n\nW - forward , S - backword\nD - Turn right, A - Turn Left\n\nFor example: To go into Mars attic -> M-W.  \nModify your stream to use Keyed Stream"
    val c = Util.Formatter.encode(d)
    print(c)
  }

}
