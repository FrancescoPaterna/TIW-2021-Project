/**
 * Student Management 
 */

(function () { // avoid variables ending up in the global scope

	// page components
	var courseList, courseDate, resultDetails, pageOrchestrator = new PageOrchestrator(); // main controller

	window.addEventListener("load", () => {
		if (sessionStorage.getItem("id") == null) {
			window.location.href = "index.html";
		} else {
			pageOrchestrator.start(); // initialize the components
			pageOrchestrator.refresh();
		} // display initial content
	}, false);


	// Constructors of view components

	function PersonalMessage(_username, _id, idcontainer, messagecontainer) {
		this.username = _username;
		this.id = _id;
		this.show = function () {
			messagecontainer.textContent = this.username;
			idcontainer.textContent = this.id;
		}
	}

	function CourseList(_alert, _id_courseStud, _id_courseStudBody) {
		this.alert = _alert;
		this.courseStud = _id_courseStud; // table of courses list
		this.courseStudBody = _id_courseStudBody; // table body of courses list
		this.current_courseList;

		this.reset = function () {
			this.courseStud.style.visibility = "hidden";
		}

		this.show = function () {
			var self = this; // used to refer to the current function from inner functions
			makeCall("GET", "GetCourseStud", null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
							// parse from JSON courses received by the server and show them
							var coursesToShow = JSON.parse(req.responseText);
							courseList.current_courseList = coursesToShow;
							if (coursesToShow.length == 0) {
								self.alert.textContent = "no courses entered yet";
								return;
							}
							self.update(coursesToShow); // self visible by closure
						}
						else {
							self.alert.textContent = message;
						}
					} 
				}
			);

		};

		this.update = function (courseList) {
			var row, destcell, linkcell, anchor;
			this.courseStudBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this; // used to refer to the current function from inner functions
			courseList.forEach(function (course) { // self visible here, not this
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = course.id;
				row.appendChild(destcell);
				linkcell = document.createElement("td");
				anchor = document.createElement("a");
				linkcell.appendChild(anchor);
				linkText = document.createTextNode(course.name);
				anchor.appendChild(linkText);
				//anchor.missionid = mission.id; // make list item clickable
				anchor.setAttribute('course_id', course.id);
				anchor.setAttribute('coursename', course.name); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					courseDate.show(e.target.getAttribute("course_id"),
						e.target.getAttribute("coursename")); // the list must know the details container
					self.waiter(course);
				}, false); //TODO Repeat bubbling? 
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseStudBody.appendChild(row);

			});
			this.courseStud.style.visibility = "visible";
		}

		this.waiter = function (course) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseStudBody.innerHTML = ""; // empty the table body
			// build updated list

			// self visible here, not this
			row = document.createElement("tr");
			destcell = document.createElement("td");
			destcell.textContent = course.id;
			row.appendChild(destcell);
			destcell = document.createElement("td");
			destcell.textContent = course.name;
			row.appendChild(destcell);
			this.courseStudBody.appendChild(row);
			document.getElementById("showcourse").style.visibility = "visible";
			this.courseStud.style.visibility = "visible";
			document.getElementById("showcourse").addEventListener("click", (e) => {
				courseList.reset();
				//courseList.show();   OLD Mode
				courseList.update(courseList.current_courseList);   //Async 2.0 MODE
				courseDate.reset();
				resultDetails.reset();
				document.getElementById("showdate").style.visibility = "hidden";
				document.getElementById("showcourse").style.visibility = "hidden";
			}, false); //TODO Repeat bubbling?
		}
	}

	function CourseDate(_alert, _id_courseDateStud, _id_courseDateStudBody) {
		this.alert = _alert;
		this.courseDateStud = _id_courseDateStud;
		this.courseDateStudBody = _id_courseDateStudBody;
		this.course_id;
		this.coursename;
		this.current_course;
		this.currentDateList;

		this.reset = function () {
			this.courseDateStud.style.visibility = "hidden";
		}

		this.show = function (course_id, coursename) {
			if (course_id == this.current_course) {      //ASYNC 2.0
				this.update(this.currentDateList);
				return;
			}
			this.course_id = course_id;
			this.coursename = coursename;
			var self = this;
			makeCall("GET", "GetCourseDateStud?course_id=" + course_id, null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
							var courseDates = JSON.parse(req.responseText);
							courseDate.currentDateList = courseDates;
							if (courseDates.length == 0) {
								self.alert.textContent = "no exam dates available";
								return;
							}
							self.update(courseDates); // self visible by closure
						}
						else {
							self.alert.textContent = message;
						}
					} 
				}
			);

		};

		this.update = function (examdates) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseDateStudBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this;
			examdates.forEach(function (examdate) { // self visible here, not this
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = examdate.ID;
				row.appendChild(destcell);
				linkcell = document.createElement("td");
				anchor = document.createElement("a");
				linkcell.appendChild(anchor);
				linkText = document.createTextNode(examdate.data);
				anchor.appendChild(linkText);
				anchor.setAttribute('exam_date_id', examdate.ID);
				anchor.setAttribute('exam_date', examdate.data);
				anchor.setAttribute('course_id', self.course_id);
				anchor.setAttribute('coursename', self.coursename); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					courseDate.waiter(examdate);
					resultDetails.reset();
					resultDetails.show(e.target.getAttribute("exam_date_id"),
						e.target.getAttribute("exam_date"),
						e.target.getAttribute("course_id"),
						e.target.getAttribute("coursename")); // the list must know the details container

				}, false);
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseDateStudBody.appendChild(row);

			});
			this.courseDateStud.style.visibility = "visible";
		}

		this.waiter = function (examdate) {
			var row, destcell, linkcell, anchor;
			this.courseDateStudBody.innerHTML = "";
			row = document.createElement("tr");
			destcell = document.createElement("td");
			destcell.textContent = examdate.ID;
			row.appendChild(destcell);
			destcell = document.createElement("td");
			destcell.textContent = examdate.data;
			row.appendChild(destcell);
			this.courseDateStudBody.appendChild(row);
			document.getElementById("showdate").style.visibility = "visible";
			this.courseDateStud.style.visibility = "visible";
			document.getElementById("showdate").addEventListener("click", (e) => {
				courseDate.reset();
				//courseDate.show(this.current_course); OLD MODE
				courseDate.update(courseDate.currentDateList);  //Async 2.0 MODE
				resultDetails.reset();
				document.getElementById("showdate").style.visibility = "hidden";
			}, false); //TODO Repeat bubbling?
		}
	}

	function ResultDetails(_alert, _id_resultDetails, _id_resultDetailsBody) {
		this.alert = _alert;
		this.alertNoResults = document.getElementById("id_alertNoResults");
		this.resultDetails = _id_resultDetails;
		this.resultDetailsBody = _id_resultDetailsBody;
		this.option = document.getElementById("opt");
		this.option_button = document.getElementById("opt_button");
		this.exam_date_id;
		this.date;
		this.course_id;
		this.coursename;
		//this.result;
		this.refuseScoreData = document.getElementById("refuseScoreData");
		this.refuseInput = document.getElementById("refuseScoreInput");
		this.refuseForm = document.getElementById("refuseScoreForm");
		this.okayRef = document.getElementById("okRefusedMessage");

		this.reset = function () {
			this.alertNoResults.textContent = "";
			this.resultDetails.style.visibility = "hidden";
			this.resultDetailsBody.style.visibility = "hidden";
			this.okayRef.textContent = "";
			this.option.textContent = "";
			this.removeColumn();
			//console.log(resultDetails.result);
		}

		this.show = function (exam_date_id, exam_date, course_id, coursename) {
			this.exam_date_id = exam_date_id;
			this.date = exam_date;
			this.course_id = course_id;
			this.coursename = coursename;
			var self = this;
			makeCall("GET", "GetResultDetails?IDExamDate=" + exam_date_id, null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
							var resultDet = JSON.parse(req.responseText);
							//resultDetails.result = resultDet;
							self.update(resultDet); // self visible by closure
						} else if (req.status == 204) {
							self.alertNoResults.textContent = "The result has not been published yet!";
							return;
						}
						else {
							self.alert.textContent = message;
						}
					} 
				}
			);
		}

		this.update = function (result) {
			var elem, row, destcell, input, form;
			destcell = document.getElementById("IDStudent");
			destcell.textContent = result.IDstudent;
			destcell = document.getElementById("name");
			destcell.textContent = result.name;
			destcell = document.getElementById("surname");
			destcell.textContent = result.surname;
			destcell = document.getElementById("coursedeg");
			destcell.textContent = result.courseDeg;
			destcell = document.getElementById("date");
			destcell.textContent = this.date;
			destcell = document.getElementById("course_id");
			destcell.textContent = this.course_id;
			destcell = document.getElementById("coursename");
			destcell.textContent = this.coursename;
			destcell = document.getElementById("score");
			destcell.textContent = result.mark;
			destcell = document.getElementById("state");
			destcell.textContent = result.status;

			if (this.isRefusable(result.mark, result.status)) {
				//generate the column line on the table
				resultDetails.option.textContent = "option";
				resultDetails.refuseForm.style.visibility = "visible";

				// Show Refuse button and load the exam date id value
				// It will also generate the column option in the table
				resultDetails.refuseScoreData.setAttribute("name", "IDExamDate");
				resultDetails.refuseInput.setAttribute("name", "IDExamDate");
				resultDetails.refuseInput.setAttribute("value", "Refuse");
				resultDetails.refuseScoreData.setAttribute("value", resultDetails.exam_date_id);
				resultDetails.refuseInput.setAttribute("class", "refuse_score");
				resultDetails.refuseInput.setAttribute("type", "button");
				resultDetails.refuseInput.addEventListener("click", (e) => {
					resultDetails.refuseScore(e.target.closest("form"));
				}, false);

			}
			//make details visible
			this.resultDetails.style.visibility = "visible";
			this.resultDetailsBody.style.visibility = "visible";
		}

		this.isRefusable = function (mark, status) {
			if (status == "PUBLISHED") {
				if (mark != "30L" && isNaN(mark)) return false;
				if ((mark >= 18 && mark <= 30) || (mark == "30L")) {
					return true;
				} else return false;
			} else return false;
		}

		this.refuseScore = function (form) {
			var self = this; // used to refer to the current function from inner functions
			if (form.checkValidity()) {
				makeCall("POST", 'UpdateResultStud', form,
					function (req) {
						if (req.readyState == XMLHttpRequest.DONE) {
							var message = JSON.parse(req.responseText);
							if (req.status == 200) {
								resultDetails.reset();
								resultDetails.update(message);
								resultDetails.okayRef.textContent = "Score Refused!"

							} else {
								self.alert.textContent = message;
								self.reset();
							}
						}
					}
				);
			} else {
				form.reportValidity();
			}

		}

		this.removeColumn = function () {
			resultDetails.option.textContent = "";
			resultDetails.refuseForm.style.visibility = "hidden";
			resultDetails.refuseInput.setAttribute("type", "hidden");

		}

	}

	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function () {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			var id = sessionStorage.getItem('id');
			personalMessage = new PersonalMessage(user, id, document.getElementById("id"), document.getElementById("id_username"));
			personalMessage.show();

			courseList = new CourseList(
				alertContainer,
				document.getElementById("id_courseStud"),
				document.getElementById("id_courseStudBody"));


			courseDate = new CourseDate(
				alertContainer,
				document.getElementById("id_courseDateStud"),
				document.getElementById("id_courseDateStudBody")
			)

			resultDetails = new ResultDetails(
				alertContainer,
				document.getElementById("id_resultDetails"),
				document.getElementById("id_resultDetailsBody")
			)
			
			// remove user data from session storage when logout is clicked
			document.getElementById("logoutButton").addEventListener("click", () => {
	        	window.sessionStorage.removeItem('id');
				window.sessionStorage.removeItem('name');
				window.sessionStorage.removeItem('surname');
				window.sessionStorage.removeItem('role');
	      	});
		}
		
		this.refresh = function () {
			alertContainer.textContent = "";
			courseList.reset();
			courseDate.reset();
			resultDetails.reset();
			courseList.show(); // closure preserves visibility of this
		};
	}
})();