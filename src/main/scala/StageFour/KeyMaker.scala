package StageFour

object KeyMaker {

  private val signs = Seq('j', 'J, 'c', 'C', 'V', 'v', 'm', 'M')
  private val directions = Seq('w', 'W', 'S', 's', 'd', 'D', 'a', 'A')
  private val sign = Map()

  def commandMapper(message: String): (Char, Char)= {
    if(validate(message)) {
      val c = message.toCharArray
      c.head -> c.last
    } else {
      'x' -> 'x'
    }
  }

  private def validate(message: String): Boolean = {
    message.toCharArray match {
      case Array(k, s, d) =>
        signs.contains(k) && s == '-' && directions.contains(d)
      case _ => false
    }
  }

  def createKey(message: String): Char = {
      if(validate(message)) {
        message.toCharArray.head.toUpper
      } else {
        'x'
      }
  }

}
