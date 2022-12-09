import java.util.ArrayList;

public class Pin {
    protected boolean state = false;
    protected boolean powered = false;
    private final Component owner;
    protected ArrayList<Pin> connectionNetwork = new ArrayList<>();
    public Pin(Component owner) {
        connectionNetwork.add(this);
        this.owner = owner;
    }
    public void set(boolean newLevel) {
        powered = newLevel;
        checkState();
    }
    public boolean getState() {
        return state;
    }
    public void addConnection(Pin connection) {
        if (connectionNetwork.contains(connection)) {
            System.out.println("pin is already part of this network! (this is a bug)");
        } else {
            ArrayList<Pin> tempPins = new ArrayList<>(connection.connectionNetwork);
            for (Pin pin : tempPins) {
                connectionNetwork.add(pin);
                pin.connectionNetwork = connectionNetwork;
            }
        }
        checkState();
    }
    protected void checkState() {
        boolean connectedHasPower = false;
        for (Pin pin : connectionNetwork) {
            if (pin.powered) {
                connectedHasPower = true;
                break;
            }
        }
        for (Pin pin : connectionNetwork) {
            pin.forceSet(connectedHasPower);
        }
    }
    protected void forceSet(boolean state) {
        this.state = state;
    }
}
