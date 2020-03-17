package duc.propagation.bench;


import duc.aintea.sg.Substation;
import duc.aintea.sg.scenarios.ParaCabinetBuilder;

public class ParaCabinetReference extends GenReference {
    @Override
    public int getNbFuses() {
        return 8;
    }

    @Override
    public Substation callBuilder(boolean[] fuseClosed, double[] consumptions) {
        return ParaCabinetBuilder.build(fuseClosed, consumptions);
    }
}
