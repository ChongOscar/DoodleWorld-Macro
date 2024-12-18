import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Macro {
    private Robot robot;

    public Macro() throws AWTException {
        robot = new Robot();
    }

    public void attack(int num) {
        robot.mouseMove(565, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.delay(500);
        if (num == 1) {
            robot.mouseMove(565, 875);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        } else if (num == 2) {
            robot.mouseMove(1050, 875);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        } else if (num == 3) {
            robot.mouseMove(1500, 875);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        } else if (num == 4) {
            robot.mouseMove(2000, 875);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        }
    }

    public void run() {
        robot.mouseMove(2000, 1010);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
        robot.mouseMove(2000, 1000);
    }
}
