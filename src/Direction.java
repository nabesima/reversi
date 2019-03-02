public class Direction {
	public int x, y;

	public Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Direction[] makeEightDirections() {
		Direction[] dirs = new Direction[8];
		dirs[0] = new Direction(-1,  -1);
		dirs[1] = new Direction( 0,  -1);
		dirs[2] = new Direction(+1,  -1);
		dirs[3] = new Direction(+1,   0);
		dirs[4] = new Direction(+1,  +1);
		dirs[5] = new Direction( 0,  +1);
		dirs[6] = new Direction(-1,  +1);
		dirs[7] = new Direction(-1,   0);
		return dirs;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}
