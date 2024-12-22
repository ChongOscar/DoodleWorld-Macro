import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener, MouseWheelListener {
    private Frame frame;
    private BufferedImage background;
    private Timer timer;
    private Button button;
    private SwitchList switchList;
    private TextBox textBox;
    private TextBox bigTextBox;
    private int scrollOffset;

    public GraphicsPanel(Frame frame) throws NativeHookException {
//        try {
//            background = ImageIO.read(new File("src/background.png"));
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        this.frame = frame;
        timer = new Timer(500, this);
        timer.start();
        textBox = new TextBox(300, 80, TextBox.NAME);
        button = new Button(520, 80);
        switchList = new SwitchList();
        switchList.add(new Switch(100, 150), "move1Switch");
        bigTextBox = new TextBox(300, 300, TextBox.BIG);
        scrollOffset = 0;
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
                    button.toggle();
                }
            }
        });
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
        textBox.render(g, scrollOffset);
        button.render(g, scrollOffset);
        switchList.get("move1Switch").render(g, scrollOffset);
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
            if (button.getRectangle().contains(mouseClickLocation)) {
                button.toggle();
            }
            for (int i = 0; i < switchList.length(); i++) {
                if (switchList.get(i).getRectangle().contains(mouseClickLocation)) {
                    switchList.get(i).toggle();
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
            if (button.IsOn()) {
                if (getFocusedWindow().equals("Roblox")) {

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

}