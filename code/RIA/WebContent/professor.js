/*
* Professor Managment
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

	function CourseList(_alert, _id_coursePro, _id_courseProBody) {
		this.alert = _alert;
		this.coursePro = _id_coursePro;
		this.courseProBody = _id_courseProBody;

		this.reset = function() {
			this.coursePro.style.visibility = "hidden";
		}

		this.show = function(next) {
			var self = this;
			makeCall("GET", "GetCoursePro", null,
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
							if (next) next(); // show the default element of the list if present
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
				destcell = document.createElement("td");
				destcell.textContent = course.name;
				row.appendChild(destcell);
				self.coursePro.appendChild(row);

			});
			this.coursePro.style.visibility = "visible";
		}
	}


	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			personalMessage = new PersonalMessage(user, document.getElementById("id_username"));
			personalMessage.show();
			console.log('MIXO');

			courseList = new CourseList(
				alertContainer,
				document.getElementById("id_coursePro"),
				document.getElementById("id_courseProBody"));
		}

		this.refresh = function(currentCourse) {
			alertContainer.textContent = "";
			courseList.reset();
			courseList.show(); // closure preserves visibility of this
		};
	}
})();
