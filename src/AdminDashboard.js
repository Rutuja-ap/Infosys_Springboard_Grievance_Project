import React, { useState, useEffect, useMemo, useRef } from "react";
import "./AdminDashboard.css";
import { useNavigate } from "react-router-dom";
import { Pie, Bar } from "react-chartjs-2";
import "chart.js/auto";

import "react-toastify/dist/ReactToastify.css";


export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("complaints");
  const [complaints, setComplaints] = useState([]);
  const [users, setUsers] = useState([]);
  const [officers, setOfficers] = useState([]);
  const [theme, setTheme] = useState("light");
  const [lastComplaintId, setLastComplaintId] = useState(0);
  //const [lastSeenId, setLastSeenId] = useState(null);//*******************8


  const navigate = useNavigate();
  const lastComplaintIdRef = useRef(0); // Use ref to always have latest value in async calls
   const lastCountRef = useRef(0);
   const syncedShown = useRef(false);



useEffect(() => {
  sessionStorage.setItem("role", "ADMIN"); // only once
  
}, []);
const fetchComplaints = async (firstLoad = false) => {
  try {
        const res = await fetch("http://localhost:8080/api/admin1/complaints");
        let data = await res.json();
        data.sort((a, b) => a.id - b.id);

        // ESCALATION LOGIC
        data = data.map(c => {
            if (!c.dueDate) return { ...c, escalated: false };
            const today = new Date();
            const due = new Date(c.dueDate);
            return { ...c, escalated: today > due && c.status !== "Resolved" };
        });
        setComplaints([...data]);
    
          }

  catch (err) {
    console.error("Error fetching complaints:", err);
  }
};


 
  const fetchOfficers = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/admin1/officers");
      const data = await res.json();
      setOfficers(data);
    } catch (err) {
      console.error("Error fetching officers:", err);
    }
  };


  const fetchUsers = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/admin1/users");
      const data = await res.json();
      setUsers(data);
    } catch (err) {
      console.error("Error fetching users:", err);
    }
  };

  useEffect(() => {
    fetchComplaints(true); 
    fetchUsers();
    fetchOfficers();
    
    const interval = setInterval(() => {
      fetchComplaints();
    }, 2000); 

    return () => clearInterval(interval);
  }, []);

 
  const statusCounts = useMemo(() => {
    const counts = { Pending: 0, Ongoing: 0, Resolved: 0 };
    complaints.forEach((c) => {
      const s = c.status ? c.status.trim() : "Pending";
      if (s === "Pending") counts.Pending++;
      else if (s === "Ongoing") counts.Ongoing++;
      else if (s === "Resolved") counts.Resolved++;
    });
    return counts;
  }, [complaints]);

  const pieData = {
    labels: ["Pending", "Ongoing", "Resolved"],
    datasets: [
      {
        data: [
          statusCounts.Pending,
          statusCounts.Ongoing,
          statusCounts.Resolved,
        ],
        backgroundColor: ["#f7b731", "#f59e0b", "#2ecc71"],
        hoverOffset: 8,
      },
    ],
  };

  const pieOptions = {
    plugins: { legend: { position: "bottom" }, tooltip: { enabled: true } },
    maintainAspectRatio: false,
  };

 
  const categoryCounts = useMemo(() => {
    const countMap = {};
    complaints.forEach((c) => {
      const cat = c.category || "Unspecified";
      countMap[cat] = (countMap[cat] || 0) + 1;
    });
    return countMap;
  }, [complaints]);

  const barData = {
    labels: Object.keys(categoryCounts),
    datasets: [
      {
        label: "Complaints Count",
        data: Object.values(categoryCounts),
        backgroundColor: "#3498db",
      },
    ],
  };

  const barOptions = {
    plugins: { legend: { display: false } },
    maintainAspectRatio: false,
  };

 
  const handleStatusChange = async (id, status) => {
    try {
      await fetch(`http://localhost:8080/api/admin1/complaints/${id}/status`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status }),
      });
      await fetchComplaints();
    } catch (err) {
      console.error("Error updating status:", err);
    }
  };

  return (
    <div className={`admin-dashboard ${theme}`}>

      <div className="admin-header">
        <h2>Resolve IT - Admin Dashboard</h2>

        <div style={{ display: "flex", gap: "10px" }}>
          <button
            className="theme-toggle-btn"
            onClick={() => setTheme(theme === "light" ? "dark" : "light")}
          >
            {theme === "light" ? "🌙 Dark" : "☀️ Light"}
          </button>
          
          <div 
            className="notification-bell"
            onClick={() => navigate("/notifications")}

          >
             <button>
            🔔
            {lastCountRef.current < complaints.length && (
              <span className="notification-dot"></span>
            )}</button>
          </div>
          
          <button
            className="logout-btn"
            onClick={() => {
              sessionStorage.removeItem("admin");
              sessionStorage.removeItem("user");
              navigate("/");
            }}
          >
            Logout
          </button>
        </div>
      </div>


        
        <div className="dashboard-content">
          <div className="sidebar">
            <button className={activeTab === "complaints" ? "active" : ""} onClick={() => setActiveTab("complaints")}>
              Complaints
            </button>
            <button className={activeTab === "users" ? "active" : ""} onClick={() => setActiveTab("users")}>
              Users
            </button>
            <button className={activeTab === "officers" ? "active" : ""} onClick={() => setActiveTab("officers")}>
              Officers
            </button>
            <button
              className={activeTab === "reports" ? "active" : ""}
              onClick={() => navigate("/reports")}
            >
              Reports & Export
            </button>
          </div>

          <div className="main-panel">
           
            <div className="chart-row">
              <div className="chart-container">
                <h3>Status Overview</h3>
                <div className="pie-wrapper">
                  <Pie data={pieData} options={pieOptions} />
                </div>
              </div>
              <div className="chart-container">
                <h3>Complaints by Category</h3>
                <div style={{ height: "260px" }}>
                  <Bar data={barData} options={barOptions} />
                </div>
              </div>
            </div>

           
            <div className="summary-cards">
              <div className="card">
                <h4>Pending</h4>
                <p>{statusCounts.Pending}</p>
              </div>
              <div className="card">
                <h4>Ongoing</h4>
                <p>{statusCounts.Ongoing}</p>
              </div>
              <div className="card">
                <h4>Resolved</h4>
                <p>{statusCounts.Resolved}</p>
              </div>
            </div>

          
            {activeTab === "complaints" && (
              <div className="table-container">
                <h3>All Complaints</h3>
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Category</th>
                      <th>Assign Officer</th>
                      <th>Due Date</th>
                      <th>Action</th>
                      <th>Status</th>
                      <th>View Details</th>
                      
                    </tr>
                  </thead>
                  <tbody>
                    {complaints.length > 0 ? (
                      complaints.map((c) => (
                        <tr key={c.id}>
                          <td>{c.id}</td>
                          <td>{c.category}</td>
                          <td>
                            <select id={"assign-" + c.id} defaultValue={c.officerId || ""}>
                              <option value="">--Assign--</option>
                              {officers.map((o) => (
                                <option key={o.id} value={o.id}>
                                  {o.name} ({o.department})
                                </option>
                              ))}
                            </select>
                            <button className="small-btn"
                              onClick={async () => {
                                const sel = document.getElementById("assign-" + c.id);
                                const officerId = sel.value;
                                if (!officerId) return alert("Please select an officer");
                                await fetch(
                                  `http://localhost:8080/api/admin1/complaints/${c.id}/assign?officerId=${officerId}`,
                                  { method: "PUT" }
                                );
                                fetchComplaints();
                              }}
                            >
                              Assign
                            </button>
                          </td>
                          <td>
                            {c.dueDate || "Not Set"}
                            <button className="small-btn"
                              onClick={async () => {
                                const newDue = prompt("Enter Due Date (YYYY-MM-DD)");
                                if (!newDue) return;
                                const formatted = new Date(newDue).toISOString();
                                await fetch(`http://localhost:8080/api/admin1/complaints/${c.id}/dueDate`, {
                                  method: "PUT",
                                  headers: { "Content-Type": "application/json" },
                                  body: JSON.stringify({ dueDate: formatted }),
                                });
                                fetchComplaints();
                              }}
                            >
                              Set
                            </button>
                          </td>
                          <td>
                            <button className="status-btn status-pending small-status-btn" background-color="#c47321ff" onClick={() => handleStatusChange(c.id, "Pending")}>
                              Pending
                            </button>
                            <button className="status-btn status-ongoing small-status-btn" background-color="#2183c4ff" onClick={() => handleStatusChange(c.id, "Ongoing")}>
                              Ongoing
                            </button>
                            <button className="status-btn status-resolved small-status-btn" background-color="#26bd62ff" onClick={() => handleStatusChange(c.id, "Resolved")}>
                              Resolved
                            </button>
                          </td>
                          <td style={{ fontWeight: "bold", color: c.escalated ? "red" : "inherit" }}>
                            {c.escalated ? "Escalated ⛔" : c.status}
                          </td>
                          <td>
                            <button className="small-btn" onClick={() => navigate(`/admin/complaint/${c.id}`)} >
                              View Details
                            </button>
                          </td>
                          
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" style={{ textAlign: "center" }}>
                          No complaints found.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            )}

            
            {activeTab === "users" && (
              <div className="table-container">
                <h3>Registered Users</h3>
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>First Name</th>
                      <th>Last Name</th>
                      <th>Email</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.length > 0 ? (
                      users.map((u) => (
                        <tr key={u.id}>
                          <td>{u.id}</td>
                          <td>{u.firstName}</td>
                          <td>{u.lastName}</td>
                          <td>{u.email}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="4" style={{ textAlign: "center" }}>
                          No users registered.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            )}

            
            {activeTab === "officers" && (
              <div className="table-container">
                <h3>Officers</h3>
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Department</th>
                      <th>Phone</th>
                      <th>Complaint_number</th>
                    </tr>
                  </thead>
                  <tbody>
                    {officers.length > 0 ? (
                      officers.map((o) => (
                        <tr key={o.id}>
                          <td>{o.id}</td>
                          <td>{o.name}</td>
                          <td>{o.email}</td>
                          <td>{o.department}</td>
                          <td>{o.phone}</td>
                          <td>{o.Complaint_number}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="6" style={{ textAlign: "center" }}>
                          No officers found.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>
  
  );
}
