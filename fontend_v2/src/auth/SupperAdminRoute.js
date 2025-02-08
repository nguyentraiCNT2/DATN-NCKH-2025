import React, { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './useAuth';

const SupperAdminRoute = () => {
  const { isAuthenticated, loading, userRole, error } = useAuth();

  useEffect(() => {
    if (error) {
      alert(error); // Hiển thị thông báo một lần nếu có lỗi
    }
  }, [error]); // Khi có lỗi, thông báo sẽ được hiển thị

  if (loading) {
    console.log('PrivateRoute isAuthenticated:', isAuthenticated); // Ghi lại trạng thái xác thực
    console.log('supper Route userRole:', userRole); // Ghi lại vai trò người dùng

    if (!isAuthenticated) {
      return <Navigate to="/login" />;
    }
    if (userRole !== '[ROLE_SUPER_ADMIN]') {
      return <Navigate to="/403" />;
    }
    return <Outlet />;
  }

  return null; // Trả về null khi chưa tải xong
};

export default SupperAdminRoute;
