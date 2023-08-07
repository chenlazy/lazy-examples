package com.delin.flink;


import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * DataStreamCase
 *
 * @author: chendl
 * @date: Created in 2023/8/7 12:09
 * @description: com.delin.flink.DataStreamCase
 */
public class DataStreamExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Person> filterStream = environment.fromElements(
                new Person("张三", 28),
                new Person("李四", 21),
                new Person("王五", 43),
                new Person("刘子", 13)
        );

        SingleOutputStreamOperator<Person> filter = filterStream.filter((Person person) ->
                person.age >= 18
        );

        filter.print();

        environment.execute();
    }

    public static class Person {
        public String name;
        public Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return this.name.toString() + ": age " + this.age.toString();
        }
    }
}
