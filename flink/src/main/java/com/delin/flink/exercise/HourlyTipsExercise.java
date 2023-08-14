package com.delin.flink.exercise;

import com.delin.flink.entity.TaxiFare;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * HourlyTipsExercise 窗口分析 (每小时小费)
 *
 * @author: chendl
 * @date: Created in 2023/8/8 12:24
 * @description: com.delin.flink.exercise.HourlyTipsExercise
 */
public class HourlyTipsExercise {

    private SourceFunction<TaxiFare> taxiFareSource;

    public HourlyTipsExercise(SourceFunction<TaxiFare> taxiFareSource) {
        this.taxiFareSource = taxiFareSource;
    }

    public void execute() throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        environment.addSource(taxiFareSource);

        environment.execute();
    }
}
