import React, { useState } from "react";
import "./LoginPage.css";
import { useNavigate } from "react-router-dom";

export default function LoginPage({ onLogin }) {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      let data = null;

      // --- ADMIN LOGIN ---
      let response = await fetch("http://localhost:8080/api/admin1/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: formData.username,
          password: formData.password,
        }),
      });

      if (response.ok) {
        data = await response.json();
        data.role = "admin";
      } else {
        // --- OFFICER LOGIN ---
        response = await fetch("http://localhost:8080/api/officers/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            email: formData.username,
            password: formData.password,
          }),
        });

        if (response.ok) {
          data = await response.json();
          data.role = "officer";
        } else {
          // --- USER LOGIN ---
          response = await fetch("http://localhost:8080/api/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              email: formData.username,
              password: formData.password,
            }),
          });

          if (response.ok) {
            data = await response.json();
            data.role = "user";
          } else {
            const errorText = await response.text();
            alert("Login failed: " + errorText);
            return;
          }
        }
      }

      alert("Login successful!");
      sessionStorage.clear();

      // --- Assign SessionStorage & Redirect ---
      if (data.role === "admin") {
        sessionStorage.setItem("adminId", data.adminId);
        sessionStorage.setItem("adminEmail", data.email);
        sessionStorage.setItem("adminName", data.firstName);
        navigate("/admin-dashboard");
      } 
      else if (data.role === "officer") {
        sessionStorage.setItem("officerId", data.id);
        sessionStorage.setItem("officerName", data.name);
        navigate("/officer-dashboard");
      } 
      else {
        sessionStorage.setItem("userId", data.userId ?? data.id);
        sessionStorage.setItem("userEmail", data.email);
        sessionStorage.setItem("userName", data.firstName);
        navigate("/user-dashboard");
      }

      onLogin(data);

    } catch (error) {
      console.error("Error:", error);
      alert("Server not responding");
    }
  };

  return (
    
    <div className="login-container">
      <div className="left-side">
        {/* <h6 className="title">RESOLVE IT</h6> */}
        <h6 className="title">RESOLVE IT</h6>
        <p className="info">
          Resolve IT is an online complaint and feedback management system where 
          users can easily submit their complaints, upload images, and track their 
          status — whether it's pending, resolved, or in progress. 
          Our platform also helps administrators efficiently monitor and manage 
          all registered complaints in one place.
        </p>
      </div>

      <div className="login-box">
        <h2>Login</h2>
        <form onSubmit={handleLogin}>
          <label>Username:</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Enter username"
          />

          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter password"
          />

          <button type="submit">Login</button>
        </form>

        <p>
          Don’t have an account?{" "}
          <button onClick={() => navigate("/create-account")} className="link-btn">
            Create Account
          </button>
        </p>

        <button onClick={() => navigate("/user-dashboard")} className="link-btn">
          Anonymous Login
        </button>
      </div>
    </div>
  );
}
