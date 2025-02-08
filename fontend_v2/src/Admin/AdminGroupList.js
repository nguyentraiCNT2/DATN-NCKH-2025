import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminGroupContent from "./AdminComponents/AdminGroupContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminGroupList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminGroupContent />
          </div>
        </div>
      );
};

export default AdminGroupList;
