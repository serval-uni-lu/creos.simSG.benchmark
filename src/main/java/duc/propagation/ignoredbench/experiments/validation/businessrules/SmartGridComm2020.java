package duc.propagation.ignoredbench.experiments.validation.businessrules;

import duc.sg.java.importer.json.JsonImporter;
import duc.sg.java.importer.json.ValidationException;
import duc.sg.java.model.Fuse;
import duc.sg.java.model.SmartGrid;
import duc.sg.java.model.Substation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class SmartGridComm2020 {
    public static final int NB_REPETITION = 10;
    public static final Random RANDOM = new Random();
    private static final char COMMA = ',';

    private static List<Path> getTopoFiles() {
        URL filesFolder = SmartGridComm2020.class
                .getClassLoader()
                .getResource("xp/smartgridcomm2020");

        final var topologies = new ArrayList<Path>(3);
        try {
            Path filesFolderPath = Path.of(filesFolder.toURI());
            try (Stream<Path> files = Files.walk(filesFolderPath)) {
                files.filter((Path p) -> p.toString().endsWith(".json"))
                        .forEach(topologies::add);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return topologies;
    }

    private static Substation importSubs(Path jsonPath) {
        try {
            var reader = Files.newBufferedReader(jsonPath);
            Optional<SmartGrid> optSG = JsonImporter.from(reader);
            if(optSG.isEmpty()) {
                throw new RuntimeException("Error during the json import.");
            }

            SmartGrid sg = optSG.get();
            return sg.getSubstations().iterator().next();

        } catch (IOException | ValidationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Collection<Fuse> select(Collection<Fuse> all, int nb) {
        var indexes = new HashSet<Integer>(nb);
        for (int i = 0; i < nb; i++) {
            int randIndex;
            do {
                randIndex = RANDOM.nextInt(all.size());
            }while(indexes.contains(randIndex));
            indexes.add(randIndex);
        }

        var result = new ArrayList<Fuse>();
        int idxAll = 0;
        for(Fuse f: all) {
            if(indexes.contains(idxAll)) {
                result.add(f);
            }
            idxAll++;
        }

        return result;
    }

    private static void makeUncertain(Collection<Fuse> fuses) {
        for(Fuse fuse: fuses) {
            // confidence does not matter for this experiment
            //should be between ]0; 1[
            fuse.getStatus().setConfIsClosed(0.5);
        }
    }

    private static void makeCertain(Collection<Fuse> uFuse) {
        for(Fuse f: uFuse) {
            f.getStatus().makeCertain();
        }
    }

    private static final void printProgressBar(double percentage) {
        int tick = (int) (percentage / 10);
        var toPrint = new StringBuilder();

        toPrint.append("|");
        for (int i = 0; i < tick; i++) {
            toPrint.append("-");
        }

        for (int i = tick; i < 10; i++) {
            toPrint.append(" ");
        }

        var df = new DecimalFormat("#0.00");
        toPrint.append("|  [")
                .append(df.format(percentage))
                .append("%]\r");

        if(percentage == 100) {
            toPrint.append(System.lineSeparator());
        }

        System.out.print(toPrint);
    }

    public static Fuse get(Collection<Fuse> fuses, String name) {
        for (Fuse fuse: fuses) {
            if(fuse.getName().equals(name)) {
                return fuse;
            }
        }

        return null;
    }


    public static void main(String[] args) throws IOException {
        List<Path> topologies = getTopoFiles();

        Map<Integer, List<String>> map = new HashMap<>();


        for (Path t: topologies) {
          Substation substation = importSubs(t);
          int nb = substation.extractFuses().size();
          map.compute(nb, (integer, strings) -> {
              if(strings == null) {
                  strings = new ArrayList<>(1);
              }
              strings.add(t.getFileName().toString());
              return strings;
          });
        }

        map.entrySet()
                .stream()
                .map(new Function<Map.Entry<Integer, List<String>>, String>() {
                    @Override
                    public String apply(Map.Entry<Integer, List<String>> integerListEntry) {
                        return integerListEntry.getKey() + " -> " + Arrays.toString(integerListEntry.getValue().toArray());
                    }
                })
                .forEach(System.out::println);





//        List<Path> topologies = getTopoFiles();
//
//        try (FileWriter writer = new FileWriter("smartgridcomm2020-rq1-org.csv", false)) {
//
//            writer.append("File Name")
//                    .append(COMMA)
//                    .append("Idx Repetition")
//                    .append(COMMA)
//                    .append("Nb. UFuses")
//                    .append(COMMA)
//                    .append("Nb. Configurations")
//                    .append(COMMA)
//                    .append("Nb. Valid Conf.")
//                    .append(System.lineSeparator());
//
//            printProgressBar(0.);
//
//            double totalExec = topologies.size() * NB_REPETITION;
//
//            int idxTopo = 0;
//            for (Path topoPath : topologies) {
//                Substation substation = importSubs(topoPath);
//                Collection<Fuse> allFuses = substation.extractFuses();
//
//                for (int idxRepetition = 0; idxRepetition < NB_REPETITION; idxRepetition++) {
//                    for (int nbUFuses = 0; nbUFuses <= allFuses.size(); nbUFuses++) {
//                        //select nbUFuses fuses to be uncertain
//                        Collection<Fuse> uFuse = select(allFuses, nbUFuses);
//
//                        // make them uncertain
//                        makeUncertain(uFuse);
//
//                        // Count valid configurations
//                        ConfigurationMatrix configurations = UncertainLoadApproximator.getAllConfigurations(substation);
//
//                        writer.append(topoPath.getFileName().toString())
//                                .append(COMMA)
//                                .append(String.valueOf(idxRepetition))
//                                .append(COMMA)
//                                .append(String.valueOf(nbUFuses))
//                                .append(COMMA)
//                                .append(String.valueOf((int) Math.pow(2, nbUFuses)))
//                                .append(COMMA)
//                                .append(String.valueOf(configurations.nbConfigurations()))
//                                .append(System.lineSeparator());
//
//                        //cleanup method
//                        makeCertain(uFuse);
//                    }
//                    double current = idxTopo * NB_REPETITION + idxRepetition;
//                    printProgressBar(current * 100 / totalExec);
//                }
//                idxTopo++;
//                int current = idxTopo * NB_REPETITION;
//                printProgressBar(current * 100 / totalExec);
//            }
//        }

    }

}
