public class Light extends Component{
    private final Pin input;
    private boolean state;
    public Light() {
        input = new Pin();
        state = false; //off
    }
    @Override
    public void doCycle() {
        super.doCycle();
        state = input.getState();
    }
    public void showState() {
        if (state) {
            System.out.println("Light is ON");
        } else {
            System.out.println("Light is OFF");
        }
    }
    public Pin getInput() {
        return input;
    }
}
