package StageFour

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

class WindowFunc(state: ValueStateDescriptor[Pos]) extends ProcessWindowFunction[(Char, Char), String, Char, GlobalWindow] {
  override def process(key: Char, context: Context, elements: Iterable[(Char, Char)], out: Collector[String]): Unit = {
    val pos = context.windowState.getState(state).value()
    val file = s"$key${pos.row}${pos.column}"
    out.collect(file)
  }
}
