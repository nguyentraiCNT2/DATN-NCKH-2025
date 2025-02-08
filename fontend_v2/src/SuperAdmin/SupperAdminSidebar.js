import React from 'react';
import axios from 'axios';
const SupperAdminSidebar = () => {
    const token = localStorage.getItem('token');
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
  return (
    <aside className="super-admin-sidebar">
      <nav>
        <ul>
          <li> <a href='/supper/admin' className='super-admin-action'>Quản lý người dùng</a> </li>
          <li> <a href='/admin' className='super-admin-action'>Trang Admin</a> </li>
          <li> <a href='/' className='super-admin-action'>Trang người dùng</a> </li>
          <li onClick={() => handleLogOut()}>Đăng xuất</li>
        </ul>
      </nav>
    </aside>
  );
};

export default SupperAdminSidebar;