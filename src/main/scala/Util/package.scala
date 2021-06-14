package Util

import org.slf4j.{Logger, LoggerFactory}

object model {
  val logger: Logger = LoggerFactory.getLogger("FlinkLogger")

  object StreamingMode extends Enumeration {
    type StreamingMode = Value
    val Local = Value("Local")
    val Flink = Value("Flink")
  }

}
