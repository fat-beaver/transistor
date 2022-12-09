public class Inverter extends Component {
    private final Pin in;
    private final Pin out;
    private final Pin supply;
    public Inverter() {
        in = new Pin(this);
        out = new Pin(this);
        supply = new Pin(this);
    }
    public Pin getIn() {
        return in;
    }
    public Pin getOut() {
        return out;
    }
    public Pin getSupply() {
        return supply;
    }
    @Override
    public void doCycle() {
        super.doCycle();
        if (supply.getState()) {
            out.set(!in.getState());
        }
    }
}
