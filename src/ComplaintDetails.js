import { useParams } from "react-router-dom";
import axios from "axios";
import "./ComplaintDetail.css";
import React from "react";
import { useEffect, useState } from "react";
import "./FeedbackForm.js"


export default function ComplaintDetails() {
  const { id } = useParams();
  const [complaint, setComplaint] = useState(null);
  const [loading, setLoading] = useState(true);
  const [feedback, setFeedback] = useState([]);
  const [notes, setNotes] = useState([]);
const [newNote, setNewNote] = useState("");
const [noteType, setNoteType] = useState("external");
const [activeTab, setActiveTab] = useState("external");

const role = sessionStorage.getItem("role")?.toUpperCase() || "USER";


const addNote = () => {
  if (!newNote.trim()) return;

  const payload = {
    complaintId: id,
    message: newNote,
    noteType: noteType,
    createdBy: role,
  };
 
  axios.post("http://localhost:8080/api/notes", payload)
  .then(res => setNotes(prevNotes => [res.data, ...prevNotes]))
  .then(() => setNewNote(""))
  .catch(err => console.error("Error adding note", err));

}

useEffect(() => {
  axios
    .get(`http://localhost:8080/api/complaints/${id}`)
    .then((res) => {
      setComplaint(res.data); 
    })
    .catch((err) => console.error(err))
    .finally(() => setLoading(false));

  
  axios
    .get(`http://localhost:8080/api/feedback/complaint/${id}`)
    .then((res) => {
      setFeedback(res.data);
    })
    .catch(() => setFeedback([]));
  axios
  .get(`http://localhost:8080/api/notes/complaint/${id}`)
    .then(res => setNotes(res.data))
   
    .catch(err => console.error("Error loading notes", err));

}, [id]);

  const fmt = (d) => (d ? new Date(d).toLocaleString() : "—");

  const statusClass = (s) => {
    if (!s) return "status-unknown";
    if (s.toLowerCase().includes("escal")) return "status-escalated";
    if (s.toLowerCase().includes("resolv")) return "status-resolved";
    if (s.toLowerCase().includes("ongoing")) return "status-ongoing";
    return "status-open";
  };

  if (loading) return <div className="cd-loading">Loading complaint...</div>;
  if (!complaint) return <div className="cd-empty">Complaint not found.</div>;

  return (
    <div className="cd-root">

     
      <div className="cd-left">
        <div className="cd-header">
          <h1 className="cd-title">
            Complaint #{complaint.complaintNumber || complaint.id}
          </h1>
          <div className={`cd-badge ${statusClass(complaint.status)}`}>
            {complaint.status}
          </div>

          <div className="cd-meta">
            <div><strong>Created:</strong> {fmt(complaint.createdAt)}</div>
            <div><strong>Due:</strong> {fmt(complaint.dueDate)}</div>
          </div>
        </div>

     
        <section className="cd-card">
          <h2 className="cd-section-title">Complaint Information</h2>

          <div className="cd-row">
            <span>Level</span>
            <p>{complaint.title}</p>
          </div>

          <div className="cd-row">
            <span>Category</span>
            <p>{complaint.category}</p>
          </div>

          <div className="cd-row full">
            <span>Description</span>
            <p>{complaint.description}</p>
          </div>
          <div className="images-section">
          <p className="image-label">Before Image</p>
          <div className="image-box">
            {complaint.beforeImage ? (
              <img src={complaint.beforeImage} alt="Before" />
            ) : (
              "No Before Image"
            )}
          </div>

          <p className="image-label">After Image</p>
          <div className="image-box">
            {complaint.afterImage ? (
              <img src={complaint.afterImage} alt="After" />
            ) : (
            "No After Image"
            )}
          </div>
        </div>
        </section>

      
        <section className="cd-card">
          <h2 className="cd-section-title">Status Timeline</h2>

          <div className="timeline">
            <div className="time-box">
              <span>Created</span>
              <p>{fmt(complaint.createdAt)}</p>
            </div>

            <div className="time-box">
              <span>Ongoing</span>
              <p>{fmt(complaint.ongoingAt)}</p>
            </div>

            <div className="time-box">
              <span>Resolved</span>
              <p>{fmt(complaint.resolvedAt)}</p>
            </div>

            <div className="time-box">
              <span>Escalated</span>
              <p>{fmt(complaint.escalatedAt)}</p>
            </div>
          </div>
        </section>

        
        <section className="cd-card">
          <h2 className="cd-section-title">Status History</h2>

          <ul className="cd-history">
            {complaint.statusHistory?.map((h, i) => (
              <li key={i}>
                <strong>{h.status}</strong> — {fmt(h.date)}
              </li>
            ))}
          </ul>
        </section>
      </div>

      
      <div className="cd-right">
        <section className="cd-card">
          <h2 className="cd-section-title">User Details</h2>

          <div className="cd-row">
            <span>Name</span>
            <p>
              {complaint.userFirstName} {complaint.userLastName}
            </p>
          </div>

          <div className="cd-row">
            <span>Email</span>
            <p>{complaint.userEmail}</p>
          </div>

          <div className="cd-row">
            <span>Location</span>
            <p>{complaint.userLocation}</p>
          </div>
        </section>

        <section className="cd-card">
          <h2 className="cd-section-title">Officer Details</h2>

          <div className="cd-row">
            <span>Name</span>
            <p>{complaint.officerName}</p>
          </div>

          <div className="cd-row">
            <span>Email</span>
            <p>{complaint.officerEmail}</p>
          </div>

          <div className="cd-row">
            <span>Phone</span>
            <p>{complaint.officerPhone}</p>
          </div>
        </section>
        <section className="cd-card">
          <h2 className="cd-section-title">User Feedback</h2>

          {(!feedback || feedback.length === 0) ? (
            <p className="empty-feedback">No feedback submitted yet.</p>
          ) : (
            feedback.map((fb, index) => (
              <div key={index} className="fb-box">
                <div className="fb-header">
                  <strong>{fb.name}</strong> <span>({fb.location})</span>
                </div>

                <p className="fb-comment">“{fb.comment}”</p>

                <p className="fb-rating">
                  Rating: {"★".repeat(fb.rating)}{"☆".repeat(5 - fb.rating)}
                </p>

                <div className="fb-answers">
                  <p><b>Q1:</b> {fb.question1}</p>
                  <p><b>Q2:</b> {fb.question2}</p>
                  <p><b>Q3:</b> {fb.question3}</p>
                </div>
              </div>
            ))
          )}
          </section>
          <section className="cd-card">
            <div className="notes-section">

  <h2>Notes</h2>


  <div className="note-tabs">

  
    {(role === "ADMIN" || role === "OFFICER") && (
      <button
        className={activeTab === "internal" ? "active" : ""}
        onClick={() => setActiveTab("internal")}
      >
        Internal
      </button>
    )}

   
    <button
      className={activeTab === "external" ? "active" : ""}
      onClick={() => setActiveTab("external")}
    >
      External
    </button>

  </div>


  <div className="notes-list">
    {notes.filter(n => n.noteType === activeTab).length === 0 ? (
      <p className="no-notes">No notes yet.</p>
    ) : (
      notes
        .filter(n => n.noteType === activeTab)
        .map((n) => (
          <div key={n.id} className="note-box">
            <p>{n.message}</p>
            <small>
              By: {n.createdBy} | {n.createdAt.replace("T", " • ")}
            </small>
          </div>
        ))
    )}
  </div>


 


  {role === "ADMIN" && (
  <div className="add-note-box">

    <textarea
      value={newNote}
      onChange={(e) => setNewNote(e.target.value)}
      placeholder="Write a note..."
    />

    <select value={noteType} onChange={(e) => setNoteType(e.target.value)}>
      <option value="internal">Internal</option>
      <option value="external">External</option>
    </select>

    <button onClick={addNote}>Add Note</button>

  </div>
)}

  </div>

          </section>

      </div>
    </div>
  );
}
