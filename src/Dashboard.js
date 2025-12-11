import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // ✅ import
import "./Dashboard.css";

export default function Dashboard({ user }) {
  const [theme, setTheme] = useState("light");
  const [formData, setFormData] = useState({
    category: "",
    urgency: "",
    complaint: "",
    location: "",
    image: null,
  });

  const navigate = useNavigate(); 


  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image") {
      setFormData({ ...formData, image: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };


  const handleSubmit = async (e) => {
  e.preventDefault();
  const userId = Number(sessionStorage.getItem("userId"));
  console.log("User ID while submitting:", sessionStorage.getItem("userId"));
  const formDataToSend = new FormData();
  formDataToSend.append("userId", sessionStorage.getItem("userId"));
  formDataToSend.append("category", formData.category);
  formDataToSend.append("urgency", formData.urgency);
  formDataToSend.append("complaint",formData.complaint);
  formDataToSend.append("location", formData.location);
  if (formData.image) {
    formDataToSend.append("image", formData.image);
  }

  try {
    const response = await fetch("http://localhost:8080/api/complaints/add", {
      method: "POST",
      body: formDataToSend, 
    });

    if (response.ok) {
      alert("✅ Complaint submitted successfully!");  
      setFormData({ category: "", urgency: "", complaint: "", location: "", image: null });
      navigate("/user-dashboard");
    } else {
      alert("⚠️ Something went wrong!");
    }
  } catch (error) {
    alert("⚠️ Server error, please try again later!");
  }
};

    

  const handleLogout = () => {
    navigate("/"); 
  };

  return (
    <div className={`dash-container ${theme === "dark" ? "dark-theme" : "light-theme"}`}>
      <header className="dash-header">
        <h2>Welcome {user?.firstName || ""}</h2>
        <div className="header-actions">
        <button onClick={handleLogout} className="logout-btn">
          Logout
        </button>
        <div className="theme-toggle">
        <button
            className="theme-toggle-btn"
            onClick={() => setTheme(theme === "light" ? "dark" : "light")}
          >
            {theme === "light" ? "🌙 Dark" : "☀️ Light"}
          </button>
          </div>
        </div>
      </header>

      <div className="dash-content">
        <h3>{user?.type === "named" ? "Anonymous Feedback" : "Your Feedback Panel"}</h3>
        <p>
          {user?.type === "named"
            ? "Your identity is visible. Feel free to share honest feedback safely."
            : "Here you can submit grievances or feedback to improve your institution’s environment."}
        </p>

        <form className="feedback-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Complaint Category:</label>
            <select
              name="category"
              value={formData.category}
              onChange={handleChange}
              required
            >
              <option value="">Select</option>
              <option value="Water">Water</option>
              <option value="Air">Air</option>
              <option value="Electricity">Electricity</option>
              <option value="Cleanliness">Cleanliness</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label>Urgency Level:</label>
            <div className="urgency-options">
              {["Low", "Medium", "High"].map((level) => (
                <label key={level}>
                  <input
                    type="radio"
                    name="urgency"
                    value={level}
                    checked={formData.urgency === level}
                    onChange={handleChange}
                  />
                  {level}
                </label>
              ))}
            </div>
          </div>

          <div className="form-group">
            <label>Complaint Description:</label>
            <textarea
              name="complaint"
              placeholder="Write your feedback or complaint..."
              value={formData.complaint}
              onChange={handleChange}
              required
            ></textarea>
          </div>
          <div className="form-group">
            <label>Location:</label>
            <textarea
              name="location"
              placeholder="Enter your Location"
              value={formData.location}
              onChange={handleChange}
              required
            ></textarea>
          </div>

          <div className="form-group">
            <label>Upload Image (optional):</label>
            <input
              type="file"
              name="image"
              accept="image/*"
              onChange={handleChange}
            />
            {formData.image && (
              <div className="preview">
                <img src={URL.createObjectURL(formData.image)} alt="Preview" />
              </div>
            )}
          </div>

          <button type="submit" className="submit-btn">
            Submit Complaint
          </button>
        </form>
      </div>
    </div>
  );
}
