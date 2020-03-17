package duc.propagation.bench;

import duc.aintea.sg.Fuse;
import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.IndirectParaBuilder;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 5, value = 10)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class IndirectParaUncertain extends GenUncertain {
    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
    private int nbUncFuses;

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return IndirectParaBuilder.build(fuseClosed, consumptions);
    }

    @Override
    public int getNbFuses() {
        return 10;
    }

    @Override
    public Fuse[] callFuseExtractor() {
        return IndirectParaBuilder.extractFuses(substation);
    }

    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }

}
