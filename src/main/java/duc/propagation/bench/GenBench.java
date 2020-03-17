package duc.propagation.bench;

import duc.aintea.sg.Substation;

import java.util.Arrays;
import java.util.Random;

public abstract class GenBench {
    protected Random random = new Random(12345);
    protected Substation substation;

    public abstract Substation callBuilder(boolean[] fuseClosed, double[] consumptions);
    public abstract int getNbFuses();

    protected void initSmartGrid() {
        var fuseClosed = new boolean[getNbFuses()];
        Arrays.fill(fuseClosed, true);

        var consumptions = new double[getNbFuses() / 2];
        for (int i = 0; i < consumptions.length; i++) {
            consumptions[i] = random.nextDouble() * 100;
        }

        substation = callBuilder(fuseClosed, consumptions);
    }


}
