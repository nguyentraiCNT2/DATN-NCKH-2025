import { useState } from "react";
import { useLocation } from "react-router-dom";
import "./../styles/AdminSidebar.css";
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
          className={`admin-sidebar-item ${location.pathname === "/admin/theme" ? "active" : ""}`}
        >
          <a href="/admin/theme" className="admin-sidebar-link">
            <i class="fas fa-paint-brush"></i> themes
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
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/post/like"  ? "active" : ""}`}
        >
          <a href="/admin/post/like" className="admin-sidebar-link">
          <i class="fa-solid fa-thumbs-up"></i> Post By Likes
          </a>
        </li>
        <li
          className={`admin-sidebar-item ${location.pathname === "/admin/post/comment"  ? "active" : ""}`}
        >
          <a href="/admin/post/comment" className="admin-sidebar-link">
          <i class="fa-regular fa-rectangle-list"></i> Post By Comments
          </a>
        </li>
      </ul>
    </div>
  );
}

export default AdminSidebar;
