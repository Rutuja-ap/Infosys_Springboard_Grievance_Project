import React, { useState } from "react";
import "./CreateAccount.css"; 
import  "./LoginPage.js";
import { useNavigate } from "react-router-dom";


export default function CreateAccount({ onCreateAccount , onLogin}){
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/users/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setMessage("✅ Account created successfully!");
        setFormData({ firstName: "", lastName: "", email: "", password: "" });
      } else {
        setMessage("⚠️ Something went wrong while creating account!");
      }
    } catch (error) {
      setMessage("⚠️ Server error. Please try again later.");
    }
  };

  return (
    <div className="create-container">
      <h2>Create Account</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="firstName"
          placeholder="First Name"
          value={formData.firstName}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="lastName"
          placeholder="Last Name"
          value={formData.lastName}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <button type="submit">Create Account</button>
        <p>
          Already have an account?{" "}
          <button onClick={() => navigate("/")} className="link-btn">
            Login
          </button>
        </p>
      </form>

      {message && (
        <p className={message.startsWith("✅") ? "success" : "error"}>
          {message}
        </p>
      )}
    </div>
  );
};

