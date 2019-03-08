import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


public abstract class PlayState extends GameState {

	protected Board      board;       // 盤面
	protected List<Move> moves;       // 現在指すことが可能な手のリスト
	protected Move       prevMove;    // １つ前に指した手
	protected int        elapsed;     // 経過時間

	/**
	 * ゲーム中の状態を表すオブジェクトを生成する
	 * @param board     現在の盤面オブジェクト
	 * @param prevMove  １つ前に指した手．存在しない場合は null
	 */
	public PlayState(Board board, Move prevMove) {
		this.board    = board;
		this.moves    = board.getPossibleMoves();
		this.prevMove = prevMove;
		this.elapsed  = 0;
	}

	/**
	 * ゲーム中の状態を描画する
	 */
	@Override
	public void paint(Graphics g) {

		// １つ前の手の描画色
		Color prevColor = new Color(1.0f, 0.25f, 0.25f, 1.0f / (0.2f * elapsed + 1));

		// 盤面を描画
		GameState.paint(g, board, prevMove, prevColor);

        // 手番を描画
        int sx = board.getBoardWidth() + 10;
        g.drawString("Next", sx, 144);
        if (board.isTurn(Stone.WHITE)) {
        	Stone.WHITE.paintIcon(g, sx + 6, 160, 30);
        	g.drawString("YOU", sx + 40, 179);
        }
        else {
        	Stone.BLACK.paintIcon(g, sx + 6, 160, 30);
        	g.drawString("COM", sx + 40, 179);
        }
	}

}
