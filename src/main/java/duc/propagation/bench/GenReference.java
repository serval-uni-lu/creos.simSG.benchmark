package duc.propagation.bench;


import duc.sg.java.loadapproximator.loadapproximation.LoadApproximator;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 3, value = 6)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class GenReference extends GenBench {

    @Setup(Level.Trial)
    public void setup() {
        initSmartGrid();
    }

    @Benchmark
    public void referenceBench() {
        LoadApproximator.approximate(substation);
    }


}
