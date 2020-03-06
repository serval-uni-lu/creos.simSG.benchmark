package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc4F3F5Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[2].openFuse();
        fuses[4].openFuse();
    }
}
