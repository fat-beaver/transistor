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
        long startTime = System.currentTimeMillis();
        setUpComponents(pane, constraints);
        System.out.println("Finished adding components, took " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Counting transistors now");
        startTime = System.currentTimeMillis();
        long transistorCount = getTransistorCount();
        System.out.println("Finished counting transistors, took " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("There are: " + transistorCount + " transistors");

        window.pack();
        window.toFront();
        window.setVisible(true);

        cycleTimer.start();
    }
    private void setUpComponents(Container pane, GridBagConstraints constraints) {
        SupplyPin supply = new SupplyPin();

        Oscillator clock = new Oscillator();
        subComponents.add(clock);
        clock.getSupply().addWithoutCheck(supply);

        DualSourceMemory workingMemory = new DualSourceMemory();
        subComponents.add(workingMemory);
        workingMemory.getSupply().addWithoutCheck(supply);

        DualSourceMemory programMemory = new DualSourceMemory();
        subComponents.add(programMemory);
        programMemory.getSupply().addWithoutCheck(supply);

        ControlPanel controlPanel = new ControlPanel(pane, constraints, TwoFiftySixWordCell.ADDRESS_SIZE, TwoFiftySixWordCell.WORD_SIZE);
        subComponents.add(controlPanel);
        controlPanel.getSupply().addWithoutCheck(supply);
        controlPanel.getRAMWrite().addConnection(workingMemory.getWriteTwo());
        controlPanel.getProgramWrite().addConnection(programMemory.getWriteTwo());
        controlPanel.getOverride().addConnection(workingMemory.getSelect());
        controlPanel.getOverride().addConnection(programMemory.getSelect());

        for (int i = 0; i < TwoFiftySixWordCell.ADDRESS_SIZE; i++) {
            workingMemory.getAddressTwo(i).addConnection(controlPanel.getAddress(i));
        }
        for (int i = 0; i < TwoFiftySixWordCell.WORD_SIZE; i++) {
            workingMemory.getWordTwo(i).addConnection(controlPanel.getWord(i));
            workingMemory.getWordOut(i).addConnection(controlPanel.getWordDisplay(i));
            programMemory.getWordTwo(i).addConnection(controlPanel.getWord(i));
            programMemory.getWordOut(i).addConnection(controlPanel.getInstructionDisplay(i));
        }

        EightBitCounter addressCounter = new EightBitCounter();
        subComponents.add(addressCounter);
        addressCounter.getSupply().addWithoutCheck(supply);
        addressCounter.getClear().addConnection(controlPanel.getClear());
        for (int i = 0; i < TwoFiftySixWordCell.ADDRESS_SIZE; i++) {
            addressCounter.getOutput(i).addConnection(workingMemory.getAddressOne(i));
            addressCounter.getOutput(i).addConnection(programMemory.getAddressOne(i));
            controlPanel.getAddressDisplay(i).addConnection(workingMemory.getAddressOut(i));
        }

        SixteenBitAdder adder = new SixteenBitAdder();
        subComponents.add(adder);
        adder.getSupply().addWithoutCheck(supply);
        adder.getCarryOut().addConnection(controlPanel.getError());

        SixteenBitLatch latch = new SixteenBitLatch();
        subComponents.add(latch);
        latch.getSupply().addWithoutCheck(supply);
        latch.getClear().addConnection(controlPanel.getClear());
        latch.getClock().addConnection(clock.getOut());

        for (int i = 0; i < TwoFiftySixWordCell.WORD_SIZE; i++) {
            adder.getInOne(i).addConnection(workingMemory.getWordOut(i));
            adder.getInTwo(i).addConnection(latch.getOut(i));
            adder.getSumOut(i).addConnection(latch.getIn(i));
            latch.getOut(i).addConnection(controlPanel.getAccumulatorDisplay(i));
        }

        TwoFiftySixBitAddresser instructionSelector = new TwoFiftySixBitAddresser();
        subComponents.add(instructionSelector);
        instructionSelector.getSupply().addWithoutCheck(supply);
        for (int i = 0; i < TwoFiftySixBitAddresser.NUMBER_OF_BITS; i++) {
            instructionSelector.getIn(i).addConnection(clock.getOut()); //only send clock signals to the current instruction
        }
        for (int i = 0; i < TwoFiftySixBitAddresser.ADDRESS_SIZE; i++) {
            instructionSelector.getAddress(i).addConnection(programMemory.getWordOut(i));
        }

        instructionSelector.getOut(0).addConnection(addressCounter.getClock()); //NOP

        supply.checkState();
    }
}
