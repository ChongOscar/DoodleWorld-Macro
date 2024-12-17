import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

public class Main {
    public static void main(String[] args) {
        try {
            String pokemonName = "Appluff";

            ScreenCapture screenCapture = new ScreenCapture();
            screenCapture.captureImage(370, 140, 500, 60, "name");

            screenCapture.captureImage(362, 245, 50, 50, "type");

            ImageParser imageParser = new ImageParser();
            String parsedPokemonName = imageParser.readImageText("name");
            parsedPokemonName = parsedPokemonName.replaceAll("\\P{Print}","");
            if (Objects.equals(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase())) {
                if (imageParser.matchImageColor("name", new Color(200, 200, 200)) && (imageParser.matchImageColor("type", new Color(130, 150, 115)) || imageParser.matchImageColor("type", new Color(0, 0, 0)))) {
                    System.out.println("kill");
                } else {
                    System.out.println("not kill");
                }
            }

            imageParser.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}