public class Inverter extends Component {
    private final Pin in;
    private final Pin out;
    public Inverter() {
        in = new Pin();
        out = new Pin();
    }
    public Pin getIn() {
        return in;
    }
    public Pin getOut() {
        return out;
    }
    @Override
    public void doCycle() {
        super.doCycle();
        out.set(!in.getState());
    }
}
