package duc.propagation.bench;

import duc.sg.java.scenarios.ScenarioName;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 3, value = 6)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParaTransformerUncertain extends GenScBasedUncertain {

    @Param({"0", "1", "2", "3", "4", "5", "6"})
    private int nbUncFuses;



    @Override
    public ScenarioName getSCName() {
        return ScenarioName.PARA_TRANSFORMER;
    }

    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }
}
