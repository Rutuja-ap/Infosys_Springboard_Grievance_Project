import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./OfficerDashboard.css";

export default function OfficerDashboard() {
  const [complaints, setComplaints] = useState([]);
  const [theme, setTheme] = useState("light");
  const navigate = useNavigate();
  const officerId = sessionStorage.getItem("officerId");
  const [count, setCount] = useState(0);
  
  useEffect(() => {
    sessionStorage.setItem("role", "OFFICER");
  }, []);

  useEffect(() => {
    if (!officerId) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8080/api/officers/${officerId}/complaints`)
      .then((res) => res.json())
      .then(setComplaints)
      .catch(console.error);

    fetch(`http://localhost:8080/api/officers/${officerId}/complaints/count`)
        .then(res => res.json())
        .then(data => setCount(data.count));
  }, [officerId, navigate]);

  const updateStatus = async (id, status) => {
    await fetch(`http://localhost:8080/api/admin1/complaints/${id}/status`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status }),
    });

    const res = await fetch(
      `http://localhost:8080/api/officers/${officerId}/complaints`
    );
    setComplaints(await res.json());
  };

  return (
    <div className={`officer-dashboard ${theme}`}>
      <div className="top-bar">
        <h2>Officer Dashboard</h2>

       
        <button
          className="theme-toggle"
          onClick={() => setTheme(theme === "light" ? "dark" : "light")}
        >
          {theme === "light" ? "🌙 Dark Mode" : "☀️ Light Mode"}
        </button>
      </div>

      <p>Welcome, {sessionStorage.getItem("officerName")}</p>
     


      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
           
            <th>Status</th>
            <th>Action</th>
            <th>Location</th>
            <th>View Details</th>
          </tr>
        </thead>

        <tbody>
          {complaints.map((c) => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.title}</td>
            
              <td>{c.status}</td>
              <td>
                <button onClick={() => updateStatus(c.id, "Ongoing")}>
                  Ongoing
                </button>

                <button onClick={() => updateStatus(c.id, "Resolved")}>
                  Resolved
                </button>

                <button onClick={() => updateStatus(c.id, "Pending")}>
                  Pending
                </button>
              </td>
              <td>{c.location}</td>
              <td>
                  <button
                  onClick={() => navigate(`/admin/complaint/${c.id}`)}
                  className="btn btn-info"
                  >
                  View Details
                  </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
