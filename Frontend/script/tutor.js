let descriptions = document.getElementById("doubtDescription");
let subjectExpertise = document.getElementById("subjectExpertise");
let nameDoubt = document.getElementById("nameDount");
console.log(nameDoubt);


let userData = JSON.parse(localStorage.getItem("userData"));
subjectExpertise.innerText = userData.subjectExpertise;
nameDoubt.innerText = "Add Your Response Mr. " + userData.name;


function ResolveDoubt(id){
    var submitBtn = document.getElementById("submit");
    submitBtn.innerText = "ResolveDoubt"
    console.log(id)
    document.getElementById("submit").addEventListener("click", (e) => {
        e.preventDefault();
        let description = descriptions.value;
        console.log(description, id);
        
        const token = localStorage.getItem("jwtToken");
    const userData = JSON.parse(localStorage.getItem("userData"));

    

    fetch(`http://localhost:8888/users/doubtRequest/tutor/${id}/solve?solutionDescription=${description}`,{
        method:"GET",
        headers: {
            'Authorization': `Bearer ${token}`
          }
          
    })

    .then((response) => response.text())
    .then((result) => console.log(result),
      
      alert("Doubt Resolve successfully"),
      setTimeout(()=>{
        window.location.href="/tutor.html"
      },1000) 
        
    )
    .catch((error) => alert("error", error));
    
    })
}

getallusers();
function getallusers() {
    // const usertablebody=document.getElementById("tbody");
    const token = localStorage.getItem('jwtToken');
    // console.log(token+" hello");

    fetch("http://localhost:8888/users/doubtRequest/tutor/doubtRequestBasedOnSubject", {
        method: "GET",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                // console.log(data);
                // console.log(data);
                data.forEach(user => {
                    console.log(user);
                    console.log(user.doubtSubject);
                    if (user.tutor == null) {
                        user.tutor = { id: "NA", name: "NA" };
                        const row = document.createElement("tr");
                        row.innerHTML = `
                                <td>${user.id}</td>
                                <td>${user.student.name}</td>
                                <td>${user.doubtSubject}</td>
                                <td>${user.timestamp}</td>
                                <td>${user.doubtDescription}</td>
                                <td>${user.doubtResolved}</td>
                                <td>${user.tutor.id}</td>
                                <td>${user.tutor.name}</td>
                                <td>
                                   
                                    <button class="btn btn-primary" onclick="acceptDoubt(${user.id})">Accept</button>
                                </td>
                `;
                        tbody.appendChild(row);
                    }
                    const row = document.createElement("tr");
                    row.innerHTML = `
                                <td>${user.id}</td>
                                <td>${user.student.name}</td>
                                <td>${user.doubtSubject}</td>
                                <td>${user.timestamp}</td>
                                <td>${user.doubtDescription}</td>
                                <td>${user.doubtResolved}</td>
                                <td>${user.tutor.id}</td>
                                <td>${user.tutor.name}</td>
                                <td>
                                <button class="btn btn-primary" onclick="ResolveDoubt(${user.id})">Resolve</button>
                                </td>
                `;
                    tbody.appendChild(row);
                })
            }).catch((e)=>{
                alert(e)
            })
        }
    })
}



function acceptDoubt(doubtId){
    console.log(doubtId);
    const token = localStorage.getItem("jwtToken");
    console.log("check deleteDoubt");

    fetch(`http://localhost:8888/users/doubtRequest/tutor/${doubtId}/accept`, {
        method: "GET",
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


