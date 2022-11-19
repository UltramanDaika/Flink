package com.yizeng.chapter05;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import java.util.ArrayList;

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

        textstream.print("text");
        numstream.print("nums");
        eventstream.print("event");
        elementstream.print("element");
        socketstream.print("socket");
        env.execute();
    }

}
