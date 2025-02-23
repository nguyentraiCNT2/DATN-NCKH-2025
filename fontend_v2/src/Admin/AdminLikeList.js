import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminLikeContent from "./AdminComponents/AdminLikeContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminLikeList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminLikeContent />
          </div>
        </div>
      );
};

export default AdminLikeList;
