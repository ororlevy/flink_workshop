package StageTwo

import StageThree.WindowStreamer.logger
import Util.model.logger
import Util.{Formatter, model}
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object ParserStreamer{

  def main(args: Array[String]) {
    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val mode = env.getJavaEnv match {
      case _: LocalStreamEnvironment => model.StreamingMode.Local
      case _ => model.StreamingMode.Flink
    }
    logger.info(s"Running on $mode")
    env.setParallelism(1)
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/answers"
    val consumer = KafkaSource.getKafkaConsumer()
    val kafka = env.addSource(consumer)


    val dataStream =
      kafka.map(
        fileName =>
          Formatter.readFile(fileName, resourcesDirectory)(logger)
      ).map(
        text => {
          logger.info(s"Got text: ${text.substring(0, 10)}")
          Formatter.decode(text)
        })
    val config = OutputFileConfig
      .builder()
      .withPartPrefix("sol2")
      .withPartSuffix(".ext")
      .build()

    val sink = StreamingFileSink
      .forRowFormat(new Path(resourcesDirectory + "/1"), new SimpleStringEncoder[String]("UTF-8"))
      .withBucketCheckInterval(5000)
      .withRollingPolicy(new OnNewElementCheckpointPolicy[String, String])
      .withOutputFileConfig(config)
      .build()

    dataStream.addSink(sink)
    env.enableCheckpointing(5000)
    env.execute()

  }

}
