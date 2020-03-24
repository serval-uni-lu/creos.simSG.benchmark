package duc.propagation.bench;

import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.IndirectParaBuilder;

public class IndirectParaReference extends GenReference {
    @Override
    public int getNbFuses() {
        return 10;
    }

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return  IndirectParaBuilder.build(fuseClosed, consumptions);
    }
}
