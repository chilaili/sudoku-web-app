package sudoku.logic;

import java.io.Serializable;

public class Sudoku implements Serializable {

	private Cell[][] cells;
	public static final int BLOCK_SIZE = 3;
	public static final int PUZZLE_SIZE = BLOCK_SIZE * BLOCK_SIZE;

	public Sudoku() {
		cells = new Cell[9][9];
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}

	public Sudoku(int[][] matrix) {
		this(matrix, false);
	}

	public Sudoku(int[][] matrix, boolean setGivens) {
		cells = new Cell[9][9];
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				Cell cell = new Cell(i, j, matrix[i][j]);
				if (setGivens) {
					cell.normalize();
				}
				cells[i][j] = cell;
			}
		}
	}

	public Sudoku clone() {
		return new Sudoku(this.toArray());
	}

	public Cell getCell(final int row, final int column) {
		return cells[row][column];
	}

	public Cell nextCell(final Cell cell) {
		int r = cell.getRow(), c = cell.getColumn();
		if (c < PUZZLE_SIZE - 1) {
			++c;
		} else if (r < PUZZLE_SIZE - 1) {
			c = 0;
			++r;
		} else {
			return null;
		}

		return getCell(r, c);
	}

	public void normalize() {
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				Cell cell = this.getCell(j, i);
				cell.normalize();
			}
		}
	}

	public Cell previousCell(final Cell cell) {
		int r = cell.getRow(), c = cell.getColumn();
		if (c > 0) {
			--c;
		} else if (r != 0) {
			--r;
			c = PUZZLE_SIZE - 1;
		} else {
			return null;
		}

		return getCell(r, c);
	}

	public int[][] toArray() {
		int[][] matrix = new int[PUZZLE_SIZE][PUZZLE_SIZE];

		for (int i = 0; i < PUZZLE_SIZE; i++) {
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				matrix[i][j] = cells[i][j].getValue();
			}
		}

		return matrix;
	}

	@Override
	public String toString() {

		String boardString = "";
		int length = cells.length;

		for (int i = 0; i < length; i++) {
			if (i % 3 == 0) {
				boardString += "====================\n";
			}
			for (int j = 0; j < length; j++) {
				if (j % 3 == 0) {
					boardString += " | ";
				}

				try {
					boardString += Integer.toString(cells[i][j].getValue());
				} catch (RuntimeException e) {
					boardString += " ";
				}
			}
			boardString += "|";
			boardString += "\n";
		}
		boardString += "====================\n";

		return boardString;
	}
}
