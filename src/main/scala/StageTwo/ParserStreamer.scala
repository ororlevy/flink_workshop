package StageTwo

import Util.Formatter
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.BasePathBucketAssigner
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment



object ParserStreamer {


  def main(args: Array[String]) {
    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val resourcesDirectory = new java.io.File(".").getCanonicalPath + "/src/main/resources/"
    val kafka = env.addSource(KafkaSource.getKafkaConsumer())
    val dataStream = kafka.map(fileName => Formatter.readFile(fileName, resourcesDirectory)).map(text => Formatter.decode(text))
    val config = OutputFileConfig
      .builder()
      .withPartPrefix("prefix")
      .withPartSuffix(".ext")
      .build()
    val sink = StreamingFileSink
      .forRowFormat(new Path(resourcesDirectory), new SimpleStringEncoder[String]("UTF-8"))
      .withBucketAssigner(new BasePathBucketAssigner[String]())
      .withRollingPolicy(OnCheckpointRollingPolicy.build())
      .withOutputFileConfig(config)
      .build()
    dataStream.addSink(sink)
  }

}
