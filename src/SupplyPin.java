public class SupplyPin extends Pin {
    public SupplyPin() {
        super(null);
        powered = true;
    }
    @Override
    public void set(boolean ignored) {
        if (!powered) {
            System.out.println("supply is unpowered?");
        }
    }
}
