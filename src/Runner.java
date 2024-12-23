import com.github.kwhat.jnativehook.NativeHookException;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Runner {
    private Macro macro;
    private ImageParser imageParser;
    private ScreenCapture screenCapture;
    private TextBox nameTextBox;
    private TextBox exceptionsTextBox;
    private SwitchList switchList;

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
        macro = new Macro();
        imageParser = new ImageParser();
        screenCapture = new ScreenCapture();
        this.nameTextBox = nameTextBox;
        this.exceptionsTextBox = exceptionsTextBox;
        this.switchList = switchList;
    }

    public void run() throws AWTException, IOException, NativeHookException, InterruptedException, TesseractException {
        initSwitches();
        initDetection();

        System.out.println(imageParser.getAverageRGBNoBackground("name", 15));
        System.out.println(imageParser.getAverageRGBNoBackground("type", 15));
        System.out.println(parsedPokemonName + "\nclose enough: "
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
        String string = imageParser.readImageText(name);
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

    private void attack(boolean attack1Toggle, boolean attack2Toggle, boolean attack3Toggle, boolean attack4Toggle) throws TesseractException {
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
        parsedPokemonName = getImageText(370, 140, 500, 60, "name").toLowerCase();
        screenCapture.captureImage(362, 245, 50, 50, "type");
        isWhite = imageParser.matchImageColor("name", new Color(200, 200, 200));
        isSkin = imageParser.matchImageColor("name", new Color(120, 50, 60));
        isNormal = imageParser.matchImageColor("type", new Color(0, 0, 0));
        isCaptured = imageParser.matchImageColor("type", new Color(130, 150, 115));
        isMisprint = imageParser.matchImageColor("type", new Color(65, 65, 25));
        isUnique = imageParser.matchImageColor("type", new Color(60, 80, 80));
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
}

