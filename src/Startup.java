import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Startup {
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final int TIME_PER_FRAME = 20;
    private static final int PADDING = 2;
    private static final int CYCLES_PER_REPAINT = 1;

    ArrayList<Component> components = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startup::new);
    }
    public Startup() {
        JFrame window = new JFrame();

        Timer cycleTimer = new Timer(TIME_PER_FRAME, actionEvent -> SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < CYCLES_PER_REPAINT; i++) {
                for (Component component : components) {component.doCycle();}
            }
            window.repaint();
        }));
        cycleTimer.setRepeats(true);

        window.setTitle("transistor");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        Container pane = window.getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        setUpMemory(pane, constraints);

        window.pack();
        window.toFront();
        window.setVisible(true);

        cycleTimer.start();
    }
    private void setUpMemory(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(new JLabel("Address"), constraints);
        constraints.gridy = 1;
        pane.add(new JLabel("Input"), constraints);
        constraints.gridy = 2;
        pane.add(new JLabel("Save"), constraints);
        constraints.gridy = 3;
        pane.add(new JLabel("Output"), constraints);

        Switch inputSwitch = new Switch();
        components.add(inputSwitch);
        inputSwitch.getIn().addConnection(supply);
        constraints.gridx = 1;
        constraints.gridy = 1;
        pane.add(inputSwitch.getVisuals(), constraints);

        Switch clockSwitch = new Switch();
        components.add(clockSwitch);
        clockSwitch.getIn().addConnection(supply);
        constraints.gridy = 2;
        pane.add(clockSwitch.getVisuals(), constraints);

        Light outputLight = new Light();
        components.add(outputLight);
        constraints.gridy = 3;
        pane.add(outputLight.getVisuals(), constraints);

        SixteenWordCell cell = new SixteenWordCell();
        components.add(cell);
        cell.getSupply().addConnection(supply);
        cell.getIn().addConnection(inputSwitch.getOut());
        cell.getWrite().addConnection(clockSwitch.getOut());
        cell.getOut().addConnection(outputLight.getInput());

        Switch[] addressSwitches = new Switch[SixteenWordCell.ADDRESS_SIZE];
        constraints.gridy = 0;
        for (int i = 0; i < SixteenWordCell.ADDRESS_SIZE; i++) {
            addressSwitches[i] = new Switch();
            components.add(addressSwitches[i]);
            addressSwitches[i].getIn().addConnection(supply);
            addressSwitches[i].getOut().addConnection(cell.getAddress(i));
            constraints.gridx = SixteenWordCell.ADDRESS_SIZE - i;
            pane.add(addressSwitches[i].getVisuals(), constraints);
        }
    }
    private void setUpLatch(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        RSLatch latch = new RSLatch();
        components.add(latch);
        latch.getSupply().addConnection(supply);

        Light light = new Light();
        components.add(light);
        light.getInput().addConnection(latch.getOut());
        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(light.getVisuals(), constraints);

        Switch switchOne = new Switch();
        components.add(switchOne);
        switchOne.getIn().addConnection(supply);
        switchOne.getOut().addConnection(latch.getSet());
        constraints.gridy = 0;
        pane.add(switchOne.getVisuals(), constraints);

        Switch switchTwo = new Switch();
        components.add(switchTwo);
        switchTwo.getIn().addConnection(supply);
        switchTwo.getOut().addConnection(latch.getReset());
        constraints.gridx = 1;
        constraints.gridy = 0;
        pane.add(switchTwo.getVisuals(), constraints);
    }
    private void setUpAdder(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        ByteAdder byteAdder = new ByteAdder();
        components.add(byteAdder);
        byteAdder.getSupply().addConnection(supply);

        constraints.weightx = 0;
        constraints.weighty = 0;
        Switch operation = new Switch();
        components.add(operation);
        operation.getIn().addConnection(supply);
        operation.getOut().addConnection(byteAdder.getCarryIn());
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(operation.getVisuals(), constraints);
        constraints.gridy = 1;
        pane.add(new JLabel("Subtraction enabled"), constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        for (int i = 0; i < ByteAdder.WORD_SIZE; i++) {
            constraints.gridy = 2;
            Switch topSwitch = new Switch();
            components.add(topSwitch);
            topSwitch.getIn().addConnection(supply);
            topSwitch.getOut().addConnection(byteAdder.getInOne(i));
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(topSwitch.getVisuals(), constraints);

            constraints.gridy = 3;
            Switch bottomSwitch = new Switch();
            components.add(bottomSwitch);
            bottomSwitch.getIn().addConnection(supply);
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(bottomSwitch.getVisuals(), constraints);

            XORGate subtractionGate = new XORGate();
            components.add(subtractionGate);
            subtractionGate.getSupply().addConnection(supply);
            subtractionGate.getInputOne().addConnection(operation.getOut());
            subtractionGate.getInputTwo().addConnection(bottomSwitch.getOut());
            subtractionGate.getOutput().addConnection(byteAdder.getInTwo(i));

            constraints.gridy = 4;
            Light light = new Light();
            components.add(light);
            light.getInput().addConnection(byteAdder.getSumOut(i));
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(light.getVisuals(), constraints);
        }

        Light carryLight = new Light();
        components.add(carryLight);

        XORGate carryXor = new XORGate();
        components.add(carryXor);
        carryXor.getSupply().addConnection(supply);
        carryXor.getInputOne().addConnection(byteAdder.getCarryOut());
        carryXor.getInputTwo().addConnection(operation.getOut());
        carryXor.getOutput().addConnection(carryLight.getInput());

        constraints.gridx = 0;
        constraints.gridy = 4;
        pane.add(carryLight.getVisuals(), constraints);
    }
    private void setUpCounter(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridy = 0;

        BinaryCounter counter = new BinaryCounter();
        components.add(counter);
        counter.getSupply().addConnection(supply);

        for (int i = 0; i < BinaryCounter.WORD_SIZE; i++) {
            Light light = new Light();
            components.add(light);
            constraints.gridx = BinaryCounter.WORD_SIZE - i;
            light.getInput().addConnection(counter.getOutput(i));
            pane.add(light.getVisuals(), constraints);
        }

        Oscillator clockSwitch = new Oscillator();
        components.add(clockSwitch);
        clockSwitch.getSupply().addConnection(supply);
        clockSwitch.getOut().addConnection(counter.getClock());
    }
}
