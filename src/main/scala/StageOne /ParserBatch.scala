package StageOne


import Util.Formatter
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._


object ParserBatch {


  def main(args: Array[String]) {

    val params: ParameterTool = ParameterTool.fromArgs(args)

    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment


    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"

    val text = env.readTextFile(resourcesDirectory + "welcome")

    text.map(txt => Formatter.decode(txt)).map(decodedTxt => Formatter.toFile(decodedTxt, "sol1", resourcesDirectory))

    text.print()

    env.execute("First stage")
  }

}
