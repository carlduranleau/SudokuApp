package dev.zackland.application.sudoku;

import java.util.Random;
import java.util.Vector;

public class SudokuTable {

	private boolean debug = false;

	private Vector[] _matrix = new Vector[81];
	private Cell[] _cells = new Cell[81];
	
	public SudokuTable (boolean generate) {
		if (generate) rebuildTable();
	}

	public Cell[] getData() {
		return _cells.clone();
	}
	
	public void generateTable () {
		rebuildTable();
	}
	
	private void rebuildTable () {
		_cells = null;
		_cells = new Cell[81];
		Random rnd = new Random();
		int counter = 0;
		int backPos = 0;
		int backValue = 0;
		Cell newCell = null;
	
		for (int i = 0; i < 81; i++) {
			_matrix[i] = null;
			_matrix[i] = new Vector();
			for (int v = 0; v < 9; v++) {
				_matrix[i].add(new Integer(v + 1));
			}
			if (debug) System.out.print(".");
		}
		
		counter = 0;
		while (counter != 81) {
			if (!(_matrix[counter].size() == 0)) {
				if (debug) System.out.print("a");
				backPos = ((int)rnd.nextInt(_matrix[counter].size()));
				backValue = ((Integer)_matrix[counter].elementAt(backPos)).intValue();
				newCell = createCell(counter, backValue);
				if (debug) System.out.print("(" + Integer.toString(newCell.getValue()) + ", " + Integer.toString(backValue) + ", " + Integer.toString(backPos) + ")");
				if (!isInConflict(newCell)) {
					if (debug) System.out.print("No conflict");
					_cells[counter] = newCell;
					_matrix[counter].removeElementAt(backPos);
					counter++;
				} else {
					if (debug) System.out.print("conflict");
					_matrix[counter].removeElementAt(backPos);
				}
			} else {
				if (debug) System.out.print("b");
				for (int i = 0; i < 9; i++) {
					_matrix[counter].add(new Integer(i + 1));
				}
				_cells[--counter] = null;
			}
			//if (debug) System.out.print(Integer.toString(counter));
			if (debug) System.out.println("\n\n");
			if (debug) System.out.println(this);
			//if (counter==9) System.exit(0);
		}
	}

	public Cell createCell(int index, int value) {
		Cell c = new Cell();
		
		c.setAcross(getAcrossFromIndex(index));
		c.setDown(getDownFromIndex(index));
		c.setRegion(getRegionFromIndex(index));
		c.setValue(value);
		c.setIndex(index);
		
		return c;
	}
	
	private int getDownFromIndex(int index) {
		return (int)index / 9;
	}
	
	private int getAcrossFromIndex(int index) {
		int i = 0;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (index == i) return y;
				i++;
			}
		}
		return 0;
	}
	
	private int getRegionFromIndex(int index) {
		int a = getAcrossFromIndex(index);
		int d = getDownFromIndex(index);
		
		if (0 <= a && a < 3 && 0 <= d && d < 3) return 1;
		if (3 <= a && a < 6 && 0 <= d && d < 3) return 2;
		if (6 <= a && a < 9 && 0 <= d && d < 3) return 3;
		if (0 <= a && a < 3 && 3 <= d && d < 6) return 4;
		if (3 <= a && a < 6 && 3 <= d && d < 6) return 5;
		if (6 <= a && a < 9 && 3 <= d && d < 6) return 6;
		if (0 <= a && a < 3 && 6 <= d && d < 9) return 7;
		if (3 <= a && a < 6 && 6 <= d && d < 9) return 8;
		if (6 <= a && a < 9 && 6 <= d && d < 9) return 9;
		return 0;
	}
	
	private boolean isInConflict(Cell value) {
		if (value == null) if (debug) System.out.println("Value is null");
		if (debug) System.out.println ("Checking cell @ " + Integer.toString(value.getIndex()) + " with value " + Integer.toString(value.getValue()));
		if (debug) System.out.println("Across @ " + Integer.toString(value.getAcross()));
		if (debug) System.out.println("Down @ " + Integer.toString(value.getDown()));
		if (debug) System.out.println("Region @ " + Integer.toString(value.getRegion()));
		for (int i = 0; i < 81; i++) {
			if (i != value.getIndex()) {
				if (_cells[i] == null) if (debug) System.out.println("Cell @ " + Integer.toString(i) + " is null");
				if (_cells[i] == null) return false;
	
				if (debug) System.out.println("Validating cell @ " + Integer.toString(i) + " with value " + Integer.toString(_cells[i].getValue()));
				if (debug) System.out.println("Across @ " + Integer.toString(_cells[i].getAcross()));
				if (debug) System.out.println("Down @ " + Integer.toString(_cells[i].getDown()));
				if (debug) System.out.println("Region @ " + Integer.toString(_cells[i].getRegion()));
				
				if (_cells[i].getAcross() == value.getAcross()) if (debug) System.out.println("Same column");
				if (_cells[i].getDown() == value.getDown()) if (debug) System.out.println("Same line");
				if (_cells[i].getRegion() == value.getRegion()) if (debug) System.out.println("Same region");
	
				if (_cells[i].getAcross() == value.getAcross() || _cells[i].getDown() == value.getDown() || _cells[i].getRegion() == value.getRegion()) {
					if (_cells[i].getValue() == value.getValue()) return true;
				}
			}
		}
		return false;
	}
	
	private boolean isInConflict(Cell value, Cell[] cells) {
		if (value == null) if (debug) System.out.println("Value is null");
		if (value == null) return true;
		if (debug) System.out.println ("Checking cell @ " + Integer.toString(value.getIndex()) + " with value " + Integer.toString(value.getValue()));
		if (debug) System.out.println("Across @ " + Integer.toString(value.getAcross()));
		if (debug) System.out.println("Down @ " + Integer.toString(value.getDown()));
		if (debug) System.out.println("Region @ " + Integer.toString(value.getRegion()));
		for (int i = 0; i < 81; i++) {
			if (i != value.getIndex()) {
				if (cells[i] == null) if (debug) System.out.println("Cell @ " + Integer.toString(i) + " is null");
				if (cells[i] != null) {
					if (debug) System.out.println("Validating cell @ " + Integer.toString(i) + " with value " + Integer.toString(cells[i].getValue()));
					if (debug) System.out.println("Across @ " + Integer.toString(cells[i].getAcross()));
					if (debug) System.out.println("Down @ " + Integer.toString(cells[i].getDown()));
					if (debug) System.out.println("Region @ " + Integer.toString(cells[i].getRegion()));
					
					if (cells[i].getAcross() == value.getAcross()) if (debug) System.out.println("Same column");
					if (cells[i].getDown() == value.getDown()) if (debug) System.out.println("Same line");
					if (cells[i].getRegion() == value.getRegion()) if (debug) System.out.println("Same region");
					
					if (cells[i].getAcross() == value.getAcross() || cells[i].getDown() == value.getDown() || cells[i].getRegion() == value.getRegion()) {
						if (cells[i].getValue() == value.getValue()) return true;
					}
				}
			}
		}
		return false;
	}
	

	public boolean isValid (Cell cell, Cell[] cells) {
		return !isInConflict(cell, cells);
	}
	
	public String toString() {
		String out = "";
		int l = 0;
		int c = 0;
		int s = 0;
		for (int x = 0; x < 81; x++) {
			if (_cells[x] != null)
				out += " " + Integer.toString(_cells[x].getValue());
			else
				out += "-";
			if ((x + 1) % 9 == 0) out += "\n";
		}
		return out;
	}
}