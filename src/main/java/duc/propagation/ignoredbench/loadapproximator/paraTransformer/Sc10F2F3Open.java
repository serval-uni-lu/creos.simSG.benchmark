package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc10F2F3Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[1].openFuse();
        fuses[2].openFuse();
    }
}
