## Welcome to Flink Adventure

#### Your backpack is ready for you  
We have packed for you all the tools for the upcoming adventure 
1. Flink v1.12.3 for scala 2.11
    - just unzip dist.zip in flink-1.12.3/lib
    - on linux/mac just run 'make flink-ready'
2. Kafka to work locally.

#### Controlling kafka
We will use a local Kafka in this adventure, and we made some commands
to help you operate Kafka. Of course,
you could use the old-fashioned commands if you feel like it. 

- make kafka: will start Kafka server and zookeeper for you
- made topic: will create the main topic
- make solve: will open a producer for you to send messages
- make shutdown: will shutdown Kafka server and zookeeper 
- make delete-history: will delete all Kafka logs and topics

### Your Flink Application
Your Flink is armed with a top-notch cipher that can decode any messages in the journey.
But your application is effortless; you will have to upgrade it later on 
You can compile and build JAR by simpling using sbt assembly 