package duc.propagation.bench;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class DebuggerRunner {

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder()
                .include("duc.propagation.bench.IndirectParaUncertain.ucLoadApprox")
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .warmupForks(3)
                .forks(0)
                .param("nbUncFuses", "7")
                .build();
        new Runner(opt).run();
    }
}
