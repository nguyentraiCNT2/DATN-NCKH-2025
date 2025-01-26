import React, { useEffect, useState } from 'react';
import axios from 'axios';

const FollowerList = () => {
    const [followers, setFollowers] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const token = localStorage.getItem('token');

    const fetchFollowers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/home/friend/list', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setFollowers(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu.');
        }
    };

    useEffect(() => {
        fetchFollowers();
    }, []);

    const handleFriendDetail = (id) => {
        window.location.href = `/friend/${id}`;
    };

    return (
        <div>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {followers.length > 0 ? (
                <ul className="right-bar-option">
                    {followers.map((follower) => (
                        <li
                            key={follower.id}
                            className="right-bar-li"
                            onClick={() => handleFriendDetail(follower.friend.userId)}
                        >
                            <img
                                src={follower.friend.profilePicture || 'img/avatar.png'}
                                className="right-bar-img"
                                alt={follower.friend.fullName}
                            />
                            <p className="right-bar-name">{follower.friend.fullName}</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="no-followers-message">Bạn chưa theo dõi ai</p>
            )}
        </div>
    );
};

export default FollowerList;
