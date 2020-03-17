package duc.propagation.bench;

import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Fuse;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public abstract class GenUncertain extends GenBench{

    public abstract Fuse[] callFuseExtractor();
    public abstract int getNbUcFuses();

    @Setup(Level.Trial)
    public void setup() {
        initSmartGrid();
        Fuse[] fuses = callFuseExtractor();
        Utils.makeRandFuseUc(fuses, getNbUcFuses(), random);
    }

    @Benchmark
    public void ucLoadApprox() {
        UncertainLoadApproximator.approximate(substation);
    }
}
