package duc.propagation.ignoredbench.loadapproximator;

import duc.aintea.loadapproximation.LoadApproximator;
import duc.aintea.loadapproximation.UncertainLoadApproximator;
import duc.aintea.sg.Substation;
import duc.aintea.sg.importer.JSONImporter;
import duc.aintea.sg.importer.ValidationException;

import java.io.File;
import java.util.Optional;

public class ApproximatorRunner {
    private Substation substation;

    public boolean importFromJson(File json) {
        try {
            Optional<Substation> optSubs = JSONImporter.from(json);
            if (optSubs.isPresent()) {
                substation = optSubs.get();
                return true;
            }
            return false;
        } catch (ValidationException e) {
            return false;
        }
    }

    public void executeCertain() {
        if(substation != null) {
            LoadApproximator.approximate(substation);
        }
    }

    public void executeUncertain() {
        if(substation != null) {
            UncertainLoadApproximator.approximate(substation);
        }
    }

}
