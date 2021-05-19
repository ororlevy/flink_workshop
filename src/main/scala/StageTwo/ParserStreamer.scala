package StageTwo
import Util.Formatter
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment


object ParserStreamer {

  def main(args: Array[String]) {
    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"
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
      .forRowFormat(new Path(resourcesDirectory), new SimpleStringEncoder[String]("UTF-8"))
      .withRollingPolicy(OnCheckpointRollingPolicy.build())
      .withOutputFileConfig(config)
      .build()

    dataStream.addSink(sink)
    env.execute("flinkFile")



    //val sink = StreamingFileSink
    //      .forRowFormat(new Path(resourcesDirectory+"/1"), new SimpleStringEncoder[String]("UTF-8"))
    //      .withBucketAssigner(new KeyBucketAssigner())
    //      .withBucketAssigner(new BasePathBucketAssigner[String]())
    //      .withRollingPolicy(OnCheckpointRollingPolicy.build())
    //      .withOutputFileConfig(config)
    //      .build()


    //    val sink: StreamingFileSink[String] = StreamingFileSink
    //      .forRowFormat(new Path(resourcesDirectory), new SimpleStringEncoder[String]("UTF-8"))
    //      .withRollingPolicy(
    //        DefaultRollingPolicy.builder()
    //          .withRolloverInterval(TimeUnit.SECONDS.toMillis(15))
    //          .withInactivityInterval(TimeUnit.SECONDS.toMillis(5))
    //          .withMaxPartSize(100)
    //          .build()).withOutputFileConfig(config)
    //      .build()

    //    val sink: StreamingFileSink[String] = StreamingFileSink
    //      .forRowFormat(new Path(outputPath), new SimpleStringEncoder[String]("UTF-8"))
    //      .withRollingPolicy(
    //        DefaultRollingPolicy.builder()
    //          .withRolloverInterval(TimeUnit.SECONDS.toMillis(15))
    //          .withInactivityInterval(TimeUnit.SECONDS.toMillis(5))
    //          .withMaxPartSize(100)
    //          .build())
    //      .build()
    //
    //    input.addSink(sink)


  }

}
