package StageTwo
import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.CheckpointRollingPolicy
class OnNewElementCheckpointPolicy[IN, BucketId] extends CheckpointRollingPolicy[IN, BucketId]{
  private val serialVersionUID = 1L
  override def shouldRollOnEvent(partFileState: PartFileInfo[BucketId], element: IN): Boolean = true
  override def shouldRollOnProcessingTime(partFileState: PartFileInfo[BucketId], currentTime: Long): Boolean = true
}