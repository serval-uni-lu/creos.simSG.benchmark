package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc18F3F4Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[2].openFuse();
        fuses[3].openFuse();
    }
}
