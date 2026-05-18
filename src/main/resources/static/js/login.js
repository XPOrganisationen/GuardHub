const loginForm = document.getElementById("loginForm");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", async function (event)
{
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/api/users/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (!response.ok){
            throw new Error("Login failed");
        }

        const user = await response.json();

        if (user.userType === "ADMIN"){
            window.location.href="/administration.html";
        } else if (user.userType === "GUARD"){
            window.location.href = "/shifts.html";
        }
        else {
            errorMessage.textContent = "Unknown usertype. Please contact support."; //Not sure what to write here; do we want to tell them it's a usertype problem?
        }
    }
    catch (error){
        errorMessage.textContent = "Wrong e-mail or password.";

        document.getElementById("password").value = "";
    }
});