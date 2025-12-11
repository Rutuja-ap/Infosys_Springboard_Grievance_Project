import React, { useEffect, useState } from "react";
import "./Notification.css";

export default function Notification() {
  const [newComplaints, setNewComplaints] = useState([]);
  const [reviewedComplaints, setReviewedComplaints] = useState([]);



  useEffect(() => {
    Promise.all([
        fetch("http://localhost:8080/api/admin1/notifications/unseen")
            .then((res) => res.json()),
        fetch("http://localhost:8080/api/admin1/notifications/last24hrs")
            .then((res) => res.json())
    ]).then(([unseen, last24hrs]) => {

      const reviewed = last24hrs.filter(
    n => !unseen.some(u => u.id === n.id)
  );
        setNewComplaints(unseen);
        setReviewedComplaints(reviewed);
    });
}, []);


    const markAsRead = async (id) => {
  await fetch(`http://localhost:8080/api/admin1/notifications/read/${id}`, {
    method: "PUT",
  });

  setNewComplaints((prevNew) => {
    const updatedNew = prevNew.filter((n) => n.id !== id);
    const item = prevNew.find((n) => n.id === id);

    
    if (item) {
      setReviewedComplaints((prevR) => [...prevR, { ...item, read: true }]);
    }

    return updatedNew;  
  });
};


  return (
    <div className="notif-page">
      <h2>Notifications</h2>

     
      <div className="notif-section">
        <h3>New Complaints</h3>

        {newComplaints.length === 0 && <p className="empty">No new notifications</p>}

        {newComplaints.map((n) => (
          <div className="notif-card new" key={n.id}>
            <p>{n.message}</p>
            <p>{n.category}</p>
            <p>{n.urgency}</p>
            <p>{n.location}</p>
            <button onClick={() => markAsRead(n.id)}>Mark as Read</button>
          </div>
        ))}
      </div>

    
      <div className="notif-section">
        <h3>Reviewed</h3>

        {reviewedComplaints.length === 0 && <p className="empty">No reviewed items</p>}

        {reviewedComplaints.map((n) => (
          <div className="notif-card reviewed" key={n.id}>
            <p>{n.message}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
