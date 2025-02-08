import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminCommentContent from "./AdminComponents/AdminCommentContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminCommentList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminCommentContent />
          </div>
        </div>
      );
};

export default AdminCommentList;
