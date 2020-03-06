package duc.propagation.ignoredbench.loadapproximator.cabinet;

public class Sc4F3F4Open extends CabinetBench {
    @Override
    protected void openFuses() {
        fuses[2].openFuse();
        fuses[3].openFuse();
    }
}
