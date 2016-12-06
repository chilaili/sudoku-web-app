package sudoku.logic;

import java.util.HashSet;
import java.util.Random;

import sudoku.logic.exceptions.ConstraintViolationException;

/**
 * SudokuManager is the class responsible to generate, solve, dig-out sudokus
 * objects
 * 
 */

public class SudokuManager {

	/**
	 * generates a complete filled and valid Sodoku
	 * 
	 ** @return Sudoku return the generated Sudoku
	 * 
	 */
	public Sudoku generateSudoku() {
		Sudoku sudoku = new Sudoku();
		this.solve(sudoku, sudoku.getCell(0, 0));
		return sudoku;
	}

	/**
	 * generates a Sudoku and dig-out generating the puzzle form the generated
	 * Soduko leaving n givens cell filled
	 * 
	 * @param int
	 *            givens number of cell needs to remain filled
	 * 
	 * @return Sudoku puzzle a new sudoku object not completely filled
	 */

	public Sudoku makePuzzle(int givens) {

		Sudoku sudoku = new Sudoku();
		this.solve(sudoku);
		return this.puzzle(sudoku, givens);
	}

	/**
	 * dig-out and generates a Sudoku puzzle from a given filled Sudoku template
	 * leaving n givens cells filled
	 * 
	 * @param Sudoku
	 *            template the sudoku template to dig-out
	 * 
	 * @param int
	 *            givens number of cells needs to remain filled
	 * 
	 * @return Sudoku a new sudoku puzzle generated from template
	 */
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

	/**
	 * resolve a puzzle starting from the cell at position (0,0)
	 * 
	 * @param Sudoku
	 *            puzzle takes as parameter the puzzle to solve
	 * 
	 */
	public void solve(final Sudoku puzzle) {
		this.solve(puzzle, puzzle.getCell(0, 0));
	}

	/**
	 * resolve a puzzle starting from the cell passed as parameter
	 * 
	 * @param Sudoku
	 *            sudoku the sudoku puzzle to solve
	 * 
	 * @param Cell
	 *            cell the init cell to start solving the puzzle
	 */
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

	/**
	 * sets a random and valid number in a cell of a Sudoku puzzle
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku to insert a valid number
	 * 
	 * @param Cell
	 *            cell the cell to insert a valid number
	 * 
	 */
	public void setRandomValue(final Sudoku sudoku, Cell cell) {

		int randomNumber = this.getConstraintRandomNumber(sudoku, cell);
		cell.setValue(randomNumber);
	}

	/**
	 * check if the whole Sudoku has a valid solutions
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku to check
	 * 
	 * 
	 * @exception ConstraintViolationException
	 *                if the sudoku is not valid throws an exception with error
	 *                message
	 */

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

	/**
	 * utility method to check whether a row in a sudoku is well formed
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku object to check
	 * 
	 * @param int
	 *            rowIndex the index of the row to check if it's well formed
	 * 
	 * @exception ConstraintViolationException
	 *                if the row is not valid throws an exception with error
	 *                message
	 */

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

	/**
	 * utility method to check whether a column in a sudoku is well formed
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku object to check
	 * 
	 * @param int
	 *            columnIndex the index of the column to check if it's well
	 *            formed
	 * 
	 *            * @exception ConstraintViolationException if the column is not
	 *            valid throws an exception with error message
	 */
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

	/**
	 * utility method to check if a block grid in the puzzle is valid
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku object to check
	 * 
	 * @param int
	 *            rowStartIndex the index row of a block
	 * 
	 * @param int
	 *            columnStartIndex the index column of a block
	 * 
	 *            * @exception ConstraintViolationException if the block is not
	 *            valid throws an exception with error message
	 */

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

	/**
	 * generate a valid number to be solution for a cell in the sudoku puzzle
	 * 
	 * @param Sudoku
	 *            sudoku the context sodoku puzzle
	 * 
	 * @param Cell
	 *            cell the cell where the number will be inserted
	 * 
	 * @return int return the generated valid number
	 */

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

	/**
	 * filters possible valid numbers from all the number used in the same row
	 * of the given cell
	 * 
	 * @param Sudoku
	 *            sudoku the context Sudoku
	 * 
	 * @param Cell
	 *            cell the reference cell
	 * 
	 * @param HashSet<Integer>
	 *            constraintNumbers possible numbers that can be filtered
	 * 
	 * @return HashSet<Integer> constraintNumbers set of filtered numbers
	 * 
	 */
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

	/**
	 * filters possible valid numbers from all the number used in the same
	 * column of the given cell
	 * 
	 * @param Sudoku
	 *            sudoku the context Sudoku
	 * 
	 * @param Cell
	 *            cell the reference cell
	 * 
	 * @param HashSet<Integer>
	 *            constraintNumbers possible numbers that can be filtered
	 * 
	 * @return HashSet<Integer> constraintNumbers set of filtered numbers
	 * 
	 */
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

	/**
	 * filters possible valid numbers from all the number used in the same block
	 * of the given cell
	 * 
	 * @param Sudoku
	 *            sudoku the context Sudoku
	 * 
	 * @param Cell
	 *            cell the reference cell
	 * 
	 * @param HashSet<Integer>
	 *            constraintNumbers possible numbers that can be filtered
	 * 
	 * @return HashSet<Integer> constraintNumbers set of filtered numbers
	 * 
	 */
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

	/**
	 * Utility function to get the top-left row index of a block where the given
	 * row index lives
	 * 
	 * @param Sudoku
	 *            sudoku the context sudoku
	 * 
	 * @param int
	 *            rowIndex the reference row index to get the top-left block row
	 *            index
	 * 
	 * @return int the top-left block row index
	 */
	private int getBlockRowIndex(final Sudoku sudoku, final int rowIndex) {
		return (rowIndex / Sudoku.BLOCK_SIZE) * Sudoku.BLOCK_SIZE;
	}

	/**
	 * Utility function to get the top-left column index of a block where the
	 * given column index lives
	 * 
	 * @param Sudoku
	 *            sudoku the context sudoku
	 * 
	 * @param int
	 *            rowIndex the reference column index to get the top-left block
	 *            row index
	 * 
	 * @return int the top-left block column index
	 */
	private int getBlockColumnIndex(final Sudoku sudoku, final int columnIndex) {
		return (columnIndex / Sudoku.BLOCK_SIZE) * Sudoku.BLOCK_SIZE;
	}

	/**
	 * Utility function to generate all the possible valid value for a Sudoku
	 * cell
	 * 
	 * 
	 * @return HashSet<Integer> set of possible valid numbers
	 */
	private HashSet<Integer> generateAllValues() {
		HashSet<Integer> numbers = new HashSet<Integer>(9);
		for (int i = 1; i <= 9; i++) {
			numbers.add(i);
		}

		return numbers;
	}

}
