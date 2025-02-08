import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminPostContent from "./AdminComponents/AdminPostContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminPostList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminPostContent />
          </div>
        </div>
      );
};

export default AdminPostList;
