package StageTwo

import Util.model.logger
import Util.{Formatter, model}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.{CheckpointRollingPolicy, OnCheckpointRollingPolicy}
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object ParserStreamer{

  def getConsumer: FlinkKafkaConsumer[String] = ???
  def defineDataStreamSource(env: StreamExecutionEnvironment,
                             consumer: FlinkKafkaConsumer[String]): DataStream[String] = ???

  def createStreamFile(outputPath: String, checkPointElement: CheckpointRollingPolicy[String,String],
                       config: OutputFileConfig): StreamingFileSink[String] = ???
  def enableCheckpointing(env: StreamExecutionEnvironment): StreamExecutionEnvironment = ???


  def main(args: Array[String]) {
    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val mode = env.getJavaEnv match {
      case _: LocalStreamEnvironment => model.StreamingMode.Local
      case _ => model.StreamingMode.Flink
    }
    logger.info(s"Running on $mode")
    env.setParallelism(1)
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"
    val answersFolder = "s2-answers/"
    val solFolder = "/s2-sol/"
    val path = resourcesDirectory + answersFolder

    val consumer = getConsumer
    val kafka = defineDataStreamSource(env, consumer)

    val dataStream =
      kafka.map(
        fileName =>
          Formatter.readFile(fileName, path)(logger)
      ).map(
        text => {
          logger.info(s"Got text: ${text.substring(0, 10)}")
          Formatter.decode(text)
        })


    val config = OutputFileConfig
      .builder()
      .withPartPrefix("attempt")
      .withPartSuffix(".txt")
      .build()

    val outputPath  = resourcesDirectory + solFolder

    // replace checkpoint rolling policy
    val sink:StreamingFileSink[String] = createStreamFile(outputPath, new OnNewElementCheckpointPolicy[String, String], config)

    dataStream.addSink(sink)
    enableCheckpointing(env)
    env.execute()

  }


}