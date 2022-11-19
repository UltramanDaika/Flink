package com.yizeng.chapter05;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.ArrayList;
import java.util.Properties;

public class sourcetest
{
    public static void main(String[] args) throws Exception
    {
        // 1.创建执行环境，并设置并行度为1
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2.从文件中读取数据
        DataStreamSource<String> textstream = env.readTextFile("input/clicks.txt");

        // 3.从集合中读取数据
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        DataStreamSource<Integer> numstream = env.fromCollection(nums);

        ArrayList<event> events = new ArrayList<>();
        events.add(new event("Leo","./home",1000L));
        events.add(new event("Tommy","./cart",2000L));
        DataStreamSource<event> eventstream = env.fromCollection(events);

        // 4.从元素读取数据
        DataStreamSource<event> elementstream = env.fromElements(
                new event("Leo", "./home", 1000L),
                new event("Tommy", "./cart", 2000L));

        // 5.从socket文本流读取数据
        DataStreamSource<String> socketstream = env.socketTextStream("node1", 7777);

        // 6.从kafka读取数据
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","node1:9092");
        properties.setProperty("group.id","consumer-group");
        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset","latest");
        DataStreamSource<String> kafkastream = env.addSource(new FlinkKafkaConsumer<String>("clicks", new SimpleStringSchema(), properties));
        textstream.print("text");
        numstream.print("nums");
        eventstream.print("event");
        elementstream.print("element");
        socketstream.print("socket");
        kafkastream.print("kafka");
        env.execute();
    }

}
