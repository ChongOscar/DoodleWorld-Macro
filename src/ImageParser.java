import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

public class ImageParser {
    tesseract.TessBaseAPI api;
    BytePointer outText;
    lept.PIX image;
    public ImageParser() {
        api = new tesseract.TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(null, "eng") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }
    }

    public String readImageText(String name) {
        image = pixRead(name + ".png");
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        return outText.getString();
    }

    public Color getAverageRGBNoBackground(String imageName, Color backgroundColor, int threshold) throws IOException {
        File inputFile = new File(imageName + ".png"); // Path to your image
        BufferedImage image = ImageIO.read(inputFile);

        int width = image.getWidth();
        int height = image.getHeight();

        long totalRed = 0, totalGreen = 0, totalBlue = 0;
        int validPixelCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel, true); // true to include alpha

                // Check if the pixel is similar to the background color
                if (isSimilarColor(color, backgroundColor, threshold)) {
                    // Make the pixel transparent (remove background)
                    image.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                } else {
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
        System.out.println(avgRed + " " + avgBlue + " " + avgGreen);
        return new Color(avgRed, avgGreen, avgBlue);
    }

    public boolean isSimilarColor(Color c1, Color c2, int threshold) {
        int diffRed = Math.abs(c1.getRed() - c2.getRed());
        int diffGreen = Math.abs(c1.getGreen() - c2.getGreen());
        int diffBlue = Math.abs(c1.getBlue() - c2.getBlue());
        return (diffRed <= threshold) && (diffGreen <= threshold) && (diffBlue <= threshold);
    }

    public boolean matchImageColor(String imageName, Color matchColor) throws IOException {
        return isSimilarColor(getAverageRGBNoBackground(imageName, new Color(50,102,132,255), 30), matchColor, 30);
    }

    public void end() {
        api.End();
        outText.deallocate();
        pixDestroy(image);
    }
}
