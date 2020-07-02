package duc.propagation.ignoredbench.experiments.smartgridcomm2020.old;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.Utils;
import duc.sg.java.loadapproximator.uncertain.bsrules.ConfigurationMatrix;
import duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static duc.propagation.ignoredbench.experiments.smartgridcomm2020.Utils.printProgressBar;

public class RQ1 {
    public static final int NB_REPETITION = 1000;
    private static final char COMMA = ',';


    public static Fuse get(Collection<Fuse> fuses, String name) {
        for (Fuse fuse: fuses) {
            if(fuse.getName().equals(name)) {
                return fuse;
            }
        }

        return null;
    }


    public static void main(String[] args) throws IOException {
        List<Path> topologies = Utils.getTopoFiles();

        try (FileWriter writer = new FileWriter("smartgridcomm2020-rq1-org.csv", false)) {

            writer.append("File Name")
                    .append(COMMA)
                    .append("Idx Repetition")
                    .append(COMMA)
                    .append("Nb. UFuses")
                    .append(COMMA)
                    .append("Nb. Fuses")
                    .append(COMMA)
                    .append("Nb. Configurations")
                    .append(COMMA)
                    .append("Nb. Valid Conf.")
                    .append(System.lineSeparator());

            Utils.printProgressBar(0.);

            double totalExec = topologies.size() * NB_REPETITION;

            int idxTopo = 0;
            for (Path topoPath : topologies) {
                Substation substation = Utils.importSubs(topoPath);
                Collection<Fuse> allFuses = substation.extractFuses();

                for (int idxRepetition = 0; idxRepetition < NB_REPETITION; idxRepetition++) {
                    for (int nbUFuses = 0; nbUFuses <= allFuses.size(); nbUFuses++) {
                        //select nbUFuses fuses to be uncertain
                        Collection<Fuse> uFuse = Utils.select(allFuses, nbUFuses);

                        // make them uncertain
                        Utils.makeUncertain(uFuse);

                        // Count valid configurations
                        ConfigurationMatrix configurations = UncertainLoadApproximator.getAllConfigurations(substation);

                        writer.append(topoPath.getFileName().toString())
                                .append(COMMA)
                                .append(String.valueOf(idxRepetition))
                                .append(COMMA)
                                .append(String.valueOf(nbUFuses))
                                .append(COMMA)
                                .append(String.valueOf(allFuses.size()))
                                .append(COMMA)
                                .append(String.valueOf((int) Math.pow(2, nbUFuses)))
                                .append(COMMA)
                                .append(String.valueOf(configurations.nbConfigurations()))
                                .append(System.lineSeparator());

                        //cleanup method
                        Utils.makeCertain(uFuse);
                    }
                    double current = idxTopo * NB_REPETITION + idxRepetition;
                    printProgressBar(current * 100 / totalExec);
                }
                idxTopo++;
                int current = idxTopo * NB_REPETITION;
                printProgressBar(current * 100 / totalExec);
            }
        }
    }

}
