import axios from "axios";
import { useEffect, useRef, useState } from "react";
import io from "socket.io-client";
import "../../assets/css/notification.css";

const NotificationPage = () => {
    const [notifications, setNotifications] = useState([]);
    const socketRef = useRef(null);
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    // Lấy token từ localStorage
    const getToken = () => {
        return token || "";
    };
useEffect(() => {
        if (!token || !userId) {
            console.error("Missing token or userId in localStorage", { token, userId });
            return;
        }

        const socket = io("http://localhost:8081", {
            query: { token, userId },
            transports: ["websocket"],
            withCredentials: true,
            reconnection: true,
            reconnectionAttempts: 5,
            reconnectionDelay: 1000,
        });
        socketRef.current = socket;

        socket.on("connect", () => {
            console.log("WebSocket connected: socketId=" + socket.id + ", userId=" + userId);
            socket.emit("subscribeNotifications");
            console.log("Emitted subscribeNotifications for userId=" + userId);
        });

        socket.on("newNotification", (notification) => {
            console.log("Received newNotification:", JSON.stringify(notification, null, 2));
            setNotifications((prev) => {
                if (!notification.notificationId) {
                    console.warn("Invalid notification: missing notificationId");
                    return prev;
                }
                if (!prev.some((notif) => notif.notificationId === notification.notificationId)) {
                    console.log("Adding new notification to state");
                    return [notification, ...prev];
                }
                console.log("Duplicate notification skipped: id=" + notification.notificationId);
                return prev;
            });
        });

        socket.on("connect_error", (error) => {
            console.error("WebSocket connect error:", error.message);
        });

        socket.on("error", (error) => {
            console.error("WebSocket error:", error);
        });

        return () => {
            console.log("Disconnecting WebSocket for userId=" + userId);
            socket.disconnect();
        };
    }, [token, userId]);

    // Lấy danh sách thông báo từ API khi component mount
    useEffect(() => {
        axios
            .get("http://localhost:8080/notifications/follow", {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
                },
            })
            .then((response) => {
                console.log("Fetched notifications:", response.data);
                setNotifications(response.data);
            })
            .catch((error) => {
                console.error("Error fetching notifications:", error);
            });
    }, []);

    // Đánh dấu thông báo đã đọc
    const markAsRead = (id) => {
        axios
            .put(
                `http://localhost:8080/notifications/read/notification/${id}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${getToken()}`,
                    },
                }
            )
            .then(() => {
                setNotifications((prev) =>
                    prev.map((notif) =>
                        notif.notificationId === id ? { ...notif, readStatus: true } : notif
                    )
                );
            })
            .catch((error) => {
                console.error("Error marking notification as read:", error);
            });
    };

    // Đánh dấu tất cả thông báo đã đọc
    const markAsReadAll = () => {
        axios
            .post(
                `http://localhost:8080/notifications/read/all/notification`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${getToken()}`,
                    },
                }
            )
            .then(() => {
                setNotifications((prev) =>
                    prev.map((notif) => ({ ...notif, readStatus: true }))
                );
            })
            .catch((error) => {
                console.error("Error marking all notifications as read:", error);
            });
    };

    // Định dạng thời gian
    const formatTime = (createdAt) => {
        const postDate = new Date(createdAt);
        const now = new Date();
        const diffInMilliseconds = now - postDate;
        const diffInMinutes = Math.floor(diffInMilliseconds / (1000 * 60));
        const diffInHours = Math.floor(diffInMilliseconds / (1000 * 60 * 60));
        const diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24));

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
                <a onClick={markAsReadAll} className="notification-read-all-button">
                    Đọc tất cả
                </a>
                <div className="list-notification">
                    {notifications.length > 0 ? (
                        notifications.map((notification, index) => (
                            <div
                                className={`notification-item ${index === 0 ? "new" : ""}`}
                                key={notification.notificationId}
                                onClick={() => markAsRead(notification.notificationId)}
                            >
                                <div className="notification-item-info">
                                    <img
                                        src={
                                            notification?.user?.profilePicture
                                                ? notification.user.profilePicture
                                                : "/img/avatar.png"
                                        }
                                        className="notification-avatar"
                                        alt="Avatar"
                                    />
                                    <div className="notification-content-group">
                                        <p className="notification-content">{notification.content}</p>
                                        <br />
                                        <small>{formatTime(notification.createdAt)}</small>
                                    </div>
                                </div>
                                {!notification.readStatus && (
                                    <span>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            height="24px"
                                            viewBox="0 -960 960 960"
                                            width="24px"
                                            fill="#2196F3"
                                        >
                                            <path d="M480-280q83 0 141.5-58.5T680-480q0-83-58.5-141.5T480-680q-83 0-141.5 58.5T280-480q0 83 58.5 141.5T480-280Zm0 200q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z" />
                                        </svg>
                                    </span>
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