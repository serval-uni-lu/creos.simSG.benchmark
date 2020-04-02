package duc.propagation.bench;

import duc.sg.java.model.State;
import duc.sg.java.model.Substation;
import org.openjdk.jmh.annotations.Param;

public class RC3SReference extends GenReference {
    @Param({"Substation 1", "Substation 2", "Substation 3"})
    private String subName;


    @Override
    public Substation callBuilder(State[] fuseClosed, double[] consumptions) {
        return RealCase3SubsHelper.getSubs(subName, consumptions);
    }


    @Override
    public int getNbFuses() {
        if(subName.equals("Substation 1")) return 30;
        return 16;
    }

}
