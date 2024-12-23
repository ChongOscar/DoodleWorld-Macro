import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ScreenCapture {
    private Robot robot;

    public ScreenCapture() throws AWTException {
        robot = new Robot();
    }

    public void captureImage(int x, int y, int width, int height, String outputName) {
        try {
            Rectangle captureArea = new Rectangle(x, y, width, height);
            // Capture the screen area as a BufferedImage
            BufferedImage pokemonName = robot.createScreenCapture(captureArea);
            // Save the captured image to a file
            File outputFile = new File("screencaptures/" + outputName +".png");
            ImageIO.write(pokemonName, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}