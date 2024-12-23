import com.github.kwhat.jnativehook.NativeHookException;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class StartPanel extends JPanel implements MouseListener, ActionListener {
    private Frame frame;
    private StartButton button;
    private float alpha;
    private Timer timer;
    private LoadBar loadBar;
    private boolean isStarted;
    private boolean alphaStop;
    private MainFrame mainFrame;
    public StartPanel(Frame frame) {
        this.frame = frame;
        this.alpha = 1.0f;
        button = new StartButton((frame.getWidth() - 17) / 2 - 150 / 2, 200);
        timer = new Timer(10, this);
        loadBar = new LoadBar((frame.getWidth() - 17) / 2 - 500 / 2, 320);
        isStarted = false;
        alphaStop = false;
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(new Font("Courier New", Font.BOLD, 80));
        g2d.drawString("Doodle World Macro", (frame.getWidth() - 17) / 2 - g.getFontMetrics().stringWidth("Doodle World Macro") / 2, 150);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        button.render(g2d, 0);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.abs(alpha - 1)));
        loadBar.render(g2d, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (!alphaStop) {
                changeAlpha();
            }
            if (loadBar.isFinished()) {
                frame.setVisible(false);
                mainFrame.setVisible();
                timer.stop();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            if (isStarted) return;
            if (button.getRectangle().contains(mouseClickLocation)) {
                button.toggle();
                timer.start();
                loadBar.toggle();
                isStarted = true;
                try {
                    mainFrame = new MainFrame();
                } catch (IOException | NativeHookException | AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            if (button.isOn()) {
                button.toggle();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void changeAlpha() {
        alpha -= 0.05f; // Decrease opacity
        if (alpha <= 0.0f) {
            alpha = 0.0f;
            alphaStop = true;
        }
    }
}
