package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton implements RelativeComponent {
	
	private int intervalIndex = -1;
	
    private Color currentBg = Colors.BTN_DEFAULT;
    private Color normalBg = Colors.BTN_DEFAULT;
    private Color hoverBg = Colors.BTN_HOVER;
    private Color borderColor = Colors.BTN_BORDER;

    public StyledButton(String text) {
        super(text);
        setFontSize(15);
        setForeground(Colors.TEXT_LIGHT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentBg.equals(normalBg)) {
                    currentBg = hoverBg;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (currentBg.equals(hoverBg)) {
                    currentBg = normalBg;
                    repaint();
                }
            }
        });
    }
    
    
    
    public int getIntervalIndex() {
		return intervalIndex;
	}



	public void setIntervalIndex(int intervalIndex) {
		this.intervalIndex = intervalIndex;
	}



	public void disableHoverPainting() {
    	hoverBg = normalBg;
    }
    
    public void setFontSize(int fontSize) {
    	setFont(new Font("SansSerif", Font.BOLD, fontSize));
    }

    // --- Useful Styling API ---

    /** Sets standard background color override (e.g., Action Blue for Primary buttons) */
    public void setPrimaryColor(Color bg) {
        this.normalBg = bg;
        this.hoverBg = bg.brighter();
        this.currentBg = bg;
        repaint();
    }

    /** Flash correct green */
    public void markCorrect() {
        this.currentBg = Colors.CORRECT_GREEN;
        repaint();
    }
    
    public boolean markedCorrect() {
    	return this.currentBg == Colors.CORRECT_GREEN;
    }

    /** Flash wrong red */
    public void markWrong() {
        this.currentBg = Colors.WRONG_RED;
        repaint();
    }

    /** Reset to standard colors */
    public void resetStyle() {
        this.currentBg = normalBg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw background
        g2.setColor(currentBg);
        g2.fillRoundRect(0, 0, w, h, 12, 12);

        // Draw outline border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);

        g2.dispose();

        super.paintComponent(g);
    }

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}
}