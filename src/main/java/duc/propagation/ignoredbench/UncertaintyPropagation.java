package duc.propagation.ignoredbench;

import org.ejml.alg.dense.decomposition.lu.LUDecompositionAlt_D64;
import org.ejml.alg.dense.linsol.lu.LinearSolverLu;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup
public class UncertaintyPropagation {
    private static final int NB_FUSES = 10;
    private Random random = new Random(12345);
    private DenseMatrix64F fuseStates;
    private DenseMatrix64F consumptions;
    private DenseMatrix64F solution;
    private LinearSolverLu solver;

    private double randomConsumptions() {
        return random.nextDouble() * 100;
    }

    private double[] randomFuseStates() {
        var res = new double[NB_FUSES * NB_FUSES];
        for (int i = 0; i < res.length; i++) {
            res[i] = random.nextInt(3) - 3; //{-1, 0, 1}
        }
        return res;
    }

    @Setup(Level.Iteration)
    public void setup() {
        var consumptionsData = new double[NB_FUSES];
        for (int i = 0; i < consumptionsData.length / 2; i++) {
            consumptionsData[i] = randomConsumptions();
        }
        consumptions = new DenseMatrix64F(NB_FUSES, 1, true, consumptionsData);
        fuseStates = new DenseMatrix64F(NB_FUSES, NB_FUSES,true, randomFuseStates());
        solution = new DenseMatrix64F(NB_FUSES, 1);
        solver = new LinearSolverLu(new LUDecompositionAlt_D64());
    }


    @Benchmark
    public void nNormalProcess() {
        solver.setA(fuseStates);
        solver.solve(consumptions, solution);
    }

    @Benchmark
    public void nInverseMult() {
        solver.setA(fuseStates);
        solver.invert(fuseStates);
        CommonOps.mult(fuseStates, consumptions, solution);
    }

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder().include(UncertaintyPropagation.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
