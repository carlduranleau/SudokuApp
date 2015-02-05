package dev.zackland.application.sudoku;

import javax.swing.SwingUtilities;

public class SudokuApp {

	public static void main (String argv[]) {
		final SudokuTable st = new SudokuTable(false);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SudokuFrame inst = new SudokuFrame(st);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
}