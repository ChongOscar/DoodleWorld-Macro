import com.github.kwhat.jnativehook.NativeHookException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;

    public MainFrame() throws IOException, NativeHookException {
        JFrame frame = new JFrame("Doodle World Macro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 580); // 540 height of image + 40 for window menu bar
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // auto-centers frame in screen
        frame.setIconImage(ImageIO.read(new File("assets/icon.png")));

        // create and add panel
        panel = new GraphicsPanel(frame);
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
