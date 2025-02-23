import React from "react";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import AdminListPostByCommentContent from "./AdminComponents/AdminListPostByCommentContent";
import AdminHeader from "./AdminComponents/AdminHeader";
import "./styles/AdminDashboard.css";
 
const AdminListPostByComment = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminListPostByCommentContent />
          </div>
        </div>
      );
};

export default AdminListPostByComment;
