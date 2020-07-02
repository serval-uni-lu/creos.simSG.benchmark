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

public class VarianceCableAllVsValid {
    private static final String OUT_FILE_NAME = "sgc-2020-varload-sc.csv";
    private static final char COMMA = ',';
    private static final int NB_REPETITION = 100;
    private static final int NB_UFUSES = 20;

    private static void printResAndRemove(Substation substation, FileWriter writer, Map<Cable, Integer> mapIdxCable, final int[] lastIdx, StandardDeviation stdComputer, boolean withNaiveVersion) throws IOException {
        var visitedCables = new HashSet<Cable>();
        for(Fuse f: substation.getAllFuses()) {
            Cable c = f.getCable();
            if(!visitedCables.contains(c)) {
                visitedCables.add(c);
                final var idxCable = mapIdxCable.compute(c, (cable, idx) -> {
                    if(idx == null) {
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

                double[] arrVal = new double[values.size()];
                for (int i1 = 0; i1 < values.size(); i1++) {
                    arrVal[i1] = values.get(i1);
                }

                double std = stdComputer.evaluate(arrVal);
                stdComputer.clear();


                writer.append(String.valueOf(idxCable))
                        .append(COMMA);
                if(withNaiveVersion) {
                    writer.append("naive");
                } else {
                    writer.append("bs");
                }
                writer.append(COMMA)
                        .append(String.valueOf(std))
                        .append(System.lineSeparator());
                c.getFirstFuse().resetULoad();
                c.getSecondFuse().resetULoad();
            }
        }
    }

    private static void scenarioBased() throws IOException {
        Optional<Substation> optSubs = Utils.importSubs("realcase-Sub1.json");
        if(optSubs.isEmpty()) {
            System.err.println("Error while loading the substation");
            return;
        }

        Substation substation = optSubs.get();
        substation.updateAllFuses();

        try(var writer = new FileWriter(OUT_FILE_NAME, false)) {
            writer.append("cableId")
                    .append(COMMA)
                    .append("categorie")
                    .append(COMMA)
                    .append("stdLoad")
                    .append(System.lineSeparator());

            final var mapIdxCable = new HashMap<Cable, Integer>();
            final var stdComputer = new StandardDeviation();
            final var lastIdx = new int[]{0};
            for (int nbRepet = 0; nbRepet < NB_REPETITION; nbRepet++) {
                Collection<Fuse> randoms = Utils.select(substation.getAllFuses(), NB_UFUSES);
                Utils.makeUncertain(randoms);
                Utils.setRandomConsumption(substation);

                // naive version
                duc.sg.java.loadapproximator.uncertain.naive.UncertainLoadApproximator.approximate(substation);
                printResAndRemove(substation, writer, mapIdxCable, lastIdx, stdComputer, true);

                //with bs rules
                duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator.approximate(substation);
                printResAndRemove(substation, writer, mapIdxCable, lastIdx, stdComputer, false);

                Utils.makeCertain(randoms);
                Utils.printProgressBar(nbRepet*100./NB_REPETITION);
            }

        }
    }

    private static final int NB_SUBSTATION = 100;
    private static final int NB_REP_PER_SUBS = 100;
    private static final String OUT_EXISTENCE_LS = "sgc-2020-varload-ls.csv";

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
                                .append(String.valueOf(std))
                                .append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                c.getFirstFuse().resetULoad();
                c.getSecondFuse().resetULoad();
            }
        }
    }

    private static void lsXp() throws IOException, InterruptedException {
        final int totalExec = NB_SUBSTATION * NB_REP_PER_SUBS;
        var stdComputer = new StandardDeviation();

        try(var writer = new FileWriter(OUT_EXISTENCE_LS, false)) {
            writer.append("version")
                    .append(COMMA)
                    .append("std")
                    .append(System.lineSeparator());

            Utils.printProgressBar(0);
            for (int idSubs = 0; idSubs < NB_SUBSTATION; idSubs++) {
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

                var runnable = new Runnable() {

                    @Override
                    public void run() {
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
                };

                for (int idxRepet = 0; idxRepet < NB_REP_PER_SUBS; idxRepet++) {
                    var thread = new Thread(runnable);
                    thread.start();
                    thread.join();

                    System.gc();
                    int progress = idSubs*NB_REP_PER_SUBS + idxRepet + 1;
                    Utils.printProgressBar(progress*100./totalExec);

                }

            }

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("sc")) {
                System.out.println("Run scenario based xp");
                scenarioBased();
            } else if(args[0].equalsIgnoreCase("ls")) {
                System.out.println("Run large scale xp");
                lsXp();
            } else {
                System.out.println("No argument passed. No run to do.... Expected argument: sc or ls");
                System.out.println("Actual: " + Arrays.toString(args));
            }

        } else {
            System.out.println("No (or too many?) argument passed. No run to do.... Expected argument: sc or ls");
            System.out.println("Actual: " + Arrays.toString(args));
        }

    }
}
