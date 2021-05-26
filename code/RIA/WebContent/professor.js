/*
* Professor Managment
*/

(function() { // avoid variables ending up in the global scope

	// page components
	var missionDetails, missionsList, wizard,
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

	function PersonalMessage(_username, messagecontainer) {
		this.username = _username;
		this.show = function() {
			messagecontainer.textContent = this.username;
		}
	}

	function PageOrchestrator() {
		this.start = function() {
			var user = sessionStorage.getItem('name') + ' ' + sessionStorage.getItem('surname');
			personalMessage = new PersonalMessage(user, document.getElementById("id_username"));
			personalMessage.show();

		}
	}
})();
