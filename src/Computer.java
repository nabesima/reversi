import java.util.List;


public class Computer implements Runnable {

	private Board       board;
	private Move        selected     = null;
	private static int  numStates    = 0;
	private static int  maxNumStates = 0;
	private static long cpuTime      = 0;
	private static int  numRuns      = 0;

	public static boolean debug  = false;

	public Computer(Board board) {
		this.board = board;
	}

	private void think() {
		// (1) 最初に見つかった手を返す
		//selected = board.getPossibleMoves().get(0);

//		// (2) 相手の石をできるかぎりめくる
//	    selected = selectMaxMove(board, new Evaluator() {
//	        public double evaluate(Board new_board) {
//	            return new_board.getNumStones(Stone.BLACK)
//	                       - board.getNumStones(Stone.BLACK);
//	        }
//	    });

//		// (3) 相手の手数をできるかぎり減らす
//	    selected = selectMaxMove(board, new Evaluator() {
//	       public double evaluate(Board new_board) {
//	          int blackMoves = new_board.getNumPossibleMoves(Stone.BLACK);
//	          int whiteMoves = new_board.getNumPossibleMoves(Stone.WHITE);
//	          return blackMoves - whiteMoves;
//	       }
//	    });

//	    // (4) 相手の手数をできるかぎり減らす＋隅を評価＋確定石を評価＋終局時の石数を評価
//	    selected = selectMaxMove(board, new Evaluator() {
//	       public double evaluate(Board new_board) {
//	          if (new_board.isFinished())
//	             return 1000 * (new_board.getNumStones(Stone.BLACK)
//	                                  - new_board.getNumStones(Stone.WHITE));
//	          int blackMoves  = new_board.getNumPossibleMoves(Stone.BLACK);
//	          int whiteMoves  = new_board.getNumPossibleMoves(Stone.WHITE);
//	          int cornerValue = new_board.getCornerValue();
//	          int fixedStones = new_board.getNumFixedStones(Stone.BLACK);
//	          return blackMoves - whiteMoves + cornerValue + 64 * fixedStones;
//	       }
//	    });

//	   // (5) ５手先まで読む
//	   doMinMaxSearch(board, 5, false, new Evaluator() {
//	      public double evaluate(Board new_board) {
//	         if (new_board.isFinished())
//	            return 1000 * (new_board.getNumStones(Stone.BLACK)
//	                                 - new_board.getNumStones(Stone.WHITE));
//	         int blackMoves  = new_board.getNumPossibleMoves(Stone.BLACK);
//	         int whiteMoves  = new_board.getNumPossibleMoves(Stone.WHITE);
//	         int cornerValue = new_board.getCornerValue();
//	         int fixedStones = new_board.getNumFixedStones(Stone.BLACK);
//	         return blackMoves - whiteMoves + cornerValue + 64 * fixedStones;
//	      }
//	   });

	   // (6) ７手先まで読む
	   doMinMaxSearch(board, 7, true, new Evaluator() {
	      public double evaluate(Board new_board) {
	         if (new_board.isFinished())
	            return 1000 * (new_board.getNumStones(Stone.BLACK)
	                                 - new_board.getNumStones(Stone.WHITE));
	         int blackMoves  = new_board.getNumPossibleMoves(Stone.BLACK);
	         int whiteMoves  = new_board.getNumPossibleMoves(Stone.WHITE);
	         int cornerValue = new_board.getCornerValue();
	         int fixedStones = new_board.getNumFixedStones(Stone.BLACK);
	         return blackMoves - whiteMoves + cornerValue + 64 * fixedStones;
	      }
	   });

	}

	@SuppressWarnings("unused")
	private Move selectMaxMove(Board board, Evaluator evaluator) {
		double max_val  = Double.NEGATIVE_INFINITY;
		Move   max_move = null;
		for (Move move : board.getPossibleMoves()) {
			numStates++;
			maxNumStates = Math.max(numStates, maxNumStates);
			if (evaluator.evaluate(board.put(move)) > max_val) {
				max_val  = evaluator.evaluate(board.put(move));
				max_move = move;
			}
		}
		return max_move;
	}

	@SuppressWarnings("unused")
	private void doMinMaxSearch(Board board, int depth, boolean cut, Evaluator evaluator) {
		if (debug) {
			System.out.println("========== ORG BOARD ==========");
			System.out.println(board);
			System.out.println(board.getPossibleMoves());
			System.out.println("===============================");
		}
		double max_val  = Double.NEGATIVE_INFINITY;
		Move   max_move = null;
		for (Move move : board.getPossibleMoves()) {
			if (debug) System.out.println("move = " + move);
			Board new_board = board.put(move);
			double val = evaluate(new_board, 1, depth, cut, evaluator, max_val, Double.POSITIVE_INFINITY);
			if (max_val < val) {
				max_val  = val;
				max_move = move;
			}
		}
		if (debug) {
			System.out.println("numStates = " + numStates);
			System.out.println("selected  = " + max_move);
			System.out.println("max val   = " + max_val);
		}
		selected = max_move;
	}

	private double evaluate(Board new_board, int nest, int depth, boolean cut, Evaluator evaluator, double lb, double ub) {
		numStates++;
		maxNumStates = Math.max(numStates, maxNumStates);
		if (debug) {
			System.out.println(header(nest) + "BEGIN evaluate(" + new_board.getStone() + "," + depth + "," + lb + "," + ub + ")");
			System.out.println(new_board.toString(header(nest)));
		}
		if (depth == nest || new_board.isFinished())
			return evaluator.evaluate(new_board);
		if (new_board.isSkip()) {
			double val = evaluate(new_board.skip(), nest + 1, depth, cut, evaluator, lb, ub);
			if (debug) System.out.println(header(nest) + "END val = " + val);
			return val;
		}

		List<Move> moves = new_board.getPossibleMoves();
		assert(!moves.isEmpty());
		if (debug) System.out.println(header(nest) + moves);

		if (new_board.isTurn(Stone.BLACK)) {
			double max_val = Double.NEGATIVE_INFINITY;
			for (Move move : moves) {
				if (debug) System.out.println(header(nest) + "move = " + move);
				double val = evaluate(new_board.put(move), nest + 1, depth, cut, evaluator, lb, ub);
				max_val = Math.max(val, max_val);
				lb = Math.max(lb, max_val);
				if (cut && ub <= lb) {
					if (debug) System.out.println(header(nest) + "break BLACK->WHITE");
					break;
				}
			}
			if (debug) System.out.println(header(nest) + "END max val = " + max_val);
			return max_val;
		}
		else {
			double min_val = Double.POSITIVE_INFINITY;
			for (Move move : moves) {
				if (debug) System.out.println(header(nest) + "move = " + move);
				double val = evaluate(new_board.put(move), nest + 1, depth, cut, evaluator, lb, ub);
				min_val = Math.min(val,  min_val);
				ub = Math.min(ub, min_val);
				if (cut && ub <= lb) {
					if (debug) System.out.println("break WHITE->BLACK");
					break;
				}
			}
			if (debug) System.out.println(header(nest) + "END min val = " + min_val);
			return min_val;
		}
	}

	@Override
	public void run() {
		numRuns++;
		numStates = 0;
		long time = System.currentTimeMillis();
		think();
		cpuTime += System.currentTimeMillis() - time;
	}

	public Move getSelectedMove() {
		return selected;
	}

	public static int getNumStates() {
		return numStates;
	}

	public static int getMaxNumStates() {
		return maxNumStates;
	}

	public static void clearMaxNumStates() {
		maxNumStates = 0;
	}

	public static double getAvgCpuTime() {
		return cpuTime / 1000.0 / numRuns;
	}

	private static String header(int nest) {
		String head = nest + " ";
		for (int i = 0; i < nest; i++)
			head += "  ";
		return head;
	}

	private abstract class Evaluator {
		public abstract double evaluate(Board new_board);
	}

}
