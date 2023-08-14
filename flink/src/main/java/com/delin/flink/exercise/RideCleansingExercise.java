package com.delin.flink.exercise;

import com.delin.flink.entity.TaxiRide;
import com.delin.flink.utils.GeoUtils;
import com.delin.flink.utils.TaxiRideGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;

/**
 * RideCleansingExercise
 *
 * @author: chendl
 * @date: Created in 2023/8/8 9:46
 * @description: com.delin.flink.exercise.RideCleansingExercise
 */
public class RideCleansingExercise {

    private final SourceFunction<TaxiRide> source;

    private final SinkFunction<TaxiRide> sink;

    /**
     * Creates a job using the source and sink provided.
     */
    public RideCleansingExercise(SourceFunction<TaxiRide> source, SinkFunction<TaxiRide> sink) {

        this.source = source;
        this.sink = sink;
    }

    public JobExecutionResult execute() throws Exception {

        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.of(10, TimeUnit.SECONDS)));
        // set up the pipeline
        DataStreamSource<TaxiRide> taxiRideDataStreamSource = env.addSource(source);

        SingleOutputStreamOperator<TaxiRide> outputStreamOperator = taxiRideDataStreamSource
                .filter((TaxiRide taxiRide) -> GeoUtils.isInNYC(taxiRide.startLon, taxiRide.startLat) && GeoUtils.isInNYC(taxiRide.endLon, taxiRide.endLat));

        outputStreamOperator.print();
        // run the pipeline and return the result
        return env.execute("Taxi Ride Cleansing");
    }

    public static void main(String[] args) throws Exception {
        RideCleansingExercise job =
                new RideCleansingExercise(new TaxiRideGenerator(), new PrintSinkFunction<>());

        job.execute();
    }

    /**
     * Keep only those rides and both start and end in NYC.
     */
    public static class NYCFilter implements FilterFunction<TaxiRide> {
        @Override
        public boolean filter(TaxiRide taxiRide) throws Exception {
            return GeoUtils.isInNYC(taxiRide.startLon, taxiRide.startLat) && GeoUtils.isInNYC(taxiRide.endLon, taxiRide.endLat);
        }
    }
}
