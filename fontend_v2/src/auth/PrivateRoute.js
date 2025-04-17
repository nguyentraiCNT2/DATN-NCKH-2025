// src/components/PrivateRoute.js
import React, { useEffect, useState } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './useAuth';

const PrivateRoute = () => {
  const { isAuthenticated, loading, userRole, error } = useAuth();
  const [showError, setShowError] = useState(false); // State để điều khiển hiển thị thông báo
  const handleAgree = () => {
    setShowError(false); // Đóng thông báo khi nhấn "Đồng ý"
  };

  useEffect(() => {
    if (error) {
      
      setShowError(true); // Khi có lỗi, hiển thị thông báo
    }
  }, [error]); // Khi có lỗi, thông báo sẽ được hiển thị

  if (loading) {


    if (!isAuthenticated) {
      return <Navigate to="/login" />;
    }
    return <Outlet />;
  }
  return null; // Trả về null khi chưa tải xong
};

export default PrivateRoute;
