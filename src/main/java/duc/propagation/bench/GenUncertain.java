package duc.propagation.bench;

import duc.sg.java.loadapproximator.uncertain.naive.UncertainLoadApproximator;
import duc.sg.java.model.Fuse;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public abstract class GenUncertain extends GenBench {

    public abstract int getNbUcFuses();

    @Setup(Level.Trial)
    public void setup() {
        initSmartGrid();
        Fuse[] fuses = substation.extractFuses().toArray(new Fuse[0]);
        Utils.makeRandFuseUc(fuses, getNbUcFuses(), random);
    }

    @Benchmark
    public void ucLoadApprox() {
        UncertainLoadApproximator.approximate(substation);
    }
}
