public class Model {

    private View view;                // MVC における View
    private Controller controller;    // MVC における Controller
    private GameState state;          // ゲームの状態

    /**
     * MVC におけるモデルオブジェクトを生成
     */
    public Model() {
        view       = new View(this);
        controller = new Controller(this);
        state      = new PlayerState(new Board(Stone.WHITE), null);
    }

    /**
     * イベントを処理する
     * @param move  ユーザがクリックした場合，盤面上でのその座標．もしくは一定時間が経過した場合 null が渡される
     */
	public synchronized void process(Move move) {
		state = state.process(move);    // 状態を更新
		view.repaint();                 // 再描画する
    }

	/**
	 * View オブジェクトを取得
	 * @return View オブジェクト
	 */
    public View getView() {
        return view;
    }

    /**
     * Controller オブジェクトを取得
     * @return Controller オブジェクト
     */
    public Controller getController() {
        return controller;
    }

    /**
     * ゲームの状態を取得
     * @return ゲームの状態を表す GameState オブジェクト
     */
	public GameState getState() {
		return state;
	}
}
