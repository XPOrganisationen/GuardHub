import {injectHeader} from "./util.js";

const logoutButton = document.getElementById("logoutButton")
injectHeader('Administration');

logoutButton.addEventListener("click", function(){
    // TODO: Need to clear the login session when we have authentication
    window.location.href = "/login.html"
})