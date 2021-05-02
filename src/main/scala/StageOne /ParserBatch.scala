package StageOne


import Util.Formatter
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._


object ParserBatch {

  def main(args: Array[String]) {
    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"
    val text = env.readTextFile(resourcesDirectory + "welcome")
    val fileName = text.map(txt => Formatter.decode(txt)).map(decodedTxt => Formatter.writeToFile(decodedTxt, "sol1.txt", resourcesDirectory))
    fileName.print()
  }

}
