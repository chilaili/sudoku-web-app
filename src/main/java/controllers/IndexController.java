package controllers;

import java.io.StringWriter;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import sudoku.logic.Cell;
import sudoku.logic.Sudoku;
import sudoku.logic.SudokuManager;
import sudoku.logic.exceptions.ConstraintViolationException;

/**
 * Spring MVC Controller
 */

@Controller
@SessionAttributes({ "sudoku", "puzzle" })
public class IndexController {

	@Resource
	private SudokuManager manager;

	/**
	 * this is the init method that generates the puzzle with 33 givens by
	 * default
	 *
	 * the difference between sudoku and puzzle are sudoku is completly filled
	 * and correct puzzle takes as template sudoku and dig out holes to generate
	 * the puzzle
	 * 
	 * 
	 * @param ModelMap
	 *            spring object to store request parameters
	 * 
	 * @return String name of the view where spring bounds the data model and
	 *         generates html
	 */

	@RequestMapping("/")
	public String index(ModelMap modelMap) {

		Sudoku sudoku = manager.generateSudoku();
		Sudoku puzzle = manager.puzzle(sudoku, 33);

		modelMap.addAttribute("sudoku", sudoku);
		modelMap.addAttribute("puzzle", puzzle);
		modelMap.addAttribute("matrix", puzzle.toArray());

		return "index";
	}

	/**
	 * this method check if the puzzle is a valid and filled sudoku puzzle
	 * 
	 * @param int[][]
	 *            matrix the matrix that represents the sudoku grid
	 * 
	 * @return String return the response as json string object
	 */

	@ResponseBody
	@RequestMapping(value = "/checkCorrectAnswers", method = RequestMethod.POST)
	public String checkCorrectAnswers(@RequestBody int[][] matrix) {

		boolean result = true;

		try {
			manager.checkValidAnswers(new Sudoku(matrix));
		} catch (ConstraintViolationException e) {
			result = false;
		}
		return "{\"result\" : " + result + "}";
	}

	/**
	 * check wheter a particular value is the correct one for a particular cell
	 * of the puzzle
	 * 
	 * @param int[][]
	 *            matrix the matrix that represents the sudoku grid
	 * 
	 * @param Sudoku
	 *            sudoku the Sudoku object stored in the session
	 * 
	 * 
	 * @return String return the response as json string object
	 */

	@ResponseBody
	@RequestMapping("/checkCell")
	public String checkCell(@ModelAttribute("sudoku") Sudoku sudoku, @RequestBody int[] requestData) {

		int row = requestData[0];
		int column = requestData[1];
		int value = requestData[2];

		Cell cell = sudoku.getCell(row, column);
		boolean result = cell.getValue() == value;

		return "{\"result\" : " + result + ", \"value\": " + cell.getValue() + "}";
	}

	/**
	 * resolve the puzzle
	 * 
	 * @param Sudoku
	 *            puzzle the Sudoku puzzle object stored in the session
	 * 
	 * 
	 * @return String return the solution for the puzzle as array of arrays in a
	 *         json string object
	 */

	@ResponseBody
	@RequestMapping("/solvePuzzle")
	public String solvePuzzle(@ModelAttribute("puzzle") Sudoku puzzle) {
		manager.solve(puzzle);
		return "{\"matrix\" : " + this.toJson(puzzle.toArray()) + "}";
	}

	/**
	 * utility method to convert java object in json string object
	 * 
	 * @param Object
	 *            object java object you need to convert
	 * 
	 * @return String return the json string object
	 */

	private String toJson(Object object) {
		final StringWriter sw = new StringWriter();
		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(sw, object);
		} catch (Exception e) {
			return "error";
		}
		return sw.toString();
	}

}
