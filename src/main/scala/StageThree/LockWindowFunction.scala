package StageThree
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector
import org.slf4j.Logger

class LockWindowFunction(logger: Logger) extends ProcessAllWindowFunction[String, String, GlobalWindow] {
  override def process(context: Context, elements: Iterable[String], out: Collector[String]): Unit = {
    logger.info("Window closed")
    val sum = MagicLock.solve(elements.toSeq)
    logger.info(s"Got new checkSum $sum")
    out.collect(sum)
  }
}
