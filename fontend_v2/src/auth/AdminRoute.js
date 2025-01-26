// src/components/PrivateRoute.js
import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../useAuth';

const AdminRoute = () => {
  const { isAuthenticated, loadding, userRole , validateToken} = useAuth();
  if(loadding){
    console.log('PrivateRoute isAuthenticated:', isAuthenticated); // Ghi lại trạng thái xác thực
    console.log('PrivateRoute userRole:', userRole); // Ghi lại vai trò người dùng
  
    if (!isAuthenticated) {
      alert("Phiên đăng nhập dã hết hạn!");
      return <Navigate to="/login" />;
    }

    // Thay thế điều kiện này bằng điều kiện kiểm tra quyền truy cập của bạn
    if (userRole === '[ROLE_USER]') {
      return <Navigate to="/403" />;
    }
  
    return <Outlet />;
  }
};

export default AdminRoute;
