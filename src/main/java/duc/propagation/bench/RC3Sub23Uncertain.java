package duc.propagation.bench;

import duc.sg.java.model.Substation;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 5, value = 10)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Timeout(time = 15, timeUnit = TimeUnit.MINUTES)
public class RC3Sub23Uncertain extends GenUncertain {
    @Param({"Substation 2", "Substation 3"})
    private String subName;

    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16"})
    private int nbUncFuses;

    @Override
    public int getNbUcFuses() {
        return nbUncFuses;
    }

    @Override
    public Substation callBuilder(duc.sg.java.model.State[] fuseClosed, double[] consumptions) {
        return RealCase3SubsHelper.getSubs(subName, consumptions);
    }

    @Override
    public int getNbFuses() {
        return 16;
    }
}
