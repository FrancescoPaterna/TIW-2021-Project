/*
* Professor Management
*/

(function () { // avoid variables ending up in the global scope

	// page components
	var courseList, courseDate, sessionEnrolls,
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
		this.current_courseList;
		this.current_course_name;

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
							courseList.current_courseList = CoursesToShow;
							if (CoursesToShow.length == 0) {
								self.alert.textContent = "no courses entered yet";
								return;
							}
							self.update(CoursesToShow); // self visible by closure
						}
						else {
							self.alert.textContent = message;
						}
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
					courseList.current_course_name = course.name;
					courseDate.show(e.target.getAttribute("course_id")); // the list must know the details container
					courseList.waiter(course);
				}, false); //TODO Repeat bubbling? 
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseProBody.appendChild(row);

			});
			this.coursePro.style.visibility = "visible";
		}

		this.waiter = function (course) {
			var elem, i, row, destcell, linkcell, anchor;
			this.courseProBody.innerHTML = ""; // empty the table body
			// build updated list

			// self visible here, not this
			row = document.createElement("tr");
			destcell = document.createElement("td");
			destcell.textContent = course.id;
			row.appendChild(destcell);
			destcell = document.createElement("td");
			destcell.textContent = course.name;
			row.appendChild(destcell);
			this.courseProBody.appendChild(row);
			document.getElementById("showcourse").style.visibility = "visible";
			this.coursePro.style.visibility = "visible";
			document.getElementById("showcourse").addEventListener("click", (e) => {
				courseList.reset();
				//courseList.show();   OLD Mode
				courseList.update(courseList.current_courseList);   //Async 2.0 MODE
				courseDate.reset();
				sessionEnrolls.resetMain();
				document.getElementById("showdate").style.visibility = "hidden";
				document.getElementById("showcourse").style.visibility = "hidden";
			}, false); //TODO Repeat bubbling?
		}

	}

	function CourseDate(_alert, _id_courseDatePro, _id_courseDateProBody) {
		this.alert = _alert;
		this.courseDatePro = _id_courseDatePro;
		this.courseDateProBody = _id_courseDateProBody;
		this.current_course;
		this.currentDateList;

		this.reset = function () {
			this.courseDatePro.style.visibility = "hidden";
		}

		this.show = function (course_id) {
			if (course_id == this.current_course) {      //ASYNC 2.0
				this.update(this.currentDateList);
				return;
			}
			var self = this;
			this.current_course = course_id;
			makeCall("GET", "GetCourseDatePro?course_id=" + course_id, null,
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
							var CoursesDates = JSON.parse(req.responseText);
							courseDate.currentDateList = CoursesDates;
							if (CoursesDates.length == 0) {
								self.alert.textContent = "no exam dates available";
								return;
							}
							self.update(CoursesDates); // self visible by closure
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
			this.courseDateProBody.innerHTML = ""; // empty the table body = document.getElementById("modify_surname")
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
				anchor.setAttribute('exam_date_id', examdate.ID); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					sessionEnrolls.save(e.target.getAttribute("exam_date_id")); // save the selected exam_date_id in the current_exam VAR
					sessionEnrolls.show(e.target.getAttribute("exam_date_id")); // the list must know the details container
					courseDate.waiter(examdate);
				}, false);
				anchor.href = "#";
				row.appendChild(linkcell);
				self.courseDateProBody.appendChild(row);

			});
			this.courseDatePro.style.visibility = "visible";
		}

		this.waiter = function (examdate) {
			var row, destcell, linkcell, anchor;
			this.courseDateProBody.innerHTML = "";
			row = document.createElement("tr");
			destcell = document.createElement("td");
			destcell.textContent = examdate.ID;
			row.appendChild(destcell);
			destcell = document.createElement("td");
			destcell.textContent = examdate.data;
			row.appendChild(destcell);
			this.courseDateProBody.appendChild(row);
			document.getElementById("showdate").style.visibility = "visible";
			this.courseDatePro.style.visibility = "visible";
			document.getElementById("showdate").addEventListener("click", (e) => {
				courseDate.reset();
				//courseDate.show(this.current_course); OLD MODE
				courseDate.update(courseDate.currentDateList);  //Async 2.0 MODE
				sessionEnrolls.resetMain();
				document.getElementById("showdate").style.visibility = "hidden";
			}, false);
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
		this.modal_content = document.getElementById("modalContent");   //ModalContent
		this.span = document.getElementsByClassName("close")[0];        //CloseButton
		this.modal_allert = document.getElementById("modal_alert");   //ModalAllert


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

		/****************************************i**********************************SECOND TABLE SIGNLE MODIFY******/
		/**************/this.second_single_modify = document.getElementById("second_single_modify");  //Table
		/**************/this.s_id = document.getElementById("modify_id");
		/**************/this.s_name = document.getElementById("modify_name");
		/**************/this.s_surname = document.getElementById("modify_surname");

		//+++++++++++++++++++++++++++++++++++++++++++++RECORD++++++++++++++++++++++++++++++++++++++++++++++++//
		this.recordDiv = document.getElementById("recordDiv");
		this.recordTable = document.getElementById("recordTable");
		this.recordTableBody = document.getElementById("recordTableBody");
		this.recordForm = document.getElementById("record_form");
		this.recordLogo = document.getElementById("recordLogo");
		this.recordLegalValue = document.getElementById("recordLegalValue");
		this.recordInput = document.getElementById("record_exam_id");
		this.second_line_record = document.getElementById("second_line_record");

		//+++++++++MULTIPLE MODIFY+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		this.multipleModalForm = document.getElementById("multipleModifyForm");
		/**********************************************************************************************************/

		//++++++++++++++++++++++++++++++++++++++++++PUBLISH+++++++++++++++++++++++++++++++++++++++++++++++++++++//
		this.publishInput = document.getElementById("publish_exam_id");
		/**********************************************************************************************************/

		//BUTTONS
		this.isRecordable;
		this.isPublishable;
		this.record_button = document.getElementById("record_button");
		this.multiple_modify_button = document.getElementById("multiple_modify");
		this.publish_button = document.getElementById("publish");
		this.print_button = document.getElementById("print_button");
		//VAR
		var current_exam;

		this.resetMain = function () {
			this.sessionEnrolls.style.visibility = "hidden";
			this.record_button.style.visibility = "hidden";
			this.publish_button.style.visibility = "hidden";
			this.multiple_modify_button.style.visibility = "hidden";
			try { document.getElementsByClassName("boldarrow")[0].className = "normalarrow" 
			}
			catch(exception){
				//no click ever made
			}
			try {
				this.multiple_modify_button.removeEventListener("click", sessionEnrolls.multiple_event);
			}
			catch(exception){
				//multiple_modify_button in disable state yet
			}
		}




		this.resetModal = function () {
			//this.modal.style.visibility = "hidden";
			this.modal_title.textContent = "";
			this.modal_message.textContent = "";
			this.modal_allert.textContent = "";
			this.first_single_modify.style.visibility = "hidden";
			this.second_single_modify.style.visibility = "hidden";
			this.multipleModalForm.style.visibility = "hidden";
			this.recordDiv.style.visibility = "hidden";
			this.recordTable.style.visibility = "hidden";
			this.recordTableBody.innerHTML = "";
			this.recordTableBody.style.visibility = "hidden";
			this.recordLogo.style.visibility = "hidden";
			this.recordLegalValue.style.visibility = "hidden";
			this.print_button.style.visibility = "hidden";

		}

		this.save = function (exam_date_id) {
			this.current_exam = exam_date_id;
		}

		this.show = function (exam_date_id) {
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
						else {
							self.alert.textContent = message;
							return;
						}
					} 
				}
			);
			this.sessionEnrolls.style.visibility = "visible";
			this.record_button.style.visibility = "visible";
			this.publish_button.style.visibility = "visible";
			this.multiple_modify_button.style.visibility = "visible";
		};

		this.update = function (enrolls) {
			var elem, flag, row, destcell, input, select, option, label, button;
			this.sessionEnrollsBody.innerHTML = ""; // empty the table body
			this.multipleModalForm.innerHTML = "";
			//clear memory on record button
			this.isRecordable = false;
			this.isPublishable = false;
			this.multiple_modify_button_flag = false;
			// build updated list
			var self = this;     //FIRST 
			flag = 0;
			enrolls.forEach(function (enroll) { // self visible here, not this
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = enroll.IDstudent;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.name;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.surname;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.mail;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.courseDeg;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.mark;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = enroll.status;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				if (enroll.status == "NOT_INSERTED") {
					self.AppendIfNOTINSERTED(enroll);
					self.multiple_modify_button_flag = true;
				}

				// if there is a mark in "PUBLISHED" or "REJECTED" status, enable record button
				if (enroll.status.trim() == "PUBLISHED" || enroll.status.trim() == "REJECTED") {
					self.isRecordable = true;
				}

				// if there is a mark in "INSERTED" status, enable published button
				if (enroll.status.trim() == "INSERTED") {
					self.isPublishable = true;
				}

				/*If a score == INSERTED OR NOT_INSERTED OR PUBLISH, Enable the Single Modify Button */
				if (self.isModifible(enroll.status.trim())) {
					elem = document.createElement("button");
					elem.classList.add("smodify");
					elem.textContent = "Modify";
					destcell.appendChild(elem);

					//SINGLE MODIFY EVENT LISTNER
					elem.addEventListener("click", (e) => {
						self.resetModal();
						self.modal.style.display = "block";
						sessionEnrolls.modal_content.style.height = "55%";
						sessionEnrolls.modal_content.style.width = "75%";
						self.first_single_modify.style.visibility = "visible";
						self.second_single_modify.style.visibility = "visible";
						self.modify_handler_id.setAttribute("value", enroll.IDstudent);
						self.modify_handler_examdate.setAttribute("value", self.current_exam);
						//UPDATE THE MODAL WINDOW WITH UPDATE_SINGLE_MODIFIER FUNCTION
						self.update_single_modifier(enroll.IDstudent, enroll.name, enroll.surname,
							enroll.mail, enroll.mark, enroll.courseDeg, enroll.status);
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
			self.multiple_modify_button.setAttribute("class", "modifylo");

			if (self.multiple_modify_button_flag) {
				self.multipleModalForm.appendChild(document.createElement("br"));

				select = document.createElement("select");
				select.required = true;
				select.setAttribute("id", "multi_score");
				select.setAttribute("name", "score");
				option = document.createElement("option");
				option.setAttribute("selected", "selected");
				option.setAttribute("value", "");
				option.textContent = "";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "ABSENT");
				option.textContent = "ABSENT";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "RIMANDATO");
				option.textContent = "RIMANDATO";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "RIPROVATO");
				option.textContent = "RIPROVATO";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "18");
				option.textContent = "18";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "19");
				option.textContent = "19";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "20");
				option.textContent = "20";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "21");
				option.textContent = "21";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "22");
				option.textContent = "22";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "23");
				option.textContent = "23";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "24");
				option.textContent = "24";
				select.appendChild(option);
				option = document.createElement("option");
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
				option.setAttribute("value", "29");
				option.textContent = "29";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "30");
				option.textContent = "30";
				select.appendChild(option);
				option = document.createElement("option");
				option.setAttribute("value", "30L");
				option.textContent = "30L";
				select.appendChild(option);

				self.multipleModalForm.appendChild(select);


				button = document.createElement("input");
				button.setAttribute("type", "button");
				button.setAttribute("name", "UPDATE");
				button.setAttribute("id", "UPDATE");
				button.setAttribute("value", "UPDATE");
				button.classList.add("smodify");



				var selfInButton = self;
				button.addEventListener("click", (e) => {
					var form = e.target.closest("form");
					if (form.checkValidity()) {
						var array = $("input[name='IDStudent']:checked").map(function () {
							return this.value;
						}).get();
						if (array.length == 0) {
							sessionEnrolls.modal_allert.textContent = "Please select at least one student!"
							return;
						}
						var score = form.querySelector("select[name = 'score']").value;
						form = {
							"id_stud": array,
							"score": score,
							"exam_date_id": selfInButton.current_exam
						}
						form = JSON.stringify(form);

						var self = this;
						makeCallJSON("POST", 'UpdateMultipleScore', form,
							function (req) {
								if (req.readyState == 4) {
									var message = req.responseText;
									if (req.status == 200) {
										self.resetModal();
										self.resetMain();
										self.show(self.current_exam);
										self.modal.style.display = "none";
										self.modal_content.style.height = "auto";
										self.modal_content.style.width = "80%";
										self.multiple_modify_button_flag = false;
										self.multiple_modify_button.removeEventListener("click", sessionEnrolls.multiple_event);
									} else {
										sessionEnrolls.modal_allert.textContent = message;
									}
								}
							}
						);
					} else {
						form.reportValidity();
					}
				});

				self.multipleModalForm.appendChild(button);
				self.multiple_modify_button.setAttribute("class", "modify");
				self.multiple_modify_button.addEventListener("click", sessionEnrolls.multiple_event, false);
			}






			// Register event to record button only if there is at least one mark in the "PUBLISHED" status
			if (self.isRecordable) {
				// make the record button clickable
				self.record_button.setAttribute("class", "record");
				self.record_button.addEventListener("click", self.recordFunction);
			} else {
				// make the record button inactive
				self.record_button.setAttribute("class", "recordlo");
				self.record_button.removeEventListener("click", self.recordFunction);
			}





			// Register event to publish button only if there is at least one mark in the "INSERTED" status
			if (self.isPublishable) {
				// make the publish button clickable
				self.publish_button.setAttribute("id", "publish");
				self.publish_button.addEventListener("click", self.publishFunction);
			} else {
				// make the publish button inactive
				self.publish_button.setAttribute("id", "publishlo");
				self.publish_button.removeEventListener("click", self.publishFunction);
			}

		}


		this.recordFunction = function (e) {
			// create an input field where to put the exam_date_id to be sent to the servlet
			var input = sessionEnrolls.recordInput;
			input.setAttribute("value", sessionEnrolls.current_exam);

			// send the form if valid
			var form = e.target.closest("form");
			if (form.checkValidity()) {
				var self = sessionEnrolls;
				makeCall("POST", 'RecordScores', form,
					function (req) {
						if (req.readyState == XMLHttpRequest.DONE) {
							var message = req.responseText;
							if (req.status == 200) {
								var record = JSON.parse(req.responseText);
								self.showRecordedEnrolls(record); // self visible by closure
								self.resetMain();
								self.show(self.current_exam);
							} else {
								self.alert.textContent = message;
							}
						}
					}
				);
			} else {
				form.reportValidity();
			}
		}





		this.publishFunction = function (e) {
			// create an input field where to put the exam_date_id to be sent to the servlet
			var input = sessionEnrolls.publishInput;
			input.setAttribute("value", sessionEnrolls.current_exam);

			// send the form if valid
			var form = e.target.closest("form");
			if (form.checkValidity()) {
				var self = sessionEnrolls;
				makeCall("POST", 'UpdateStatus', form,
					function (req) {
						if (req.readyState == XMLHttpRequest.DONE) {
							var message = req.responseText;
							if (req.status == 200) {
								self.resetMain();
								self.show(self.current_exam);
							} else {
								self.alert.textContent = message;
							}
						}
					}
				);
			} else {
				form.reportValidity();
			}
		}



		this.isModifible = function (status) {
			if (status == "NOT_INSERTED" || status == "INSERTED")
				return true;
		}

		this.AppendIfNOTINSERTED = function (ed) {

			var rowModal, label, div;
			div = document.createElement("div");
			div.classList.add("inputGroup");
			rowModal = document.createElement("input");
			div.appendChild(rowModal);

			rowModal.setAttribute("type", "checkbox");
			rowModal.setAttribute("name", "IDStudent");
			rowModal.setAttribute("value", ed.IDstudent);
			label = document.createElement("label");
			div.appendChild(label);

			label.setAttribute("for", ed.IDstudent);
			label.textContent = "  " + ed.IDstudent + " - " + ed.name + "  " + ed.surname;
			this.multipleModalForm.appendChild(div);

			this.multipleModalForm.appendChild(document.createElement("br"));
		}

		// When the user clicks on <span> (x), close the modal
		this.CloseButton = function () {
			this.modal.style.display = "none";
		}

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function (event) {
			if (event.target == sessionEnrolls.modal) {
				sessionEnrolls.modal.style.display = "none";
				sessionEnrolls.resetModal();
			}
		}

		//Need to separate the Event Listner of MultipleModifyButton, to Add and Remove Easly
		this.multiple_event = function () {
			sessionEnrolls.resetModal();
			sessionEnrolls.modal_title.textContent = "MULTIPLE MODIFY";
			sessionEnrolls.modal_content.style.height = "60%";
			sessionEnrolls.modal_content.style.width = "40%";
			sessionEnrolls.modal.style.display = "block";
			sessionEnrolls.multipleModalForm.style.visibility = "visible";
			sessionEnrolls.span.addEventListener("click", (c) => {
				// dependency close button
				sessionEnrolls.modal.style.display = "none";
				sessionEnrolls.modal_content.style.height = "80%";
				sessionEnrolls.modal_content.style.width = "80%";
			});
		}


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
			this.resetModal;

			// INSERT TITLE
			this.modal_title.textContent = "MODIFY SCORE";

			// INSERT MESSAGE
			this.modal_message.textContent = "Insert The Score";

			var self = this;
			/*Function That Support the UPDATE Button in Modal Windows SINGLE MODIFY*/
			this.sm_update.addEventListener('click', (event) => {
				var form = event.target.closest("form")
				var new_score = form.querySelector("select[name = 'score']").value;
				if (form.checkValidity()) {
					makeCall("POST", "UpdateScore", form,
						function (req) {
							var self2 = self;
							if (req.readyState == 4) {
								var self3 = self2;
								switch (req.status) {
									case 200:
										self3.f_score.textContent = "";
										self3.f_score.textContent = new_score;
										self3.f_status.textContent = "INSERTED";
										self3.resetMain();
										self3.show(self3.current_exam);
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
				} else {
					form.reportValidity();
				}
			});

		}









		this.showRecordedEnrolls = function (record) {
			var row, destcell;
			// clear the modal page
			this.resetModal();
			sessionEnrolls.modal_content.style.height = "80%";
			sessionEnrolls.modal_content.style.width = "80%";
			this.print_button.style.visibility = "visible";
			this.modal_content.setAttribute("class", "modal_record");
			this.modal.style.display = "block";
			// setup the modal page content
			this.modal_title.textContent = "University of NightCity Official Record";
			this.modal_message.textContent = 'Document #' + record.IDRecord + ' - generated and digitally signed on ' + record.date +
				' at ' + record.time;

			this.second_line_record.textContent = "Registration of the Session " + sessionEnrolls.current_exam +
				' relating to the course of ' + courseList.current_course_name + ' held by Prof. ' + sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			this.recordDiv.style.visibility = "visible";

			// fill the table with recorded enrolls
			var self = this;
			record.recordedEnrolls.forEach(function (recordedEnroll) {
				row = document.createElement("tr");
				destcell = document.createElement("td");
				destcell.textContent = recordedEnroll.IDstudent;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = recordedEnroll.surname;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = recordedEnroll.name;
				row.appendChild(destcell);
				destcell = document.createElement("td");
				destcell.textContent = recordedEnroll.mark;
				row.appendChild(destcell);
				self.recordTableBody.appendChild(row);
			});

			// register event on close button
			this.span.addEventListener("click", (c) => {
				self.second_line_record.textContent = "";
				self.modal.style.display = "none";
				self.modal_content.setAttribute("class", "modal-content");
			}, false);

			// show only record components in the modal page
			this.recordTable.style.visibility = "visible";
			this.recordTableBody.style.visibility = "visible";
			this.recordLogo.style.visibility = "visible";
			this.recordLegalValue.style.visibility = "visible";
		}







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

			// remove user data from session storage when logout is clicked
			document.getElementById("logoutButton").addEventListener("click", () => {
				window.sessionStorage.removeItem('id');
				window.sessionStorage.removeItem('name');
				window.sessionStorage.removeItem('surname');
				window.sessionStorage.removeItem('role');
			});
		};

		this.refresh = function () {
			alertContainer.textContent = "";
			courseList.reset();
			courseDate.reset();
			sessionEnrolls.resetMain();
			sessionEnrolls.resetModal();
			courseList.show();
		};


	}
})();