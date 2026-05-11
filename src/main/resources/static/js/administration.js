const logoutButton = document.getElementById("logoutButton")

logoutButton.addEventListener("click", function(){
    // TODO: Need to clear the login session when we have authentication
    window.location.href = "/login.html"
})