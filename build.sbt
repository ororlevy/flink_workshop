name := "workshop"

version := "0.1"

scalaVersion := "2.12.0"

val flinkVersion =  "1.12.2"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
  "org.apache.flink" %% "flink-runtime-web" % flinkVersion,
  "org.apache.flink" %% "flink-statebackend-rocksdb" % flinkVersion,
  "org.apache.flink" %% "flink-queryable-state-runtime" % flinkVersion,
  "org.apache.flink" % "flink-metrics-dropwizard" % flinkVersion
)