import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

public class ImageParser {
    private ITesseract tesseract;
    private Color transparentPixel;
    private Color blackPixel;
    private Color whitePixel;

    public ImageParser() {
        // Initialize Tesseract instance
        tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");

        // Set the language (e.g., English)
        tesseract.setLanguage("eng");
        transparentPixel = new Color(0, 0, 0, 0);
        blackPixel = new Color(0, 0, 0, 255);
        whitePixel = new Color(255, 255, 255, 255);
    }

    public String readImageText(String name) throws TesseractException {
        File imageFile = new File("screencaptures/" + name + ".png");
        String str = tesseract.doOCR(imageFile);
        str.replaceAll("\\P{Print}", "");
        str = str.replaceAll("y410\\)", "2/30");         //this is so dumb
        return str;
    }

    public void removeBackgroundColor(String imageName, Color[] colors) throws IOException {
        File inputFile = new File("screencaptures/" + imageName + ".png"); // Path to your image
        BufferedImage image = ImageIO.read(inputFile);

        int width = image.getWidth();
        int height = image.getHeight();

        image = ensureTransparency(image, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel, true); // true to include alpha

                if (x < 8 && y > (height - 8 + x)) {
                    image.setRGB(x, y, transparentPixel.getRGB());
                }
                if (matchesAnyColor(color, colors)) {
                    image.setRGB(x, y, transparentPixel.getRGB());
                }
            }
        }

        // Save the modified image to a new file
        File outputFile = new File("screencaptures/" + imageName + ".png");
        ImageIO.write(image, "png", outputFile);
    }

    public Color getAverageRGB(String imageName) throws IOException {
        File inputFile = new File("screencaptures/" + imageName + ".png"); // Path to your image
        BufferedImage image = ImageIO.read(inputFile);

        int width = image.getWidth();
        int height = image.getHeight();

        long totalRed = 0, totalGreen = 0, totalBlue = 0;
        int validPixelCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel, true); // true to include alpha
                if (color.getAlpha() != 0) {
                    // Accumulate RGB values for non-background pixels
                    totalRed += color.getRed();
                    totalGreen += color.getGreen();
                    totalBlue += color.getBlue();
                    validPixelCount++;
                }
            }
        }
        // Calculate average RGB
        int avgRed = validPixelCount == 0 ? 0 : (int) (totalRed / validPixelCount);
        int avgGreen = validPixelCount == 0 ? 0 : (int) (totalGreen / validPixelCount);
        int avgBlue = validPixelCount == 0 ? 0 : (int) (totalBlue / validPixelCount);
        return new Color(avgRed, avgGreen, avgBlue);
    }

    public void isolateWhiteText(String imageName) throws IOException {
        File inputFile = new File("screencaptures/" + imageName + ".png"); // Path to your image
        BufferedImage image = ImageIO.read(inputFile);

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel); // true to include alpha

                if (!isSimilarColor(color, whitePixel, 30)) {
                    image.setRGB(x, y, blackPixel.getRGB()); // black pixel
                } else {
                    image.setRGB(x, y, whitePixel.getRGB());
                }

            }
        }
        image = ImageHelper.invertImageColor(image);

        // Save the modified image to a new file
        File outputFile = new File("screencaptures/" + imageName + ".png");
        ImageIO.write(image, "png", outputFile);
    }

    public void isolateNameText(String imageName) throws IOException {
        File inputFile = new File("screencaptures/" + imageName + ".png"); // Path to your image
        BufferedImage image = ImageIO.read(inputFile);

        int width = image.getWidth();
        int height = image.getHeight();

        image = ImageHelper.convertImageToBinary(image);

        BufferedImage argbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = argbImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        image = argbImage;

        image = ImageHelper.invertImageColor(image);

        // Save the modified image to a new file
        File outputFile = new File("screencaptures/" + imageName + "_OCR.png");
        ImageIO.write(image, "png", outputFile);
    }

    private BufferedImage ensureTransparency(BufferedImage image, int width, int height) {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
            return image; // Already supports transparency
        }

        BufferedImage argbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = argbImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return argbImage;
    }

    public boolean isSimilarColor(Color c1, Color c2, int threshold) {
        int diffRed = Math.abs(c1.getRed() - c2.getRed());
        int diffGreen = Math.abs(c1.getGreen() - c2.getGreen());
        int diffBlue = Math.abs(c1.getBlue() - c2.getBlue());
        return (diffRed <= threshold) && (diffGreen <= threshold) && (diffBlue <= threshold);
    }

    public boolean matchImageColor(String imageName, Color matchColor) throws IOException {
        return isSimilarColor(getAverageRGB(imageName), matchColor, 15);
    }

    private boolean matchesAnyColor(Color color, Color[] targetColors) {
        for (Color targetColor : targetColors) {
            if (isSimilarColor(color, targetColor, 10)) {
                return true;
            }
        }
        return false;
    }
}