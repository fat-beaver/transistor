import java.util.ArrayList;

public class Pin {
    protected boolean state = false;
    protected boolean powered = false;
    protected ArrayList<Pin> connectionNetwork = new ArrayList<>();
    public Pin() {
        connectionNetwork.add(this);
    }
    public void set(boolean state) {
        powered = state;
        checkState();
    }
    public boolean getState() {
        checkState();
        return state;
    }
    public void addConnection(Pin connection) {
        if (connectionNetwork.contains(connection)) {
            System.out.println("pin is already part of this network! (this is a bug)");
        } else {
            connectionNetwork.addAll(connection.connectionNetwork);
            connection.connectionNetwork = connectionNetwork;
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
            pin.state = connectedHasPower;
        }
    }
}
