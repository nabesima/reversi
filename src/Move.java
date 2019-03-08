public class Move {

	public Stone stone;
	public int x;
	public int y;

	/**
	 * １手を表すオブジェクトを先制する
	 * @param stone  石の色
	 * @param x      X 座標
	 * @param y      Y 座標
	 */
	public Move(Stone stone, int x, int y) {
		this.x = x;
		this.y = y;
		this.stone = stone;
	}

	@Override
	public String toString() {
		return "(" + (char)('a' + x) + "," + (y + 1) + ")";
	}
}
