package duc.propagation.ignoredbench.loadapproximator.paraCabinet;

public class Sc12F5F6F7Open extends ParaCabinetBench {
    @Override
    protected void openFuses() {
        fuses[4].openFuse();
        fuses[5].openFuse();
        fuses[6].openFuse();
    }
}
