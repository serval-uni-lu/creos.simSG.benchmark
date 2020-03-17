package duc.propagation.ignoredbench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 1)
@Measurement(iterations = 3)
@Warmup(iterations = 2)
@State(Scope.Thread)
public class TestParams {

    @Param({"1", "2", "3", "4", "5", "6"})
    private int nbUncFuses;

    private int other;


    @Setup(Level.Trial)
    public void setup() {
        other = nbUncFuses *2;
        System.out.println("SetUp executed" + nbUncFuses);
    }

    @Benchmark
    public void bench(Blackhole bh) {
        bh.consume(Math.pow(2, other));
    }

    /*@TearDown
    public void tearDown() {
        System.out.println("Tear Down executed");
    }
*/

    public static void main(String[] args) throws RunnerException {
        var opts = new OptionsBuilder()
                .include(TestParams.class.getSimpleName())
                .build();
        new Runner(opts).runSingle();
    }
}
