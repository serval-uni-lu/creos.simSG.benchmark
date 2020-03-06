package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc5F4F6Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[3].openFuse();
        fuses[5].openFuse();
    }
}
