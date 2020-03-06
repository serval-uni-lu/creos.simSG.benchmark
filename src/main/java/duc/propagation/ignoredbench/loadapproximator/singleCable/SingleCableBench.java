package duc.propagation.ignoredbench.loadapproximator.singleCable;

import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Cable;
import duc.aintea.sg.Fuse;
import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.SingleCableBuilder;
import duc.propagation.ignoredbench.loadapproximator.GenericBench;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class SingleCableBench extends GenericBench {

    @Override
    protected Substation initSubs() {
        return SingleCableBuilder.build();
    }

    @Override
    protected Fuse[] initFuses() {
        return SingleCableBuilder.extractFuses(substation);
    }

    @Override
    protected Cable[] getCables() {
        return SingleCableBuilder.extractCables(substation);
    }

    @Benchmark
    public void benchUncertainApprox1Fuse() {
        makeUncertain(1);
        UncertainLoadApproximator.approximate(substation);
    }

    @Benchmark
    public void benchUncertainApprox2Fuse() {
        makeUncertain(2);
        UncertainLoadApproximator.approximate(substation);
    }
    
}
