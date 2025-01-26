import React, { useState, useEffect } from "react";
import axios from "axios";
const BlockList = () => {
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [search, setSearch] = useState('');
    const token = localStorage.getItem('token');
    const [userList, setUserList] = useState([]);
    const fetchFriends = async () => {
        try {
            const url = `http://localhost:8080/friend/block`;

            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setUserList(response.data);
        } catch (err) {
            setError('Lỗi khi lấy danh sách bạn bè');
        }
    };

    const unblockFriend = async (id) => {
        try {

            await axios.put(`http://localhost:8080/friend/unblock/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert('Bỏ chặn bạn thành công');
            window.location.reload();

        } catch (error) {
            alert('Lỗi khi bỏ chặn bạn');
            console.error(error);
        }
    };

    useEffect(() => {
        fetchFriends();
    }, [search]);
    return (
        <>
            {userList.length > 0 ? (
                userList.map(friend => (
                    <div className="setting-content-group">
                        <div className="setting-content-group-block-info">
                            <div className="group-block-info-item">
                                <img src={friend?.friend?.profilePicture ? `${friend.friend?.profilePicture}` : '/img/avatar.png'} alt="avarta" className="block-avatar" />
                                <label className="label-content">{friend?.friend?.fullName}</label>
                            </div>

                            <button className="unblock-user-button" onClick={() => unblockFriend(friend.friend.userId)}>bỏ chặn</button>
                        </div>
                    </div>
                ))
            ) : (
                <p>Bạn không chặn bất kỳ ai.</p>
            )}
        </>
    );
};

export default BlockList;
