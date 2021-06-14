package StageFour

import org.apache.flink.api.common.functions.{ReduceFunction, RichAggregateFunction}
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector
import org.slf4j.Logger

class ReduceMazer extends ReduceFunction[(Char, Char)] {
  override def reduce(value1: (Char, Char), value2: (Char, Char)): (Char, Char) = ???
}

class WindowFunc(state: ValueStateDescriptor[Pos]) extends ProcessWindowFunction[(Char, Char), String, Char, GlobalWindow] {
  override def process(key: Char, context: Context, elements: Iterable[(Char, Char)], out: Collector[String]): Unit = {
    val pos = context.windowState.getState(state).value()
    val file = s"$key+${pos.row}+${pos.column}"
    out.collect(file)
  }
}


//class WindowFunc(state: ValueStateDescriptor[Pos]) extends RichWindowFunction[Pos, Pos, Char, GlobalWindow] {
//  override def apply(key: Char, window: GlobalWindow, input: Iterable[Pos], out: Collector[Pos]): Unit = {
//    getRuntimeContext.getState(state).update(input.last)
//    out.collect(input.last)
//  }
//}

class AggregateMazer(mazes: Map[Char, Room], logger: Logger, states: ValueStateDescriptor[Pos]) extends RichAggregateFunction[(Char, Char), Pos, Pos] {
  override def createAccumulator(): Pos = Pos(2, 1)

  override def add(value: (Char, Char), accumulator: Pos): Pos = {

    val (key, step) = value
    mazes.get(key).fold(accumulator) {
      room =>
        val (message, pos) = room.progress(step, accumulator)
        logger.info(key + message)
        getRuntimeContext.getState(states).update(pos)
        pos
    }
  }

  override def getResult(accumulator: Pos): Pos = accumulator

  override def merge(a: Pos, b: Pos): Pos = {
    b
  }
}