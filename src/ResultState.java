import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class ResultState extends GameState {

	protected Board board;    // ゲーム終了時の盤面

	/**
	 * ゲーム終了状態を表すオブジェクト
	 * @param board  ゲーム終了時の盤面
	 */
	public ResultState(Board board) {
		this.board = board;
		// コンソールに石数を出力
        int white = board.getNumStones(Stone.WHITE);
        int black = board.getNumStones(Stone.BLACK);
		System.out.println("W" + white + ",B" + black);
	}

	/**
	 * イベント処理関数
	 */
	@Override
	public GameState process(Move move) {
		// クリックイベントがあれば最初の画面に戻る
		if (move != null) {
			Computer.clearMaxNumStates();
			return new PlayerState(new Board(Stone.WHITE), null);
		}
		return this;
	}

	/**
	 * ゲームの終了画面を描画
	 */
	@Override
	public void paint(Graphics g) {
        // 盤面を描画
		GameState.paint(g, board, null, null);

    	g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 80));

        int white = board.getNumStones(Stone.WHITE);
        int black = board.getNumStones(Stone.BLACK);

        if (white > black)
        	g.drawString("YOU WIN!", 125, 280);
        else if (black > white)
        	g.drawString("COM WIN!", 125, 280);
        else
        	g.drawString("DRAW!", 125, 280);
	}

}
