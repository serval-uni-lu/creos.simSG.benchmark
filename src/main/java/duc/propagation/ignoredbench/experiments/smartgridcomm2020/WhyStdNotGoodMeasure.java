package duc.propagation.ignoredbench.experiments.smartgridcomm2020;

import duc.sg.java.cycle.all.InitAllCycleSubs2;
import duc.sg.java.model.Cable;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;
import duc.sg.java.uncertainty.PossibilityDouble;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

public class WhyStdNotGoodMeasure {
    private static final char COMMA = ',';
    private static final int NB_UFUSES = 20;
    private static final String OUT_EXISTENCE_LS = "test-varload-ls.csv";

    private static String prettyFuseName(Fuse fuse) {
        return fuse.getName().replace(" ", "_");
    }

    private static String getCableId(Cable cable) {
        return prettyFuseName(cable.getFirstFuse()) + "_" + prettyFuseName(cable.getSecondFuse());
    }

    private static void printAndCleanLsXP(Writer writer, Substation substation, HashSet<Cable> cableInCircles, StandardDeviation stdComputer, boolean isNaive) {
        var visitedCable = new HashSet<Cable>();
        for (Fuse f: substation.getAllFuses()) {
            Cable c = f.getCable();
            if(!visitedCable.contains(c)) {
                visitedCable.add(c);

                if(cableInCircles.contains(c)) {
                    List<Double> values = c.getUncertainLoad()
                            .format()
                            .stream()
                            .map(PossibilityDouble::getValue)
                            .collect(Collectors.toList());
                    var arrVal = new double[values.size()];
                    for (int i = 0; i < values.size(); i++) {
                        arrVal[i] = values.get(i);
                    }
                    double std = stdComputer.evaluate(arrVal);
                    stdComputer.clear();

                    int version = isNaive? 0 : 1;
                    try {
                        writer.append(String.valueOf(version))
                                .append(COMMA)
                                .append(getCableId(c))
                                .append(COMMA)
                                .append(String.valueOf(std));

                        for (Double v: values) {
                            writer.append(COMMA)
                                    .append(String.valueOf(v));
                        }
                        writer.append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                c.getFirstFuse().resetULoad();
                c.getSecondFuse().resetULoad();
            }
        }
    }

    private static void lsXp() throws IOException {
        var stdComputer = new StandardDeviation();

        try(var writer = new FileWriter(OUT_EXISTENCE_LS, false)) {
            writer.append("version")
                    .append(COMMA)
                    .append("cableId")
                    .append(COMMA)
                    .append("std")
                    .append(COMMA)
                    .append("loads")
                    .append(System.lineSeparator());

            Substation[] substation = new Substation[]{Utils.randomlyGenerateTopo()};
            substation[0].updateAllFuses();

            InitAllCycleSubs2.init(substation[0]);

            Collection<Fuse[]> circles = substation[0].getCycles();
            var cablesInCircles = new HashSet<Cable>();
            for (Fuse[] cycle : circles) {
                for (Fuse f : cycle) {
                    cablesInCircles.add(f.getCable());
                }
            }

            Collection<Fuse> uFuses = Utils.select(substation[0].getAllFuses(), NB_UFUSES);
            Utils.makeUncertain(uFuses);
            Utils.setRandomConsumption(substation[0]);

            // naive version
            duc.sg.java.loadapproximator.uncertain.naive.UncertainLoadApproximator.approximate(substation[0]);
            printAndCleanLsXP(writer, substation[0], cablesInCircles, stdComputer, true);

            // with bs rules version
            duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator.approximate(substation[0]);
            printAndCleanLsXP(writer, substation[0], cablesInCircles, stdComputer, false);

            Utils.makeCertain(uFuses);
        }

    }

    public static void main(String[] args) throws IOException {
        lsXp();
    }
}
