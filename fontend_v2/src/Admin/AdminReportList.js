import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminReportContent from "./AdminComponents/AdminReportContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminReportList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminReportContent />
          </div>
        </div>
      );
};

export default AdminReportList;
