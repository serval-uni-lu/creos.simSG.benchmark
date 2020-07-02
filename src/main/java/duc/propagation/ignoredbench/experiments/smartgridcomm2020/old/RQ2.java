package duc.propagation.ignoredbench.experiments.smartgridcomm2020.old;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.Utils;
import duc.sg.java.loadapproximator.uncertain.bsrules.ConfigurationMatrix;
import duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RQ2 {

    private static String prettyFusePrint(Collection<Fuse> fuses) {
        return Arrays.toString(fuses.stream()
                .map(Fuse::getName)
                .toArray());
    }

    public static void main(String[] args) {
        List<Path> topologies = Utils.getTopoFiles();
        for (Path topoPath : topologies) {
            Substation substation = Utils.importSubs(topoPath);
            substation.updateAllFuses();
            Collection<Fuse> allFuses = substation.getAllFuses();

            for (int nbUFuses = 0; nbUFuses <= allFuses.size(); nbUFuses++) {
                //select nbUFuses fuses to be uncertain
                Collection<Fuse> uFuse = Utils.select(allFuses, nbUFuses);

                // make them uncertain
                Utils.makeUncertain(uFuse);

                // Count valid configurations using the derived rules
                ConfigurationMatrix configurationsDerived = UncertainLoadApproximator.getAllConfigurations(substation);

                // Count valid configurations using the naive version
                ConfigurationMatrix confNaive = GetAllConfiguration.getAllConfiguration(substation);

                if(!configurationsDerived.equals(confNaive)) {
                    System.out.println("bad");
                }

                //cleanup method
                Utils.makeCertain(uFuse);
            }
        }

    }
}
