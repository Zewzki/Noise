import javax.swing.*;
import java.awt.*;

/**
 * Created by Michael on 6/30/2017.
 */
public class nButton extends JButton {

    private Font font = new Font("Arial Black", Font.PLAIN, 32);
    private Color fontColor = new Color(0, 18, 255);
    private Color transparent = new Color(0, 0, 0, 0);

    public nButton(String name) {
        super(name);

        setFont(font);
        setBackground(transparent);
        setForeground(fontColor);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

    }

}
