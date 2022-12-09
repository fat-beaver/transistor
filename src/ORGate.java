public class ORGate extends LogicGate {
    public ORGate() {
        super();

        Transistor transistorOne = new Transistor();
        subComponents.add(transistorOne);

        Transistor transistorTwo = new Transistor();
        subComponents.add(transistorTwo);

        transistorOne.getCollector().addWithoutCheck(supply);
        transistorTwo.getCollector().addWithoutCheck(supply);

        transistorOne.getBase().addConnection(inOne);
        transistorTwo.getBase().addConnection(inTwo);

        transistorOne.getEmitter().addConnection(out);
        transistorTwo.getEmitter().addConnection(out);
    }
}
