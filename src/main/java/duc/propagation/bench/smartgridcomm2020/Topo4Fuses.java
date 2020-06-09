package duc.propagation.bench.smartgridcomm2020;

import org.openjdk.jmh.annotations.Param;

public class Topo4Fuses extends BenchSG2020 {
    @Param({"0", "1", "2", "3", "4"})
    private int nbUFuses;

    @Param({"paraDeadEnd.json"})
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
