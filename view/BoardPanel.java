package view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	
	public BoardPanel(Board board) {
		
		setLayout(new GridLayout(
				board.getLines(), board.getColunms()));
	
		
		
		board.forEach(b -> add(new ButtonField(b)));
		
		board.registerObserver(e -> {
			SwingUtilities.invokeLater(()-> {
			if(e.isWin()) {
				JOptionPane.showMessageDialog(this, "You Win :)");
			} else {
				JOptionPane.showMessageDialog(this, "You Lose :)");
			}
			
				board.restart();
			});
		});
	}
}