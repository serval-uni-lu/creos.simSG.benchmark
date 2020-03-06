package duc.propagation.ignoredbench.loadapproximator.singleCable;

public class FSubsOpen extends SingleCableBench {

    @Override
    protected void openFuses() {
        fuses[0].openFuse();
    }
}
