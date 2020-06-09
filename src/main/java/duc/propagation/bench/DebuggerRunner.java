package duc.propagation.bench;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class DebuggerRunner {

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder()
                .include("duc.propagation.bench.smartgridcomm2020")
                .shouldFailOnError(false)
                .shouldDoGC(true)
                .warmupForks(0)
                .forks(0)
                .measurementIterations(2)
                .mode(Mode.SingleShotTime)
                .threads(-1)
                .warmupIterations(0)
                .warmupTime(TimeValue.NONE)
                .timeout(TimeValue.minutes(1))
                .resultFormat(ResultFormatType.CSV)
                .result("test.csv")
                .build();
        new Runner(opt).run();
    }
}
