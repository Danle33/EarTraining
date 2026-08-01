package utils;

import java.awt.Component;

public interface RelativeComponent {
	// Force the component to provide a reference to itself
    Component getComponent();

    default void setRelativeProperties(double xPct, double yPct, double wPct, double hPct) {
        PercentBounds.setRelative(getComponent(), xPct, yPct, wPct, hPct);
    }
    
    default void setAbsoluteProperties(double x, double y, double w, double h) {
        PercentBounds.setAbsolute(getComponent(), x, y, w, h);
    }
}
