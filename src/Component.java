import java.util.ArrayList;

public abstract class Component {
    protected final ArrayList<Component> subComponents = new ArrayList<>();
    public void doCycle() {
        for (Component subComponent : subComponents) {
            subComponent.doCycle();
        }
    }
}
