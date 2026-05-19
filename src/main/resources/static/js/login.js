import {BASE_API_URL, sendRequestTo} from "./util.js";

const loginForm = document.getElementById("loginForm");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", async function (event)
{
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = await sendRequestTo(BASE_API_URL + "users/login", {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            email: email,
            password: password
        })
    });

    if (user.userType === "ADMIN"){
        window.location.href="/administration.html";
    } else if (user.userType === "GUARD"){
        window.location.href = "/shifts.html";
    }
    else {
        errorMessage.textContent = "Unknown usertype. Please contact support."; //Not sure what to write here; do we want to tell them it's a usertype problem?
    }
});
