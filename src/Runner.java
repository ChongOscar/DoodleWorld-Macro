import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Runner {
    boolean run = true;
    boolean isWhite = true;
    boolean isNormal = true;
    boolean isCaptured = true;

    public void run() throws AWTException, IOException, NativeHookException, InterruptedException {
        // Start global key listener
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new HotkeyListener(this));
        String pokemonName = "nibblen";
        while (true) {
            if (run) {
                Macro macro = new Macro();
                ImageParser imageParser = new ImageParser();
                ScreenCapture screenCapture = new ScreenCapture();

                screenCapture.captureImage(370, 140, 500, 60, "name");
                screenCapture.captureImage(362, 245, 50, 50, "type");

                String parsedPokemonName = imageParser.readImageText("name");
                parsedPokemonName = parsedPokemonName.replaceAll("\\P{Print}","");
                isWhite = imageParser.matchImageColor("name", new Color(200, 200, 200));
                isNormal = imageParser.matchImageColor("type", new Color(0, 0, 0));
                isCaptured = imageParser.matchImageColor("type", new Color(130, 150, 115));
                System.out.println(imageParser.getAverageRGBNoBackground("name", new Color(50,102,132,255), 30));
                System.out.println(imageParser.getAverageRGBNoBackground("type", new Color(50,102,132,255), 30));
                System.out.println(parsedPokemonName + "\nclose enough: " +StringSimilarity.isCloseEnough(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase()) + "\n" + "name white: " + isWhite + "\n" + "normal: " + isNormal+ "\n" + "captured: " + isCaptured);
                if (!parsedPokemonName.isEmpty()) {
                    if (StringSimilarity.isCloseEnough(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase())) {
                        if (isWhite && (isNormal || isCaptured)) {
                            macro.attack(2);
                        }
                    } else {
                        macro.run();
                    }
                    imageParser.end();
                }
            }
            Thread.sleep(500);
        }
    }

    public void toggleRun() {
        run = !run;
    }
}

