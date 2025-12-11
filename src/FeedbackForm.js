import React, { useState } from "react";
import { useParams } from "react-router-dom";
import "./FeedbackForm.css";

export default function FeedbackForm() {
  const { complaintId } = useParams();
  const userId = sessionStorage.getItem("userId");
  const userName = sessionStorage.getItem("userName") || "";
  const userLocation = sessionStorage.getItem("location") || "";

  
  const [name, setName] = useState(userName);
  const [location, setLocation] = useState(userLocation);
  
 
  const [q1, setQ1] = useState("");
  const [q2, setQ2] = useState("");
  const [q3, setQ3] = useState("");

  const [comment, setComment] = useState("");
  const [rating, setRating] = useState(0);
  const [submitted, setSubmitted] = useState(false);


  const handleStarClick = (value) => {
    setRating(value);
  };

  const submitFeedback = async (e) => {
    e.preventDefault();

    const payload = {
      complaintId: Number(complaintId),
      userId: Number(userId),
      rating,
      comment,
      name,
      location,
      question1: q1,
      question2: q2,
      question3: q3
    };  

    const res = await fetch("http://localhost:8080/api/feedback", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (res.ok) {
      setSubmitted(true);
    } else {
      alert("Something went wrong");
    }
  };

  return (
    <div className="feedback-container">
      <h2>Complaint Feedback #{complaintId}</h2>

      {submitted && <p className="success-msg">Feedback Submitted Successfully ✔</p>}

      <form onSubmit={submitFeedback}>

      
        <label>Full Name:</label>
        <input
          type="text"
          value={name}
          onChange={(e)=>setName(e.target.value)}
          required
        />

        
        <label>Your Location:</label>
        <input
          type="text"
          value={location}
          onChange={(e)=>setLocation(e.target.value)}
          required
        />

        
        <div className="question-block">
          <p>How satisfied are you with the resolution speed?</p>
          <label><input type="radio" name="q1" value="Excellent" onChange={()=>setQ1("Excellent")}/> Excellent</label>
          <label><input type="radio" name="q1" value="Good" onChange={()=>setQ1("Good")}/> Good</label>
          <label><input type="radio" name="q1" value="Average" onChange={()=>setQ1("Average")}/> Average</label>
          <label><input type="radio" name="q1" value="Poor" onChange={()=>setQ1("Poor")}/> Poor</label>
        </div>

        <div className="question-block">
          <p>Was the issue handled professionally?</p>
          <label><input type="radio" name="q2" value="Yes" onChange={()=>setQ2("Yes")}/> Yes</label>
          <label><input type="radio" name="q2" value="No" onChange={()=>setQ2("No")}/> No</label>
        </div>

        <div className="question-block">
          <p>Would you recommend this service to others?</p>
          <label><input type="radio" name="q3" value="Definitely" onChange={()=>setQ3("Definitely")}/> Definitely</label>
          <label><input type="radio" name="q3" value="Maybe" onChange={()=>setQ3("Maybe")}/> Maybe</label>
          <label><input type="radio" name="q3" value="No" onChange={()=>setQ3("No")}/> No</label>
        </div>

        
        <label>Detailed Feedback:</label>
        <textarea
          rows="5"
          placeholder="Write your experience..."
          value={comment}
          onChange={(e)=>setComment(e.target.value)}
          required
        ></textarea>

        <label>Your Rating:</label>
        <div className="stars">
          {[1,2,3,4,5].map((star)=>(
            <span
              key={star}
              className={star<=rating ? "star filled":"star"}
              onClick={()=>handleStarClick(star)}
            >★</span>
          ))}
        </div>
        
        <button type="submit">Submit Feedback</button>

      </form>
    </div>
  );
}
