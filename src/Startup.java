import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Startup {
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final int TIME_PER_FRAME = 10;
    private static final int PADDING = 2;

    ArrayList<Component> components = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startup::new);
    }
    public Startup() {
        JFrame window = new JFrame();

        Timer cycleTimer = new Timer(TIME_PER_FRAME, actionEvent -> SwingUtilities.invokeLater(() -> {
            for (Component component : components) {component.doCycle();}
            window.repaint();
        }));
        cycleTimer.setRepeats(true);

        window.setTitle("transistor");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        Container pane = window.getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        setUpComponents(pane, constraints);

        window.pack();
        window.toFront();
        window.setVisible(true);

        cycleTimer.start();
    }
    private void setUpComponents(Container pane, GridBagConstraints constraints) {
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
}
