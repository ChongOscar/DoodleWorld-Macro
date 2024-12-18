import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.Objects;

// Hotkey listener to stop the robot
public class HotkeyListener implements NativeKeyListener {

    Runner runner;
    public HotkeyListener(Runner runner) {
        this.runner = runner;
    }
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
            runner.toggleRun();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // No action on key release
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No action on key typed
    }
}
