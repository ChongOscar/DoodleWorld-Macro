import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Button extends Interactable {
    private BufferedImage buttonImageOn;
    private BufferedImage buttonImageOff;
    private boolean isOn;

    public Button(int x, int y) {
        super(x, y);
        isOn = false;
        try {
            buttonImageOn = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/button-image-on.png")));
            buttonImageOff = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/button-image-off.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, getImage().getWidth(), getImage().getHeight());
    }

    public BufferedImage getImage() {
        return isOn ? buttonImageOn : buttonImageOff;
    }

    public void toggle() {
        isOn = !isOn;
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = displayY;
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawImage(getImage(), x, displayY, null);
        g.setColor(Color.black);
        g.drawString(isOn ? "STOP" : "START", x + (isOn ? 34 : 27), displayY + 30);
    }
}
