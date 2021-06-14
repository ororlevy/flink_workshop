package StageFour

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.slf4j.Logger

class MazeTrigger(mazes: Map[Char, Room], states: ValueStateDescriptor[Pos], logger: Logger) extends Trigger[(Char, Char), GlobalWindow] {
  override def onElement(element: (Char, Char), timestamp: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = {
    val state = ctx.getPartitionedState(states)
    val pos = state.value()
    val validPos = if(pos==null) TreasureHunt.startingPos else pos
    val (key, step) = element
    mazes.get(key).fold(TriggerResult.CONTINUE) {
      room =>
        val (message, next) = room.progress(step, validPos)
        logger.info(key + message)
        state.update(next)
        val result = if (room.lastPoint == next) TriggerResult.FIRE else TriggerResult.CONTINUE
        result
    }


  }

  override def onProcessingTime(time: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  override def onEventTime(time: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  override def clear(window: GlobalWindow, ctx: Trigger.TriggerContext): Unit =
    ctx.getPartitionedState(states).clear()
}
