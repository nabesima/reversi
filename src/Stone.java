import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public enum Stone {
	NONE, WHITE, BLACK;    // 石の色

	private BufferedImage image;
	private BufferedImage icon;

	// 石の色を反転させる
	public Stone toggle() {
		switch (this) {
		case BLACK: return WHITE;
		case WHITE: return BLACK;
		case NONE:  assert(false);
		}
		return null;
	}

	public boolean isBlack() { return BLACK.equals(this); }
	public boolean isWhite() { return WHITE.equals(this); }
	public boolean isEmpty() { return NONE.equals(this); }

	// 石を描画する
	public void paint(Graphics g, int x, int y, int size) {
		if (image == null)
			image = makeImage(size);
		g.drawImage(image, x, y, null);
	}

	private BufferedImage makeImage(int size) {
		image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = image.createGraphics();
		paint(g2, size, true);
		g2.dispose();
		return image;
	}

	// 現在の石数を表すためのアイコンを描画
	public void paintIcon(Graphics g, int x, int y, int size) {
		if (icon == null) {
			icon = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = icon.createGraphics();
			paint(g2, size, false);
			g2.dispose();
		}
		g.drawImage(icon, x, y, null);
	}

	private void paint(Graphics2D g, int size, boolean shadow) {
		if (NONE.equals(this)) return;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Retains the previous state
        Paint  oldPaint  = g.getPaint();
        Stroke oldStroke = g.getStroke();

        Color face = WHITE.equals(this) ? Color.WHITE : Color.BLACK;
        Color back = WHITE.equals(this) ? Color.BLACK : Color.WHITE;

        size -= 10;
        int x = 5;
        int y = 3;
        int height = (int)(size * 0.95);

        // 影を描画
        if (shadow) {
	        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
	        g.fillOval(x - 2, y + 2, size + 4, height + 4);
        }
        // 背面を描画
        g.setColor(back);
        g.fillOval(x, y + size - height, size, height);
        // 前面を描画
        g.setColor(face);
        g.fillOval(x, y, size, height);

        // 前面の下部にハイライトを描画
        g.setPaint(new GradientPaint(x, y + (int)(height * 0.7), new Color(1.0f, 1.0f, 1.0f, 0.0f),
           	                          x, y + (int)(height * 0.9), new Color(0.6f, 0.6f, 0.6f, 0.8f)));
        g.setStroke(new BasicStroke(2));
        g.drawOval(x, y, size, (int)(height * 0.9));

        // 前面中央にハイライトを描画
        AffineTransform a = AffineTransform.getTranslateInstance(x,  y + size / 16);
        a.scale(1.0, 0.7);
        g.setPaint(
        		new RadialGradientPaint(
        				new Point2D.Double(size / 2.0, size / 2.0),        // center
        				size * 0.7f,                                               // radius
        				new Point2D.Double(size / 2.0, size / 2.0),        // focus
        				new float[] { 0.0f, 0.8f },                                // fractions
        				new Color[] { new Color(0.8f, 0.8f, 0.8f, 0.2f),           // colors
        					          new Color(0.8f, 0.8f, 0.8f, 0.0f) },
        			    RadialGradientPaint.CycleMethod.NO_CYCLE,
        			    RadialGradientPaint.ColorSpaceType.SRGB,
        			    a));
        g.fillOval(x, y, size, (int)(size * 0.9));

        // Restores the previous state
        g.setPaint(oldPaint);
        g.setStroke(oldStroke);
	}

	public static void main(String[] args) {
		try {
			BufferedImage black = Stone.BLACK.makeImage(64);
			ImageIO.write(black, "png", new File("black-stone.png"));
			BufferedImage white = Stone.WHITE.makeImage(64);
			ImageIO.write(white, "png", new File("white-stone.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
