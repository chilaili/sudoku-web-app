//wait untill all elements are loaded in the DOM and then attachs events
$(document).ready(function() {

	$('#checkAnswers').on('click', function() {
		checkCorrectAnswers();
	});

	$('#solvePuzzle').on('click', function() {
		solvePuzzle();
	});

	$('#resetter').on('click', function() {
		resetTable();
	})
});




//send AJAX request to check if the number inserted in the cell is correct or not
function checkCell(input) {

	var active = $('#helpme').is(':checked');

	if (active) {
		
		var rowIndex = input.parentElement.parentElement.rowIndex;
		var columnIndex = input.parentElement.cellIndex;
		var value = input.value;
		
		var requestData = [ rowIndex, columnIndex, input.value ];

		$.ajax({
			type : "POST",
			url : '/sudoku/checkCell',
			data : JSON.stringify(requestData),
			contentType : "application/json; charset=utf-8",
			dataType : "text json",
			success : function(response) {
				if (!response.result){
					showHelpNumber(input, response.value);
				}
			},
			error : function(e) {
				alert('Error: ' + e.responseText);
			}
		});
	}
}





//send an AJAX request to solve automatically the puzzle and then fills the table
function solvePuzzle() {

	var matrix = getMatrix();

	$.ajax({
		type : "POST",
		url : '/sudoku/solvePuzzle',
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(response) {
			fillTable(response.matrix);
		},
		error : function(e) {
			alert('error server side while solving puzzle: please fix it as soon as possible');
		}
	});
}








//send a AJAX request to check whether the filled in answer are correct or not
function checkCorrectAnswers() {

	if (checkNoEmptyCells()){
		var matrix = getMatrix();

		$.ajax({
			type : "POST",
			url : '/sudoku/checkCorrectAnswers',
			// JSON.stringify(data)
			data : JSON.stringify(matrix),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(response) {
				showAnswer(response.result)
			},
			error : function(e) {
				alert(e.message);
			}
		});
	}
	
}







//show the correct number in the cell input as suggestion
function showHelpNumber(input, number){
	
	var $myInput = $(input); 
	var userInput = $myInput.val();
	
	$myInput.attr('class','error');
	$myInput.val(number);
	
	setTimeout(function() {
		$myInput.attr('class','');
		$myInput.val(userInput);
	}, 400);
}




//checks if the table hasn't empty cells
function checkNoEmptyCells() {

	var rows = $('#sudokutable tbody >tr');
	var columns;
	for (var i = 0; i < rows.length; i++) {
		columns = $(rows[i]).find('td');
		for (var j = 0; j < columns.length; j++) {
			var cell = $(columns[j]).find('input')[0];
			if (cell.value == undefined || cell.value == null || cell.value == '') {
				display('you must fill all the puzzle to check answers','error');
				return false
			}
		}
	}

	return true;
}



//get the values in the table and creates the relative matrix
function getMatrix() {

	var matrix = new Array(9);

	var rows = $('#sudokutable tbody >tr');
	var columns;
	for (var i = 0; i < rows.length; i++) {
		matrix[i] = new Array(9);
		columns = $(rows[i]).find('td');
		for (var j = 0; j < columns.length; j++) {
			var cell = $(columns[j]).find('input')[0];
			matrix[i][j] = cell.value;
		}
	}
	return matrix;
}





//display in a DIV the result of the AJAX request checkCorrectAnswers
function showAnswer(success) {
	var $display = $('#display');

	var cssClass = success ? 'success' : 'error';
	var text = success ? 'congratulation, that\'s the right answer.'
			: 'this is not a valid answer, try again!';
	display(text, cssClass)
}




//fill the sudoku table with the values in an array of arrays
function fillTable(matrix) {

	var rows = $('#sudokutable tbody >tr');
	var columns;
	for (var i = 0; i < rows.length; i++) {
		columns = $(rows[i]).find('td');
		for (var j = 0; j < columns.length; j++) {
			$(columns[j]).find('input')[0].value = matrix[i][j];
		}
	}
}




//general function to display messages with relative style
function display(text, className){
	var $display = $('#display');
	$display.attr('class', className);
	$display.html('<h1>' + text + '</h1>');
	
	setTimeout(function() {
		resetDisplay();
	}, 5000);
}

//reset the display
function resetDisplay(){
	var $display = $('#display');
	$display.attr('class', '');
	$display.html('');
}




//clean all the input inserted in the puzzle by the user
function resetTable() {

	var rows = $('#sudokutable tbody >tr');
	var columns;
	for (var i = 0; i < rows.length; i++) {
		columns = $(rows[i]).find('td');
		for (var j = 0; j < columns.length; j++) {
			var cell = $(columns[j]).find('input')[0];
			if (!cell.readOnly) {
				cell.value = '';
			}
		}
	}
	
	resetDisplay();
	
	
}


//prevent to insert illegal values in the sudoku puzzle cells
function isNumberKey(evt) {
	var charCode = (evt.which) ? evt.which : evt.keyCode
	return !(charCode > 31 && (charCode <= 48 || charCode > 57));
}