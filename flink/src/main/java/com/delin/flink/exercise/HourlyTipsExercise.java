package com.delin.flink.exercise;

import com.delin.flink.entity.TaxiFare;
import com.delin.flink.utils.TaxiFareGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * HourlyTipsExercise 窗口分析 (每小时小费)
 *
 * 所希望的结果是每小时产生一个 Tuple3<Long, Long, Float> 记录的数据流。 这个记录（Tuple3<Long, Long, Float>）应包含该小时结束时的时间戳（对应三元组的第一个元素）、
 * 该小时内获得小费最多的司机的 driverId（对应三元组的第二个元素）以及他的实际小费总数（对应三元组的第三个元素））。
 * @author: chendl
 * @date: Created in 2023/8/8 12:24
 * @description: com.delin.flink.exercise.HourlyTipsExercise
 */
public class HourlyTipsExercise {

    private SourceFunction<TaxiFare> taxiFareSource;

    private PrintSinkFunction<Tuple3<Long, Long, Float>> printSinkFunction;

    public HourlyTipsExercise(SourceFunction<TaxiFare> taxiFareSource, PrintSinkFunction<Tuple3<Long, Long, Float>> printSinkFunction) {
        this.taxiFareSource = taxiFareSource;
        this.printSinkFunction = printSinkFunction;
    }

    public void execute() throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

                        SingleOutputStreamOperator<TaxiFare> fareSingleOutputStreamOperator = environment.addSource(taxiFareSource).assignTimestampsAndWatermarks(WatermarkStrategy.<TaxiFare>forMonotonousTimestamps()
                .withTimestampAssigner((event, time) -> event.getEventTimeMillis()));


        SingleOutputStreamOperator<Tuple3<Long, Long, Float>> processed = fareSingleOutputStreamOperator.keyBy(TaxiFare::getDriverId)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .process(new MyWastefulMax());

        SingleOutputStreamOperator<Tuple3<Long, Long, Float>> maxed = processed.windowAll(TumblingEventTimeWindows.of(Time.hours(1))).max(2);

        maxed.addSink(printSinkFunction);
        environment.execute();
    }

    /**
     * 批量处理，ProcessWindowFunction 会缓存 Iterable 和窗口内容，供接下来全量计算
     * 处理，每一次有事件被分配到窗口时，都会调用 ReduceFunction 或者 AggregateFunction 来增量计算
     * 通过 ReduceFunction 或者 AggregateFunction 预聚合的增量计算结果在触发窗口时， 提供给 ProcessWindowFunction 做全量计算。
     */
    static class MyWastefulMax extends ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow> {

        @Override
        public void process(Long key, ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow>.Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Float>> out) throws Exception {
            float a = 0F;
            for (TaxiFare element : elements) {
                a += element.getTip();
            }
            out.collect(Tuple3.of(key, context.window().getEnd(), a));
        }
    }

    static class MyWindowAggFunction extends ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow> {

        @Override
        public void process(Long aLong, ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow>.Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Float>> out) throws Exception {

        }
    }

    public static void main(String[] args) throws Exception {
        HourlyTipsExercise hourlyTipsExercise = new HourlyTipsExercise(new TaxiFareGenerator(), new PrintSinkFunction<>());

        hourlyTipsExercise.execute();
    }
}
