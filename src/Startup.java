import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Startup {
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final int TIME_PER_FRAME = 10;
    private static final int PADDING = 2;

    private static final int WORD_SIZE = 8;

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

        constraints.gridx = 0;
        constraints.gridy = 3;
        pane.add(new JLabel("Result"), constraints);
        constraints.gridy = 0;
        pane.add(new JLabel("Significance"), constraints);

        Adder lastAdder = null;
        for (int i = 0; i < WORD_SIZE; i++) {

            constraints.gridx = WORD_SIZE - i;
            constraints.gridy = 0;
            pane.add(new JLabel(String.valueOf(i)), constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            pane.add(new JLabel("Number #1"), constraints);
            Switch topSwitch = new Switch();
            components.add(topSwitch);
            topSwitch.getIn().addConnection(supply);
            constraints.gridx = WORD_SIZE - i;
            pane.add(topSwitch.getVisuals(), constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            pane.add(new JLabel("Number #2"), constraints);
            Switch bottomSwitch = new Switch();
            components.add(bottomSwitch);
            bottomSwitch.getIn().addConnection(supply);
            constraints.gridx = WORD_SIZE - i;
            pane.add(bottomSwitch.getVisuals(), constraints);

            constraints.gridy = 4;
            Light light = new Light();
            components.add(light);
            constraints.gridx = WORD_SIZE - i;
            pane.add(light.getVisuals(), constraints);

            Adder adder = new Adder();
            components.add(adder);
            adder.getSupply().addConnection(supply);
            adder.getInOne().addConnection(topSwitch.getOut());
            adder.getInTwo().addConnection(bottomSwitch.getOut());
            adder.getSumOut().addConnection(light.getInput());
            if (i != 0) {
                adder.getCarryIn().addConnection(lastAdder.getCarryOut());
            }
            lastAdder = adder;
        }
        Light carryLight = new Light();
        components.add(carryLight);
        lastAdder.getCarryOut().addConnection(carryLight.getInput());

        constraints.gridx = 0;
        constraints.gridy = 4;
        pane.add(carryLight.getVisuals(), constraints);

        constraints.gridy = 3;
        constraints.gridx = WORD_SIZE;
        JButton stopButton = new JButton("Compute");
        stopButton.addActionListener(actionEvent -> System.out.println("STOP"));
        pane.add(stopButton, constraints);
    }
}
