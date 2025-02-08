import axios from "axios";
import React, { useEffect, useState } from 'react';
export const UserProfileFuntions = () => {
    const [userData , setUserData] = useState(null);
    const token = localStorage.getItem('token');

    const fetchProfile = async () => {
        try {
            const url = 'http://localhost:8080/user/me'; // Hiển thị tất cả
    
            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}` // Thêm token vào header
                }
            });
            setUserData(response.data);
    
        } catch (err) {
            console.error('Error fetching user data:', err.response.data);
        }
    };
        useEffect(() => {
            fetchProfile();
        }, [token]);
    return {userData};
};
