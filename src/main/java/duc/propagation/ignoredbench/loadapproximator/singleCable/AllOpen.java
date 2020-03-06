package duc.propagation.ignoredbench.loadapproximator.singleCable;

public class AllOpen extends SingleCableBench {

    @Override
    protected void openFuses() {
        fuses[0].openFuse();
        fuses[1].openFuse();
    }
}
