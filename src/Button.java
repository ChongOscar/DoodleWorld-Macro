import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Button {
    private BufferedImage buttonImageOn;
    private BufferedImage buttonImageOff;
    private int x;
    private int y;
    private boolean isOn;
    private Rectangle rectangle;

    public Button(int x, int y) {
        this.x = x;
        this.y = y;
        isOn = false;
        try {
            buttonImageOn = ImageIO.read(new File("assets/button-image-on.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            buttonImageOff = ImageIO.read(new File("assets/button-image-off.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, getImage().getWidth(), getImage().getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        if (isOn) {
            return buttonImageOn;
        } else {
            return buttonImageOff;
        }
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = y + scrollOffset;
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawImage(getImage(), x, displayY, null);
        g.setColor(Color.black);
        if (isOn) {
            g.drawString("STOP", x + 34, displayY + 30);
        } else {
            g.drawString("START", x + 27, displayY + 30);
        }
    }

    public void toggle() {
        isOn = !isOn;
    }

    public boolean IsOn() {
       return isOn;
    }
}
