package solutions

import StageThree.LockWindowFunction
import StageTwo.{KafkaSource, OnNewElementCheckpointPolicy}
import Util.model.logger
import Util.{Formatter, model}
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.{AllWindowedStream, DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

object WindowStreamer {


  def createWindow(dataStreamKafkaConsumer: DataStream[String]): AllWindowedStream[String, GlobalWindow] =
    dataStreamKafkaConsumer.countWindowAll(3)

  def createWindow2(dataStreamKafkaConsumer: DataStream[String]): AllWindowedStream[String, GlobalWindow] =
    dataStreamKafkaConsumer.windowAll(TumblingEventTimeWindows.of(Time.seconds(10)))

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
    val answersFolder = "s3-answers/"
    val solFolder = "/s3-sol/"
    val path = resourcesDirectory + answersFolder
    logger.info(s"path: $path")
    val consumer = KafkaSource.getKafkaConsumer()
    val kafka = env.addSource(consumer)


    val dataStream =
      createWindow(kafka).process(new LockWindowFunction(logger)).map {
        fileName =>
          Formatter.readFile(fileName, path)(logger)
      }.map(
        text => {
          logger.info(s"Got text: ${text.substring(0, 10)}")
          Formatter.decode(text)
        })

    val config = OutputFileConfig
      .builder()
      .withPartPrefix("attempt")
      .withPartSuffix(".txt")
      .build()

    val sink = StreamingFileSink
      .forRowFormat(new Path(resourcesDirectory + solFolder), new SimpleStringEncoder[String]("UTF-8"))
      .withBucketCheckInterval(5000)
      .withRollingPolicy(new OnNewElementCheckpointPolicy[String, String])
      .withOutputFileConfig(config)
      .build()

    dataStream.addSink(sink)
    env.enableCheckpointing(5000)
    env.execute()

  }

}