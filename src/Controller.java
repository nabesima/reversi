import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;

public class Controller implements MouseListener, ActionListener {

    private Model model;    // イベントの通知先であるモデル
    private Timer timer;    // 一定時間経過したことを通知するためのタイマーオブジェクト

    private static final int DELAY = 100; // msec

    /**
     * イベントの発生をモデルに通知する Controller オブジェクトを生成する
     * @param m
     */
    public Controller(Model m) {
        model = m;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * ユーザがクリックした場合，それを盤面上の座標に変換してモデルに通知
     */
	@Override
	public void mouseClicked(MouseEvent e) {
		Move move = new Move(Stone.WHITE, e.getX() / Board.UNIT, e.getY() / Board.UNIT);
		model.process(move);
	}

	/**
	 * 一定時間経過した場合，それをモデルに通知
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		model.process(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}