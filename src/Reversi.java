import javax.swing.JFrame;

public class Reversi extends JFrame {

    private Model model = null;

    public Reversi() {
        // Window タイトルの設定
        setTitle("Reversi");
        // Window を閉じるボタンを有効にする
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // モデルの生成
        model = new Model();

        // この Window に view を登録
        setContentPane(model.getView());
        // マウスイベントのリスナとして controller を登録
        model.getView().addMouseListener(model.getController());
        // ウィンドウサイズを適切に設定
        pack();
        // この Window を表示
        setVisible(true);
    }

    /** 起動用 main 関数 */
    public static void main(String[] args) {
    	new Reversi();
    }
}
