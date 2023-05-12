package view;

import javax.swing.JFrame;

import model.Board;

@SuppressWarnings("serial")
public class WindowMain extends JFrame{
	
	public WindowMain() {
		
		Board board = new Board(16, 30, 50);
		add(new BoardPanel(board));
		
		setTitle("Mine-Field");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new WindowMain();
	}

}
