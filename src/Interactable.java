import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Parent class
public abstract class Interactable {
    protected int x;
    protected int y;
    protected Rectangle rectangle;

    public Interactable(int x, int y) {
        this.x = x;
        this.y = y;
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

    public abstract void render(Graphics g, int scrollOffset);
}