import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./UserDashboard.css"; // 
import "./FeedbackForm.js";
export default function UserDashboard() {
  const [complaints, setComplaints] = useState([]);
  const navigate = useNavigate();
  const userId = sessionStorage.getItem("userId"); 

  useEffect(() => {   
    sessionStorage.setItem("role", "USER");
  }, []);
  useEffect(() => {
   

    const fetchComplaints = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/complaints/user/${userId}`
        );
        if (response.ok) {
          const data = await response.json();
          setComplaints(data);
        } else {
          console.error("Failed to fetch complaints");
        }
      } catch (error) {
        console.error("Error fetching complaints:", error);
      }
    };

    fetchComplaints();
  }, [userId, navigate]);
  const handleGiveFeedback = (complaintId) => {
    navigate(`/user/feedback/${complaintId}`);
  }

  return (
    <div className="user-dashboard-container">
      <header className="dashboard-header">
        <h2>Welcome, {sessionStorage.getItem("userName")}</h2>
        <button className="new-btn" onClick={() => navigate("/dashboard")}>
          + New Complaint
        </button>
      </header>

      <section className="complaint-section">
        <h3>My Complaints</h3>
        {complaints.length === 0 ? (
          <p>No complaints found.</p>
        ) : (
          <table className="complaints-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
                
                <th>Location</th>
                <th>View Details</th>
                <th>Feedback</th>

              </tr>
            </thead>
            <tbody>
              {complaints.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td>{c.title}</td>
                  <td>{c.description}</td>
                  <td
                    className={`status ${
                      c.status?.toLowerCase().replace(" ", "-") || "unknown"
                    }`}
                  >
                    {c.status}
                  </td>
                  
                  <td>{c.location }</td>
                  <td>
                          <button
                            onClick={() => navigate(`/admin/complaint/${c.id}`)}
                            className="btn btn-info"
                          >
                              View Details
                          </button>
                  </td>
                  <td>
                      <button
                          className="btn-feedback"
                          onClick={() => handleGiveFeedback(c.id)}
                      >
                          Give Feedback
                      </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        
      </section>
    </div>
  );
}
