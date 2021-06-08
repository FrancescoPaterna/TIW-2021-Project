/**
 * Login management
 */

(function () { // avoid variables ending up in the global scope

	document.getElementById("loginbutton").addEventListener('click', (e) => {
		var form = e.target.closest("form");
		if (form.checkValidity()) {
			makeCall("POST", 'CheckLogin', e.target.closest("form"),
				function (req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						switch (req.status) {
							case 200:
								var message = JSON.parse(req.responseText);
								sessionStorage.setItem('id', message.id);
								sessionStorage.setItem('name', message.name);
								sessionStorage.setItem('surname', message.surname);
								sessionStorage.setItem('role', message.role);

								if (message.role == "professor") {
									window.location.href = "HomePro.html";

								}
								else if (message.role == "student") {
									window.location.href = "HomeStud.html";
								}
								else {
									document.getElementById("error_message").textContent = "Invalid Role - Contact The Support"

								}
								break;

							case 400: // bad request
								var message = req.responseText;
								document.getElementById("error_message").textContent = message;
								break;

							case 401: // unauthorized
								var message = req.responseText;
								document.getElementById("error_message").textContent = message;
								break;
								
							case 500: // server error
								var message = req.responseText;
								document.getElementById("error_message").textContent = message;
								break;
						}
					}
				}
			);
		} else {
			form.reportValidity();
		}
	});

})();