import javax.swing.*;
import java.awt.*;

public class Startup extends Component{
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final int TIME_PER_FRAME = 20;
    private static final int PADDING = 2;
    private static final int CYCLES_PER_REPAINT = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startup::new);
    }
    public Startup() {
        JFrame window = new JFrame();

        Timer cycleTimer = new Timer(TIME_PER_FRAME, actionEvent -> SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < CYCLES_PER_REPAINT; i++) {
                doCycle();
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
        System.out.println("Adding components now");
        long startTime = System.nanoTime();
        setUpMemory(pane, constraints);
        System.out.println("Finished adding components, took " + (System.nanoTime() - startTime) + "ns");
        System.out.println("Counting transistors now");
        startTime = System.nanoTime();
        long transistorCount = getTransistorCount();
        System.out.println("Finished counting transistors, took " + (System.nanoTime() - startTime) + "ns");
        System.out.println("There are: " + transistorCount + " transistors");

        window.pack();
        window.toFront();
        window.setVisible(true);

        cycleTimer.start();
    }
    private void setUpMemory(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        TwoFiftySixByteCell cell = new TwoFiftySixByteCell();
        subComponents.add(cell);
        cell.getSupply().addConnection(supply);

        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(new JLabel("Address"), constraints);
        Switch[] addressSwitches = new Switch[TwoFiftySixByteCell.ADDRESS_SIZE];
        for (int i = 0; i < TwoFiftySixByteCell.ADDRESS_SIZE; i++) {
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addConnection(supply);
            addressSwitches[i].getOut().addConnection(cell.getAddress(i));
            constraints.gridx = TwoFiftySixByteCell.ADDRESS_SIZE - i;
            pane.add(addressSwitches[i].getVisuals(), constraints);
        }

        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(new JLabel("Input"), constraints);
        constraints.gridy = 2;
        pane.add(new JLabel("Save"), constraints);
        constraints.gridy = 3;
        pane.add(new JLabel("Output"), constraints);

        Switch[] inputSwitches = new Switch[TwoFiftySixByteCell.WORD_SIZE];
        Light[] outputLights = new Light[TwoFiftySixByteCell.WORD_SIZE];

        for (int i = 0; i < TwoFiftySixByteCell.WORD_SIZE; i++) {
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addConnection(supply);
            inputSwitches[i].getOut().addConnection(cell.getIn(i));
            constraints.gridy = 1;
            constraints.gridx = TwoFiftySixByteCell.WORD_SIZE - i;
            pane.add(inputSwitches[i].getVisuals(), constraints);

            outputLights[i] = new Light();
            subComponents.add(outputLights[i]);
            outputLights[i].getInput().addConnection(cell.getOut(i));
            constraints.gridy = 3;
            constraints.gridx = TwoFiftySixByteCell.WORD_SIZE - i;
            pane.add(outputLights[i].getVisuals(), constraints);
        }
        constraints.gridx = 0;
        constraints.gridy = 2;
        Switch saveSwitch = new Switch();
        subComponents.add(saveSwitch);
        saveSwitch.getIn().addConnection(supply);
        saveSwitch.getOut().addConnection(cell.getWrite());
        constraints.gridx = 1;
        pane.add(saveSwitch.getVisuals(), constraints);
    }
    private void setUpLatch(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        RSLatch latch = new RSLatch();
        subComponents.add(latch);
        latch.getSupply().addConnection(supply);

        Light light = new Light();
        subComponents.add(light);
        light.getInput().addConnection(latch.getOut());
        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(light.getVisuals(), constraints);

        Switch switchOne = new Switch();
        subComponents.add(switchOne);
        switchOne.getIn().addConnection(supply);
        switchOne.getOut().addConnection(latch.getSet());
        constraints.gridy = 0;
        pane.add(switchOne.getVisuals(), constraints);

        Switch switchTwo = new Switch();
        subComponents.add(switchTwo);
        switchTwo.getIn().addConnection(supply);
        switchTwo.getOut().addConnection(latch.getReset());
        constraints.gridx = 1;
        constraints.gridy = 0;
        pane.add(switchTwo.getVisuals(), constraints);
    }
    private void setUpAdder(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        ByteAdder byteAdder = new ByteAdder();
        subComponents.add(byteAdder);
        byteAdder.getSupply().addConnection(supply);

        constraints.weightx = 0;
        constraints.weighty = 0;
        Switch operation = new Switch();
        subComponents.add(operation);
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
            subComponents.add(topSwitch);
            topSwitch.getIn().addConnection(supply);
            topSwitch.getOut().addConnection(byteAdder.getInOne(i));
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(topSwitch.getVisuals(), constraints);

            constraints.gridy = 3;
            Switch bottomSwitch = new Switch();
            subComponents.add(bottomSwitch);
            bottomSwitch.getIn().addConnection(supply);
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(bottomSwitch.getVisuals(), constraints);

            XORGate subtractionGate = new XORGate();
            subComponents.add(subtractionGate);
            subtractionGate.getSupply().addConnection(supply);
            subtractionGate.getInputOne().addConnection(operation.getOut());
            subtractionGate.getInputTwo().addConnection(bottomSwitch.getOut());
            subtractionGate.getOutput().addConnection(byteAdder.getInTwo(i));

            constraints.gridy = 4;
            Light light = new Light();
            subComponents.add(light);
            light.getInput().addConnection(byteAdder.getSumOut(i));
            constraints.gridx = ByteAdder.WORD_SIZE + 1 - i;
            pane.add(light.getVisuals(), constraints);
        }

        Light carryLight = new Light();
        subComponents.add(carryLight);

        XORGate carryXor = new XORGate();
        subComponents.add(carryXor);
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
        subComponents.add(counter);
        counter.getSupply().addConnection(supply);

        for (int i = 0; i < BinaryCounter.WORD_SIZE; i++) {
            Light light = new Light();
            subComponents.add(light);
            constraints.gridx = BinaryCounter.WORD_SIZE - i;
            light.getInput().addConnection(counter.getOutput(i));
            pane.add(light.getVisuals(), constraints);
        }

        Oscillator clockSwitch = new Oscillator();
        subComponents.add(clockSwitch);
        clockSwitch.getSupply().addConnection(supply);
        clockSwitch.getOut().addConnection(counter.getClock());
    }
}
