public class Oscillator extends Component {
    private static final int CYCLES_PER_PULSE = 64;
    private final Pin out;
    private final Pin supply;
    private int cycleCount;
    public Oscillator() {
        out = new Pin(this);
        supply = new Pin(this);
        cycleCount = 0;
    }
    @Override
    public void doCycle() {
        super.doCycle();
        out.set(false);
        if (supply.getState()) {
            cycleCount++;
            if (cycleCount == CYCLES_PER_PULSE) {
                out.set(true);
                cycleCount = 0;
            }
        }
    }
    public Pin getOut() {
        return out;
    }
    public Pin getSupply() {
        return supply;
    }
}
