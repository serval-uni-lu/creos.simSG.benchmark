package duc.propagation.external;

import org.ejml.alg.dense.linsol.svd.SolvePseudoInverseSvd;
import org.ejml.data.DenseMatrix64F;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@Fork(3)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@State(Scope.Thread)
public class SmallvsBigMatrices {

    @Param({"small", "big"})
    private String sizeMatrix;

    private DenseMatrix64F[] toInverse;

    private void setupSmall() {
        Random random = new Random(123456);
        toInverse = new DenseMatrix64F[100];

        for (int i = 0; i < toInverse.length; i++) {
            var data = new double[144];
            for (int j = 0; j < data.length; j++) {
                data[j] = random.nextDouble();
            }
            toInverse[i] = new DenseMatrix64F(12,12, true, data);

        }

    }

    private void setupBig() {
        Random random = new Random(123456);
        toInverse = new DenseMatrix64F[1];

        var data = new double[14400];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextDouble();
        }

        toInverse[0] = new DenseMatrix64F(120, 120, true, data);
    }


    @Setup
    public void setup() {
        switch (sizeMatrix) {
            case "small": setupSmall(); break;
            case "big": setupBig(); break;
            default: throw new RuntimeException("Add case for \"" + sizeMatrix + "\"");
        }
    }
    
    @Benchmark
    public void bench(Blackhole bh) {

        for (int i = 0; i < toInverse.length; i++) {
            SolvePseudoInverseSvd solver = new SolvePseudoInverseSvd();
            bh.consume(solver.setA(toInverse[i]));
        }
    }

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder()
                .include(SmallvsBigMatrices.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
