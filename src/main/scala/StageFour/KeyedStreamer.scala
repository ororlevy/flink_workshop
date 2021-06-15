package StageFour

import StageTwo.{KafkaSource, OnNewElementCheckpointPolicy}
import Util.model.logger
import Util.{Formatter, model}
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.{KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

object KeyedStreamer {

  def createKey(element: (Char, Char)): Char = ???

  def createWindow(dataStreamKafkaConsumer: KeyedStream[(Char, Char), Char]): WindowedStream[(Char, Char), Char, GlobalWindow] = ???


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
    val answersFolder = "s4-answers/"
    val solFolder = "/s4-sol/"
    val path = resourcesDirectory + answersFolder
    logger.info(s"path: $path")
    val consumer = KafkaSource.getKafkaConsumer()
    val kafka = env.addSource(consumer)
    kafka.dataType.createSerializer(env.getConfig)
    val hunt = TreasureHunt.mazes
    val states = new ValueStateDescriptor[Pos]("last-pos", classOf[Pos])


    val keyed = kafka
      .map(m => KeyMaker.commandMapper(m))
      .keyBy(ele => createKey(ele))
    val dataStream =
      createWindow(keyed)
        .trigger(new MazeTrigger(hunt, states, logger))
        .process(new WindowFunc(states))
        .map {
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
    env.enableCheckpointing(10000)
    env.execute()

  }

}
