package Util

import org.slf4j.Logger

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.charset.StandardCharsets
import java.util.Base64
import scala.io.Source

object Formatter {

  def encode(txt: String): String = {
    Base64.getEncoder.encodeToString(txt.getBytes(StandardCharsets.UTF_8))
  }


  def decode(encodedText: String): String = {
    val decoded = Base64.getDecoder.decode(encodedText).map(_.toChar).mkString
    decoded.replace("+", " ")
  }

  def writeToFile(text: String, fileName: String, path: String): String = {
    val file = new File(path + fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(text)
    bw.close()
    fileName

  }

  def readFile(fileName: String, path: String)(logger: Logger): String = {
    val pathToFile = path + fileName
    val exists = scala.reflect.io.File(path + fileName).exists && scala.reflect.io.File(path + fileName).isFile
    val finalFile = if (exists) pathToFile else path + "error"
    logger.info(s"going to read file: $finalFile")
    val source = Source.fromFile(finalFile)
    val file = source.getLines.mkString
    source.close()
    file
  }
}
