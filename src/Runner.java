import com.github.kwhat.jnativehook.NativeHookException;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.IOException;

public class Runner {
    private Macro macro;
    private ImageParser imageParser;
    private ScreenCapture screenCapture;
    private TextBox nameTextBox;
    private TextBox exceptionsTextBox;
    private SwitchList switchList;
    private Toolkit toolkit;
    private int screenWidth;
    private int screenHeight;
    private int frameWidth;
    private int frameHeight;
    private int startX;
    private int startY;
    private float screenScale;

    String pokemonName;
    String exceptions;
    boolean misprintSwitch;
    boolean skinsSwitch;
    boolean uniquesSwitch;
    boolean otherCosmeticSwitch;
    boolean anyMisprintsSwitch;
    boolean anySkinsSwitch;
    boolean anyUniquesSwitch;
    boolean anyOtherCosmeticSwitch;
    boolean killExceptionsSwitch;
    boolean stopConditionSwitch;
    boolean attack1Toggle;
    boolean attack2Toggle;
    boolean attack3Toggle;
    boolean attack4Toggle;

    String parsedPokemonName;
    boolean isWhite;
    boolean isSkin;
    boolean isNormal;
    boolean isCaptured;
    boolean isMisprint;
    boolean isUnique;
    boolean isRightPokemon;
    boolean isException;

    boolean matchesCondition;
    boolean runAway;

    public Runner(TextBox nameTextBox, SwitchList switchList, TextBox exceptionsTextBox) throws AWTException {
        toolkit = Toolkit.getDefaultToolkit();
        screenWidth = toolkit.getScreenSize().width;
        screenHeight = toolkit.getScreenSize().height;
        initFrameRes();
        macro = new Macro(frameWidth, frameHeight, startX, startY, screenScale);
        imageParser = new ImageParser();
        screenCapture = new ScreenCapture();
        this.nameTextBox = nameTextBox;
        this.exceptionsTextBox = exceptionsTextBox;
        this.switchList = switchList;
    }

    public void run() throws AWTException, IOException, NativeHookException, InterruptedException, TesseractException {
        initSwitches();
        initDetection();

        System.out.println();
        System.out.println("name avg RGB: " + imageParser.getAverageRGBNoBackground("name", 15));
        System.out.println("icon avg RGB: " + imageParser.getAverageRGBNoBackground("type", 15));
        System.out.println("scanned name: " + parsedPokemonName + "close enough: "
                + StringSimilarity.isCloseEnough(parsedPokemonName.toLowerCase(), pokemonName.toLowerCase()) + "\n"
                + "name white: " + isWhite + "\n" + "normal: " + isNormal + "\n"
                + "captured: " + isCaptured+ "\n" + "skin: " + isSkin + "\n" + "misprint: " + isMisprint + "\n"
                + "unique: " + isUnique);
        matchesCondition = false;
        runAway = true;

        if (parsedPokemonName.isEmpty()) return;

        if (stopConditionSwitch) {
            andLogic();
        } else {
            orLogic();
        }
        if (isException) {
            if (killExceptionsSwitch) {
                attack(attack1Toggle, attack2Toggle, attack3Toggle, attack4Toggle); // Attack exception
            }
        } else if (!matchesCondition && isRightPokemon) {
            attack(attack1Toggle, attack2Toggle, attack3Toggle, attack4Toggle); // Attack right Pokémon
        } else if (runAway){
                macro.runAway();
        }
    }

    private String getImageText(int x, int y, int width, int height, String name) throws TesseractException {
        screenCapture.captureImage(x, y, width, height, name);
        return imageParser.readImageText(name);
    }

    private boolean isMoveAvailable(String str) {
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

    private void attack(boolean attack1Toggle, boolean attack2Toggle, boolean attack3Toggle, boolean attack4Toggle) throws TesseractException {
        macro.fight();
        String attack1pp = getImageText(relativeXPos(0.1720779220779f), relativeYPos(0.72784810126582f), relativeX(0.077922077922f), relativeY(0.05696202531645569f), "attack1");
        String attack2pp = getImageText(relativeXPos(0.424025974025973f), relativeYPos(0.72784810126582f), relativeX(0.077922077922f), relativeY(0.05696202531645569f), "attack2");
        String attack3pp = getImageText(relativeXPos(0.670779220779220f), relativeYPos(0.72784810126582f), relativeX(0.077922077922f), relativeY(0.05696202531645569f), "attack3");
        String attack4pp = getImageText(relativeXPos(0.92077922077922f), relativeYPos(0.72784810126582f), relativeX(0.077922077922f), relativeY(0.05696202531645569f), "attack4");
        if (isInt(attack1pp)) {
            if (attack1Toggle && isMoveAvailable(attack1pp)) {
                macro.attack(1);
            } else if (attack2Toggle && isMoveAvailable(attack2pp)) {
                macro.attack(2);
            } else if (attack3Toggle && isMoveAvailable(attack3pp)) {
                macro.attack(3);
            } else if (attack4Toggle && isMoveAvailable(attack4pp)) {
                macro.attack(4);
            }
        }
    }

    private void initSwitches() {
        pokemonName = nameTextBox.getString().replaceAll("\\s+","").toLowerCase();
        exceptions = exceptionsTextBox.getString().toLowerCase();
        misprintSwitch = switchList.get("Misprints").isOn();
        skinsSwitch = switchList.get("Skins").isOn();
        uniquesSwitch = switchList.get("Uniques").isOn();
        otherCosmeticSwitch = switchList.get("Other cosmetics").isOn();
        anyMisprintsSwitch = switchList.get("Any misprints").isOn();
        anySkinsSwitch = switchList.get("Any skins").isOn();
        anyUniquesSwitch = switchList.get("Any uniques").isOn();
        anyOtherCosmeticSwitch = switchList.get("Any other cosmetics").isOn();
        killExceptionsSwitch = switchList.get("Kill exceptions").isOn();
        stopConditionSwitch = switchList.get("Stop condition: ").isOn();
        attack1Toggle = switchList.get("Move 1").isOn();
        attack2Toggle = switchList.get("Move 2").isOn();
        attack3Toggle = switchList.get("Move 3").isOn();
        attack4Toggle = switchList.get("Move 4").isOn();
    }

    private void initDetection() throws IOException, TesseractException {
        parsedPokemonName = getImageText(relativeXPos(0.02272727272f), relativeYPos(0.05063291139f), relativeX(0.2435064935f), relativeY(0.056962025316455f), "name").toLowerCase();
        screenCapture.captureImage(relativeXPos(0.0162337662f), relativeYPos(0.1518987341772f), relativeX(0.02922077922f), relativeY(0.0569620253164f), "type");
        if (screenScale != 1) {
            isWhite = imageParser.matchImageColor("name", new Color(175, 180, 190));
            isSkin = imageParser.matchImageColor("name", new Color(170, 20, 30));
        } else {
            isWhite = imageParser.matchImageColor("name", new Color(200, 200, 200));
            isSkin = imageParser.matchImageColor("name", new Color(190, 10, 10));
        }
        isNormal = imageParser.matchImageColor("type", new Color(0, 0, 0));
        isCaptured = imageParser.matchImageColor("type", new Color(130, 150, 115));
        isMisprint = imageParser.matchImageColor("type", new Color(65, 65, 25));
        isUnique = imageParser.matchImageColor("type", new Color(65, 65, 60));
        isRightPokemon = StringSimilarity.isCloseEnough(parsedPokemonName, pokemonName);
        isException = false;
        if (!exceptions.isEmpty()) {
            for (String exception : exceptions.split(",")) {
                if (StringSimilarity.isCloseEnough(parsedPokemonName, exception)) {
                    isException = true;
                }
            }
        }
    }

    private void orLogic() {
        if (anyMisprintsSwitch && isMisprint && !isCaptured && !isNormal) {
            matchesCondition = true;
            runAway = false;
        } else if (anySkinsSwitch && isSkin) {
            matchesCondition = true;
            runAway = false;
        } else if (anyUniquesSwitch && isUnique && !isCaptured && !isNormal) {
            matchesCondition = true;
            runAway = false;
        } else if (anyOtherCosmeticSwitch && !isWhite) {
            matchesCondition = true;
            runAway = false;
        }

        // Check specific switches (requires Pokémon name match)
        if (isRightPokemon) {
            if (misprintSwitch && isMisprint && !isCaptured && !isNormal) {
                matchesCondition = true;
                runAway = false;
            } else if (skinsSwitch && isSkin) {
                matchesCondition = true;
                runAway = false;
            } else if (uniquesSwitch && isUnique && !isCaptured && !isNormal) {
                matchesCondition = true;
                runAway = false;
            } else if (otherCosmeticSwitch && !isWhite) {
                matchesCondition = true;
                runAway = false;
            }
        }
    }

    public void andLogic() {
        boolean stop = false;
        if (anyMisprintsSwitch && isMisprint && !isCaptured && !isNormal) {
            matchesCondition = true;
            runAway = false;
        } else if (anyMisprintsSwitch) {
            matchesCondition = false;
            runAway = true;
            stop = true;
        }
        if (anySkinsSwitch && isSkin && !stop) {
            matchesCondition = true;
            runAway = false;
        } else if (anySkinsSwitch && !anyOtherCosmeticSwitch) {
            matchesCondition = false;
            runAway = true;
            stop = true;
        }
        if (anyUniquesSwitch && isUnique && !isCaptured && !isNormal && !stop) {
            matchesCondition = true;
            runAway = false;
        } else if (anyUniquesSwitch) {
            matchesCondition = false;
            runAway = true;
            stop = true;
        }
        if (anyOtherCosmeticSwitch && !isWhite && !stop) {
            matchesCondition = true;
            runAway = false;
        } else if (anyOtherCosmeticSwitch) {
            matchesCondition = false;
            runAway = true;
            stop = true;
        }

        // Check specific switches (requires Pokémon name match)
        if (isRightPokemon) {
            if (misprintSwitch && isMisprint && !isCaptured && !isNormal && !stop) {
                matchesCondition = true;
                runAway = false;
            } else if (misprintSwitch) {
                matchesCondition = false;
                runAway = true;
                stop = true;
            }
            if (skinsSwitch && isSkin && !stop) {
                matchesCondition = true;
                runAway = false;
            } else if (skinsSwitch && !otherCosmeticSwitch) {
                matchesCondition = false;
                runAway = true;
                stop = true;
            }
            if (uniquesSwitch && isUnique && !isCaptured && !isNormal && !stop) {
                matchesCondition = true;
                runAway = false;
            } else if (uniquesSwitch) {
                matchesCondition = false;
                runAway = true;
                stop = true;
            }
            if (otherCosmeticSwitch && !isWhite && !stop) {
                matchesCondition = true;
                runAway = false;
            } else if (otherCosmeticSwitch) {
                matchesCondition = false;
                runAway = true;
            }
        }
    }
    private void initFrameRes() {
        if ((double) screenWidth / screenHeight == 64.0 / 27) {
            frameWidth = 1900;
            frameHeight = 980;
            startX = 330;
            startY = 90;
            screenScale = 2560.0f / screenWidth;
        } else if ((double) screenWidth / screenHeight == 16.0 / 9) {
            frameWidth = 1540;
            frameHeight = 790;
            startX = 190;
            startY = 175;
            screenScale = 1920.0f / screenWidth;
        } else if ((double) screenWidth / screenHeight == 16.0 / 10) {
            frameWidth = 1540;
            frameHeight = 790;
            startX = 190;
            startY = 230;
            screenScale = 1920.0f / screenWidth;
        }
    }

    private int relativeXPos(float pos) {
        return Math.round(((startX + (pos * frameWidth)) / screenScale));
    }
    private int relativeYPos(float pos) {
        return Math.round(((startY + (pos * frameHeight)) / screenScale));
    }
    private int relativeX(float pos) {
        return Math.round(((pos * frameWidth) / screenScale));
    }
    private int relativeY(float pos) {
        return Math.round(((pos * frameHeight) / screenScale));
    }

}

