package duc.propagation.bench.smartgridcomm2020;

import org.openjdk.jmh.annotations.Param;

public class Topo12Fuses extends BenchSG2020 {
    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    private int nbUFuses;

    @Param({"runningexample.json"})
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
