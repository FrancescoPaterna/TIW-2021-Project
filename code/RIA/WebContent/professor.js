/*
* Professor Managment
*/

(function () { // avoid variables ending up in the global scope

	// page components
	var currentExamDate, courseList, courseDate, sessionEnrolls,
		pageOrchestrator = new PageOrchestrator(); // main controller

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

	function CourseList(_alert, _id_coursePro, _id_courseProBody) {
		this.alert = _alert;
		this.coursePro = _id_coursePro;
		this.courseProBody = _id_courseProBody;

		this.reset = function () {
			this.coursePro.style.visibility = "hidden";
		}

		this.show = function () {
			var self = this;
			makeCall("GET", "GetCoursePro", null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
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

		this.update = function (courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseProBody.innerHTML = ""; // empty the table body
			// build updated list
			var self = this;
			courlist.forEach(function (course) { // self visible here, not this
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

		this.reset = function () {
			this.courseDatePro.style.visibility = "hidden";
		}

		this.show = function (course_id) {
			var self = this;
			makeCall("GET", "GetCourseDatePro?course_id=" + course_id, null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
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

		this.update = function (courlist) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseDateProBody.innerHTML = ""; // empty the table body = document.getElementById("modify_surname")
			// build updated list
			var self = this;
			courlist.forEach(function (examdates) { // self visible here, not this
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

		/***********************MODAL WINDOW*********************************************************************/
		this.modal_title = document.getElementById("modal_title");      //ModalTitle  
		this.modal_message = document.getElementById("modal_message");  //ModalMessage
		this.modal = document.getElementById("myModal");                //MainModal
		this.span = document.getElementsByClassName("close")[0];        //CloseButton

		//+++++++++++++++SINGLE MODIFY+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		this.single_modify_form = document.getElementById("sendNewScore"); //The Single Modify Form
		this.modify_handler_id = document.getElementById("id_stud");        //Insert The ID_STUD in the FORM
		this.modify_handler_examdate = document.getElementById("exam_date_id");   //Insert The Exam_Date_Id in the FORM
		this.sm_update = document.getElementById("update");    //The Button UPDATE

		/***************************************************************************FIRST TABLE SINGLE MODIFY*******/
		/**************/this.first_single_modify = document.getElementById("first_single_modify");  //Table
		/**************/this.f_id = document.getElementById("f_modify_idstud");
		/**************/this.f_name = document.getElementById("f_modify_name");
		/**************/this.f_surname = document.getElementById("f_modify_surname");
		/**************/this.f_email = document.getElementById("f_modify_email");
		/**************/this.f_coursedeg = document.getElementById("f_modify_coursedeg");
		/**************/this.f_score = document.getElementById("f_modify_score");
		/**************/this.f_status = document.getElementById("f_modify_status");

		/*****************************************modi**********************************SECOND TABLE SIGNLE MODIFY******/
		/**************/this.second_single_modify = document.getElementById("second_single_modify");  //Table
		/**************/this.s_id = document.getElementById("modify_id");
		/**************/this.s_name = document.getElementById("modify_name");
		/**************/this.s_surname = document.getElementById("modify_surname");
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//


		//+++++++++MULTIPLE MODIFY+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		this.multipleModalForm = document.getElementById("multipleModifyForm");
		/**********************************************************************************************************/


		//BUTTONS
		this.record_button = document.getElementById("record");
		this.multiple_modify_button = document.getElementById("multiple_modify");
		this.publish_button = document.getElementById("publish");
		//VAR
		var current_exam;

		this.reset = function () {
			this.sessionEnrolls.style.visibility = "hidden";
			this.record_button.style.visibility = "hidden";
			this.publish_button.style.visibility = "hidden";
			this.multiple_modify_button.style.visibility = "hidden";
			this.modal_title.textContent = "";
			this.modal_message.textContent = "";
			this.first_single_modify.style.visibility = "hidden";
			this.second_single_modify.style.visibility = "hidden";
			this.multipleModalForm.style.visibility = "hidden";

		}

		this.resetModalOnly = function () {
			//this.modal.style.visibility = "hidden";
			this.modal_title.textContent = "";
			this.modal_message.textContent = "";
			this.first_single_modify.style.visibility = "hidden";
			this.second_single_modify.style.visibility = "hidden";
			this.multipleModalForm.style.visibility = "hidden";
		}

		this.show = function (exam_date_id) {
			this.current_exam = exam_date_id;
			var self = this;
			makeCall("GET", "GetSessionEnrolls?exam_date_id=" + exam_date_id, null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
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
			this.sessionEnrolls.style.visibility = "visible";
			this.record_button.style.visibility = "visible";
			this.publish_button.style.visibility = "visible";
			this.multiple_modify_button.style.visibility = "visible";
		};

		this.update = function (courlist) {
			var elem, i, row, destcell, input, select, option, label, button;
			this.sessionEnrollsBody.innerHTML = ""; // empty the table body
			this.multipleModalForm.innerHTML = "";
			// build updated list
			var self = this;     //FIRST 
			courlist.forEach(function (examdates) { // self visible here, not this
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
				if (examdates.status == "NOT_INSERTED") {
					this.appendIfNotInserted(examdates);
				}


				var selfInModal = self;

				this.appendIfNotInserted = function (examdate) {
					rowModal = document.createElement("input");
					rowModal.setAttribute("type", "checkbox");
					rowModal.setAttribute("name", "IDStudent");
					rowModal.setAttribute("value", examdate.IDstudent);
					label = document.createElement("label");
					label.setAttribute("for", examdate.IDstudent);
					label.textContent = examdate.IDstudent;
					selfInModal.multipleModalForm.appendChild(label);
					selfInModal.multipleModalForm.appendChild(rowModal);
					selfInModal.multipleModalForm.appendChild(document.createElement("br"));
				}


				/*If a score == INSERTED OR NOT_INSERTED OR PUBLISH, Enable the Single Modify Button */
				if (self.isModifible(examdates.status.trim())) {
					elem = document.createElement("button");
					elem.classList.add("smodify");
					elem.textContent = "Modify";
					destcell.appendChild(elem);

					//SINGLE MODIFY EVENT LISTNER
					elem.addEventListener("click", (e) => {
						self.resetModalOnly;
						self.modal.style.display = "block";
						self.first_single_modify.style.visibility = "visible";
						self.second_single_modify.style.visibility = "visible";
						self.modify_handler_id.setAttribute("value", examdates.IDstudent);
						self.modify_handler_examdate.setAttribute("value", self.current_exam);
						//UPDATE THE MODAL WINDOW WITH UPDATE_SINGLE_MODIFIER FUNCTION
						self.update_single_modifier(examdates.IDstudent, examdates.name, examdates.surname,
							examdates.mail, examdates.mark, examdates.courseDeg, examdates.status);
						var self2 = self;
						//ENABLE THE CLOSE MODAL WINDOW BUTTON
						self.span.addEventListener("click", (c) => {
							self2.modal.style.display = "none";
						}, false);
					}, false);
					elem.href = "#";
					row.appendChild(destcell);
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

			self.multipleModalForm.appendChild(document.createElement("br"));

			select = document.createElement("select");
			select.required;
			select.setAttribute("name", "score");
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "");
			option.textContent = "";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "ABSENT");
			option.textContent = "ABSENT";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "RIMANDATO");
			option.textContent = "RIMANDATO";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "RITIRATO");
			option.textContent = "RITIRATO";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "18");
			option.textContent = "18";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "19");
			option.textContent = "19";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "20");
			option.textContent = "20";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "21");
			option.textContent = "21";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "22");
			option.textContent = "22";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "23");
			option.textContent = "23";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "24");
			option.textContent = "24";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "25");
			option.textContent = "25";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("value", "26");
			option.textContent = "26";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("value", "27");
			option.textContent = "27";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("value", "28");
			option.textContent = "28";
			select.appendChild(option);
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "29");
			option.textContent = "29";
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "30");
			option.textContent = "30";
			option = document.createElement("option");
			option.setAttribute("selected", "selected");
			option.setAttribute("value", "30L");
			option.textContent = "30L";
			self.multipleModalForm.appendChild(select);


			button = document.createElement("input");
			button.setAttribute("type", "button");
			button.setAttribute("name", "UPDATE");
			button.classList.add("smodify");



			selfInButton = self;

			button.addEventListener("click", (e) => {

				var form = e.target.closest("form");

				if (form.checkValidity()) {

					var array = $("input[name='IDStudent']:checked").map(function () {
						return this.value;
					}).get();

					var score = form.querySelector("select[name = 'score']").value;

					form = {
						"id_stud": array,
						"score": score,
						"exam_date_id": selfInButton.current_exam
					}

					form = JSON.stringify(form);
<<<<<<< HEAD


					var self = this;
					makeCallJSON("POST", 'UpdateMultipleScore', form,
						function (req) {
							if (req.readyState == 4) {
								var message = req.responseText;
								if (req.status == 200) {
									// TODO pageOrchestrator.refresh();
								} else {
									self.alert.textContent = message;
								}
							}
						}
					);
				} else {
					form.reportValidity();
				}
=======
					
		        	
						var self = this;
						makeCallJSON("POST", 'UpdateMultipleScore', form,
				            function(req) {
				              if (req.readyState == XMLHttpRequest.DONE) {
				                var message = req.responseText;
				                if (req.status == 200) {
				                  // TODO pageOrchestrator.refresh();
				                } else {
				                  self.alert.textContent = message;
				                }
				              }
				            }
						);
		        } else {
		          form.reportValidity();
		        }
>>>>>>> branch 'master' of https://github.com/FrancescoPaterna/TIW-2021-Project.git
			}, false);

			self.multipleModalForm.appendChild(button);

			var multipleModifyButton = document.getElementById("multiple_modify");
			multipleModifyButton.classList.add("modify");
			multipleModifyButton.addEventListener("click", (e) => {
				self.resetModalOnly;
				self.modal_title = "MULTIPLE MODIFY";
				self.modal.style.display = "block";
				self.multipleModalForm.style.visibility = "visible";
				var self2 = self;
				self.span.addEventListener("click", (c) => {
					// dependency close button
					self2.modal.style.display = "none";
				}, false);
			}, false);
		}



		this.isModifible = function (status) {
			if (status == "NOT_INSERTED" || status == "INSERTED" || status == "PUBLISHED")
				return true
		}

		// When the user clicks on <span> (x), close the modal
		this.CloseButton = function () {
			this.modal.style.display = "none";
		}

		// When the user clicks anywhere outside of the modal, close it
		/*this.window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}*/


		/* Update the parameters in the Single Modify Modal Window*/
		this.update_single_modifier = function (id, name, surname, email, score, coursedeg, status) {

			/****FIRST TABLE******/
			this.f_id.textContent = id;
			this.f_name.textContent = name;
			this.f_surname.textContent = surname;
			this.f_email.textContent = email;
			this.f_coursedeg.textContent = coursedeg;
			this.f_score.textContent = score;
			this.f_status.textContent = status;
			/****SECOND TABLE******/
			this.s_id.textContent = id;
			this.s_name.textContent = name;
			this.s_surname.textContent = surname;

			/*Insert the value in the first table in the Modal Window MODIFY*/
			this.resetModalOnly;

			// INSERT TITLE
			this.modal_title.textContent = "MODIFY SCORE";

			// INSERT MESSAGE
			this.modal_message.textContent = "Insert The Score";

			var self = this;
			/*Function That Support the UPDATE Button in Modal Windows SINGLE MODIFY*/
			this.sm_update.addEventListener('click', (event) => {
				makeCall("POST", "UpdateScore", event.target.closest("form"),
					function (req) {
						var self2 = self;
						if (req.readyState == 4) {
							var score_confirm = req.responseText;
							var self3 = self2;
							switch (req.status) {
								case 200:
									self3.f_score.textContent = score_confirm;
									self3.f_status = "INSERTED";
									self3.reset();
									self3.show();
									console.log(score_confirm + " --> SCORE UPDATED!");
									break;
								case 400: // bad request
									break;
								case 401: // unauthorized
									break;
								case 500: // server error
									break;
							}
						}
					}
				);
			});

		}



		/*Function That Support the PUBLISH button*/
		document.getElementById("publish").addEventListener('click', (event) => {
			makeCall("POST", "UpdateStatus", event.target.closest("form"),
				function (req) {
					if (req.readyState == 4) {
						switch (req.status) {
							case 200:
								console.log("SCORE PUBLISHED!");
								break;
							case 400: // bad request
								break;
							case 401: // unauthorized
								break;
							case 500: // server error
								break;
						}
					}
				}
			);
		});

	}

	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function () {
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



		this.refresh = function (code) {
			var self = this;
			alertContainer.textContent = "";
			if (code = 1) {
				courseList.reset();
				courseDate.reset();
				sessionEnrolls.reset();
				courseList.show();
			}
			else if (code = 2) {
				courseDate.reset();
				sessionEnrolls.reset();
				courseDate.show();

			}
			else if (code = 3) {
				sessionEnrolls.reset();
				sessionEnrolls.show(self.current_exam);

			}
		};
	}
})();