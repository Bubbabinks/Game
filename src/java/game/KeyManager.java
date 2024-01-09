package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

public class KeyManager implements KeyListener {

    private static HashSet<Integer> keysPressed = new HashSet<Integer>();
    private static ArrayList<KeyListener> keyListeners = new ArrayList<KeyListener>();

    public static void addKeyListener(KeyListener keyListener) {
        keyListeners.add(keyListener);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        for (var kl: keyListeners) {
            kl.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
        for (var kl: keyListeners) {
            kl.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
        for (var kl: keyListeners) {
            kl.keyReleased(e);
        }
    }

    public static boolean input(Integer keycode) {
        return keysPressed.contains(keycode);
    }
}
