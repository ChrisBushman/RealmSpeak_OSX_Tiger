package com.robin.general.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;

public class VerticalLabelUI extends BasicLabelUI {

	static {
		labelUI = new VerticalLabelUI(false);
	}

	protected boolean clockwise;

	public VerticalLabelUI(boolean clockwise) {
		super();
		this.clockwise = clockwise;
	}

	public Dimension getPreferredSize(JComponent c) {
		Dimension dim = super.getPreferredSize(c);
		return new Dimension(dim.height, dim.width);
	}

	private static Rectangle paintIconR = new Rectangle();
	private static Rectangle paintTextR = new Rectangle();
	private static Rectangle paintViewR = new Rectangle();
	private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

	public void paint(Graphics g, JComponent c) {
		JLabel label = (JLabel) c;
		String text = label.getText();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

		if ((icon == null) && (text == null)) {
			return;
		}

		int cw = c.getWidth();
		int ch = c.getHeight();
		if (cw <= 0 || ch <= 0) return;

		// Render text horizontally into an offscreen image with dimensions swapped.
		// Drawing text under an AffineTransform on the component Graphics is unreliable
		// on some JVMs (e.g. SGI IRIX Java 1.4 X11 pipeline); rendering to a BufferedImage
		// first and then rotating the image avoids this.
		BufferedImage offscreen = new BufferedImage(ch, cw, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2off = offscreen.createGraphics();
		g2off.setFont(c.getFont());
		FontMetrics fm = g2off.getFontMetrics();

		paintViewInsets = c.getInsets(paintViewInsets);
		paintViewR.x = paintViewInsets.left;
		paintViewR.y = paintViewInsets.top;
		paintViewR.height = cw - (paintViewInsets.left + paintViewInsets.right);
		paintViewR.width = ch - (paintViewInsets.top + paintViewInsets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

		String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

		if (icon != null) {
			icon.paintIcon(c, g2off, paintIconR.x, paintIconR.y);
		}
		if (clippedText != null) {
			int textX = paintTextR.x;
			int textY = paintTextR.y + fm.getAscent();
			if (label.isEnabled()) {
				paintEnabledText(label, g2off, clippedText, textX, textY);
			} else {
				paintDisabledText(label, g2off, clippedText, textX, textY);
			}
		}
		g2off.dispose();

		// Rotate the offscreen image and blit it onto the component.
		// drawImage under a transform is reliable even where drawString is not.
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform saved = g2.getTransform();
		if (clockwise) {
			g2.rotate(Math.PI / 2);
			g2.translate(0, -cw);
		} else {
			g2.rotate(-Math.PI / 2);
			g2.translate(-ch, 0);
		}
		g2.drawImage(offscreen, 0, 0, null);
		g2.setTransform(saved);
	}

}