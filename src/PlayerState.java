import java.awt.Color;
import java.awt.Graphics;

public class PlayerState extends PlayState {

	/**
	 * 人間プレイヤーの手番状態を表すオブジェクト
	 * @param board    盤面
	 * @param prevMove １つ前の手
	 */
	public PlayerState(Board board, Move prevMove) {
		super(board, prevMove);
	}

	/**
	 * イベント処理関数
	 */
	@Override
	public GameState process(Move move) {
		elapsed++;
		// 一定時間が経過したことを表すイベントなら無視
		if (move == null)
			return this;
		// 正しい座標でない場合，または白石を打つことができない場合は無視
		if (!board.isOK(move))
			return this;

		// 指定位置に白石を打つ
		board = board.put(move);

		if (board.isSkip()) {
			// もしコンピュータがおけないならば人間の手番に
			board = board.skip();
			// 人間もおけないならば試合終了
			if (board.isSkip())
				return new ResultState(board);
			// 人間の手番
			return new PlayerState(board, null);
		}

		// コンピュータの手番
		return new ComputerState(board, null);
	}

	/**
	 * 画面の描画関数
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (moves == null)
			return;

		// 人間が打つことができるマスを薄緑で描画
		g.setColor(new Color(0, 255, 0, 100));
		for (Move move : moves) {
			int sx = move.x * Board.UNIT;
			int sy = move.y * Board.UNIT;
			g.fillRect(sx, sy, Board.UNIT, Board.UNIT);
		}
	}

}
