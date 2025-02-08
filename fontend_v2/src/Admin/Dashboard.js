import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminContent from "./AdminComponents/AdminContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const Dashboard = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminContent />
          </div>
        </div>
      );
};

export default Dashboard;
