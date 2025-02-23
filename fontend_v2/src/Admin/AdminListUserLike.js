import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminListUserLikeContent from "./AdminComponents/AdminListUserLikeContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminLikeListByPost = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminListUserLikeContent />
          </div>
        </div>
      );
};

export default AdminLikeListByPost;
