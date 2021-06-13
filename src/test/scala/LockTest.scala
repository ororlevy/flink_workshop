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

  "L1, L1 ,L1 from initState" should "be equal to 550" in {
    val move = "l1"
    val sum = MagicLock.solve(Seq(move, move, move))
    assert(sum.toInt == 550)
  }

}
