package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc5F3F5Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[2].openFuse();
        fuses[4].openFuse();
    }
}
