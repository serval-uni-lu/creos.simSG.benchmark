package duc.propagation.bench;

import duc.aintea.sg.Substation;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class RC3SReference extends GenReference {
    @Param({"Substation 1", "Substation 2", "Substation 3"})
    private String subName;


    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return RealCase3SubsHelper.getSubs(subName, consumptions);
    }

    @Override
    public int getNbFuses() {
        if(subName.equals("Substation 1")) return 30;
        return 16;
    }

    public static void main(String[] args) throws RunnerException {
        var opts = new OptionsBuilder()
                .include(RC3SReference.class.getSimpleName())
                .mode(Mode.SingleShotTime)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .forks(0)
                .warmupIterations(0)
                .warmupBatchSize(1)
                .warmupForks(1)
                .measurementIterations(1)
                .build();
        new Runner(opts).run();
    }
}
