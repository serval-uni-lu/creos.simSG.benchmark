package duc.propagation.ignoredbench.loadapproximator.paraTransformer;

public class Sc15F1F2Open extends ParaTransformerBench {
    @Override
    protected void openFuses() {
        fuses[0].openFuse();
        fuses[1].openFuse();
    }
}
