name := "workshop"

version := "0.1"

scalaVersion := "2.11.0"

val flinkVersion =  "1.12.2"
mainClass in (Compile, packageBin) := Some("StageOne.ParserBatch")
libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
  "org.apache.flink" %% "flink-runtime-web" % flinkVersion,
  "org.apache.flink" %% "flink-statebackend-rocksdb" % flinkVersion,
  "org.apache.flink" %% "flink-queryable-state-runtime" % flinkVersion,
  "org.apache.flink" % "flink-metrics-dropwizard" % flinkVersion
)

resolvers += "Bintray sbt plugin releases" at "https://dl.bintray.com/sbt/sbt-plugin-releases/"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}