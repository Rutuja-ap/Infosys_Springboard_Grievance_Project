import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./LoginPage";
import CreateAccount from "./CreateAccount";
import Dashboard from "./Dashboard";
import UserDashboard from "./UserDashboard";
import AdminDashboard from "./AdminDashboard"; 
import OfficerDashboard from "./OfficerDashboard";
import ComplaintDetails from './ComplaintDetails';

import "react-toastify/dist/ReactToastify.css";
import ExportReport from "./ExportReport";
import FeedbackForm from "./FeedbackForm";
import Notifications from "./Notification";

function App() {
  const [user, setUser] = useState(null);

  return (
    <Router>
     
      <Routes>
        <Route path="/" element={<LoginPage onLogin={setUser} theme="colored"/>} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/create-account" element={<CreateAccount />} />
        <Route path="/user-dashboard" element={<UserDashboard />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/officer-dashboard" element={<OfficerDashboard />} /> 
        <Route path="/admin/complaint/:id" element={<ComplaintDetails />} />
        <Route path="/reports" element={<ExportReport />} />
        <Route path="/user/feedback/:complaintId" element={<FeedbackForm />} />
        <Route path="/notifications" element={<Notifications />} />
      </Routes>
    </Router>
  );
}

export default App;
