import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalMousePosition {
    public static void main(String[] args) {
        try {
            // Disable JNativeHook logging
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            // Register the global mouse listener
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(new NativeMouseListener() {
                @Override
                public void nativeMouseClicked(NativeMouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("Mouse clicked at: " + x + ", " + y);
                }

                @Override
                public void nativeMousePressed(NativeMouseEvent e) {}

                @Override
                public void nativeMouseReleased(NativeMouseEvent e) {}
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}