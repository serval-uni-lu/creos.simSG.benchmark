package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc14F4F5Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[3].openFuse();
        fuses[4].openFuse();
    }
}
