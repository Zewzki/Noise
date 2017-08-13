import java.awt.Color;

import javax.swing.JSlider;


public class nSlide extends JSlider{
	
	private Color transparent = new Color(0, 0, 0, 0);
	private Color fontColor = new Color(0, 18, 255);
	
	public nSlide(int orientation, int min, int max, int start) {
		
		super(orientation, min, max, start);
		
		//setMajorTickSpacing(15);
		setPaintTicks(true);
		setPaintLabels(true);
		setBackground(transparent);
		setForeground(fontColor);
		
	}
}
