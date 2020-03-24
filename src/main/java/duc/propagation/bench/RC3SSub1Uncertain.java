package duc.propagation.bench;

import duc.aintea.sg.Substation;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 5, value = 10)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Timeout(time = 15, timeUnit = TimeUnit.MINUTES)
public class RC3SSub1Uncertain extends GenUncertain {

    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24"})
    private int nbUncFuses;

    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return RealCase3SubsHelper.getSubs("Substation 1", consumptions);
    }

    @Override
    public int getNbFuses() {
        return 30;
    }

    public static void main(String[] args) throws RunnerException {
        var opts = new OptionsBuilder()
                .include(RC3SSub1Uncertain.class.getSimpleName())
                .mode(Mode.SingleShotTime)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .forks(0)
                .warmupIterations(0)
                .warmupBatchSize(1)
                .warmupForks(1)
                .measurementIterations(1)
                .build();
        new Runner(opts).run();
    }

}
