
var xmlhttp = new XMLHttpRequest();

function checkLogin() {
	if(sessionStorage.getItem("sessionToken") === null)
		window.location.href = "/index.html";
}

function finishRegister() {   
	if (xmlhttp){
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				alert(xmlhttp.responseText);
			}
		}
          
		var data = JSON.stringify({
			"username":document.getElementById("username").value,
			"password":document.getElementById("pw").value,
			"passwordConfirmation":document.getElementById("pwconfirm").value,
			"email":document.getElementById("email").value,
			"name":document.getElementById("name").value,
			"address":document.getElementById("address").value,
			"nif":document.getElementById("nif").value,
			"phoneMobile":document.getElementById("phoneMobile").value,
			"phoneHome":document.getElementById("phoneHome").value,
			"profilePublic":document.getElementById("public").check
		});

		xmlhttp.open("POST", "/rest/register");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function showProfile() {
	if (xmlhttp){
		var managerToken = sessionStorage.getItem("sessionToken");
		var username = JSON.parse(managerToken).username;

        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				if(xmlhttp.status === 200) {
					var user = JSON.parse(xmlhttp.responseText);
					document.getElementById("username").value = username;
					document.getElementById("email").value = user.email;
					document.getElementById("name").value = user.name;
					document.getElementById("nif").value = user.nif;
					document.getElementById("address").value = user.address;
					document.getElementById("phoneMobile").value = user.phoneMobile;
					document.getElementById("phoneHome").value = user.phoneHome;
					document.getElementById("public").checked = user.publicProfile;
				}
				else
					alert(xmlhttp.responseText);
			}
		}
          
		var data = JSON.stringify({
			"targetUsername":username,
			"managerToken":managerToken
		});

		xmlhttp.open("POST", "/rest/manage/profile");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}

function updateProfile() {   
	if (xmlhttp){
		var managerToken = sessionStorage.getItem("sessionToken");
		var username = JSON.parse(managerToken).username;
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				alert(xmlhttp.responseText);
			}
		}
          
		var data = JSON.stringify({
			"name":document.getElementById("name").value,
			"address":document.getElementById("address").value,
			"nif":document.getElementById("nif").value,
			"phoneMobile":document.getElementById("phoneMobile").value,
			"phoneHome":document.getElementById("phoneHome").value,
			"profilePublic":document.getElementById("public").check,
			"userToken":managerToken
		});

		xmlhttp.open("PUT", "/rest/manage/profile");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function doLogin() {   
	if (xmlhttp){
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState === 4) {
            	if(xmlhttp.status === 200){
					sessionStorage.setItem("sessionToken", xmlhttp.responseText);
                  	window.location.href = "/login-success.html";  
           		} else if (xmlhttp.status >= 400){
            		alert(xmlhttp.responseText);
            	} 
			}
        }
		
		var data = JSON.stringify({
			"username":document.getElementById("username").value,
			"password":document.getElementById("pw").value
		});

		xmlhttp.open("POST", "/rest/login");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function removeUser() {
	if (xmlhttp){
		var targetUsername = document.getElementById("username").value;
		var managerToken = sessionStorage.getItem("sessionToken");
		var managerUsername = JSON.parse(managerToken).username;
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				if (xmlhttp.status === 200) {
					if (managerUsername === targetUsername) {
						onLogout();
						window.location.href = "/index.html";
					}
				}
				alert(xmlhttp.responseText);
			}
		}
		
		var data = JSON.stringify({
			"targetUsername":targetUsername,
			"managerToken":managerToken
		});

		xmlhttp.open("PUT", "/rest/manage/remove");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function changeRole() {
	if (xmlhttp){
		var targetUsername = document.getElementById("username").value;
		var managerToken = sessionStorage.getItem("sessionToken");
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				alert(xmlhttp.responseText);
			}
		}
		
		var data = JSON.stringify({
			"targetUsername":targetUsername,
			"newRole":document.getElementById("role").value,
			"managerToken":managerToken
		});

		xmlhttp.open("PUT", "/rest/manage/change-role");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function changeState() {
	if (xmlhttp){
		var targetUsername = document.getElementById("username").value;
		var managerToken = sessionStorage.getItem("sessionToken");
		var managerUsername = JSON.parse(managerToken).username;
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				if (xmlhttp.status === 200) {
					if (managerUsername === targetUsername) {
						onLogout();
						window.location.href = "/index.html";
					}
				}
				alert(xmlhttp.responseText);
			}
		}
		
		var data = JSON.stringify({
			"targetUsername":targetUsername,
			"state":document.getElementById("state").value,
			"managerToken":managerToken
		});

		xmlhttp.open("PUT", "/rest/manage/change-state");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function changePassword() {   
	if (xmlhttp){
        xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				alert(xmlhttp.responseText);
			}
		}
          
		var data = JSON.stringify({
			"oldPassword":document.getElementById("old-pw").value,
			"newPassword":document.getElementById("new-pw").value,
			"newPasswordConfirmation":document.getElementById("confirm-pw").value,
			"userToken":sessionStorage.getItem("sessionToken")
		});

		xmlhttp.open("PUT", "/rest/manage/change-pw");
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(data);
	}
}  

function onLoadUser() {
	var token = JSON.parse(sessionStorage.getItem("sessionToken"));
	document.getElementById("welcome").innerHTML = "<h1>Welcome back, " + token.username + "!<\h1>";
}

function onLogout() {		// I can't seem to get tokens from the database, and I don't have time to keep trying
	sessionStorage.removeItem("sessionToken");
}

function showToken() {
	var token = JSON.parse(sessionStorage.getItem("sessionToken"));
	var begDate = new Date(token.begDate);
	var expDate = new Date(token.expDate);
	document.getElementById("session-info").innerHTML = 
		'<button type="button" onclick="hideToken()">Hide session info</button>'
		+ '<br><p> Username: ' + token.username + '<br> User role: ' + token.role
		+ '<br> Session ID: ' + token.TokenID
		+ '<br> Session validity:<br>&nbsp&nbsp From ' + begDate.toString() + '<br>&nbsp&nbsp To ' + expDate.toString();
}

function hideToken() {
	document.getElementById("session-info").innerHTML = '<button type="button" onclick="showToken()">Show session info</button>';
}