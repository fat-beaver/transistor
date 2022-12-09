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
        setUpComponents(pane, constraints);

        window.pack();
        window.toFront();
        window.setVisible(true);

        cycleTimer.start();
    }
    private void setUpComponents(Container pane, GridBagConstraints constraints) {
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
