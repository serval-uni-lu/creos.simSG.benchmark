package duc.propagation.bench;

import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Extractor;
import duc.aintea.sg.Fuse;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public abstract class GenUncertain extends GenBench{

    public Fuse[] callFuseExtractor() {
        return Extractor.extractFuses(substation).toArray(new Fuse[0]);
    }

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
