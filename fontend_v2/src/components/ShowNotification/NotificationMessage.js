// ErrorNotification.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './NotificationMessage.css'
const NotificationMessage = ({ message, link, onClose }) => {
  const navigate = useNavigate();

  const handleAgree = () => {
    navigate(link);
     onClose(); // Gọi hàm onClose nếu được cung cấp
  };

  return (
    <div className="error-overlay">
      <div className="error-modal">
        <div className="error-content">
          <p>{message}</p>
          <button onClick={handleAgree} className="agree-button">
            Đồng ý
          </button>
        </div>
      </div>
    </div>
  );
};

export default NotificationMessage;