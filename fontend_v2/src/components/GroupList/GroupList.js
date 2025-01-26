import axios from 'axios';
import React, { useEffect, useState } from 'react';

const GroupList = () => {
    const [groups, setGroups] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const token = localStorage.getItem('token');
    const [loading, setLoading] = useState(false);
    const fetchGroups = async () => {
        setLoading(true);
        try {
       
            const response = await axios.get('http://localhost:8080/home/group/user', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setGroups(response.data);
            setLoading(false);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
            setLoading(false)
        }
    };

    useEffect(() => {
        fetchGroups();
    }, []);

    return (
        <div >
             {loading && (
                <div className="loading-overlay">
                    <div class="loader">
                        <div class="load-item bar1"></div>
                        <div class="load-item bar2"></div>
                        <div class="load-item bar3"></div>
                        <div class="load-item bar4"></div>
                        <div class="load-item bar5"></div>
                        <div class="load-item bar6"></div>
                        <div class="load-item bar7"></div>
                        <div class="load-item bar8"></div>
                        <div class="load-item bar9"></div>
                        <div class="load-item bar10"></div>
                        <div class="load-item bar11"></div>
                        <div class="load-item bar12"></div>
                    </div>
                </div>
            )}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {groups.length > 0 ? (
                <ul>
                    {groups.map((group) => (
                        <li key={group.id} className="sidebar-li">
                            <img src={group.groupImage || "img/GroupImg2.png"} alt="" className="group-img" />
                            <p className="group-main-name">{group.name}</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="no-groups-message">Bạn chưa theo dõi nhóm nào</p>
            )}
        </div>
    );
};

export default GroupList;
