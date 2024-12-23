import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Switch {
    private BufferedImage switchImage;
    private BufferedImage switchButton;
    private int x;
    private int y;
    private boolean isOn;
    private Rectangle rectangle;
    private Rectangle switchBox;

    public Switch(int x, int y) {
        this.x = x;
        this.y = y;
        isOn = false;
        try {
            switchImage = ImageIO.read(new File("assets/switch-image.png"));
            switchButton = ImageIO.read(new File("assets/switch-button.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, switchImage.getWidth(), switchImage.getHeight());
        switchBox = new Rectangle(x, y, switchButton.getWidth(), switchButton.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = y + scrollOffset;
        g.setColor(Color.black);
        g.drawImage(switchImage, x, displayY, null);
        g.drawImage(switchButton, (int) switchBox.getX(), (int) switchBox.getY() + scrollOffset, null);
    }

    public void toggle() {
        isOn = !isOn;
        if (isOn) {
            Utils.startThread(() -> {
                while (switchBox.getX() < rectangle.getX() + rectangle.getWidth() / 2) {
                    switchBox.x++;
                    Utils.wait(1);
                }
                switchBox.x = (int) (rectangle.getX() + rectangle.getWidth() / 2);
            });
        } else {
            Utils.startThread(() -> {
                while (switchBox.getX() > rectangle.getX()) {
                    switchBox.x--;
                    Utils.wait(1);
                }
                switchBox.x = (int) rectangle.getX();
            });
        }
    }

    public boolean isOn() {
        return isOn;
    }
}