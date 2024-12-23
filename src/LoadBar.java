import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadBar extends Interactable {
    private BufferedImage barImage;
    private BufferedImage fillImage;
    private BufferedImage fillCoverImage;
    private double fillImageX;
    private int fillCoverImageX;
    private boolean finished;

    public LoadBar(int x, int y) {
        super(x, y);
        try {
            barImage = ImageIO.read(new File("assets/load-bar-image.png"));
            fillImage = ImageIO.read(new File("assets/fill-bar-image.png"));
            fillCoverImage = ImageIO.read(new File("assets/fill-cover-image.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fillImageX = x - barImage.getWidth();
        fillCoverImageX = x - barImage.getWidth();
        finished = false;
    }
    @Override
    public void render(Graphics g, int scrollOffset) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(fillImage, (int) fillImageX, y, null);
        g2d.drawImage(barImage, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.drawImage(fillCoverImage, fillCoverImageX, y, null);
    }

    public void toggle() {
        Utils.startThread(() -> {
            while (fillImageX < x) {
                fillImageX += 0.3;
                Utils.wait(1);
            }
            System.out.println("finish");
            finished = true;
        });
    }

    public boolean isFinished() {
        return finished;
    }
}
