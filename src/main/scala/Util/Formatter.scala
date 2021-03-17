package Util

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.charset.StandardCharsets
import java.util.Base64

object Formatter {

  def encode(txt: String): String = {
    Base64.getEncoder.encodeToString(txt.getBytes(StandardCharsets.UTF_8))
  }


  def decode(encodedText: String): String = {
    try {
      Base64.getDecoder.decode(encodedText).map(_.toChar).mkString
    } catch {
      case _ => "Wrong Answer"
    }
  }

  def toFile(text: String, fileName: String, path:String): String = {
    val file = new File(path + fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(text)
    bw.close()
    fileName

  }
}
