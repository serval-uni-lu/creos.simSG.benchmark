package duc.propagation.bench;

import duc.sg.java.scenarios.ScenarioName;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 5, value = 10)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class IndirectParaUncertain extends GenScBasedUncertain {
    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
    private int nbUncFuses;

    @Override
    public ScenarioName getSCName() {
        return ScenarioName.INDIRECT_PARALLEL;
    }


    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }

}
