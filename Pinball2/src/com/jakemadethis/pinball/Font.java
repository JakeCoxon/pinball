package com.jakemadethis.pinball;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Helper for drawing bitmap fonts
 * Textures need to be 32 columns wide and 4 rows high and each cell must be square
 * If font is instantiated with monospace=false, the class will cut the blank margins
 * from each letter
 * @author Jake
 *
 */
public class Font {
	private final Texture texture;
	
	private static final int ROWS = 4;
	private static final int COLS = 32;
	
	private float inv_w;// = 1;
	private float inv_h;// = 0.03125f;
	private int[] leftMargin = new int[ROWS*COLS];
	private int[] rightMargin = new int[ROWS*COLS];
	
	public enum Alignment {LEFT, RIGHT, CENTER}

	private int w;
	private int h;

	private float letterSpacing = 5;

	public Font(Texture texture) {
		this(texture, false);
	}
	public Font(Texture texture, boolean monospace) {
		this.texture = texture;
		w = h = texture.getWidth() / COLS;
		inv_w = (float)w / texture.getWidth();
		inv_h = (float)h / texture.getHeight();
		
		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();

		for (int i=0; i < COLS; i++) {
			for (int j=0; j < ROWS; j++) {
				leftMargin[j*COLS+i] = monospace ? 0 : getLeftMargin(pixmap, i*w, j*h);
				rightMargin[j*COLS+i] = monospace ? w : getRightMargin(pixmap, i*w, j*h);
			}
		}
	}
	
	/**
	 * Sets the amount of pixels in between each letter. Default is 5px
	 * @param letterSpacing
	 */
	public void setLetterSpacing(float letterSpacing) {
		this.letterSpacing = letterSpacing;
	}
	public float getLetterSpacing() {
		return letterSpacing;
	}
	
	private int getLeftMargin(Pixmap pixmap, int x, int y) {
		int i;
		for (i = 0; i < w; i++) {
			if (!isColClear(pixmap, x+i, y))
				return i-1;
		}
		return i;
	}
	private int getRightMargin(Pixmap pixmap, int x, int y) {
		int i;
		for (i = w-1; i >= 0; i--) {
			if (!isColClear(pixmap, x+i, y))
				return i+1;
		}
		return 0;
	}
	private boolean isColClear(Pixmap pixmap, int col, int initialY) {
		for (int y = initialY; y < initialY+h; y++) {
			int pixel = pixmap.getPixel(col, y);
			boolean hasPixel = (pixel & 0x0000ff) > 0;//pixel != 0x000000ff; //
			if (hasPixel) return false;
		}
		return true;
	}
	
	/**
	 * Gets the width in pixels of this string
	 * @param str The text
	 * @param size The font size
	 * @return
	 */
	public float stringWidth(String str, float size) {
		float width = 0;
		for (int i = 0; i < str.length(); i++) {
			width += charWidth(str.charAt(i), size);
		}
		return width;
	}

	/**
	 * Gets the width in pixels of this character
	 * @param c
	 * @param size
	 * @return
	 */
	public float charWidth(char c, float size) {
		if (c == ' ') return w/2;
		return (float)(rightMargin[c] - leftMargin[c]) / this.w * size;
	}

	public void drawString(SpriteBatch batch, String str, float x, float y, float size) {
		drawString(batch, str, x, y, size, Alignment.LEFT);
	}
	
	public void drawString(SpriteBatch batch, String str, float x, float y, float size, Alignment align) {
		if (align == Alignment.RIGHT) {
			drawStringRight(batch, str, x, y, size);
			return;
		}
		else if (align == Alignment.CENTER) {
			// Find the size of the whole string
			// TODO: Fix this - its not in the middle
			x -= stringWidth(str, size)/2;
			align = Alignment.LEFT;
		}
		for (int i = 0; i < str.length(); i++) {
			float charWidth = drawLetter(batch, str.charAt(i), x, y, size, align);
			x += charWidth;
				
		}
	}
	private void drawStringRight(SpriteBatch batch, String str, float x, float y, float size) {
		for (int i = str.length()-1; i >= 0; i--) {
			float charWidth = drawLetter(batch, str.charAt(i), x, y, size, Alignment.RIGHT);
			x -= charWidth;
		}
	}
	
	/**
	 * Draw a single character aligned to the left
	 * @param batch the SpriteBatch to use
	 * @param c The character
	 * @param x X position of the character, at the left
	 * @param y Y position of the character, at the top
	 * @param size The height of the character
	 * @return The width of the character
	 */
	public float drawLetter(SpriteBatch batch, char c, float x, float y, float size) {
		return drawLetter(batch, c, x, y, size, Alignment.LEFT);
	}
	
	/**
	 * Draws a single character
	 * @param batch the SpriteBatch to use
	 * @param c The character
	 * @param x X position of the character, at the left
	 * @param y Y position of the character, at the top
	 * @param size The height of the character
	 * @param align The alignment of the character
	 * @return The width of the character
	 */
	public float drawLetter(SpriteBatch batch, char c, float x, float y, float size, Alignment align) {
		if (c == ' ') return charWidth(c, size);
		
		int num_x = c % COLS;
		int num_y = c / COLS;

		float u = num_x * inv_w;
		float u2 = (num_x+1) * inv_w;
		
		float v = num_y * inv_h;
		float v2 = (num_y+1) * inv_h;

		int num = c;
		float l = (float) leftMargin[num] / texture.getWidth();
		float r = (float) rightMargin[num] / texture.getWidth();
		u2 = u + r;
		u = u + l;
		
		float letterW = charWidth(c, size);
		
		float offset = 0;
		if (align == Alignment.RIGHT ) offset = letterW;
		else if (align == Alignment.CENTER) offset = letterW/2;
		
		batch.draw(texture, x - offset, y, letterW, size, u, v, u2, v2);
		
		return letterW + letterSpacing;
	}
}
