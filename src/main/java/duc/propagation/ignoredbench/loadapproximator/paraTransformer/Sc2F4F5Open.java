package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc2F4F5Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[3].openFuse();
        fuses[4].openFuse();
    }
}
