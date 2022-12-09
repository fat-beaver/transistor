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
        clock.getSupply().addConnection(supply);

        TwoFiftySixByteCell cell = new TwoFiftySixByteCell();
        subComponents.add(cell);
        cell.getSupply().addConnection(supply);

        ControlPanel controlPanel = new ControlPanel(pane, constraints, TwoFiftySixByteCell.ADDRESS_SIZE, TwoFiftySixByteCell.WORD_SIZE);
        subComponents.add(controlPanel);
        controlPanel.getSupply().addConnection(supply);

        cell.getWrite().addConnection(controlPanel.getWriteOut());

        for (int i = 0; i < TwoFiftySixByteCell.ADDRESS_SIZE; i++) {
            cell.getAddress(i).addConnection(controlPanel.getAddressOut(i));
        }
        for (int i = 0; i < TwoFiftySixByteCell.WORD_SIZE; i++) {
            cell.getIn(i).addConnection(controlPanel.getWordOut(i));
        }

        EightBitCounter addressCounter = new EightBitCounter();
        subComponents.add(addressCounter);
        addressCounter.getSupply().addConnection(supply);
        addressCounter.getClear().addConnection(controlPanel.getClear());
        addressCounter.getClock().addConnection(clock.getOut());
        for (int i = 0; i < TwoFiftySixByteCell.ADDRESS_SIZE; i++) {
            addressCounter.getOutput(i).addConnection(cell.getAddress(i));
        }

        SixteenBitAdder adder = new SixteenBitAdder();
        subComponents.add(adder);
        adder.getSupply().addConnection(supply);
        adder.getCarryOut().addConnection(controlPanel.getError());

        SixteenBitLatch latch = new SixteenBitLatch();
        subComponents.add(latch);
        latch.getSupply().addConnection(supply);
        latch.getClear().addConnection(controlPanel.getClear());
        latch.getClock().addConnection(clock.getOut());

        for (int i = 0; i < TwoFiftySixByteCell.WORD_SIZE; i++) {
            adder.getInOne(i).addConnection(cell.getOut(i));
            adder.getInTwo(i).addConnection(latch.getOut(i));
            adder.getSumOut(i).addConnection(latch.getIn(i));
            latch.getOut(i).addConnection(controlPanel.getDisplay(i));
        }
    }
}
