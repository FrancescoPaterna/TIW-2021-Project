/**
 * Login management
 */

(function() { // avoid variables ending up in the global scope

	document.getElementById("loginbutton").addEventListener('click', (e) => {
		var form = e.target.closest("form");
		if (form.checkValidity()) {
			makeCall("POST", 'CheckLogin', e.target.closest("form"),
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						switch (req.status) {
							case 200:
								sessionStorage.setItem('user', message);
								if (message === 'professor') {
									window.location.href = "HomePro.html";

								}
								else if (message === 'student') {
									window.location.href = "HomeStud.html";

								}
								break;
							case 400: // bad request
								document.getElementById("errormessage").textContent = message;
								break;
							case 401: // unauthorized
								document.getElementById("errormessage").textContent = message;
								break;
							case 500: // server error
								document.getElementById("errormessage").textContent = message;
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