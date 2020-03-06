package duc.propagation.ignoredbench;


import org.ejml.alg.dense.decomposition.lu.LUDecompositionAlt_D64;
import org.ejml.alg.dense.linsol.lu.LinearSolverLu;
import org.ejml.alg.dense.linsol.svd.SolvePseudoInverseSvd;
import org.ejml.data.DenseMatrix64F;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup
public class LuvsSVD {
    double[] matrix_data = new double[] {
            1,1,0,0,0,0,0,0,0,0,
            0,0,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,0,0,0,0,
            0,0,0,0,0,0,1,1,0,0,
            0,0,0,0,0,0,0,0,1,1,
            0,0,0,1,1,0,1,0,0,0,
            0,1,0,0,0,0,0,1,1,0,
            0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,0,0,0,0,0,1,
            1,0,-1,0,0,0,0,0,0,0,
    };
    double[] li = new double[] {10, 20, 30, 40, 50, 0, 0, 0, 0,0};


    private DenseMatrix64F fuses, consumptions, solution;


    @Setup
    public void setup() {
        fuses = new DenseMatrix64F(10,10, true, matrix_data);
        consumptions = new DenseMatrix64F(10,1, true, li);
        solution = new DenseMatrix64F(10, 1);
    }

    @Benchmark
    public void testLU() {
        var solver1 = new LinearSolverLu(new LUDecompositionAlt_D64());
        solver1.setA(fuses);
        solver1.solve(consumptions, solution);
    }

    @Benchmark
    public void testSVD() {
        var solver2 = new SolvePseudoInverseSvd();
        solver2.setA(fuses);
        solver2.solve(consumptions, solution);
    }


    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder().include(LuvsSVD.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }
}
