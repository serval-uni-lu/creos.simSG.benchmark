package duc.propagation.ignoredbench.experiments.smartgridcomm2020;

import duc.sg.java.cycle.all.InitAllCycleSubs2;
import duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator;
import duc.sg.java.model.Cable;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.Substation;
import duc.sg.java.uncertainty.PossibilityDouble;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ImpactUcLoad {
    private static final String OUT_FILE_NAME = "sgc-2020-impactuload-sc-load.csv";
    private static final String OUT_FILE_NAME_FACTOR = "sgc-2020-impactuload-factor-sc.csv";
    private static final char COMMA = ',';
    private static final int NB_REPETITION = 1;

    private static void existenceXP() throws IOException {
        Optional<Substation> optSub = Utils.importSubs("realcase-Sub1.json");
        if(optSub.isEmpty()) {
            System.err.println("Error while loading the substation");
            return;
        }

        Substation substation = optSub.get();
        substation.updateAllFuses();
        Utils.makeUncertain(substation.getAllFuses());

        try (var writer = new FileWriter(OUT_FILE_NAME, false); var writer2 = new FileWriter(OUT_FILE_NAME_FACTOR)) {
            writer.append("cableId")
                    .append(COMMA)
                    .append("std")
                    .append(COMMA)
                    .append("loads")
                    .append(System.lineSeparator());

            writer2.append("cableId")
                    .append(COMMA)
                    .append("factor")
                    .append(System.lineSeparator());

            var mapIdxCable = new HashMap<Cable, Integer>();
            var stdComputer = new StandardDeviation();
            int[] lastIdx = new int[]{0};
            Utils.printProgressBar(0);
            for (int i = 1; i <= NB_REPETITION; i++) {
                Utils.setRandomConsumption(substation);
                UncertainLoadApproximator.approximate(substation);

                var visitedCables = new HashSet<Cable>();
                for (Fuse f : substation.getAllFuses()) {
                    Cable c = f.getCable();
                    if (!visitedCables.contains(c)) {
                        visitedCables.add(c);
                        final var f_idxCable = mapIdxCable.compute(c, (cable, idx) -> {
                            if (idx == null) {
                                lastIdx[0]++;
                                System.out.println(c.getFirstFuse().getName() + "_" + c.getSecondFuse().getName() + " -> " + lastIdx[0]);
                                return lastIdx[0];
                            }
                            return idx;
                        });

                        List<Double> values = c.getUncertainLoad()
                                .format()
                                .stream()
                                .map(PossibilityDouble::getValue)
                                .collect(Collectors.toList());
                        values.sort(Comparator.naturalOrder());

                        double[] arrVal = new double[values.size()];
                        for (int i1 = 0; i1 < values.size(); i1++) {
                            arrVal[i1] = values.get(i1);
                        }

                        double std = stdComputer.evaluate(arrVal);
                        stdComputer.clear();
//                        writer.append(String.valueOf(f_idxCable))
//                                .append(COMMA)
//                                .append(String.valueOf(std))
//                                .append(System.lineSeparator());
                        writer.append(String.valueOf(f_idxCable))
                                .append(COMMA)
                                .append(String.valueOf(std))
                                .append(COMMA)
                                .append(Arrays.toString(arrVal))
                                .append(System.lineSeparator());
                        double min = values.get(0);
                        double max = values.get(values.size() - 1);
                        writer2.append(String.valueOf(f_idxCable))
                                .append(COMMA)
                                .append(String.valueOf(max / min))
                                .append(System.lineSeparator());

                    }
                }


                Utils.printProgressBar(i*100./NB_REPETITION);

            }
        }
    }

    private static final int NB_SUBSTATION = 100;
    private static final int NB_REP_PER_SUBS = 100;
    private static final String OUT_EXISTENCE_LARGE_SCALE = "sgc-2020-impactuload-sc-ls.csv";

    public static void existenceXPLarge() throws IOException, InterruptedException {
        final int totalExec = NB_SUBSTATION * NB_REP_PER_SUBS;

        try (var writer = new FileWriter(OUT_EXISTENCE_LARGE_SCALE, false)) {
            writer.append("category")
                    .append(COMMA)
                    .append("std")
                    .append(System.lineSeparator());

            Utils.printProgressBar(0);
            for (int idSubs = 0; idSubs < NB_SUBSTATION; idSubs++) {
                Substation substation = Utils.randomlyGenerateTopo();
                substation.updateAllFuses();
                InitAllCycleSubs2.init(substation);

                Collection<Fuse[]> circles = substation.getCycles();
                var cablesInCircles = new HashSet<Cable>();
                for (Fuse[] cycle : circles) {
                    for (Fuse f : cycle) {
                        cablesInCircles.add(f.getCable());
                    }
                }

                var stdComputer = new StandardDeviation();


                var runnable = new Runnable() {
                    @Override
                    public void run() {
                        Utils.makeUncertain(substation.getAllFuses());
                        Utils.setRandomConsumption(substation);
                        UncertainLoadApproximator.approximate(substation);

                        var visitedCables = new HashSet<Cable>();
                        for (Fuse f : substation.getAllFuses()) {
                            Cable c = f.getCable();
                            if (!visitedCables.contains(c)) {
                                visitedCables.add(c);
                                int category = cablesInCircles.contains(c) ? 0 : 1;

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

                                try {
                                    writer.append(String.valueOf(category))
                                            .append(COMMA)
                                            .append(String.valueOf(std))
                                            .append(System.lineSeparator())
                                    ;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                };




                for (int idxRepet = 0; idxRepet < NB_REP_PER_SUBS; idxRepet++) {
                    var thread = new Thread(runnable);
                    thread.start();
                    thread.join();

                    for (Fuse f: substation.getAllFuses()) {
                        f.resetULoad();
                    }

                    System.gc();
                    int progress = idSubs*NB_REP_PER_SUBS + idxRepet + 1;
                    Utils.printProgressBar(progress*100. / totalExec);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        existenceXP();
    }

}
