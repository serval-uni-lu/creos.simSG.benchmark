package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc15F3F6Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[2].openFuse();
        fuses[5].openFuse();
    }
}
