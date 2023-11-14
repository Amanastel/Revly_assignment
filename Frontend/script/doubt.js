let descriptions = document.getElementById("doubtDescription");
let subjectExpertise = document.getElementById("subjectExpertise");
let nameDoubt = document.getElementById("nameDount");
console.log(nameDoubt);


let userData = JSON.parse(localStorage.getItem("userData"));
subjectExpertise.innerText = userData.userLanguage;
nameDoubt.innerText = "Please Add Doubt Mr. "+userData.name;

document.getElementById("submit").addEventListener("click", (e) => {
    e.preventDefault();
    let description = descriptions.value;
    console.log(description);
    addDbout(description);
    
})

document.getElementById("rendomTutor").addEventListener("click", (e) =>{
    e.preventDefault();
    let description = descriptions.value;
    rendomTutorDoubt(description);
})

function addDbout(description) {
    const token = localStorage.getItem("jwtToken");
    const userData = JSON.parse(localStorage.getItem("userData"));

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append(
        "Authorization",
        "Bearer " + token
    );

    var raw = JSON.stringify({
        "doubtDescription": description
    });


    var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
    }

    fetch("http://localhost:8888/users/doubtRequest", requestOptions)
    .then((response) => response.text())
    .then((result) => console.log(result),
      
      alert("Doubt Added successfully"),
      setTimeout(()=>{
        window.location.href="../AddDoubt.html"
      },1000) 
        
    )
    .catch((error) => alert("error", error));
}


function rendomTutorDoubt(description) {
    const token = localStorage.getItem("jwtToken");
    const userData = JSON.parse(localStorage.getItem("userData"));

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Bearer " + token);

    var raw = JSON.stringify({
        "doubtDescription": description
    });

    var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
    };

    fetch("http://localhost:8888/users/doubtRequest/live", requestOptions)
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("No tutors available");
            }
        })
        .then((result) => {
            console.log(result);
            alert("Doubt Added successfully");
            setTimeout(() => {
                window.location.href = "../AddDoubt.html";
            }, 1000);
        })
        .catch((error) => {
            alert("Error: " + error.message);
        });
}



getallusers();
function getallusers(){
    // const usertablebody=document.getElementById("tbody");
    const token=localStorage.getItem('jwtToken');
    // console.log(token+" hello");

    fetch("http://localhost:8888/users/doubtRequest/all",{
       method:"GET",
       headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then(response=>{
    if(response.ok){
        response.json().then(data=>{
            // console.log(data);
            // console.log(data);
            data.forEach(user => {
                console.log(user);
                console.log(user.doubtSubject);
                // <td>${user.tutor.id}</td>
                // <td>${user.tutor.name}</td>
                if(user.tutor== null){
                    user.tutor={id:"NA",name:"NA"};
                }
                const row=document.createElement("tr");
                row.innerHTML=`
                <td>${user.id}</td>
                <td>${user.student.name}</td>
                <td>${user.doubtSubject}</td>
                <td>${user.timestamp}</td>
                <td>${user.doubtDescription}</td>
                <td>${user.doubtResolved}</td>
                <td>${user.tutor.id}</td>
                <td>${user.tutor.name}</td>
                <td>
                    <button class="btn btn-danger" onclick="deleteDoubt(${user.id})">Delete</button>
                </td>
                `;
                tbody.appendChild(row);
            })
        })
    }
    else{
        console.log("testing");
        response.json().then(data=>alert("Sometging went wrong !"));
    }
    })
}
function deleteDoubt(doubtId) {
    console.log("check deleteDoubt");
    const token = localStorage.getItem("jwtToken");

    fetch(`http://localhost:8888/users/doubtRequest/${doubtId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    })
        .then((response) => {
            if (response.ok) {
                alert("Doubt deleted successfully");
                // Reload the page to reflect the changes
                window.location.reload();
            } else {
                response.json().then((data) => alert("Error deleting doubt: " + data.message));
            }
        })
        .catch((error) => alert("Error deleting doubt: " + error));
}