package duc.propagation.ignoredbench.experiments.smartgridcomm2020;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.old.GetAllConfiguration;
import duc.sg.java.loadapproximator.uncertain.bsrules.ConfigurationMatrix;
import duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EquivalenceGeneralDerived {
    private static final int NB_RAND_SUBS = 100;
    private static final int MAX_NB_FUSES = 22;

    private static String prettyFusePrint(Collection<Fuse> fuses) {
        return Arrays.toString(fuses.stream()
                .map(Fuse::getName)
                .toArray());
    }

    private static void compare(Substation substation) {
        substation.updateAllFuses();
        Collection<Fuse> allFuses = substation.getAllFuses();

        for (int nbUFuses = 0; nbUFuses <= Math.min(allFuses.size(), MAX_NB_FUSES); nbUFuses++) {
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
                System.exit(2);
            }

            //cleanup method
            Utils.makeCertain(uFuse);
        }
    }


    public static void main(String[] args) {
        List<Path> topologies = Utils.getTopoFiles();

        int maxNbXp = topologies.size() + NB_RAND_SUBS;
        Substation substation;

        int idxXp = 0;
        Utils.printProgressBar(0);
        for (Path topoPath : topologies) {
            System.gc();
            substation = Utils.importSubs(topoPath);

            compare(substation);

            double progress = idxXp*100./maxNbXp;
            Utils.printProgressBar(progress);
            idxXp++;
        }

        for (int i = 0; i < NB_RAND_SUBS; i++, idxXp++) {
            System.gc();
            substation = Utils.randomlyGenerateTopo();
            compare(substation);

            double progress = idxXp*100./maxNbXp;
            Utils.printProgressBar(progress);
        }
        double progress = idxXp*100./maxNbXp;
        Utils.printProgressBar(progress);

    }
}
