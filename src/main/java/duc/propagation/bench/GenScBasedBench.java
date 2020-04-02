package duc.propagation.bench;

import duc.sg.java.model.State;
import duc.sg.java.model.Substation;
import duc.sg.java.scenarios.ScenarioBuilder;
import duc.sg.java.scenarios.ScenarioName;

public abstract class GenScBasedBench extends GenBench {

    public abstract ScenarioName getSCName();


    @Override
    public final int getNbFuses() {
        return getSCName().getNbFuses();
    }

    @Override
    public final Substation callBuilder(State[] fuseClosed, double[] consumptions) {
        return new ScenarioBuilder()
                .chooseScenario(getSCName())
                .setFuseStates(fuseClosed)
                .setConsumptions(consumptions)
                .build()
                .getSubstation();
    }
}
