package com.delin.flink.exercise;

import com.delin.flink.entity.RideAndFare;
import com.delin.flink.entity.TaxiFare;
import com.delin.flink.entity.TaxiRide;
import com.delin.flink.utils.TaxiFareGenerator;
import com.delin.flink.utils.TaxiRideGenerator;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Objects;

/**
 * RidesAndFaresExercise  有状态的增强(车程及车费)
 *
 * @author: chendl
 * @date: Created in 2023/8/8 11:11
 * @description: com.delin.flink.exercise.RidesAndFaresExercise
 */
public class RidesAndFaresExercise {

    private SourceFunction<TaxiRide> taxiRideSource;

    private SourceFunction<TaxiFare> taxiFareSource;

    private PrintSinkFunction<RideAndFare> rideAndFarePrintSink;

    public RidesAndFaresExercise(SourceFunction<TaxiRide> taxiRideSource, SourceFunction<TaxiFare> taxiFareSource, PrintSinkFunction<RideAndFare> rideAndFarePrintSink) {
        this.taxiRideSource = taxiRideSource;
        this.taxiFareSource = taxiFareSource;
        this.rideAndFarePrintSink = rideAndFarePrintSink;
    }

    public void execute() throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        KeyedStream<TaxiRide, Long> rideLongKeyedStream = environment.addSource(taxiRideSource).
                filter((TaxiRide taxiRide) -> taxiRide.isStart).keyBy(TaxiRide::getRideId);

        KeyedStream<TaxiFare, Long> taxiFareLongKeyedStream = environment.addSource(taxiFareSource).keyBy(TaxiFare::getRideId);

        rideLongKeyedStream.connect(taxiFareLongKeyedStream).flatMap(new NYCEnrichment()).addSink(rideAndFarePrintSink);
        environment.execute();
    }

    public static class NYCEnrichment extends RichCoFlatMapFunction<TaxiRide, TaxiFare, RideAndFare> {

        ValueState<TaxiRide> taxiRideSet;

        ValueState<TaxiFare> taxiFareSet;

        @Override
        public void open(Configuration parameters) throws Exception {
            taxiRideSet = getRuntimeContext().getState(new ValueStateDescriptor<>("taxiRide", TaxiRide.class));
            taxiFareSet = getRuntimeContext().getState(new ValueStateDescriptor<>("taxiFare", TaxiFare.class));
        }

        @Override
        public void flatMap1(TaxiRide value, Collector<RideAndFare> out) throws Exception {
            TaxiFare fare = taxiFareSet.value();
            if (Objects.isNull(fare)) {
                taxiRideSet.update(value);
                return;
            }
            taxiFareSet.clear();
            out.collect(new RideAndFare(value, fare));
        }

        @Override
        public void flatMap2(TaxiFare value, Collector<RideAndFare> out) throws Exception {
            TaxiRide ride = taxiRideSet.value();

            if (Objects.isNull(ride)) {
                taxiFareSet.update(value);
                return;
            }
            taxiRideSet.clear();
            out.collect(new RideAndFare(ride, value));
        }
    }

    public static void main(String[] args) throws Exception {
        RidesAndFaresExercise ridesAndFaresExercise = new RidesAndFaresExercise(new TaxiRideGenerator(), new TaxiFareGenerator(), new PrintSinkFunction<>());
        ridesAndFaresExercise.execute();
    }
}
