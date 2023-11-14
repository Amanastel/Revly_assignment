let descriptions = document.getElementById("doubtDescription");
let subjectExpertise = document.getElementById("subjectExpertise");
let nameDoubt = document.getElementById("nameDount");
console.log(nameDoubt);

countchek()


getallusers();
function getallusers() {
    // const usertablebody=document.getElementById("tbody");
    const token = localStorage.getItem('jwtToken');
    // console.log(token+" hello");

    fetch("http://localhost:8888/users/doubtRequest/all", {
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
                    // <td>${user.tutor.id}</td>
                    // <td>${user.tutor.name}</td>
                    if (user.tutor == null) {
                        user.tutor = { id: "NA", name: "NA" };
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
                    <button class="btn btn-danger" onclick="deleteDoubt(${user.id})">Delete</button>
                </td>
                `;
                    tbody.appendChild(row);
                })
            })
        }
        else {
            console.log("testing");
            response.json().then(data => alert("Sometging went wrong !"));
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

function countchek() {
    const token = localStorage.getItem("jwtToken");
    fetch(`http://localhost:8888/users/tutorAvailability/countOnlineTutors`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                console.log(data);
                document.getElementById("count").innerText = "Online Tutor: "+data;
            })
        }
    })

}