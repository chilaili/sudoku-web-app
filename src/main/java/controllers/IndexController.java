package controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import sudoku.logic.Cell;
import sudoku.logic.Sudoku;
import sudoku.logic.SudokuManager;
import sudoku.logic.exceptions.ConstraintViolationException;

@Controller
@SessionAttributes("sudoku")
public class IndexController {

	@Resource
	private SudokuManager manager;

	// generates the puzzle with 33 givens and send the matrix in the response

	@RequestMapping("/")
	public String index(ModelMap modelMap) {

		Sudoku sudoku = manager.generateSudoku();
		Sudoku puzzle = manager.puzzle(sudoku, 33);

		modelMap.addAttribute("sudoku", sudoku);
		modelMap.addAttribute("matrix", puzzle.toArray());

		return "index";
	}

	@RequestMapping(value = "/checkCorrectAnswers", method = RequestMethod.POST)
	public String checkCorrectAnswers(ModelMap modelMap, @ModelAttribute("matrix") int[][] matrix) {
		try {
			manager.checkValidAnswers(new Sudoku(matrix));
			modelMap.addAttribute("message", "ok");
		} catch (ConstraintViolationException e) {
			modelMap.addAttribute("message", "not ok");
		}
		return "index";
	}

	@RequestMapping("/solvePuzzle")
	public String solvePuzzle(@ModelAttribute("puzzle") Sudoku puzzle, ModelMap modelMap) {

		manager.solve(puzzle);
		modelMap.addAttribute("matrix", puzzle.toArray());
		return "index";

	}

	@ResponseBody
	@RequestMapping("/checkCell")
	public String checkCell(@ModelAttribute("sudoku") Sudoku sudoku, @RequestBody int[] requestData) {

		int row = requestData[0];
		int column = requestData[1];
		int value = requestData[2];

		Cell cell = sudoku.getCell(row, column);
		boolean result = cell.getValue() == value;

		return "{result: '" + result + "', value: " + cell.getValue() + "}";
	}

}
