package duc.propagation.bench.smartgridcomm2020;

import duc.propagation.bench.Utils;
import duc.sg.java.importer.json.JsonImporter;
import duc.sg.java.model.*;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(warmups = 5, value = 10)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Timeout(time = 15, timeUnit = TimeUnit.MINUTES)
public abstract class BenchSG2020 {
    private final Random random = new Random(12345);

    protected abstract int getNbUFuses();
    protected abstract String getJsonFile();

    private Substation substation;

    private Substation importJson() {
        String filePath = "xp/smartgridcomm2020" + "/" + getJsonFile();

        InputStream jsonPath = BenchSG2020.class
                .getClassLoader()
                .getResourceAsStream(filePath);


        try {
            var reader = new BufferedReader(new InputStreamReader(jsonPath));

            Optional<SmartGrid> optSubs = JsonImporter.from(reader);
            if(optSubs.isEmpty()) {
                throw new RuntimeException("Error during the json import.");
            }

            SmartGrid grid = optSubs.get();
            return grid.getSubstations().iterator().next();
        } catch (Exception e) {
            throw new RuntimeException(filePath + "   not found");
        }
    }

    private void initSG() {
        this.substation.updateAllFuses();
        Collection<Fuse> fuses = this.substation.getAllFuses();

        int i=0;
        for(Fuse f: fuses) {
            Cable cable = f.getCable();
            if(cable.getConsumption() == 0) {
                var meter = new Meter("meter " + i);
                meter.setConsumption(random.nextDouble() * 100);
                cable.addMeters(meter);
                i++;
            }
        }

        Utils.makeRandFuseUc(
                fuses.toArray(new Fuse[0]),
                getNbUFuses(),
                random
        );

    }

    @Setup
    public void setup() {
        this.substation = importJson();
        initSG();
    }

    @Benchmark
    public void ucLoadApproxImproved() {
        duc.sg.java.loadapproximator.uncertain.bsrules.UncertainLoadApproximator.approximate(substation);
    }

    @Benchmark
    public void ucLoadApproxNaive() {
        duc.sg.java.loadapproximator.uncertain.naive.UncertainLoadApproximator.approximate(substation);
    }



}
