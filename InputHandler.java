import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseMotionListener, KeyListener, FocusListener{

    private boolean[] keys;

    public InputHandler() {

        keys = new boolean[65536];

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        for(int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public boolean getToggleScreen() {
        boolean t = keys[KeyEvent.VK_F12];
        keys[KeyEvent.VK_F12] = false;
        return t;

    }

}


