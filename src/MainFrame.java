import javax.swing.*;
import java.awt.*;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;

    public MainFrame() {
        JFrame frame = new JFrame("Doodle World Macro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 580); // 540 height of image + 40 for window menu bar
        frame.setLocationRelativeTo(null); // auto-centers frame in screen

        // create and add panel
        panel = new GraphicsPanel();
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
