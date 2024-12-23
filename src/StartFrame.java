import com.github.kwhat.jnativehook.NativeHookException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StartFrame implements Runnable {

    private StartPanel panel;

    public StartFrame() throws IOException, NativeHookException, AWTException {
        JFrame frame = new JFrame("Doodle World Macro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 580); // 540 height of image + 40 for window menu bar
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // auto-centers frame in screen
        frame.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon.png"))));

        // create and add panel
        panel = new StartPanel(frame);
        frame.add(panel);
        // display the frame
        frame.setVisible(true);

        // start thread, required for animation
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            panel.repaint();  // we don't call "paintComponent" directly; we use repaint() instead
        }
    }
}
