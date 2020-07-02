package duc.propagation.ignoredbench;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.Utils;
import duc.sg.java.loadapproximator.loadapproximation.LoadApproximator;
import duc.sg.java.model.Meter;
import duc.sg.java.model.Substation;

import java.util.Arrays;
import java.util.Optional;

public class ToDelete {

    public static void main(String[] args) {
        Optional<Substation> optSubs = Utils.importSubs("paraDeadEnd.json");
        if(optSubs.isEmpty()) {
            System.err.println("bad...");
            System.exit(3);
        }

        Substation substation = optSubs.get();
        substation.updateAllFuses();

        var m1 = new Meter("m1");
        m1.setConsumption(40);
        substation.getFuses().get(0).getCable().addMeters(m1);

        var m2 = new Meter("m2");
        m2.setConsumption(20);
        substation.getFuses().get(1).getCable().addMeters(m2);


        LoadApproximator.approximate(substation);
        System.out.println(Arrays.toString(substation.getFuses().get(0).getUncertainLoad().format().toArray()));
        System.out.println(Arrays.toString(substation.getFuses().get(1).getUncertainLoad().format().toArray()));
    }
}
