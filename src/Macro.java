import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Macro {
    private Robot robot;

    public Macro() throws AWTException {
        robot = new Robot();
    }

    public void attack(int num) {
        robot.delay(500);
        switch (num) {
            case 1 -> {
                robot.mouseMove(565, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(560, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 2 -> {
                robot.mouseMove(1050, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(1045, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 3 -> {
                robot.mouseMove(1500, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(1505, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
            case 4 -> {
                robot.mouseMove(2000, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
                robot.mouseMove(2005, 875);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
            }
        }
    }

    public void fight() {
        robot.mouseMove(565, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.mouseMove(570, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
    }

    public void runAway() {
        robot.mouseMove(2000, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.mouseMove(2005, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
    }
}
