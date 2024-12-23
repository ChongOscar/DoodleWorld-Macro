import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Macro {
    private Robot robot;
    private int frameWidth;
    private int frameHeight;
    private int startX;
    private int startY;
    private double screenScale;

    public Macro(int frameWidth, int frameHeight, int startX, int startY, double screenScale) throws AWTException {
        robot = new Robot();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.startX = startX;
        this.startY = startY;
        this.screenScale = screenScale;
    }

    public void attack(int num) {
        switch (num) {
            case 1 -> {
                robot.mouseMove(relativeXPos(0.12368), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(relativeXPos(0.1237), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 2 -> {
                robot.mouseMove(relativeXPos(0.378947), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(relativeXPos(0.379), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 3 -> {
                robot.mouseMove(relativeXPos(0.615789), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(relativeXPos(0.6158), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 4 -> {
                robot.mouseMove(relativeXPos(0.878947), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(relativeXPos(0.879), relativeYPos(0.80102));
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
        }
    }

    public void fight() {
        robot.mouseMove(relativeXPos(0.123684), relativeYPos(0.9387755));
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.mouseMove(relativeXPos(0.1237), relativeYPos(0.9387755));
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
    }

    public void runAway() {
        robot.mouseMove(relativeXPos(0.878947), relativeYPos(0.9387755));
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.mouseMove(relativeXPos(0.879), relativeYPos(0.9387755));
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
    }

    private int relativeXPos(double pos) {
        return (int) ((startX + (pos * frameWidth)) / screenScale);
    }
    private int relativeYPos(double pos) {
        return (int) ((startY + (pos * frameHeight)) / screenScale);
    }
}
