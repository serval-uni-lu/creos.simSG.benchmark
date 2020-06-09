package duc.propagation.bench.smartgridcomm2020;

import org.openjdk.jmh.annotations.Param;

public class Topo16Fuses extends BenchSG2020 {

    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"})
    private int nbUFuses;

    @Param({"realcase-Sub2.json", "realcase-Sub3.json"})
    private String fileName;


    @Override
    protected int getNbUFuses() {
        return nbUFuses;
    }

    @Override
    protected String getJsonFile() {
        return fileName;
    }
}
