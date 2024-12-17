import java.awt.MouseInfo;
import java.awt.Point;

public class MouseCoordinates {
    public static void main(String[] args) {
        while (true) {
            try {
                // Get the current mouse position
                Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                int x = (int) mousePosition.getX();
                int y = (int) mousePosition.getY();

                // Print the coordinates
                System.out.println("Mouse Position: X=" + x + ", Y=" + y);

                // Pause for a short duration to avoid flooding the console
                Thread.sleep(100); // 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}