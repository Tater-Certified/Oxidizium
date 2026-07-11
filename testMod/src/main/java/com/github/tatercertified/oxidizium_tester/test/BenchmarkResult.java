package com.github.tatercertified.oxidizium_tester.test;

import org.jspecify.annotations.NonNull;

public class BenchmarkResult implements Comparable<BenchmarkResult> {
    private String methodName;
    private double javaAvgExecTimeUs;
    private double nativeAvgExecTimeUs;

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setJavaAvgExecTimeUs(double Us) {
        this.javaAvgExecTimeUs = Us;
    }

    public void setNativeAvgExecTimeUs(double Us) {
        this.nativeAvgExecTimeUs = Us;
    }

    /**
     * Gives the speed improvement in "times" faster. Ex: 0.75x faster.
     * Negative values means the native version is slower. Ex: -0.5 = 0.5x slower
     * @return "times" faster/slower
     */
    public double speedImprovement() {
        double output = (this.javaAvgExecTimeUs / this.nativeAvgExecTimeUs) - 1;
        return output < 0 ? 1 / output : output;
    }

    /**
     * Gets how much faster the native implementation is.
     * If it is negative, the java implementation is faster
     * @return Difference in microsecond execution time between Java and native
     */
    public double getUsDifference() {
        return this.javaAvgExecTimeUs - this.nativeAvgExecTimeUs;
    }

    @Override
    public int compareTo(@NonNull BenchmarkResult result) {
        return Double.compare(this.speedImprovement(), result.speedImprovement());
    }
}
