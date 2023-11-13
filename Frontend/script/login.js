document.getElementById("signup-toggle").addEventListener("click", function () {
    document.getElementById("login-form").classList.remove("active");
    document.getElementById("signup-form").classList.add("active");
});

document.getElementById("login-toggle").addEventListener("click", function () {
    document.getElementById("signup-form").classList.remove("active");
    document.getElementById("login-form").classList.add("active");
});

// import Swal from 'sweetalert2'

// CommonJS
// const Swal = require('sweetalert2')

const loginForm = document.getElementById("login-form");

loginForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const usernameInput = document.getElementById("loginemail");
    // const emailInput = document.querySelector('input[name="email"]');
    const passwordInput = document.getElementById("loginPassword");
    const username = usernameInput.value;
    // const email = emailInput.value;
    const password = passwordInput.value;
    if (password && username != "") {
        fetch("http://localhost:8888/signIn", {
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + btoa(username + ':' + password)
            },
        })
            .then(res => {
                if (res.status == 200 | res.status == 202) {
                    const token = res.headers.get("Authorization");
                    console.log(token)
                    localStorage.setItem("jwtToken", token)

                    res.json().then(data =>{
                        console.log(data);
                        // alert("heelo")
                        alert("Tutor sucessfully registered with Name: "+data.name)
                        localStorage.setItem("name", data.name)
                        localStorage.setItem("userData", JSON.stringify(data))
                        Swal.fire(
                            'Good job!',
                            'Successfully Registered',
                            'success'
                        )
                        if (data.userType == "ROLE_STUDENT") {
                            setTimeout(() => {
                                window.location.href = "../Student.html"
                            }, 2000)
                        } else {
                            setTimeout(() => {
                                window.location.href = "/index.html"
                            }, 2000)
                        }
                    })
            
                    console.log(res)
                } 
            })
            .catch(error => {
                console.error(error);
                alert("Invalid Credentials")
            });
    } 

});
const signupForm = document.getElementById("signup-form");

signupForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const usernameInput = document.getElementById("username");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("Password");
    const phoneInput = document.getElementById("phoneNumber");
    const addressInput = document.getElementById("address");
    const userLanguageInput = document.getElementById("userLanguage");
    const subjectExpertiseInput = document.getElementById("subjectExpertise");
    const userTypeInput = document.querySelector('input[name="userType"]:checked');

    const username = usernameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;
    const phone = phoneInput.value;
    const addrss = addressInput.value;
    const userLanguage = userLanguageInput.value;
    const subjectExpertise = subjectExpertiseInput.value;
    const userType = userTypeInput.value;


    if(userType=="TUTOR"){

        const data = {
            name: username,
            password: password,
            email: email,
            phone: phone,
            address: addrss,
            subjectExpertise: subjectExpertise,
            userType: userType
        };
    
        
        console.log(data);
    
        if (email && password && username !== "") {
            fetch("http://localhost:8888/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
            .then(response => {
                if(response.status == 201 | response.status == 200){
                    response.json().then(data => {
                        alert("Tutor sucessfully registered with Name: "+data.name)
                        console.log(data)
                    });
                }else{
                    response.json().then(data => alert(data.message));
                }
            })
                
        }
    }else{
        const data = {
            name: username,
            password: password,
            email: email,
            phone: phone,
            address: addrss,
            userLanguage: userLanguage,
            userType: userType,
            classGrade: "10"

        };
    
        
        console.log(data);
    
        if (email && password && username !== "") {
            fetch("http://localhost:8888/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
            .then(response => {
                if(response.status == 201 | response.status == 200){
                    response.json().then(data => {
                        alert("Student sucessfully registered with Name: "+data.name)
                        console.log(data)
                    });
                }else{
                    response.json().then(data => alert(data.message));
                }
            })
                
        }
    }

    
});