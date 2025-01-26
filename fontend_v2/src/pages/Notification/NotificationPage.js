import React, { useState, useEffect } from "react";
import axios from "axios";
import '../../assets/css/notification.css'
const NotificationPage = () => {
    const [notifications, setNotifications] = useState([]);

    // Lấy token từ localStorage
    const getToken = () => {
        return localStorage.getItem('token') || '';
    };

    // Lấy danh sách thông báo từ API
    useEffect(() => {
        axios.get("http://localhost:8080/notifications/follow", {
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            }
        })
            .then(response => {
                setNotifications(response.data);
                console.log('notificationdata: ', response.data);
            })
            .catch(error => {
                console.error("Error fetching notifications", error);
            });
    }, []);


    // Đánh dấu thông báo đã đọc
    const markAsRead = (id) => {
        axios.put(`http://localhost:8080/notifications/read/notification/${id}`, {}, {
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            }
        })
            .then(() => {
                setNotifications(notifications.map(notif =>
                    notif.notificationId === id ? { ...notif, readStatus: true } : notif
                ));
                window.location.reload();
            })
            .catch(error => {
                console.error("Error updating notification", error);
            });
    };
    const markAsReadAll = () => {
        axios.post(`http://localhost:8080/notifications/read/all/notification`, {}, {
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            }
        })
            .then(() => {
                setNotifications(notifications.map(notif =>
                    notif ? { ...notif, readStatus: true } : notif
                ));
                window.location.reload();
            })
            .catch(error => {
                console.error("Error updating notification", error);
            });
    };

    const formatTime = (createdAt) => {
        const postDate = new Date(createdAt);
        const now = new Date();
        const diffInMilliseconds = now - postDate;
        const diffInMinutes = Math.floor(diffInMilliseconds / (1000 * 60)); // Tính số phút
        const diffInHours = Math.floor(diffInMilliseconds / (1000 * 60 * 60)); // Tính số giờ
        const diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24)); // Tính số ngày

        if (diffInMinutes < 60) {
            return `${diffInMinutes} phút trước`;
        } else if (diffInHours < 24) {
            return `${diffInHours} giờ trước`;
        } else if (diffInDays < 7) {
            return `${diffInDays} ngày trước`;
        } else {
            return postDate.toLocaleDateString("vi-VN", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
            });
        }
    };
    return (
        <div className="notification-container">
            <div className="notification-form">
                <p className="notification-title">Thông báo</p>
                <a onClick={() => markAsReadAll()} className="notification-read-all-button">Đọc tất cả</a>
                <div className="list-notification">
                    {notifications.length > 0 ? (
                        notifications.map((notification) => (
                            <div className="notification-item" onClick={() => markAsRead(notification.notificationId)}>
                                <div className="notification-item-info" >
                                <img src={notification?.user?.profilePicture ? `${notification.user?.profilePicture}` : '/img/avatar.png'} className="notification-avatar" />
                                <div className="notification-content-group">
                                    <p className="notification-content">{notification.content}</p>
                                    <br />
                                    <small>{formatTime(notification.createdAt)}</small>
                                </div>
                                </div>
                             
                                {!notification.readStatus && (
                                    <span ><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#2196F3"><path d="M480-280q83 0 141.5-58.5T680-480q0-83-58.5-141.5T480-680q-83 0-141.5 58.5T280-480q0 83 58.5 141.5T480-280Zm0 200q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/></svg></span>
                                )}
                            </div>
                        ))
                    ) : (
                        <p className="text-center">Bạn không có thông báo nào</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default NotificationPage;
