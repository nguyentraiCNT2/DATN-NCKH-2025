import React, { useState } from "react";
import "./../styles/AdminHeader.css";
import { UserProfileFuntions } from "../../auth/UserProfile";
import axios from "axios";
function AdminHeader() {
  const {userData} = UserProfileFuntions();
  const [isMenuVisible, setMenuVisible] = useState(false);
  const token = localStorage.getItem('token');
  const toggleMenu = () => setMenuVisible(!isMenuVisible);
  const handleacion = (url) =>{
    window.location.href = url;
  }
  // Xử lý khi người dùng nhấn Đăng xuất
  const handleLogOut = async () => {
    try {
        await axios.post(
            'http://localhost:8080/auth/logout',
            {}, // Body trống vì chỉ cần gửi header
            {
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            }
        );
        window.location.href = '/login';
    } catch (error) {
        console.error("Error logging out:", error);
    }
};
console.log('admin role test',userData?.user?.roleId.name);
  return (
    <div className="admin-header">
      <h2>Admin Dashboard</h2>
      <div className="admin-header-user" onMouseEnter={toggleMenu} onMouseLeave={toggleMenu}>
        <img
        src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'}
          alt="User Avatar"
          className="admin-avatar"
        />
        {isMenuVisible && (
          <div className="admin-user-menu">
            <ul>
              <li onClick={() => handleacion(`/profile`)}>Profile</li>
              <li onClick={() => handleacion(`/`)}>User Settings</li>
              {userData?.user?.roleId?.name === "SUPER_ADMIN" && (
                   <li onClick={() => handleacion(`/supper/admin`)}>Super Admin Settings</li>
              )}
              <li  onClick={() => handleLogOut()} >Logout</li>
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminHeader;
