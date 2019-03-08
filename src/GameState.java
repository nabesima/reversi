import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


/**
 * ゲームの状態を表す抽象クラス
 */
public abstract class GameState {

	/**
	 * 状態に応じたイベント処理関数
	 * @param move  ユーザが指した手を表すオブジェクト．一定時間の経過を表すイベントの場合は null
	 * @return 次の状態を表すオブジェクト
	 */
	public abstract GameState process(Move move);

	/**
	 *  状態に応じた画面描画関数
	 * @param g  描画先のグラフィックスオブジェクト
	 */
	public abstract void paint(Graphics g);

	/**
	 *  盤面を描画するための関数
	 */
	public static void paint(Graphics g, Board board, Move prevMove, Color prevColor) {
		// アンチエイリアスを有効にする
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 盤面を描画
        board.paint(g, prevMove, prevColor);

        // 石の数を描画
        int sx = board.getBoardWidth() + 10;
    	g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

    	Stone.WHITE.paintIcon(g, sx + 6, 26, 30);
    	Stone.BLACK.paintIcon(g, sx + 6, 62, 30);
    	g.drawString("" + board.getNumStones(Stone.WHITE), sx + 40, 45);
    	g.drawString("" + board.getNumStones(Stone.BLACK), sx + 40, 81);

		// 先読みした盤面の数を描画
        g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Lookahead", sx, 270);
		g.drawString("" + Computer.getNumStates(), sx, 300);
		g.drawString("Max", sx, 340);
		g.drawString("" + Computer.getMaxNumStates(), sx, 370);
		g.drawString("Avg time", sx, 410);
		g.drawString("" + String.format("%.2f", Computer.getAvgCpuTime()), sx, 440);
	}

}
