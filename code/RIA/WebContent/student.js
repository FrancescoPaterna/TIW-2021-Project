/**
 * Student Managment 
 */

(function() { // avoid variables ending up in the global scope

	// page components
	var courseList, pageOrchestrator = new PageOrchestrator(); // main controller

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
		this.show = function() {
			messagecontainer.textContent = this.username;
			idcontainer.textContent = this.id;
		}
	}
	
	function CourseList(_alert, _id_courseStud, _id_courseStudBody) {
		this.alert = _alert;
		this.courseStud = _id_courseStud;
		this.courseStudBody = _id_courseStudBody;
		
		this.reset = function() {
			this.courseStud.style.visibility = "hidden";
		}
		
		this.show = function() {
			var self = this; // used to refer to the current function from inner functions
			makeCall("GET", "GetCourseStud", null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var CoursesToShow = JSON.parse(req.responseText);
							console.log('OI');
							if (CoursesToShow.length == 0) {
								self.alert.textContent = "no courses entered yet";
								return;
							}
							self.update(CoursesToShow); // self visible by closure
						}
					} else {
						self.alert.textContent = message;
					}
				}
			);

		};
		
		this.update = function(courseList){
			var elem, i, row, destcell, linkcell, anchor;
			this.courseStudBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this; // used to refer to the current function from inner functions
			courseList.forEach(function(course) { // self visible here, not this
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
				anchor.setAttribute('course_id', course.id); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					courseDate.show(e.target.getAttribute("course_id")); // the list must know the details container
				}, false); //TODO Repeat bubbling? 
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseStudBody.appendChild(row);

			});
			this.courseStud.style.visibility = "visible";
		}
	}
	
	function CourseDate(_alert, _id_courseDateStud, _id_courseDateStudBody) {
		this.alert = _alert;
		this.courseDateStud = _id_courseDateStud;
		this.courseDateStudBody = _id_courseDateStudBody;

		this.reset = function() {
			this.courseDateStud.style.visibility = "hidden";
		}

		this.show = function(course_id) {
			var self = this;
			makeCall("GET", "GetCourseDateStud?course_id=" + course_id, null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var courseDates = JSON.parse(req.responseText);
							if (courseDates.length == 0) {
								self.alert.textContent = "no exam dates available";
								return;
							}
							self.update(courseDates); // self visible by closure
						}
					} else {
						self.alert.textContent = message;
					}
				}
			);

		};

		this.update = function(courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseDateStudBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this;
			courlist.forEach(function(examdates) { // self visible here, not this
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = examdates.ID;
				row.appendChild(destcell);
				/*destcell = document.createElement("td");
				destcell.textContent = session.data;
				row.appendChild(destcell);
				self.courseDateProBody.appendChild(row);*/
				linkcell = document.createElement("td");
				anchor = document.createElement("a");
				linkcell.appendChild(anchor);
				linkText = document.createTextNode(examdates.data);
				anchor.appendChild(linkText);
				//anchor.missionid = mission.id; // make list item clickable
				anchor.setAttribute('exam_date_id', examdates.ID); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					sessionEnrolls.show(e.target.getAttribute("exam_date_id")); // the list must know the details container
				}, false); //TODO Repeat bubbling? 
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseDateStudBody.appendChild(row);

			});
			this.courseDateStud.style.visibility = "visible";
		}
	}

	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			var id = sessionStorage.getItem('id');
			personalMessage = new PersonalMessage(user, id, document.getElementById("id"), document.getElementById("id_username"));
			personalMessage.show();

			courseList = new CourseList(
				alertContainer,
				document.getElementById("id_courseStud"),
				document.getElementById("id_courseStudBody"));
		}
		
		courseDate = new CourseDate(
			alertContainer,
			document.getElementById("id_courseDateStud"),
			document.getElementById("id_courseDateStudBody")
		)
		
		this.refresh = function(currentCourse) {
			alertContainer.textContent = "";
			courseList.reset();
			courseList.show(); // closure preserves visibility of this
		};
	}
})();