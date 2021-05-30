package StageTwo
import Util.Formatter
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment;

object ParserStreamer {

  def main(args: Array[String]) {
    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val resourcesDirectory = "../answers/"
    val kafka = env.addSource(KafkaSource.getKafkaConsumer());

    val dataStream = kafka.map(
      fileName => Formatter.readFile(fileName, resourcesDirectory))
      .map(
        text => {
          println(text)
          Formatter.decode(text)
        })
    val config = OutputFileConfig
      .builder()
      .withPartPrefix("prefix")
      .withPartSuffix(".ext")
      .build()

        val sink = StreamingFileSink
              .forRowFormat(new Path(resourcesDirectory+"/1"), new SimpleStringEncoder[String]("UTF-8"))
              .withBucketCheckInterval(5000)
              .withRollingPolicy(new OnNewElementCheckpointPolicy[String,String])
              .withOutputFileConfig(config)
              .build()

    dataStream.addSink(sink)
    env.enableCheckpointing(5000)
    env.execute()




  }

}
