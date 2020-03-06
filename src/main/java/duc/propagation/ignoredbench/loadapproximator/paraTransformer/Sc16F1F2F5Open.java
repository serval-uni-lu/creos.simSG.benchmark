package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc16F1F2F5Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[0].openFuse();
        fuses[1].openFuse();
        fuses[4].openFuse();
    }
}
