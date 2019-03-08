import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class View extends JPanel {

    /** 初期ウィンドウサイズ（高さ） */
	public static final int HEIGHT = 512 + 1;
    /** 初期ウィンドウサイズ（幅） */
    public static final int WIDTH = 640;

    private Model model;

    /**
     * 画面描画をになう View オブジェクトを生成する
     * @param m モデル
     */
    public View(Model m) {
        // モデルを登録
    	model = m;
        // ビューのサイズを設定
    	setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    /**
     * 画面を描画する
     * @param g  描画用のグラフィックスオブジェクト
     */
    @Override
    public void paint(Graphics g) {
        // 画面をいったんクリア
        clear(g);
        // 画面を描画
        model.getState().paint(g);
    }

    /**
     * 画面を黒色でクリア
     * @param g  描画用のグラフィックスオブジェクト
     */
    public void clear(Graphics g) {
        Dimension size = getSize();
        g.setColor(new Color(0.3f, 0.3f, 0.3f));
        g.fillRect(0, 0, size.width, size.height);
    }

}
