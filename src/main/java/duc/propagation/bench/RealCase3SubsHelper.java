package duc.propagation.bench;

import duc.aintea.sg.Cable;
import duc.aintea.sg.Extractor;
import duc.aintea.sg.Meter;
import duc.aintea.sg.Substation;
import duc.aintea.sg.importer.JsonImporter;
import duc.aintea.sg.importer.ValidationException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class RealCase3SubsHelper {
    private RealCase3SubsHelper(){}

    public static Substation getSubs(String name, double[] consumptions) {
        List<Substation> substations = importJson();

        Substation substation = null;
        var idx = 0;
        while (substation == null) {
            var sub = substations.get(idx);
            if(sub.getName().equals(name)) {
                substation = sub;
            }
            idx++;
        }


        List<Cable> cables = Extractor.extractCables(substation);
        for (int i = 0; i < cables.size(); i++) {
            var meter = new Meter(substation.getName() + "_Meter " + i);
            meter.setConsumption(consumptions[i]);
            cables.get(i).addMeters(meter);
        }

        return substation;

    }

    public static List<Substation> importJson() {
        InputStream jsonPath = RC3SReference.class
                .getClassLoader()
                .getResourceAsStream("realcase-3Subs.json");

        var reader = new BufferedReader(new InputStreamReader(jsonPath));
        try {
            Optional<List<Substation>> optSubs = JsonImporter.from(reader);
            List<Substation> substations = optSubs.get();
            return substations;
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
