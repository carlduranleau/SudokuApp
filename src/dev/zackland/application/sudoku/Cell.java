package dev.zackland.application.sudoku;

public class Cell {
	private int _across;
	private int _down;
	private int _region;
	private int _value;
	private int _index;	
	
	public int getAcross () {
		return _across;
	}
	public void setAcross (int value) {
		_across = value;
	}
	public int getDown () {
		return _down;
	}
	public void setDown (int value) {
		_down = value;
	}
	public int getRegion () {
		return _region;
	}
	public void setRegion (int value) {
		_region = value;
	}
	public int getValue () {
		return _value;
	}
	public void setValue (int value) {
		_value = value;
	}
	public int getIndex () {
		return _index;
	}
	public void setIndex (int value) {
		_index = value;
	}
}