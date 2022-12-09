import javax.swing.*;
import java.awt.*;

public class Light extends Component {
    private final Pin input;
    private boolean state;
    private final JComponent visuals;
    public Light() {
        input = new Pin(this);
        state = false; //off
        visuals = new JComponent() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (state) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillOval(0, 0, 50, 50);
                setPreferredSize(new Dimension(50, 50));
            }
        };
    }
    @Override
    public void doCycle() {
        super.doCycle();
        state = input.getState();
    }
    public boolean getState() {
        return state;
    }
    public Pin getInput() {
        return input;
    }
    public JComponent getVisuals() {
        return visuals;
    }
}
