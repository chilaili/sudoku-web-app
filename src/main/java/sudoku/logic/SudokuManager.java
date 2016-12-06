package sudoku.logic;

import java.util.HashSet;
import java.util.Random;

import sudoku.logic.exceptions.ConstraintViolationException;

public class SudokuManager {

	public void solve(final Sudoku puzzle) {
		this.solve(puzzle, puzzle.getCell(0, 0));
	}

	public Sudoku generateSudoku() {
		Sudoku sudoku = new Sudoku();
		this.solve(sudoku, sudoku.getCell(0, 0));
		return sudoku;
	}

	public Sudoku makePuzzle(int givens) {

		Sudoku sudoku = new Sudoku();
		this.solve(sudoku);
		return this.puzzle(sudoku, givens);
	}

	public Sudoku puzzle(final Sudoku template, int givens) {

		Sudoku puzzle = template.clone();
		HashSet<Cell> cells = new HashSet<Cell>();

		Random rand = new Random();

		int cellsToDig = (Sudoku.PUZZLE_SIZE * Sudoku.PUZZLE_SIZE) - givens;

		for (int i = 0; i < cellsToDig; i++) {
			Cell cell = null;
			do {
				cell = puzzle.getCell(rand.nextInt(Sudoku.PUZZLE_SIZE), rand.nextInt(Sudoku.PUZZLE_SIZE));
			} while (cells.contains(cell));
			cells.add(cell);
			cell.reset();
		}

		puzzle.normalize();
		return puzzle;
	}

	public void solve(final Sudoku sudoku, Cell cell) {

		Cell current = cell;

		if (current != null) {
			if (!current.isFilled()) {
				try {
					this.setRandomValue(sudoku, current);
					// System.out.println(sudoku);
					current = sudoku.nextCell(current);
				} catch (ConstraintViolationException e) {
					current.reset();

					current = sudoku.previousCell(current);

					while (current.isGiven()) {
						current = sudoku.previousCell(current);
					}
					current.clear();
					// System.out.println(sudoku);
				}
			} else {
				current = sudoku.nextCell(current);
			}

			solve(sudoku, current);
		}

	}

	public void setRandomValue(final Sudoku sudoku, Cell cell) {

		int randomNumber = this.getConstraintRandomNumber(sudoku, cell);
		cell.setValue(randomNumber);
	}

	private void isValidRow(final Sudoku sudoku, final int rowIndex) {
		if (rowIndex >= Sudoku.PUZZLE_SIZE || rowIndex < 0)
			throw new IllegalArgumentException("column index out of bound");

		HashSet<Integer> numbers = this.generateAllValues();

		for (int j = 0; j < Sudoku.PUZZLE_SIZE; j++) {
			Cell cell = sudoku.getCell(rowIndex, j);
			int value = cell.getValue();

			if (!numbers.contains(value)) {
				throw new ConstraintViolationException(
						"constraint violation at row " + rowIndex + " : duplicate values (" + value + ")");
			}

			numbers.remove(value);
		}

		if (numbers.size() != 0) {
			throw new ConstraintViolationException("constraint violation at row " + rowIndex + " : empty values");
		}
		;
	}

	private void isValidColumn(final Sudoku sudoku, final int columnIndex) {
		if (columnIndex >= Sudoku.PUZZLE_SIZE || columnIndex < 0)
			throw new IllegalArgumentException("column index out of bound");

		HashSet<Integer> numbers = this.generateAllValues();

		for (int i = 0; i < Sudoku.PUZZLE_SIZE; i++) {
			Cell cell = sudoku.getCell(i, columnIndex);
			int value = cell.getValue();

			if (!numbers.contains(value)) {
				throw new ConstraintViolationException(
						"constraint violation at column " + columnIndex + " : duplicate values (" + value + ")");
			}

			numbers.remove(value);
		}

		if (numbers.size() != 0) {
			throw new ConstraintViolationException("constraint violation at column " + columnIndex + " : empty values");
		}
		;
	}

	private void isValidBlock(final Sudoku sudoku, final int rowStartIndex, final int columnStartIndex) {
		if (rowStartIndex >= Sudoku.PUZZLE_SIZE || rowStartIndex < 0)
			throw new IllegalArgumentException("column index out of bound");
		if (columnStartIndex >= Sudoku.PUZZLE_SIZE || columnStartIndex < 0)
			throw new IllegalArgumentException("column index out of bound");
		if (rowStartIndex % Sudoku.BLOCK_SIZE != 0)
			throw new IllegalArgumentException("row is not a start index");
		if (columnStartIndex % Sudoku.BLOCK_SIZE != 0)
			throw new IllegalArgumentException("column is not a start index");

		HashSet<Integer> numbers = this.generateAllValues();

		for (int i = rowStartIndex; i < rowStartIndex + Sudoku.BLOCK_SIZE; i++) {
			for (int j = columnStartIndex; j < columnStartIndex + Sudoku.BLOCK_SIZE; j++) {
				Cell cell = sudoku.getCell(i, j);
				int value = cell.getValue();

				if (!numbers.contains(value)) {
					throw new ConstraintViolationException("constraint violation at block[" + rowStartIndex + ","
							+ columnStartIndex + "] : duplicate values (" + value + ")");
				}

				numbers.remove(value);
			}
		}

		if (numbers.size() != 0) {
			throw new ConstraintViolationException(
					"constraint violation at block[" + rowStartIndex + "," + columnStartIndex + "] : empty values");
		}
	}

	public void checkValidAnswers(final Sudoku sudoku) {

		for (int i = 0; i < Sudoku.PUZZLE_SIZE; i++) {
			this.isValidRow(sudoku, i);
		}

		for (int j = 0; j < Sudoku.PUZZLE_SIZE; j++) {
			this.isValidColumn(sudoku, j);
		}

		for (int bi = 0; bi < Sudoku.PUZZLE_SIZE; bi += Sudoku.BLOCK_SIZE) {
			for (int bj = 0; bj < Sudoku.PUZZLE_SIZE; bj += Sudoku.BLOCK_SIZE) {
				this.isValidBlock(sudoku, bi, bj);
			}
		}
	}

	private int getConstraintRandomNumber(final Sudoku sudoku, Cell cell) {

		HashSet<Integer> constraintNumbers = this.generateAllValues();

		this.filterConstraintRowNumbers(sudoku, cell, constraintNumbers);
		this.filterConstraintColumnNumbers(sudoku, cell, constraintNumbers);
		this.filterConstraintBlockNumbers(sudoku, cell, constraintNumbers);

		constraintNumbers.removeAll(cell.getTriedValues());

		if (constraintNumbers.size() != 0) {
			Integer[] array = constraintNumbers.toArray(new Integer[constraintNumbers.size()]);
			int randomIndex = 0;

			if (constraintNumbers.size() != 1) {
				Random rand = new Random();
				randomIndex = rand.nextInt(constraintNumbers.size());
			}

			return array[randomIndex];

		}

		throw new ConstraintViolationException(
				"not able to find a valid number for cell [" + cell.getRow() + "," + cell.getColumn() + "]");
	}

	private HashSet<Integer> filterConstraintRowNumbers(final Sudoku sudoku, final Cell cell,
			HashSet<Integer> constraintNumbers) {
		if (constraintNumbers.size() != 0) {
			for (int j = 0; j < Sudoku.PUZZLE_SIZE; j++) {
				Cell c = sudoku.getCell(cell.getRow(), j);
				if (c.isFilled()) {
					constraintNumbers.remove(c.getValue());
				}
			}
		}
		return constraintNumbers;
	}

	private HashSet<Integer> filterConstraintColumnNumbers(final Sudoku sudoku, final Cell cell,
			HashSet<Integer> constraintNumbers) {

		if (constraintNumbers.size() != 0) {
			for (int index = 0; index < Sudoku.PUZZLE_SIZE; index++) {
				Cell c = sudoku.getCell(index, cell.getColumn());
				if (c.isFilled()) {
					constraintNumbers.remove(c.getValue());
				}
			}
		}

		return constraintNumbers;
	}

	private HashSet<Integer> filterConstraintBlockNumbers(final Sudoku sudoku, final Cell cell,
			HashSet<Integer> constraintNumbers) {

		if (constraintNumbers.size() != 0) {
			int blockRowIndex = this.getBlockRowIndex(sudoku, cell.getRow());
			int blockColumnIndex = this.getBlockColumnIndex(sudoku, cell.getColumn());

			for (int i = blockRowIndex; i < blockRowIndex + Sudoku.BLOCK_SIZE && i < Sudoku.PUZZLE_SIZE; i++) {
				for (int j = blockColumnIndex; j < blockColumnIndex + Sudoku.BLOCK_SIZE
						&& j < Sudoku.PUZZLE_SIZE; j++) {

					Cell c = sudoku.getCell(i, j);
					if (c.isFilled()) {
						constraintNumbers.remove(c.getValue());
					}

				}
			}
		}

		return constraintNumbers;
	}

	private int getBlockRowIndex(final Sudoku sudoku, final int rowIndex) {
		return (rowIndex / Sudoku.BLOCK_SIZE) * Sudoku.BLOCK_SIZE;
	}

	private int getBlockColumnIndex(final Sudoku sudoku, final int columnIndex) {
		return (columnIndex / Sudoku.BLOCK_SIZE) * Sudoku.BLOCK_SIZE;
	}

	private HashSet<Integer> generateAllValues() {
		HashSet<Integer> numbers = new HashSet<Integer>(9);
		for (int i = 1; i <= 9; i++) {
			numbers.add(i);
		}

		return numbers;
	}

}
