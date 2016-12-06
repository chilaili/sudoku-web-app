var data = [[1,2],[3,4],[5,6],[7,7]];

$( document ).ready(function() {
	$('#gio').on('click', function(){
		checkCorrectAnswers();
	})
	
	$('#pluto').on('click', function(){
		solvePuzzle();
	})
});

function test(){
	alert(getTableCellValue(1,3));
}

function getTableCellValue(i,j){
	return $('#matrix['+i+']['+j+']').val();
}

function checkCell(input){
	
	var active = $('#helpme').checked;
	
	if (active){
	var requestData = [input.parentElement.parentElement.rowIndex, input.parentElement.cellIndex, input.value];
	
	$.ajax({
		  type: "POST",
		  url: '/sudoku/checkCell',
		  data: JSON.stringify(requestData),
		  contentType: "application/json; charset=utf-8",
	      dataType: "text json",
		  success : function(e) {
		       alert(e.responseText);
		    },
		  error : function(e) {
		       alert('Error: ' + e.responseText);
		  }
		});
	}}

function generatTable(){
	
	var rows = $('#sudokutable tbody >tr');
    var columns;
    for (var i = 0; i < rows.length; i++) {
        columns = $(rows[i]).find('td');
        for (var j = 0; j < columns.length; j++) {
            console.log($(columns[j]).html());
        }
    }
}

function checkNoEmptyCells(){
	
	var rows = $('#sudokutable tbody >tr');
    var columns;
    for (var i = 0; i < rows.length; i++) {
        columns = $(rows[i]).find('td');
        for (var j = 0; j < columns.length; j++) {
            if (columns[j].value == undefined || columns[j] == null){
            	return false
            }
        }
    }
    
    return true;
}


function checkCorrectAnswers(){

		var matrix = $('#matrix'); 
    	$.ajax({
    		  type: "POST",
    		  url: '/sudoku/checkCorrectAnswers',
    		  //JSON.stringify(data)
    		  data: JSON.stringify(matrix),
    		  contentType: "application/json; charset=utf-8",
    	      dataType: "json",
    		  success : function(response) {
    		       alert(response);
    		    },
    		    error : function(e) {
    		       alert('Error: ' + e);
    		    }
    		});
    	}

