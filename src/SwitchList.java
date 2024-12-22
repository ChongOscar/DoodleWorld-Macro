import java.util.ArrayList;

public class SwitchList {
    private ArrayList<Switch> switches;
    private ArrayList<String> names;

    public SwitchList() {
        switches = new ArrayList<>();
        names = new ArrayList<>();
    }

    public void add(Switch newSwitch, String name) {
        switches.add(newSwitch);
        names.add(name);
    }

    public Switch get(String getName) {
        for (int i = 0; i < names.toArray().length; i++) {
            if (names.get(i).equals(getName)) {
                return switches.get(i);
            }
        }
        return null;
    }

    public Switch get(int i) {
        return switches.get(i);
    }

    public int length() {
        return switches.toArray().length;
    }
}
