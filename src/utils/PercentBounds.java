package utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import javax.swing.SwingUtilities;

public class PercentBounds {

    /**
     * Sets centered properties of a component relative to its parent container's dimensions.
     * @param comp Target UI component
     * @param xPct Left position (0.0 to 100.0)
     * @param yPct Top position (0.0 to 100.0)
     * @param wPct Width (0.0 to 100.0)
     * @param hPct Height (0.0 to 100.0)
     */
    public static void set(Component comp, double xPct, double yPct, double wPct, double hPct) {
    	Container parent = comp.getParent();
        
        int pW = (parent != null) ? parent.getWidth() : 0;
        int pH = (parent != null) ? parent.getHeight() : 0;

        // Fallback to Window size if Panel bounds aren't calculated yet
        if (pW == 0 || pH == 0) {
            Window window = (parent != null) ? SwingUtilities.getWindowAncestor(parent) : null;
            if (window != null) {
                pW = window.getWidth();
                pH = window.getHeight();
            }
        }
        
        int w = (int) Math.round((wPct / 100.0) * pW);
        int h = (int) Math.round((hPct / 100.0) * pH);
        int x = (int) Math.round((xPct / 100.0) * pW - w / 2);
        int y = (int) Math.round((yPct / 100.0) * pH - h / 2);
        

        comp.setBounds(x, y, w, h);
    }
}