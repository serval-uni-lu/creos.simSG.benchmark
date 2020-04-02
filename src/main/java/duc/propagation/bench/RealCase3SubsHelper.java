package duc.propagation.bench;

import duc.sg.java.importer.json.JsonImporter;
import duc.sg.java.importer.json.ValidationException;
import duc.sg.java.model.Cable;
import duc.sg.java.model.Meter;
import duc.sg.java.model.SmartGrid;
import duc.sg.java.model.Substation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;

public class RealCase3SubsHelper {
    private RealCase3SubsHelper(){}

    public static Substation getSubs(String name, double[] consumptions) {
        SmartGrid grid = importJson();
        Optional<Substation> optSybs = grid.getSubstation(name);
        if(optSybs.isEmpty()) {
            throw new RuntimeException("Substation " + name + "not found in the json file");
        }
        Substation substation = optSybs.get();


        Collection<Cable> cables = substation.extractCables();
        int i = 0;
        for(Cable cable: cables) {
            var meter = new Meter(substation.getName() + "_Meter " + i);
            meter.setConsumption(consumptions[i]);
            cable.addMeters(meter);
            i++;
        }

        return substation;

    }

    public static SmartGrid importJson() {
        InputStream jsonPath = RC3SReference.class
                .getClassLoader()
                .getResourceAsStream("realcase-3Subs.json");

        var reader = new BufferedReader(new InputStreamReader(jsonPath));
        try {
            Optional<SmartGrid> optSubs = JsonImporter.from(reader);
            if(optSubs.isEmpty()) {
                throw new RuntimeException("Error during the json import.");
            }
            return optSubs.get();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
