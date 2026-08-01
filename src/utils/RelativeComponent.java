package utils;

import java.awt.Component;

public interface RelativeComponent {
	// Force the component to provide a reference to itself
    Component getComponent();

    /** Default method shared by any class implementing this interface */
    default void setRelativeProperties(double xPct, double yPct, double wPct, double hPct) {
        PercentBounds.set(getComponent(), xPct, yPct, wPct, hPct);
    }
}
