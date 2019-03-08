import java.awt.Color;
import java.awt.Graphics;


public class ComputerState extends PlayState {

	private Computer computer;
	private boolean  thinking = false;

	/**
	 * コンピュータの手番の状態を表すオブジェクトを生成
	 * @param board    盤面
	 * @param prevMove １つ前の手番
	 */
	public ComputerState(Board board, Move prevMove) {
		super(board, prevMove);
		computer = new Computer(board);
	}

	/**
	 * イベント処理関数
	 */
	@Override
	public GameState process(Move dmy) {
		elapsed++;
		// 人がマウスでクリックしたイベントは無視
		if (dmy != null)
			return this;

		// まだ思考中でなければ，思考用のスレッドを新たに立ち上げる
		if (!thinking) {
			thinking = true;
			new Thread(computer).start();
		}

		// 0.5 秒はコンピュータの手候補を表示するため待つ
		if (elapsed < 5)
			return this;

		// コンピュータの手を取得
		Move move = computer.getSelectedMove();
		if (move == null)
			return this;    // 思考中なら待つ

		// 選択した場所に黒石を置く
		assert(board.isOK(move));
		board = board.put(move);

		// もし人間がおけないならばコンピュータの手番に
		if (board.isSkip()) {
			board = board.skip();
			// コンピュータもおけないならば試合終了
			if (board.isSkip())
				return new ResultState(board);
			// コンピュータの手番
			return new ComputerState(board, move);
		}

		// 人間の手番
		return new PlayerState(board, move);
	}

	/**
	 * 画面描画関数
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (moves == null)
			return;

		// コンピュータが打つことができる手を薄赤色で描画
		for (Move move : moves) {
			int sx = move.x * Board.UNIT;
			int sy = move.y * Board.UNIT;
			g.setColor(new Color(255, 64, 64, 200));
			g.fillRect(sx, sy, Board.UNIT, Board.UNIT);
			g.setColor(Color.WHITE);
			//g.drawString("r=" + board.getNumReversedStones(move), sx, sy + 24);
			//g.drawString("m=" + board.put(move).getNumPossibleMoves(),  sx, sy + 48);
		}
	}
}
