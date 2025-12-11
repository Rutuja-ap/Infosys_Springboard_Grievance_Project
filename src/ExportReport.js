import React, { useState } from "react";
import "./ExportReport.css";

const ExportReport = () => {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [category, setCategory] = useState("");
  const [format, setFormat] = useState("csv");

  const handleGenerateReport =async () => {
    if (!startDate || !endDate || !category) {
      alert("Please fill all parameters!");
      return;
    }

    const url = `http://localhost:8080/api/complaints/report/export?start=${startDate}&end=${endDate}&category=${category}&format=${format}`;
    
      try {
    const response = await fetch(url);
    if (!response.ok) {
      const text = await response.text();
      alert(text);
      return;
    }

    const blob = await response.blob();
    const downloadUrl = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = downloadUrl;
    a.download = `complaints-report.${format}`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(downloadUrl);
  } catch (error) {
    console.error(error);
    alert("Error downloading report");
  }
  };

  return (
    <div className="report-wrapper">
      <h3 className="titlee">Reports & Exports</h3>

      <p className="section-title">Report Parameters</p>

      <div className="report-card">

        <label>Date Range</label>
        <div className="date-row">
          <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)}/>
          <span>to</span>
          <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)}/>
        </div>

        <label>Complaint Categories</label>
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          <option value="">Select Category</option>
          <option value="Air">Air</option>
          <option value="Water">Water Supply</option>
          <option value="Electricity">Electricity</option>
          <option value="Cleanliness">Cleanliness</option>
          <option value="Other">Other</option>
        </select>

        <p className="section-title">Export Options</p>
        <div className="format-buttons">
          <button
            className={format === "csv" ? "active" : ""}
            onClick={() => setFormat("csv")}
          >
            CSV
          </button>
          <button
            className={format === "pdf" ? "active" : ""}
            onClick={() => setFormat("pdf")}
          >
            PDF
          </button>
        </div>

        <button className="generate-btn" onClick={handleGenerateReport}>
          Generate Report
        </button>
      </div>
    </div>
  );
};

export default ExportReport;
