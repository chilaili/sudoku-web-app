package sudoku.logic;

import java.io.Serializable;
import java.util.HashSet;

public class Cell implements Serializable {

	private int value;
	private HashSet<Integer> triedValues;
	private int row;
	private int column;
	private boolean isGiven;

	private static int CAPACITY = 9;

	public Cell() {
		value = 0;
		triedValues = new HashSet<Integer>(CAPACITY);
	}

	public Cell(final int row, final int column) {
		this();
		this.row = row;
		this.column = column;
	}

	public Cell(final int row, final int column, final int value) {
		this(row, column);
		this.value = value;
	}

	public boolean isFilled() {
		return value != 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int number) {

		if (number > 9 || number < 1)
			throw new IllegalArgumentException("number must be between 1-9");
		if (triedValues.contains(number))
			throw new IllegalArgumentException("number has been already tried for this cell");

		value = number;
		triedValues.add(number);
	}

	public void clear() {
		value = 0;
	}

	public void reset() {
		clear();
		triedValues.clear();
	}

	public boolean isFullyTried() {
		return triedValues.size() == CAPACITY;
	}

	public HashSet<Integer> getTriedValues() {
		return triedValues;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isGiven() {
		return isGiven;
	}

	public void setGiven(boolean isGiven) {
		this.isGiven = isGiven;
	}

	public void normalize() {
		this.setGiven(this.isFilled());
		this.triedValues = new HashSet<Integer>();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "cell [" + row + "," + column + "] = " + value;
	}

}