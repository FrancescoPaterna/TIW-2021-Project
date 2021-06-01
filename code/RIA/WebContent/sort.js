// Returns the text content of a cell.
var asc = true;
var old;

function getCellValue(tr, idx) {
	return tr.children[idx].textContent; // idx indexes the columns of the tr row
}

function resetArrows(rowHeaders) {
	for (let j = 0; j < rowHeaders.length; j++) {
		var toReset = rowHeaders[j].querySelectorAll("span");
		for (let i = 0; i < toReset.length; i++) {
			toReset[i].className = "normalarrow";
		}
	}
}

function changeArrow(th) {
	var toChange = asc ? th.querySelector("span:first-child") : th.querySelector("span:last-child");
	toChange.className = "boldarrow";
}
/*
* Creates a function that compares two rows based on the cell in the idx
* position.
*/
function createComparer(idx, asc) {
	return function(rowa, rowb) {
		// get values to compare at column idx
		// if order is ascending, compare 1st row to 2nd , otherwise 2nd to 1st
		var v1 = getCellValue(asc ? rowa : rowb, idx),
			v2 = getCellValue(asc ? rowb : rowa, idx);
		// If non numeric value
		if (v1 === '' || v2 === '' || isNaN(v1) || isNaN(v2)) {
			return v1.toString().localeCompare(v2); // lexical comparison
		}
		// If numeric value
		return v1 - v2; // v1 greater than v2 --> true
	};
}

// For all table headers f class sortable
function sortTable(clicked_id) {
	if (clicked_id != old) {
		asc = true;
	}
	var th = document.getElementById(clicked_id);
	var table = th.closest('table'); // get the closest table tag
	var rowHeaders = table.querySelectorAll('th');
	var columnIdx = Array.from(rowHeaders).indexOf(th);
	// For every row in the table body
	// Use Array.from to build an array from table.querySelectorAll result
	// which is an Array Like Object (see DOM specifications)
	var rowsArray = Array.from(table.querySelectorAll('tbody > tr'));
	// sort rows with the comparator function passing
	// index of column to compare, sort criterion asc or desc)
	rowsArray.sort(createComparer(columnIdx, asc));
	// Change arrow colors
	resetArrows(rowHeaders);
	changeArrow(th);
	//  Toggle the criterion
	old = clicked_id;
	asc = !asc;
	// Append the sorted rows in the table body
	for (var i = 0; i < rowsArray.length; i++) {
		table.querySelector('tbody').appendChild(rowsArray[i]);
	}
}

// For all table headers f class sortable
function sortCustom(clicked_id) {
	if (clicked_id != old) {
		asc = true;
	}
	var th = document.getElementById(clicked_id);
	var table = th.closest('table'); // get the closest table tag
	var rowHeaders = table.querySelectorAll('th');
	var columnIdx = Array.from(rowHeaders).indexOf(th);
	// For every row in the table body
	// Use Array.from to build an array from table.querySelectorAll result
	// which is an Array Like Object (see DOM specifications)
	var rowsArray = Array.from(table.querySelectorAll('tbody > tr'));
	// sort rows with the comparator function passing
	// index of column to compare, sort criterion asc or desc)
	rowsArray.sort(createCustomComparer(columnIdx, asc));
	// Change arrow colors
	resetArrows(rowHeaders);
	changeArrow(th);
	//  Toggle the criterion
	old = clicked_id;
	asc = !asc;

	// Append the sorted rows in the table body
	for (var i = 0; i < rowsArray.length; i++) {
		table.querySelector('tbody').appendChild(rowsArray[i]);
	}
}

/*
* Creates a function that compares two rows based on the cell in the idx
* position.
*/
function createCustomComparer(idx, asc) {
	return function(rowa, rowb) {
		// get values to compare at column idx
		// if order is ascending, compare 1st row to 2nd , otherwise 2nd to 1st
		var v1 = getCellValue(asc ? rowa : rowb, idx),
			v2 = getCellValue(asc ? rowb : rowa, idx);


		if (isNaN(v1) || isNaN(v2)) {

			// NULL COMPARE
			/*********************************************************** */
			if (v1 == "30L" || v2 == "30L") {
				if (v1 == "30L" && v2 == "30L") {
					return false;
				}
				if (v1 == "30L" && isNaN(v2)) {
					return true;
				}
				if (v2 == "30L" && isNaN(v1)) {
					return false;
				}
				if (v1 == "30L" && !isNaN(v2)) {
					return true;
				}
				if (v2 == "30L" && !isNaN(v1)) {
					return false;
				}
			}


			// NULL COMPARE
			/*********************************************************** */
			if (v1 === ' ' || v2 === '') {

				if (v1 === '' && isNaN(v2)) {
					return false;
				}
				if (v2 === '' && isNaN(v1)) {
					return true;
				}

			}


			// STRING COMPARE
			/*********************************************************** */
			if (isNaN(v1) && isNaN(v2)) {
				return v1.toString().localeCompare(v2); // lexical comparison
			}

			// MIX COMPARE
			/************************************************************ */
			if (!isNaN(v1) && isNaN(v2)) {
				return true;
			}
			if (!isNaN(v2) && isNaN(v1)) {
				return false;
			}

		}

		// NUM COMPARE
		/*********************************************************** */
		else {
			return v1 - v2; // v1 greater than v2 --> true
		}
	};

}




//In the old code the tutor doesn't need the on.click in HomePro.html! 

/**********************************************************OLD CODE*********************************************/

// TABLE SORTING MANAGEMENT FUNCTIONS

/*
 * Self invoking unnamed function. This generates a scope around the code which
 * causes variables and functions not to end up in the global scope.
 */



/*HERE*****************************************************************************

(function() {

  // Returns the text content of a cell.
  function getCellValue(tr, idx) {
	return tr.children[idx].textContent; // idx indexes the columns of the tr
	// row
  }


***************************HERE**************************************************/


/*
   * Creates a function that compares two rows based on the cell in the idx
   * position.
   */


/*HERE*****************************************************************************
  function createComparer(idx, asc) {
	return function(a, b) {
	  // get values to compare at column idx
	  // if order is ascending, compare 1st row to 2nd , otherwise 2nd to 1st
	  var v1 = getCellValue(asc ? a : b, idx),
		v2 = getCellValue(asc ? b : a, idx);
	  // If non numeric value
	  if (v1 === '' || v2 === '' || isNaN(v1) || isNaN(v2)) {
		return v1.toString().localeCompare(v2); // lexical comparison
	  }
	  // If numeric value
	  return v1 - v2; // v1 greater than v2 --> true
	};
  }

  // For all table headers f class sortable
  document.querySelectorAll('th.sortable').forEach(function(th) {
	// Add a listener on the click event
	th.addEventListener('click', function () {
	  var table = th.closest('table'); // get the closest table tag
	  // For every row in the table body
	  // Use Array.from to build an array from table.querySelectorAll result
	  // which is an Array Like Object (see DOM specifications)
	  Array.from(table.querySelectorAll('tbody > tr'))
		// Toggle the criterion and to sort rows with the comparator function
		// passing
		// (index of column to compare, sort criterion asc or desc) --this is
		// the the
		// element
		.sort(createComparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
		// Append the sorted rows in the table body
		.forEach(function(tr) {
		  table.querySelector('tbody').appendChild(tr)
		});
	});
  });
})(); // evaluate the function after its definition

*****************************************HERE/
**********************************************************OLD CODE*********************************************/



/*
 * ULTRA COMPACT VERSION
 *
 * const getCellValue = (tr, idx) => tr.children[idx].textContent;
 *
 * const comparer = (idx, asc) => (a, b) => ((v1, v2) => v1 !== '' && v2 !== '' &&
 * !isNaN(v1) && !isNaN(v2) ? v1 - v2 : v1.toString().localeCompare(v2)
 * )(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));
 *
 *
 * document.querySelectorAll('th.sortable').forEach(th =>
 * th.addEventListener('click', (() => { const table = th.closest('table');
 * Array.from(table.querySelectorAll('tbody > tr'))
 * .sort(comparer(Array.from(th.parentNode.children).indexOf(th), this.asc =
 * !this.asc)) .forEach(tr => table.querySelector('tbody').appendChild(tr) );
 * })));
 *
 * ADAPTED FROM:
 * https://stackoverflow.com/questions/14267781/sorting-html-table-with-javascript
 */


