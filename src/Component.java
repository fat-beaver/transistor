import java.util.ArrayList;

public abstract class Component {
    protected final ArrayList<Component> subComponents = new ArrayList<>();
    public void doCycle() {
        for (Component subComponent : subComponents) {
            subComponent.doCycle();
        }
    }
    public long getTransistorCount() {
        long currentCount = 0;
        if (subComponents.size() != 0) {
            for (Component component : subComponents) {
                currentCount += component.getTransistorCount();
            }
        } else {
            if (this.getClass().getSimpleName().equals("Transistor")) {
                currentCount += 1;
            }
        }
        return currentCount;
    }
}
