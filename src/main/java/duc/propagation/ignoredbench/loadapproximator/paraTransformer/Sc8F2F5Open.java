package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc8F2F5Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[1].openFuse();
        fuses[4].openFuse();
    }
}
