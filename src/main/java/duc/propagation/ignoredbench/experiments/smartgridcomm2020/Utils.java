package duc.propagation.ignoredbench.experiments.smartgridcomm2020;

import duc.propagation.ignoredbench.experiments.smartgridcomm2020.old.RQ1;
import duc.sg.java.importer.json.JsonImporter;
import duc.sg.java.importer.json.ValidationException;
import duc.sg.java.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

public class Utils {
    public static final Random RANDOM = new Random(56703);
    public static final int NB_CABINETS_PER_SUBS = 10;

    private Utils(){}

    public static Collection<Fuse> select(Collection<Fuse> all, int nb) {
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

    public static void setRandomConsumption(Substation substation) {
        int i=0;
        for(Fuse f: substation.getAllFuses()) {
            Cable cable = f.getCable();
            double cons = RANDOM.nextDouble() * 100 + 1;
            if(cable.getMeters().isEmpty()) {
                var meter = new Meter("meter " + i);
                meter.setConsumption(cons);
                cable.addMeters(meter);
                i++;
            } else {
                cable.getMeters()
                        .get(0)
                        .setConsumption(cons);
            }
        }
    }

    public static void makeUncertain(Collection<Fuse> fuses) {
        for(Fuse fuse: fuses) {
            // confidence does not matter for this experiment
            //should be between ]0; 1[
            fuse.getStatus().setConfIsClosed(0.5);
        }
    }

    public static void makeCertain(Collection<Fuse> uFuse) {
        for(Fuse f: uFuse) {
            f.getStatus().makeCertain();
        }
    }

    public static List<Path> getTopoFiles() {
        URL filesFolder = RQ1.class
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

    public static Substation importSubs(Path jsonPath) {
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

    public static Optional<Substation> importSubs(String fileName) {
        InputStream jsonPath = Utils.class
                .getClassLoader()
                .getResourceAsStream("xp/smartgridcomm2020/" + fileName);

        var reader = new BufferedReader(new InputStreamReader(jsonPath));
        try {
            Optional<SmartGrid> optSubs = JsonImporter.from(reader);
            if(optSubs.isEmpty()) {
                throw new RuntimeException("Error during the json import.");
            }

            Collection<Substation> subs = optSubs.get().getSubstations();
            Iterator<Substation> it = subs.iterator();
            if(it.hasNext()) {
                return Optional.of(it.next());
            }
            return Optional.empty();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printProgressBar(double percentage) {
        int tick = (int) (percentage / 1);
        var toPrint = new StringBuilder();

        toPrint.append("|");
        toPrint.append("-".repeat(tick));
        toPrint.append(" ".repeat(100 - tick));

        var df = new DecimalFormat("#0.00");
        toPrint.append("|  [")
                .append(df.format(percentage))
                .append("%]\r");

        if(percentage == 100) {
            toPrint.append(System.lineSeparator());
        }

        System.out.print(toPrint);
    }


    public static Substation randomlyGenerateTopo() {
        Substation substation = new Substation("Substation");

        int nbCabinetsToCreate = NB_CABINETS_PER_SUBS;
        var entitiesToManage = new Stack<Entity>();
        var allEntities = new ArrayList<Entity>();
        entitiesToManage.add(substation);
        allEntities.add(substation);

        int idxFuse = 0;
        int idxCab = 0;



        while (nbCabinetsToCreate != 0) {
            Entity current;

            if(entitiesToManage.isEmpty()) {
                int idxNew = Utils.RANDOM.nextInt(allEntities.size());
                current = allEntities.get(idxNew);
            } else {
                current = entitiesToManage.pop();
            }

            int maxNbNeighbours = (int) Math.round(nbCabinetsToCreate / 2.);

            int nbNeighbours;
            if(maxNbNeighbours == 1) {
                nbNeighbours = Utils.RANDOM.nextBoolean()? 0 : 1;
            } else {
                nbNeighbours = RANDOM.nextInt(maxNbNeighbours);
            }

            if(nbCabinetsToCreate == NB_CABINETS_PER_SUBS && nbNeighbours == 0) { //force substation to have at least 1 neighbour
                nbNeighbours++;
            }
            nbCabinetsToCreate -= nbNeighbours;

            for (int iCab = 0; iCab < nbNeighbours; iCab++) {
                Cabinet cabinet = new Cabinet("Cabinet " + idxCab);
                entitiesToManage.add(cabinet);
                allEntities.add(cabinet);
                idxCab++;

                int nbCableWithCurrent = Utils.RANDOM.nextBoolean()? 1 : 2;
                for (int iFuse = 0; iFuse < nbCableWithCurrent; iFuse++) {
                    Fuse f1 = new Fuse("Fuse " + idxFuse);
                    idxFuse++;
                    Fuse f2 = new Fuse("Fuse " + idxFuse);
                    idxFuse++;
                    cabinet.addFuses(f1);
                    current.addFuses(f2);

                    Cable cable = new Cable();
                    cable.setFuses(f1, f2);
                }

            }
        }
        return substation;

    }

}
