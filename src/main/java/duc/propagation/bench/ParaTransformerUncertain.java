package duc.propagation.bench;

import duc.aintea.sg.Fuse;
import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.ParaCabinetBuilder;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 3, value = 6)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParaTransformerUncertain extends GenUncertain {

    @Param({"0", "1", "2", "3", "4", "5", "6"})
    private int nbUncFuses;

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return ParaCabinetBuilder.build(fuseClosed, consumptions);
    }

    @Override
    public int getNbFuses() {
        return 6;
    }

    @Override
    public Fuse[] callFuseExtractor() {
        return ParaCabinetBuilder.extractFuses(substation);
    }

    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }
}
