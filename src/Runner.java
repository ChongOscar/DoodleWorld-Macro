import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Runner {
    private boolean run;
    private String pokemonName;
    private boolean isWhite;
    private boolean isNormal;
    private boolean isCaptured;
    private boolean attack1Toggle;
    private boolean attack2Toggle;
    private boolean attack3Toggle;
    private boolean attack4Toggle;
    private Macro macro;
    private ImageParser imageParser;
    private ScreenCapture screenCapture;

    public Runner(String pokemonName) throws AWTException {
        macro = new Macro();
        imageParser = new ImageParser();
        screenCapture = new ScreenCapture();
        run = true;
        this.pokemonName = pokemonName.replaceAll("\\s+","");
        isWhite = true;
        isNormal = true;
        isCaptured = true;
        attack1Toggle = true;
        attack2Toggle = false;
        attack3Toggle = true;
        attack4Toggle = false;
    }

    public void run() throws AWTException, IOException, NativeHookException, InterruptedException, TesseractException {
        // Start global key listener
        while (true) {
            if (run && getFocusedWindow().equals("Roblox")) {
                String parsedPokemonName = getImageText(370, 140, 500, 60, "name");
                screenCapture.captureImage(362, 245, 50, 50, "type");

                isWhite = imageParser.matchImageColor("name", new Color(200, 200, 200));
                isNormal = imageParser.matchImageColor("type", new Color(0, 0, 0));
                isCaptured = imageParser.matchImageColor("type", new Color(130, 150, 115));

                System.out.println(imageParser.getAverageRGBNoBackground("name", new Color(50,102,132,255), 30));
                System.out.println(imageParser.getAverageRGBNoBackground("type", new Color(50,102,132,255), 30));
                System.out.println(parsedPokemonName + "\nclose enough: " +StringSimilarity.isCloseEnough(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase()) + "\n" + "name white: " + isWhite + "\n" + "normal: " + isNormal+ "\n" + "captured: " + isCaptured);

                if (!parsedPokemonName.isEmpty()) {
                    if (StringSimilarity.isCloseEnough(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase())) {
                        if (isWhite && (isNormal || isCaptured)) {
                            macro.fight();
                            String attack1pp = getImageText(670, 810, 140, 40, "attack1");
                            String attack2pp = getImageText(1140, 810, 140, 40, "attack2");
                            String attack3pp = getImageText(1610, 810, 140, 40, "attack3");
                            String attack4pp = getImageText(2080, 810, 140, 40, "attack4");
                            if (isInt(attack1pp)) {
                                if (attack1Toggle && isMoveAvaliable(attack1pp)) {
                                    macro.attack(1);
                                } else if (attack2Toggle && isMoveAvaliable(attack2pp)) {
                                    macro.attack(2);
                                } else if (attack3Toggle && isMoveAvaliable(attack3pp)) {
                                    macro.attack(3);
                                } else if (attack4Toggle && isMoveAvaliable(attack4pp)) {
                                    macro.attack(4);
                                }
                            }
                        }
                    } else {
                        macro.run();
                    }
                }
            }
            Thread.sleep(500);
        }
    }

    public void toggleRun() {
        run = !run;
    }

    private String getImageText(int x, int y, int width, int height, String name) throws TesseractException {
        screenCapture.captureImage(x, y, width, height, name);
        String string = imageParser.readImageText(name);
        System.out.println(string);
        return string;
    }

    private boolean isMoveAvaliable(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            int firstNum = Integer.parseInt(str.substring(0, 1));
            return firstNum != 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isInt(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str.substring(0, 1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getFocusedWindow() {
        // Get the handle of the currently focused window
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        if (hwnd != null) {
            // Get the window title
            char[] windowText = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, windowText, 512);

            return Native.toString(windowText);
        }
        return "";
    }
}

