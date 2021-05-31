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

	function PersonalMessage(_username, messagecontainer) {
		this.username = _username;
		this.show = function() {
			messagecontainer.textContent = this.username;
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

	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			personalMessage = new PersonalMessage(user, document.getElementById("id_username"));
			personalMessage.show();

			courseList = new CourseList(
				alertContainer,
				document.getElementById("id_courseStud"),
				document.getElementById("id_courseStudBody"));
		}
		
		this.refresh = function(currentCourse) {
			alertContainer.textContent = "";
			courseList.reset();
			courseList.show(); // closure preserves visibility of this
		};
	}
})();