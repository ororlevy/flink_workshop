package StageOne


import Util.Formatter
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._


object ParserBatch {


  def main(args: Array[String]) {
    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    val params: ParameterTool = ParameterTool.fromArgs(args)
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"
    val inputFile = "welcome"
    val fullPath = if(args.isEmpty) resourcesDirectory + inputFile else params.get("input")
    val text = env.readTextFile(fullPath)
    val textBatch = text.map(txt => Formatter.decode(txt)) //.map(decodedTxt => Formatter.writeToFile(decodedTxt, "sol1.txt", resourcesDirectory))
    textBatch.map(decodedTxt => Formatter.writeToFile(decodedTxt, "sol1.txt", resourcesDirectory))
    //  textBatch.writeAsText(params.get("output"), WriteMode.OVERWRITE)
    env.execute()
  }

}
