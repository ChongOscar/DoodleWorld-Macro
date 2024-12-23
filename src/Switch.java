import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Switch extends Interactable {
    private BufferedImage switchImage;
    private BufferedImage switchButton;
    private boolean isOn;
    private Rectangle switchBox;

    public Switch(int x, int y) {
        super(x, y);
        isOn = false;
        try {
            switchImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/switch-image.png")));
            switchButton = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/switch-button.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, switchImage.getWidth(), switchImage.getHeight());
        switchBox = new Rectangle(x, y, switchButton.getWidth(), switchButton.getHeight());
    }

    public void toggle() {
        isOn = !isOn;
        Utils.startThread(() -> {
            int targetX = isOn ? (int) (rectangle.getX() + rectangle.getWidth() / 2) : (int) rectangle.getX();
            while (switchBox.getX() != targetX) {
                switchBox.x += isOn ? 1 : -1;
                Utils.wait(1);
            }
        });
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = displayY;
        g.drawImage(switchImage, x, displayY, null);
        g.drawImage(switchButton, switchBox.x, switchBox.y + scrollOffset, null);
    }
}