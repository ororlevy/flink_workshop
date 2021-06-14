package StageTwo

import java.util.Properties

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object KafkaSource {

  def getKafkaConsumer(): FlinkKafkaConsumer[String] = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "adv")
    properties.setProperty("enable.auto.commit", "true")
    // The time interval for offset submission, in milliseconds
    properties.setProperty("auto.offset.reset", "latest")
    new FlinkKafkaConsumer[String]("maze", new SimpleStringSchema(), properties)

  }

}
