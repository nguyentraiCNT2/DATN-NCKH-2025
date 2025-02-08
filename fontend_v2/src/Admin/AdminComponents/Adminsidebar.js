import React, { useState } from "react";
import  "./../styles/AdminSidebar.css";
import { useLocation } from "react-router-dom";
function AdminSidebar() {
  const [activeMenu, setActiveMenu] = useState("home");
  const location = useLocation();
  const handleMenuClick = (menu) => {
    setActiveMenu(menu);
  };

  return (
    <div className="admin-sidebar">
      <div className="admin-sidebar-title">
        <i className="admin-sidebar-icon fas fa-cogs"></i> Admin Panel
      </div>
      <ul className="admin-sidebar-menu">
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin" ? "active" : ""}`}
        >
          <a href="/admin" className="admin-sidebar-link">
            <i className="admin-sidebar-icon fas fa-home"></i> Home
          </a>
        </li>
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/post" ? "active" : ""}`}
        >
          <a href="/admin/post" className="admin-sidebar-link">
            <i className="admin-sidebar-icon fas fa-pen"></i> Posts
          </a>
        </li>
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/group" ? "active" : ""}`}
        >
          <a href="/admin/group" className="admin-sidebar-link">
            <i className="admin-sidebar-icon fas fa-users"></i> Groups
          </a>
        </li>
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/report"  ? "active" : ""}`}
        >
          <a href="/admin/report" className="admin-sidebar-link">
            <i className="admin-sidebar-icon fas fa-flag"></i> Reports
          </a>
        </li>
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/comment"  ? "active" : ""}`}
        >
          <a href="/admin/comment" className="admin-sidebar-link">
          <i class="fa-solid fa-comments"></i>Comments
          </a>
        </li>
      </ul>
    </div>
  );
}

export default AdminSidebar;
