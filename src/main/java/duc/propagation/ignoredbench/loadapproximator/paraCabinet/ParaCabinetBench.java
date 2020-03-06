package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Cable;
import duc.aintea.sg.Fuse;
import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.ParaCabinetBuilder;
import duc.propagation.ignoredbench.loadapproximator.GenericBench;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class ParaCabinetBench extends GenericBench {

    @Override
    protected Substation initSubs() {
        return ParaCabinetBuilder.build();
    }

    @Override
    protected Fuse[] initFuses() {
        return ParaCabinetBuilder.extractFuses(substation);
    }

    @Override
    protected Cable[] getCables() {
        return ParaCabinetBuilder.extractCables(substation);
    }

//    @Benchmark
//    public void benchUncertainApprox1Fuse() {
//        makeUncertain(1);
//        UncertainLoadApproximator.approximate(substation);
//    }

//    @Benchmark
//    public void benchUncertainApprox2Fuse() {
//        makeUncertain(2);
//        UncertainLoadApproximator.approximate(substation);
//    }

//    @Benchmark
//    public void benchUncertainApprox3Fuse() {
//        makeUncertain(3);
//        UncertainLoadApproximator.approximate(substation);
//    }

//    @Benchmark
//    public void benchUncertainApprox4Fuse() {
//        makeUncertain(4);
//        UncertainLoadApproximator.approximate(substation);
//    }

//    @Benchmark
//    public void benchUncertainApprox5Fuse() {
//        makeUncertain(5);
//        UncertainLoadApproximator.approximate(substation);
//    }
//
//    @Benchmark
//    public void benchUncertainApprox6Fuse() {
//        makeUncertain(6);
//        UncertainLoadApproximator.approximate(substation);
//    }
//
//    @Benchmark
//    public void benchUncertainApprox7Fuse() {
//        makeUncertain(7);
//        UncertainLoadApproximator.approximate(substation);
//    }

    @Benchmark
    public void benchUncertainApprox8Fuse() {
        makeUncertain(8);
        UncertainLoadApproximator.approximate(substation);
    }
}
