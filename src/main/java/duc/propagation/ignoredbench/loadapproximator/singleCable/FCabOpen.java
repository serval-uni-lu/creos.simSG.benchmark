package duc.propagation.ignoredbench.loadapproximator.singleCable;

public class FCabOpen extends SingleCableBench {
    @Override
    protected void openFuses() {
        fuses[1].openFuse();
    }
}
