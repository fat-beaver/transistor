public class Switch extends Component {
    private final Pin in;
    private final Pin out;
    private boolean state;
    public Switch() {
        in = new Pin();
        out = new Pin();
        state = false; //open
    }
    public void flip() {
        state = !state;
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
        if (state) {
            out.set(in.getState());
        } else {
            out.set(false);
        }
    }
}
