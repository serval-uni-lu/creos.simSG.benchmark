package duc.propagation.ignoredbench;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.Utils;
import duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator;
import duc.sg.java.model.Substation;

public class MemoryAnalysis {


    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            Substation substation = Utils.randomlyGenerateTopo();
            substation.updateAllFuses();
            Utils.makeUncertain(substation.getAllFuses());

            Utils.setRandomConsumption(substation);

            UncertainLoadApproximator.approximate(substation);

            System.gc();

        }
    }
}
