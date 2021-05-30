
kafka:
	@echo "Starting zookeeper"
	./kafka_2.12-2.3.1/bin/zookeeper-server-start.sh kafka_2.12-2.3.1/config/zookeeper.properties >/dev/null 2>&1 < /dev/null &
	sleep 2
	@echo "Starting kafka"
	./kafka_2.12-2.3.1/bin/kafka-server-start.sh kafka_2.12-2.3.1/config/server.properties >/dev/null 2>&1 < /dev/null &

topic:
	@echo "Creating topic"
	./kafka_2.12-2.3.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic maze

shutdown:
	./kafka_2.12-2.3.1/bin/kafka-server-stop.sh
	./kafka_2.12-2.3.1/bin/zookeeper-server-stop.sh

delete-history:
	rm -rf kafka_2.12-2.3.1/logs
	rm -rf /tmp/kafka-logs-workshop
	rm -rf /tmp/zookeeper-workshop

solve:
	./kafka_2.12-2.3.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test