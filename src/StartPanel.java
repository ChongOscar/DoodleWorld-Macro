import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartPanel extends JPanel implements MouseListener, ActionListener {
    private Frame frame;
    public StartPanel(Frame frame) {
        this.frame = frame;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Courier New", Font.BOLD, 80));
        g.drawString("Doodle World Macro", frame.getWidth() / 2 - g.getFontMetrics().stringWidth("Doodle World Macro") / 2, 150);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
}
