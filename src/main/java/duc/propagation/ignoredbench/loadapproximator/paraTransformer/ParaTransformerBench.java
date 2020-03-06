package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Cable;
import duc.aintea.sg.Fuse;
import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.ParaTransformerBuilder;
import duc.propagation.ignoredbench.loadapproximator.GenericBench;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class ParaTransformerBench extends GenericBench {

    @Override
    protected Substation initSubs() {
        return ParaTransformerBuilder.build();
    }

    @Override
    protected Fuse[] initFuses() {
        return ParaTransformerBuilder.extractFuses(substation);
    }

    @Override
    protected Cable[] getCables() {
        return ParaTransformerBuilder.extractCables(substation);
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
//
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
//
//    @Benchmark
//    public void benchUncertainApprox5Fuse() {
//        makeUncertain(5);
//        UncertainLoadApproximator.approximate(substation);
//    }

    @Benchmark
    public void benchUncertainApprox6Fuse() {
        makeUncertain(6);
        UncertainLoadApproximator.approximate(substation);
    }

}
