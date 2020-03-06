package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc12F1F5Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[0].openFuse();
        fuses[4].openFuse();
    }
}
