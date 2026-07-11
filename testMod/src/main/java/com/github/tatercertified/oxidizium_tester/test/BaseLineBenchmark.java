package com.github.tatercertified.oxidizium_tester.test;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = NativeTest.BENCHMARK_ITERATIONS_TIME_S, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class BaseLineBenchmark {

    @Benchmark
    public void baseline() {
        // Gets the overhead of JMH
    }
}
