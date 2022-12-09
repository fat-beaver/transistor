public class Transistor extends Component{
    private final Pin base;
    private final Pin emitter;
    private final Pin collector;
    public Transistor() {
        base = new Pin(this);
        emitter = new Pin(this);
        collector = new Pin(this);
    }
    @Override
    public void doCycle() {
        super.doCycle();
        if (base.getState()) {
            emitter.set(collector.getState());
        } else {
            emitter.set(false);
        }
    }
    public Pin getBase() {
        return base;
    }
    public Pin getEmitter() {
        return emitter;
    }
    public Pin getCollector() {
        return collector;
    }
}
