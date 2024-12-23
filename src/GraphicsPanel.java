import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener, MouseWheelListener {
    private Frame frame;
    private BufferedImage background;
    private Timer timer;
    private Button button;
    private SwitchList switchList;
    private TextBox textBox;
    private TextBox bigTextBox;
    private int scrollOffset;
    private Runner runner;
    private Storage storage;

    public GraphicsPanel(Frame frame) throws NativeHookException, AWTException {
//        try {
//            background = ImageIO.read(new File("src/background.png"));
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        this.frame = frame;
        timer = new Timer(250, this);
        timer.start();
        textBox = new TextBox(300, 80, TextBox.NAME);
        button = new Button(520, 80);
        switchList = new SwitchList();
        switchList.add(new Switch(40, 150), "Misprints");
        switchList.add(new Switch(40, 190), "Skins");
        switchList.add(new Switch(40, 230), "Uniques");
        switchList.add(new Switch(40, 270), "Other cosmetics");
        switchList.add(new Switch(40, 310), "Any misprints");
        switchList.add(new Switch(40, 350), "Any skins");
        switchList.add(new Switch(40, 390), "Any uniques");
        switchList.add(new Switch(40, 430), "Any other cosmetics");
        switchList.add(new Switch(40, 490), "Stop condition: ");
        switchList.add(new Switch(590, 470), "Kill exceptions");
        switchList.add(new Switch(680, 150), "Move 1");
        switchList.add(new Switch(680, 190), "Move 2");
        switchList.add(new Switch(680, 230), "Move 3");
        switchList.add(new Switch(680, 270), "Move 4");
        bigTextBox = new TextBox(590, 350, TextBox.BIG);
        scrollOffset = 0;
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
                    if (!textBox.getString().isEmpty()) button.toggle();
                }
            }
        });
        runner = new Runner(textBox, switchList, bigTextBox);
        storage = new Storage("savedData.txt");
        loadData();
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        addMouseWheelListener(this);
        requestFocusInWindow(); // see comment above
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
//        g.drawImage(background, 0, 0, null);
        g.setFont(new Font("Courier New", Font.BOLD, 40));
        g.drawString("Doodle World Macro", frame.getWidth() / 2 - g.getFontMetrics().stringWidth("Doodle World Macro") / 2, 50 + scrollOffset);
        g.setFont(new Font("Courier New", Font.BOLD, 15));
        g.drawString("Hotkey: F6", 535, 75 + scrollOffset);
        g.setFont(new Font("Courier New", Font.BOLD, 25));
        g.drawString("Stop for", 70, 115 + scrollOffset);
        g.drawString("Toggle Moves", 700, 115 + scrollOffset);
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        for (int i = 0; i < switchList.length(); i++) {
            g.drawString(switchList.getName(i), switchList.get(i).getX() + 80, switchList.get(i).getY() + 20 + scrollOffset);
        }
        if (switchList.get("Stop condition: ").isOn()) {
            g.drawString("And", 300, 510);
        } else {
            g.drawString("Or", 300, 510);
        }

        textBox.render(g, scrollOffset);
        button.render(g, scrollOffset);
        for (Object switches : switchList.getall()) {
            ((Switch) switches).render(g, scrollOffset);
        }
        bigTextBox.render(g, scrollOffset);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (textBox.isFocused()) {
            if (e.getKeyCode() == 27 || e.getKeyCode() == 10) {
                textBox.setFocus(false);
            } else if (e.getKeyCode() == 8 || e.getKeyCode() == 127) {
                textBox.removeChar();
            } else if (e.getKeyChar() != '\uFFFF'){
                textBox.addChar(e.getKeyChar());
            }
        }
        if (bigTextBox.isFocused()) {
            if (e.getKeyCode() == 27 || e.getKeyCode() == 10) {
                bigTextBox.setFocus(false);
            } else if (e.getKeyCode() == 8 || e.getKeyCode() == 127) {
                bigTextBox.removeChar();
            } else if (e.getKeyChar() != '\uFFFF'){
                bigTextBox.addChar(e.getKeyChar());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            System.out.println(mouseClickLocation);
            if (button.getRectangle().contains(mouseClickLocation) && !textBox.getString().isEmpty()) {
                button.toggle();
            }
            for (int i = 0; i < switchList.length(); i++) {
                if (switchList.get(i).getRectangle().contains(mouseClickLocation)) {
                    switchList.get(i).toggle();
                    if (!switchList.get("Stop condition: ").isOn()) break;
                    if (switchList.get(i).equals(switchList.get("Misprints")) && switchList.get("Uniques").isOn()) {
                        switchList.get("Uniques").toggle();
                    } else if (switchList.get(i).equals(switchList.get("Uniques")) && switchList.get("Misprints").isOn()) {
                        switchList.get("Misprints").toggle();
                    }
                    if (switchList.get(i).equals(switchList.get("Any misprints")) && switchList.get("Any uniques").isOn()) {
                        switchList.get("Any uniques").toggle();
                    } else if (switchList.get(i).equals(switchList.get("Any uniques")) && switchList.get("Any misprints").isOn()) {
                        switchList.get("Any misprints").toggle();
                    }
                    if (!switchList.get(i).equals(switchList.get("Stop condition: "))) break;
                    if (switchList.get("Uniques").isOn() && switchList.get("Misprints").isOn()) {
                        switchList.get(("Uniques")).toggle();
                    }
                    if (switchList.get("Any uniques").isOn() && switchList.get("Any misprints").isOn()) {
                        switchList.get(("Any uniques")).toggle();
                    }
                }
            }
            textBox.setFocus(textBox.getRectangle().contains(mouseClickLocation));
            bigTextBox.setFocus(bigTextBox.getRectangle().contains(mouseClickLocation));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (button.isOn()) {
                if (getFocusedWindow().equals("Roblox")) {
                    try {
                        runner.run();
                    } catch (AWTException | IOException | NativeHookException | InterruptedException |
                             TesseractException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if (scrollOffset < 0) scrollOffset += 20;
        } else if (scrollOffset > -500){
            scrollOffset -= 20;
        }
    }

    private String getFocusedWindow() {
        // Get the handle of the currently focused window
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        if (hwnd != null) {
            // Get the window title
            char[] windowText = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, windowText, 512);

            return Native.toString(windowText);
        }
        return "";
    }

    private void loadData() {
        if (storage.containsKey("nameTextBox")) {
            textBox.setString(storage.getItem("nameTextBox"));
        }
        if (storage.containsKey("bigTextBox")) {
            bigTextBox.setString(storage.getItem("bigTextBox"));
        }
        for (int i = 0; i < switchList.length(); i++) {
            if (!storage.containsKey(switchList.getName(i))) break;
            if (storage.getItem(switchList.getName(i)).equals("true")) {
                switchList.get(i).toggle();
            }
        }
    }

    public void saveData() {
        storage.setItem("nameTextBox", textBox.getString());
        storage.setItem("bigTextBox", bigTextBox.getString());
        for (int i = 0; i < switchList.length(); i++) {
            storage.setItem(switchList.getName(i), String.valueOf(switchList.get(i).isOn()));
        }
    }
}