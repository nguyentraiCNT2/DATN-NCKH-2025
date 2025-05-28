import AdminHeader from "./AdminComponents/AdminHeader";
import AdminThemeContent from "./AdminComponents/AdminThemeContentList";
import AdminSidebar from "./AdminComponents/Adminsidebar";
import "./styles/AdminDashboard.css";
 
const AdminThemeList = () => {
    return (
        <div className="admin-dashboard">
          <AdminSidebar />
          <div className="admin-main">
            <AdminHeader />
            <AdminThemeContent />
          </div>
        </div>
      );
};

export default AdminThemeList;
