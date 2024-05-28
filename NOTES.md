docker-compose exec broker bash

logs in /opt/kafka/logs

kafka-console-consumer --bootstrap-server localhost:9092 --topic test_dest --f
rom-beginning

Will see:

```terminal
11:06:49.642 [main] INFO Example -- (groupId='console-consumer-34974', isSimpleConsumerGroup=false, state=Optional[Stable])

```

~/Documents/workspace/kafka_2.13-3.4.0/bin/

kafka-consumer-groups --bootstrap-server localhost:9092 --list

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group console-consumer-34974

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group console-consumer-34974 --members