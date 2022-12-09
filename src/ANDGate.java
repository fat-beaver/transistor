public class ANDGate extends LogicGate {
    public ANDGate() {
        super();

        Transistor transistorOne = new Transistor();
        subComponents.add(transistorOne);
        transistorOne.getCollector().addWithoutCheck(supply);
        transistorOne.getBase().addConnection(inOne);

        Transistor transistorTwo = new Transistor();
        subComponents.add(transistorTwo);
        transistorTwo.getCollector().addConnection(transistorOne.getEmitter());
        transistorTwo.getBase().addConnection(inTwo);
        transistorTwo.getEmitter().addConnection(out);
    }
}
