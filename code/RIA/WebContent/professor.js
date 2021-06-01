/*
* Professor Managment
*/

(function() { // avoid variables ending up in the global scope

	// page components
	var courseList, courseDate, sessionEnrolls, pageOrchestrator = new PageOrchestrator(); // main controller

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

	function CourseList(_alert, _id_coursePro, _id_courseProBody) {
		this.alert = _alert;
		this.coursePro = _id_coursePro;
		this.courseProBody = _id_courseProBody;

		this.reset = function() {
			this.coursePro.style.visibility = "hidden";
		}

		this.show = function() {
			var self = this;
			makeCall("GET", "GetCoursePro", null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var CoursesToShow = JSON.parse(req.responseText);
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

		this.update = function(courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseProBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this;
			courlist.forEach(function(course) { // self visible here, not this
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
				self.courseProBody.appendChild(row);

			});
			this.coursePro.style.visibility = "visible";
		}
	}

	function CourseDate(_alert, _id_courseDatePro, _id_courseDateProBody) {
		this.alert = _alert;
		this.courseDatePro = _id_courseDatePro;
		this.courseDateProBody = _id_courseDateProBody;

		this.reset = function() {
			this.courseDatePro.style.visibility = "hidden";
		}

		this.show = function(course_id) {
			var self = this;
			makeCall("GET", "GetCourseDatePro?course_id=" + course_id, null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var CoursesDates = JSON.parse(req.responseText);
							if (CoursesDates.length == 0) {
								self.alert.textContent = "no exam dates available";
								return;
							}
							self.update(CoursesDates); // self visible by closure
						}
					} else {
						self.alert.textContent = message;
					}
				}
			);

		};

		this.update = function(courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseDateProBody.innerHTML = ""; // empty the table body
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
				self.courseDateProBody.appendChild(row);

			});
			this.courseDatePro.style.visibility = "visible";
		}
	}

	function SessionEnrolls(_alert, _id_sessionEnrolls, _id_sessionEnrollsBody) {
		this.alert = _alert;
		this.sessionEnrolls = _id_sessionEnrolls;
		this.sessionEnrollsBody = _id_sessionEnrollsBody;

		this.reset = function() {
			this.sessionEnrolls.style.visibility = "hidden";
		}

		this.show = function(exam_date_id) {
			var self = this;
			makeCall("GET", "GetSessionEnrolls?exam_date_id=" + exam_date_id, null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var enrolls = JSON.parse(req.responseText);
							if (enrolls.length == 0) {
								self.alert.textContent = "no student enrolled";
								return;
							}
							self.update(enrolls); // self visible by closure
						}
					} else {
						self.alert.textContent = message;
					}
				}
			);

		};

		this.update = function(courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.sessionEnrollsBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this;
			courlist.forEach(function(examdates) { // self visible here, not this
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = examdates.IDstudent;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.name;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.surname;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.mail;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.courseDeg;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.mark;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = examdates.status;
				row.appendChild(destcell);
				destcell = document.createElement("td");
	


				if(isModifible(examdates.status.trim())) {
					elem = document.createElement("button");
					elem.classList.add("smodify");
					elem.textContent = "Modify";
					destcell.appendChild(elem);
				}
				else {
					elem = document.createElement("button");
					elem.classList.add("smodifylo");
					elem.textContent = "Modify";
					destcell.appendChild(elem);
				}
				row.appendChild(destcell);

				self.sessionEnrollsBody.appendChild(row);

			});
			this.sessionEnrolls.style.visibility = "visible";
		}

	}

	function isModifible(status) {
		if (status == "NOT_INSERTED" || status == "INSERTED" || status == "PUBLISHED")
			return true
	}


	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			var id = sessionStorage.getItem('id')
			personalMessage = new PersonalMessage(user, id, document.getElementById("id"), document.getElementById("id_username"));
			personalMessage.show();

			courseList = new CourseList(
				alertContainer,
				document.getElementById("id_coursePro"),
				document.getElementById("id_courseProBody"));
		}

		courseDate = new CourseDate(
			alertContainer,
			document.getElementById("id_courseDatePro"),
			document.getElementById("id_courseDateProBody")
		)

		sessionEnrolls = new SessionEnrolls(
			alertContainer,
			document.getElementById("id_sessionEnrolls"),
			document.getElementById("id_sessionEnrollsBody")

		)

		this.refresh = function(currentCourse) {
			alertContainer.textContent = "";
			courseList.reset();
			courseList.show(); // closure preserves visibility of this
		};
	}
})();
