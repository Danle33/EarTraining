package utils;

import javax.swing.*;
import java.awt.*;

public class StyledLabel extends JLabel implements RelativeComponent {

    public StyledLabel(String text, int fontSize, boolean bold) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, fontSize));
        setForeground(Colors.TEXT_LIGHT);
    }

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}

}