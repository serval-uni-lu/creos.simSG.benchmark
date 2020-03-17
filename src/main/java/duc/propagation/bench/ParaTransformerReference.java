package duc.propagation.bench;

import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.ParaTransformerBuilder;

public class ParaTransformerReference extends GenReference {

    @Override
    public int getNbFuses() {
        return 6;
    }

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return ParaTransformerBuilder.build(fuseClosed, consumptions);
    }

}
