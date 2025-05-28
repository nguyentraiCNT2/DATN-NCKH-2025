// src/hooks/useAuth.js
import axios from 'axios';
import { useEffect, useState } from 'react';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState('');
  const token = localStorage.getItem('token');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null); // Thêm trạng thái để theo dõi lỗi

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await axios.get('http://localhost:8080/auth/status', {
          headers: {
            'Authorization': `Bearer ${token}` // Thêm token vào header
          }
        });

        setIsAuthenticated(response.data.authenticated);
        setUserRole(response.data.role);
      } catch (error) {
        console.error('Error fetching authentication status:', error); // Ghi lại lỗi
        setIsAuthenticated(false);
        setUserRole('');
        setError('Phiên đăng nhập đã hết hạn!'); // Lưu thông báo lỗi
      } finally {
        setLoading(true);
      }
    };

    checkAuth();
  }, [token]);

  return { isAuthenticated, userRole, loading, error };
};
