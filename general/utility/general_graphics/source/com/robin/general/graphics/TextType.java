package com.robin.general.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class TextType {
	
	public static final class Alignment {
		private final String _name;
		private final int _ordinal;
		private Alignment(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final Alignment Left = new Alignment("Left", 0);
		public static final Alignment Center = new Alignment("Center", 1);
		public static final Alignment Right = new Alignment("Right", 2);

		private static final Alignment[] _VALUES = { Left, Center, Right };
		public static Alignment[] values() { Alignment[] r = new Alignment[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static Alignment valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	protected static final Font defaultFont = new Font("Dialog",Font.PLAIN,11);
	protected static final Color defaultColor = Color.black;

	protected static Hashtable typeFonts = null;
	protected static Hashtable typeColors = null;
	
	/**
	 * Adds a type to the TextType palette
	 */
	public static void addType(String typeName,Font font,Color color) {
		if (font!=null) {
			if (typeFonts==null) {
				typeFonts = new Hashtable();
			}
			typeFonts.put(typeName,font);
		}
		if (color!=null) {
			if (typeColors==null) {
				typeColors = new Hashtable();
			}
			typeColors.put(typeName,color);
		}
	}

	private String text;
	private String[] line = null;
	private String type;
	private int width;
	
	private int rotate = 0;
	private String delims = " ";
	private String space = " ";
	
	public TextType(String inText,int width,String type) {
		text = inText;
		this.width = width;
		this.type = type;
	}
	public void setRotate(int val) {
		rotate = val;
	}
	
	public Font getFont() {
		Font font = null;
		if (typeFonts!=null) {
			font = (Font) typeFonts.get(type);
		}
		if (font==null) {
			font = defaultFont;
		}
		return font;
	}
	
	public String getText() {
		return text;
	}
	
	public Color getColor() {
		Color color = null;
		if (typeColors!=null) {
			color = (Color) typeColors.get(type);
		}
		if (color==null) {
			color = defaultColor;
		}
		return color;
	}
	
	private int getMaxWidth(Graphics screen) {
		screen.setFont(getFont());
		FontMetrics metrics = screen.getFontMetrics();
		int max = 0;
		for (int i=0;i<line.length;i++) {
			int len = metrics.stringWidth(line[i]);
			if (len>max) {
				max = len;
			}
		}
		return max;
	}
	
	private void updateSpacing(Graphics screen) {
		if (line==null) {
			screen.setFont(getFont());
			FontMetrics metrics = screen.getFontMetrics();
			
			// Break up all words
			StringTokenizer tokens = new StringTokenizer(text,delims);
			String[] word = new String[tokens.countTokens()];
			int[] wordWidth = new int[tokens.countTokens()];
			for (int i=0;i<word.length;i++) {
				word[i] = tokens.nextToken();
				wordWidth[i] = metrics.stringWidth(word[i]);
			}
			int spaceWidth = metrics.stringWidth(space);
			
			// Build lines
			int currentWidth = 0;
			StringBuffer sb = new StringBuffer();
			ArrayList lines = new ArrayList();
			for (int i=0;i<word.length;i++) {
				int newWidth = currentWidth+wordWidth[i];
				
				if (sb.length()>0 && (newWidth+spaceWidth)>width) {
					lines.add(sb.toString());
					sb = new StringBuffer();
					currentWidth = 0;
					newWidth = wordWidth[i];
				}
				if (sb.length()>0) {
					sb.append(space);
					newWidth += spaceWidth;
				}
				sb.append(word[i]);
				currentWidth = newWidth;
			}
			if (sb.length()>0) {
				lines.add(sb.toString());
			}
			
			line = (String[]) lines.toArray(new String[lines.size()]);
		}
	}
	
	public int getHeight(Graphics screen) {
		updateSpacing(screen);
		screen.setFont(getFont());
		return screen.getFontMetrics().getAscent() * line.length;
	}
	public int getLineHeight(Graphics screen) {
		screen.setFont(getFont());
		return screen.getFontMetrics().getAscent();
	}
	
	public int getWidth(Graphics screen) {
		updateSpacing(screen);
		return getMaxWidth(screen);
	}
	
	public void draw(Graphics screen,int x,int y) {
		draw(screen,x,y,Alignment.Center,getColor());
	}
	
	public void draw(Graphics screen,int x,int y,Color override) {
		draw(screen,x,y,Alignment.Center,override);
	}
	
	public void draw(Graphics screen,int x,int y,Alignment alignment) {
		draw(screen,x,y,alignment,getColor());
	}
	
	public void draw(Graphics screen,int x,int y,Alignment alignment,Color override) {
		if (text!=null) {
			updateSpacing(screen);
			screen.setColor(override);
			screen.setFont(getFont());
			int lineHeight = getLineHeight(screen);
			int ypos = lineHeight;
			for (int i=0;i<line.length;i++) {
				drawText(screen,line[i],x,y+ypos,width,rotate,alignment);
				ypos += lineHeight;
			}
		}
	}
	
	public static void drawText(Graphics screen,String text,int x,int y,int width,int rotate,Alignment alignment) {
		Graphics2D g = (Graphics2D)screen;
		AffineTransform orig = null;
		if (rotate!=0) {
			orig = g.getTransform();
			AffineTransform rotated = new AffineTransform(orig);
			rotated.rotate(Math.toRadians(rotate),x,y);
			g.setTransform(rotated);
		}
		int offset = 0;
		if (alignment == Alignment.Center) {
			offset = (width - screen.getFontMetrics().stringWidth(text))>>1;
		}
		else if (alignment == Alignment.Right) {
			offset = width - screen.getFontMetrics().stringWidth(text);
		}
		g.drawString(text,x+offset,y);
		if (orig!=null) {
			g.setTransform(orig);
		}
	}
	public void setDelims(String delims) {
		this.delims = delims;
	}
	public void setSpace(String space) {
		this.space = space;
	}
}