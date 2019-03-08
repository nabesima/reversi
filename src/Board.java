import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public class Board {

	private Stone stone = Stone.NONE;             // この盤面における次の手番の石の居ろ
	private Stone[][] board = new Stone[8][8];    // 盤面を表す二次元配列
	private boolean[][] fixed = null;             // 確定石かどうかを表すブール配列

	private final static Direction[] dirs       = Direction.makeEightDirections();
	private final static Color       green      = new Color(0, 128, 0);
	private final static Color       lightGreen = new Color(0, 150, 0);

	public final static int UNIT = 64;            // 画面上での石のサイズ

	// 各マスに対する重み
	private final static double weight[][] = {
		{  100, -500, 50, 50, 50, 50, -500,  100 },
		{ -500, -500,  0,  0,  0,  0, -500, -500 },
		{   50,    0, 25, 25, 25, 25,    0,   50 },
		{   50,    0, 25, 25, 25, 25,    0,   50 },
		{   50,    0, 25, 25, 25, 25,    0,   50 },
		{   50,    0, 25, 25, 25, 25,    0,   50 },
		{ -500, -500,  0,  0,  0,  0, -500, -500 },
		{  100, -500, 50, 50, 50, 50, -500,  100 }
	};

	/**
	 * 指定された石色で始まる初期盤面を生成
	 * @param stone  手番の石色
	 */
	public Board(Stone stone) {
		this.stone  = stone;
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++)
				board[x][y] = Stone.NONE;
		board[3][3] = Stone.WHITE;
		board[4][4] = Stone.WHITE;
		board[3][4] = Stone.BLACK;
		board[4][3] = Stone.BLACK;

//		board[0][0] = Stone.NONE;
//		board[1][0] = Stone.NONE;
//		board[2][0] = Stone.WHITE;
//		board[3][0] = Stone.WHITE;
//		board[4][0] = Stone.WHITE;
//		board[5][0] = Stone.WHITE;
//		board[6][0] = Stone.WHITE;
//		board[7][0] = Stone.NONE;
//
//		board[0][1] = Stone.NONE;
//		board[1][1] = Stone.NONE;
//		board[2][1] = Stone.WHITE;
//		board[3][1] = Stone.BLACK;
//		board[4][1] = Stone.BLACK;
//		board[5][1] = Stone.WHITE;
//		board[6][1] = Stone.NONE;
//		board[7][1] = Stone.NONE;
//
//		board[0][2] = Stone.WHITE;
//		board[1][2] = Stone.WHITE;
//		board[2][2] = Stone.BLACK;
//		board[3][2] = Stone.WHITE;
//		board[4][2] = Stone.BLACK;
//		board[5][2] = Stone.WHITE;
//		board[6][2] = Stone.WHITE;
//		board[7][2] = Stone.WHITE;
//
//		board[0][3] = Stone.WHITE;
//		board[1][3] = Stone.WHITE;
//		board[2][3] = Stone.WHITE;
//		board[3][3] = Stone.WHITE;
//		board[4][3] = Stone.BLACK;
//		board[5][3] = Stone.WHITE;
//		board[6][3] = Stone.WHITE;
//		board[7][3] = Stone.NONE;
//
//		board[0][4] = Stone.WHITE;
//		board[1][4] = Stone.WHITE;
//		board[2][4] = Stone.WHITE;
//		board[3][4] = Stone.WHITE;
//		board[4][4] = Stone.BLACK;
//		board[5][4] = Stone.WHITE;
//		board[6][4] = Stone.NONE;
//		board[7][4] = Stone.BLACK;
//
//		board[0][5] = Stone.WHITE;
//		board[1][5] = Stone.WHITE;
//		board[2][5] = Stone.WHITE;
//		board[3][5] = Stone.WHITE;
//		board[4][5] = Stone.BLACK;
//		board[5][5] = Stone.WHITE;
//		board[6][5] = Stone.NONE;
//		board[7][5] = Stone.NONE;
//
//		board[0][6] = Stone.NONE;
//		board[1][6] = Stone.NONE;
//		board[2][6] = Stone.NONE;
//		board[3][6] = Stone.WHITE;
//		board[4][6] = Stone.WHITE;
//		board[5][6] = Stone.WHITE;
//		board[6][6] = Stone.NONE;
//		board[7][6] = Stone.NONE;
//
//		board[0][7] = Stone.NONE;
//		board[1][7] = Stone.NONE;
//		board[2][7] = Stone.NONE;
//		board[3][7] = Stone.WHITE;
//		board[4][7] = Stone.BLACK;
//		board[5][7] = Stone.NONE;
//		board[6][7] = Stone.NONE;
//		board[7][7] = Stone.NONE;
	}

	/**
	 * 引数で指定された盤面のクローンを生成する
	 * @param org  クローン元の盤面
	 */
	private Board(Board org) {
		stone = org.stone;
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++)
				board[x][y] = org.board[x][y];
	}

	public double getWeight() {
		double w = 0.0;
		for (int x=0; x < board.length; x++) {
			for (int y=0; y < board[x].length; y++) {
				if (board[x][y] == Stone.BLACK)
					w += weight[x][y];
				else if (board[x][y] == Stone.WHITE)
					w -= weight[x][y];
			}
		}
		return w;
	}

	/**
	 * 指定の石の数を調べる
	 * @param stone  調べたい石の色
	 * @return 指定の石の数
	 */
	public int getNumStones(Stone stone) {
		int num = 0;
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++)
				if (board[x][y].equals(stone))
					num++;
		return num;
	}

	/**
	 * この盤面において打つことが可能な手のリストを返す
	 * @return この盤面において打つことが可能な手のリスト
	 */
	public List<Move> getPossibleMoves() {
		List<Move> moves = new ArrayList<Move>();
		for (int x=0; x < board.length; x++) {
			for (int y=0; y < board[x].length; y++) {
				Move move = new Move(stone, x, y);
				if (isOK(move))
					moves.add(move);
			}
		}
		return moves;
	}

	/**
	 * この盤面において打つことが可能な手数を返す
	 * @return この盤面において打つことが可能な手数
	 */
	public int getNumPossibleMoves() {
		return getNumPossibleMoves(stone);
	}

	/**
	 * この盤面において打つことが可能な手数を返す
	 * @param stone  調べたい石の色
	 * @return この盤面において打つことが可能な手数
	 */
	public int getNumPossibleMoves(Stone stone) {
		int num = 0;
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++)
				if (isOK(stone, x, y))
					num++;
		return num;
	}

	/**
	 * この盤面の評価値を返す
	 * @return 盤面の評価値
	 */
	public double value() {
		int blackMoves  = getNumPossibleMoves(Stone.BLACK);    // 打つことが可能な黒の石数
		int whiteMoves  = getNumPossibleMoves(Stone.WHITE);    // 打つことが可能な白の石数
		int fixedStones = getNumFixedStones(Stone.BLACK) - getNumFixedStones(Stone.WHITE);  // 確定石数の評価値

		// 盤面の隅を評価
		int corners = 0;                                       // 盤面の隅の評価値
		if (isOK(Stone.BLACK, 0, 0)) corners += 3;
		if (isOK(Stone.BLACK, 0, 7)) corners += 3;
		if (isOK(Stone.BLACK, 7, 0)) corners += 3;
		if (isOK(Stone.BLACK, 7, 7)) corners += 3;
		if (isOK(Stone.WHITE, 0, 0)) corners -= 3;
		if (isOK(Stone.WHITE, 0, 7)) corners -= 3;
		if (isOK(Stone.WHITE, 7, 0)) corners -= 3;
		if (isOK(Stone.WHITE, 7, 7)) corners -= 3;
		if (board[0][0].isEmpty()) {
			if (board[0][1].isBlack() || board[1][0].isBlack() || board[1][1].isBlack())
				corners -= 1;
			if (board[0][1].isWhite() || board[1][0].isWhite() || board[1][1].isWhite())
				corners += 1;
		}
		if (board[0][7].isEmpty()) {
			if (board[0][6].isBlack() || board[1][7].isBlack() || board[1][6].isBlack())
				corners -= 1;
			if (board[0][6].isWhite() || board[1][7].isWhite() || board[1][6].isWhite())
				corners += 1;
		}
		if (board[7][0].isEmpty()) {
			if (board[6][0].isBlack() || board[7][1].isBlack() || board[6][1].isBlack())
				corners -= 1;
			if (board[6][0].isWhite() || board[7][1].isWhite() || board[6][1].isWhite())
				corners += 1;
		}
		if (board[7][7].isEmpty()) {
			if (board[7][6].isBlack() || board[6][7].isBlack() || board[6][6].isBlack())
				corners -= 1;
			if (board[7][6].isWhite() || board[6][7].isWhite() || board[6][6].isWhite())
				corners += 1;
		}

		if (Computer.debug) {
			System.out.println("blackMoves  = " + blackMoves);
			System.out.println("whiteMoves  = " + whiteMoves);
			System.out.println("fixedStones = " + fixedStones);
			System.out.println("corners     = " + corners);
		}

		int score = blackMoves - whiteMoves + 64 * fixedStones + 4 * corners;

		// もし終局であれば，石数の優劣を評価値とする
		if (isFinished()) {
			score = 1000 * (getNumStones(Stone.BLACK) - getNumStones(Stone.WHITE));
			if (Computer.debug) System.out.println("FINISHED = " + (getNumStones(Stone.BLACK) - getNumStones(Stone.WHITE)));
		}

		return score;
	}

	/**
	 * 指定位置に石を打てるか調べる
	 * @param move  調べたい手
	 * @return 指定位置に石を打てる場合 true を返す
	 */
	public boolean isOK(Move move) {
		return isOK(move.stone, move.x, move.y);
	}

	/**
	 * 指定位置に石を打てるか調べる
	 * @param stone  石の色
	 * @param x      石の X 座標
	 * @param y      石の Y 座標
	 * @return 指定位置に石を打てる場合 true を返す
	 */
	public boolean isOK(Stone stone, int x, int y) {
		if (x < 0 || 7 < x || y < 0 || 7 < y)
			return false;
		if (board[x][y] != Stone.NONE)
			return false;
		NEXT_DIR:
		for (Direction d : dirs) {
			int nx  = x + d.x;
			int ny  = y + d.y;
			int num = 0;
			while (0 <= nx && nx <= 7 && 0 <= ny && ny <= 7) {
				if (board[nx][ny] == Stone.NONE)
					continue NEXT_DIR;
				else if (board[nx][ny] == stone.toggle()) {
					nx  += d.x;
					ny  += d.y;
					num += 1;
					continue;
				}
				else if (num == 0)
					break;
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定位置に石を打つ
	 * @param move  石の座標
	 * @return 指定位置に石を打った後の新しい盤面
	 */
	public Board put(Move move) {
		assert(board[move.x][move.y] == Stone.NONE);
		assert(isOK(move));
		Board next = new Board(this);
		next.board[move.x][move.y] = stone;
		NEXT_DIR:
		for (Direction d : dirs) {
			int nx  = move.x + d.x;
			int ny  = move.y + d.y;
			int len = 1;
			while (0 <= nx && nx <= 7 && 0 <= ny && ny <= 7) {
				if (board[nx][ny] == Stone.NONE)
					continue NEXT_DIR;
				else if (board[nx][ny] == stone.toggle()) {
					nx  += d.x;
					ny  += d.y;
					len += 1;
					continue;
				}
				else if (len == 1)
					break;
				nx -= d.x;
				ny -= d.y;
				while (move.x != nx || move.y != ny) {
					assert(board[nx][ny] == stone.toggle());
					next.board[nx][ny] = stone;
					nx -= d.x;
					ny -= d.y;
				}
				break;
			}
		}
		next.stone = next.stone.toggle();
		return next;
	}

	/**
	 * 指定場所に石を打つと反転できる石数を返す
	 * @param move  石を打ちたい場所
	 * @return 反転できる石数
	 */
	public int getNumReversibleStones(Move move) {
		assert(board[move.x][move.y] == Stone.NONE);
		int num = 0;
		NEXT_DIR:
		for (Direction d : dirs) {
			int nx  = move.x + d.x;
			int ny  = move.y + d.y;
			int len = 1;
			while (0 <= nx && nx <= 7 && 0 <= ny && ny <= 7) {
				if (board[nx][ny] == Stone.NONE)
					continue NEXT_DIR;
				else if (board[nx][ny] == stone.toggle()) {
					nx  += d.x;
					ny  += d.y;
					len += 1;
					continue;
				}
				else if (len == 1)
					break;
				nx -= d.x;
				ny -= d.y;
				while (move.x != nx || move.y != ny) {
					assert(board[nx][ny] == stone.toggle());
					num++;
					nx -= d.x;
					ny -= d.y;
				}
				break;
			}
		}
		return num;
	}

	/**
	 * この盤面における手番の石色を調べる
	 * @return この盤面における手番の石色
	 */
	public Stone getStone() {
		return stone;
	}

	/**
	 * この盤面が指定の手番かどうか調べる
	 * @param color  石の色
	 * @return 指定の色の手番であれば true を返す
	 */
	public boolean isTurn(Stone color) {
		return stone == color;
	}

	/**
	 * 終局であるか調べる
	 * @return 終局の場合 true を返す
	 */
	public boolean isFinished() {
		if (!isSkip())
			return false;
		if (!skip().isSkip())
			return false;
		return true;
	}

	/**
	 * この盤面の手番が石を置けるかどうか調べる
	 * @return 石を置けない場合 true を返す
	 */
	public boolean isSkip() {
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++)
				if (isOK(stone, x, y))
					return false;
		return true;
	}

	/**
	 * スキップする
	 * @return スキップ語の新しい盤面（手番のみ入れ替わる）
	 */
	public Board skip() {
		Board next = new Board(this);
		next.stone = next.stone.toggle();
		return next;
	}

	/**
	 * 確定石の数を調べる
	 * @param stone  調べたい石の色
	 * @return 指定色の確定式の数
	 */
	public int getNumFixedStones(Stone stone) {
		int num = 0;
		for (int x=0; x < board.length; x++) {
			for (int y=0; y < board[x].length; y++) {
				if (board[x][y] != stone)
					continue;
				if (isFixed(x, y, stone))
					num++;
			}
		}
		return num;
	}

	// 確定席の数を調べるための作業用関数
	private void checkFixedStones() {
		fixed = new boolean[8][8];
		while (true) {
			boolean changed = false;
			for (int x=0; x < board.length; x++) {
				for (int y=0; y < board[x].length; y++) {
					if (board[x][y] == Stone.NONE || fixed[x][y])
						continue;
					boolean covered = true;
					for (int i=0; i < 4; i++)
						if (!isFixed(x + dirs[i].x, y + dirs[i].y, board[x][y]) &&
							!isFixed(x - dirs[i].x, y - dirs[i].y, board[x][y])) {
							covered = false;
							break;
						}
					if (covered) {
						fixed[x][y] = true;
						changed = true;
					}
				}
			}
			if (!changed)
				break;
		}
	}

	/**
	 * 指定場所の石が確定石かどうか調べる
	 * @param x      X 座標
	 * @param y      Y 座標
	 * @param stone  調べたい石色
	 * @return 指定位置の石が確定石であれば true を返す
	 */
	private boolean isFixed(int x, int y, Stone stone) {
		if (x < 0 || 7 < x || y < 0 || 7 < y)
			return true;
		if (fixed == null)
			checkFixedStones();
		if (board[x][y] == stone && fixed[x][y])
			return true;
		return false;
	}

	/**
	 * 盤面の隅の評価値を返す
	 * @return 盤面の隅の評価値
	 */
	public int getCornerValue() {
		int corners = 0;    // 盤面の隅の評価値

		// もし隅がとれるようであれば高く評価
		if (isOK(Stone.BLACK, 0, 0)) corners += 10;
		if (isOK(Stone.BLACK, 0, 7)) corners += 10;
		if (isOK(Stone.BLACK, 7, 0)) corners += 10;
		if (isOK(Stone.BLACK, 7, 7)) corners += 10;

		// 逆に隅がとられるようであれば低く評価
		if (isOK(Stone.WHITE, 0, 0)) corners -= 10;
		if (isOK(Stone.WHITE, 0, 7)) corners -= 10;
		if (isOK(Stone.WHITE, 7, 0)) corners -= 10;
		if (isOK(Stone.WHITE, 7, 7)) corners -= 10;

		// もし隅が空の場合で，かつ隅の隣接マスが黒ならば低評価
		if (board[0][0].isEmpty() && (board[0][1].isBlack() || board[1][0].isBlack() || board[1][1].isBlack()))
			corners -= 5;
		if (board[0][7].isEmpty() && (board[0][6].isBlack() || board[1][7].isBlack() || board[1][6].isBlack()))
			corners -= 5;
		if (board[7][0].isEmpty() && (board[6][0].isBlack() || board[7][1].isBlack() || board[6][1].isBlack()))
			corners -= 5;
		if (board[7][7].isEmpty() && (board[7][6].isBlack() || board[6][7].isBlack() || board[6][6].isBlack()))
			corners -= 5;

		// もし隅が空の場合で，かつ隅の隣接マスが白ならば高評価
		if (board[0][0].isEmpty() && (board[0][1].isWhite() || board[1][0].isWhite() || board[1][1].isWhite()))
			corners += 5;
		if (board[0][7].isEmpty() && (board[0][6].isWhite() || board[1][7].isWhite() || board[1][6].isWhite()))
			corners += 5;
		if (board[7][0].isEmpty() && (board[6][0].isWhite() || board[7][1].isWhite() || board[6][1].isWhite()))
			corners += 5;
		if (board[7][7].isEmpty() && (board[7][6].isWhite() || board[6][7].isWhite() || board[6][6].isWhite()))
			corners += 5;
		return corners;
	}

	/**
	 * 盤面の横幅を返す
	 * @return 盤面の横幅
	 */
	public int getBoardWidth() {
		return UNIT * 8;
	}

	public void paint(Graphics g, Move prevMove, Color prevColor) {
		// 緑の盤面を描画
		int sx = 0;
		int sy = 0;
		int ex = UNIT * 8;
		int ey = UNIT * 8;
		g.setColor(green);
		g.fillRect(sx, sy, ex, ey);
		// 1つ前の手を描画
		if (prevMove != null) {
			g.setColor(prevColor);
			g.fillRect(sx + prevMove.x * UNIT, sy + prevMove.y * UNIT, UNIT, UNIT);
		}
		// 各セルの線分を描画
		for (int x=0; x < board.length; x++) {
			g.setColor(Color.BLACK);
			g.drawLine(x * UNIT, sy, x * UNIT, ey);
			g.setColor(lightGreen);
			g.drawLine(x * UNIT + 1, sy, x * UNIT + 1, ey);
		}
		for (int y=0; y < board.length; y++) {
			g.setColor(Color.BLACK);
			g.drawLine(sx, y * UNIT, ex, y * UNIT);
			g.setColor(lightGreen);
			g.drawLine(sx, y * UNIT + 1, ex, y * UNIT + 1);
		}
		// 各マスを描画
		for (int x=0; x < board.length; x++)
			for (int y=0; y < board[x].length; y++) {
				board[x][y].paint(g, x * UNIT, y * UNIT, UNIT);
//				if (isFixed(x, y, Stone.WHITE)) {
//					g.setColor(Color.RED);
//					g.fillOval(sx + x * UNIT, sy + y * UNIT, UNIT, UNIT);
//				}
//				if (isFixed(x, y, Stone.BLACK)) {
//					g.setColor(Color.CYAN);
//					g.fillOval(sx + x * UNIT, sy + y * UNIT, UNIT, UNIT);
//				}
			}
	}


	public String toString(String header) {
		StringBuilder s = new StringBuilder();
		//s.append(header + "[" + stone + "]\n");
		s.append(header + "  a  b  c  d  e  f  g  h");
		for (int y=0; y < 8; y++) {
			s.append("\n" + header + (y + 1));
			for (int x=0; x < 8; x++) {
				switch (board[x][y]) {
				case BLACK:
					if (isFixed(x, y, Stone.BLACK))
						s.append("|■");
					else
						s.append("|●");
					break;
				case WHITE:
					if (isFixed(x, y, Stone.WHITE))
						s.append("|□");
					else
						s.append("|○");
					break;
				case NONE:
					s.append("|  ");
					break;
				}
			}
			s.append("|");
		}
		return s.toString();
	}

	@Override
	public String toString() {
		return toString("");
	}
}
