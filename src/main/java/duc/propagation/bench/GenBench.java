package duc.propagation.bench;


import duc.sg.java.model.State;
import duc.sg.java.model.Substation;

import java.util.Arrays;
import java.util.Random;

public abstract class GenBench {
    protected Random random = new Random(12345);
    protected Substation substation;

    public abstract Substation callBuilder(State[] fuseClosed, double[] consumptions);
    public abstract int getNbFuses();

    protected void initSmartGrid() {
        var fuseClosed = new State[getNbFuses()];
        Arrays.fill(fuseClosed, State.CLOSED);

        var consumptions = new double[getNbFuses() / 2];
        for (int i = 0; i < consumptions.length; i++) {
            consumptions[i] = random.nextDouble() * 100;
        }

        substation = callBuilder(fuseClosed, consumptions);
    }


}
