import axios from 'axios';
import React, { useEffect, useState, useRef, useCallback } from 'react';
import '../assets/css/chatPage.css';
import ChatContent from '../components/ChatContent/ChatContent';
const ChatPage = () => {
    const [images, setImages] = useState([]);
    const [videos, setVideos] = useState([]);
    const imageInputRef = useRef(null);
    const videoInputRef = useRef(null);
    const [rooms, setRooms] = useState([]);
    const [errorRoom, setErrorRoom] = useState('');
    const userId = Number(localStorage.getItem('userId')); // Chuyển đổi userId từ chuỗi thành số
    const receiverId = localStorage.getItem('receiverId'); // Lấy `receiverId` từ localStorage
    console.log("UserId", userId);
    console.log("receiverIds", receiverId);
    const roomId = localStorage.getItem('roomId');
    const [message, setMessage] = useState('');
    const usernameMessage = localStorage.getItem('messageUser');
    const avatarMessage = localStorage.getItem('receiverAvatar');
    const [messages, setMessages] = useState([]);
    const [intervalId, setIntervalId] = useState(null);
    useEffect(() => {
        fetchRooms();
        // Tạo polling (gửi request định kỳ)
        const id = setInterval(() => {
            fetchMessages();
        }, 100); 

        setIntervalId(id);

        // Dọn dẹp interval khi component unmount hoặc roomId thay đổi
        return () => {
            clearInterval(id);
        };
    }, [roomId]);

    const handleImageChange = (e) => {
        setImages(Array.from(e.target.files));
    };

    const handleVideoChange = (e) => {
        setVideos(Array.from(e.target.files));
    };
    const fetchRooms = async () => {
        try {
            const response = await axios.get('http://localhost:8080/messages/message/room', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            setRooms(response.data);
        } catch (error) {
            if (error.response) {
                console.error(error.response.data);
                setErrorRoom(error.response.data || 'An error occurred');
            } else {
                console.error('An error occurred:', error);
                setErrorRoom('An unexpected error occurred');
            }
        }
    };

    const sendMessage = async () => {
        if (message.trim() || images.length > 0 || videos.length > 0) {
            const formData = new FormData();
            formData.append('content', message);
            formData.append('receiverId', receiverId);
            images.forEach((image) => formData.append('images', image));
            videos.forEach((video) => formData.append('videos', video));

            try {
                await axios.post('http://localhost:8080/messages/send', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setMessage('');
                setImages([]);
                setVideos([]);
                fetchMessages();
                // Cuộn xuống cuối danh sách tin nhắn
                // messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
            } catch (error) {
                // setErrorMessage(error.response?.data || 'Error sending message');
                console.error('Error sending message:', error);
            }
        }
    };

    const fetchMessages = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8080/messages/between/${roomId}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            const messagesData = response.data;

            setMessages(messagesData);
            // Khi nhận được tin nhắn mới, cuộn đến cuối danh sách

        } catch (error) {
            console.error('Error fetching messages:', error);
        }
    };
    const blockFriend = async (id) => {
        try {
            await axios.put(`http://localhost:8080/friend/block/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            alert('Chặn Người dùng thành công');
        } catch (error) {
            alert('Lỗi khi chặn người dùng');
            console.error(error);
        }
    };

    const handleRoom = (id, receiverId, username, avatar) => {
        // Lưu thông tin vào localStorage
        localStorage.setItem('roomId', id);
        localStorage.setItem('receiverAvatar', avatar);
        localStorage.setItem('receiverId', receiverId);
        localStorage.setItem('messageUser', username);

        // Reload lại trang để cập nhật trạng thái
        window.location.reload();
    };
    const handleRemoveImage = (index) => {
        setImages(images.filter((_, i) => i !== index));
    };

    const handleRemoveVideo = (index) => {
        setVideos(videos.filter((_, i) => i !== index));
    };

    const handleImageClick = () => {
        imageInputRef.current.click();
    };

    const handleVideoClick = () => {
        videoInputRef.current.click();
    };
    const handleacion = (id) => {
        window.location.href = `/friend/${id}`;
    }
    return (
        <div>

            <div class="chat-container">
                <div class="chat-body">
                    <aside class="chat-list">
                        {/* <div class="chat-search">
                            <input type="text" class="chat-search-input" placeholder="Tìm kiếm đoạn chat" />
                            <button class="chat-search-submit"><svg xmlns="http://www.w3.org/2000/svg" height="24px"
                                viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                <path
                                    d="M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z" />
                            </svg></button>
                        </div> */}

                        <ul class="list-room">
                            {rooms.map((room, index) => (

                                <li class="room-item"
                                    onClick={() => handleRoom(
                                        room.id,
                                        room.member1.userId !== userId ? room.member1.userId : room.member2.userId,
                                        room.member1.userId !== userId ? room.member1.fullName : room.member2.fullName,
                                        room.member1.userId !== userId ? room.member1.profilePicture : room.member2.profilePicture
                                    )}
                                >
                                    <img
                                        src={
                                            room.member1.userId !== userId
                                                ? (room.member1.profilePicture || '/img/avatar.png')
                                                : (room.member2.profilePicture || '/img/avatar.png')
                                        }
                                        alt="Người dùng 1"

                                        className='room-avatar'
                                    />
                                    <p className='room-chat-name'>  {room.member1.userId !== userId ? room.member1.fullName : room.member2.fullName}</p>
                                </li>

                            ))}
                        </ul>
                    </aside>
                    <section class="chat-window">
                        <header class="chat-header">
                            <img src={avatarMessage == 'null' ? '/img/avatar.png' : `${avatarMessage}`} alt="Người dùng 3" className='chat-box-avatar' />
                   
                                <h3>{usernameMessage}</h3>

                        </header>
                        <div class="chat-messages">
                            {messages.map((message, index) => (
                                <ChatContent item={message} />
                            ))}
                            <div id="last-chat"></div>
                        </div>
                        <div className="message-input-container">
                            <div className="media-preview">
                                {images.map((image, i) => (
                                    <div key={i} className="media-item-send">
                                        <img
                                            src={URL.createObjectURL(image)}
                                            alt={`Selected ${i}`}

                                            className="preview-image"
                                        />
                                        <div className='remove-img'>
                                            <svg xmlns="http://www.w3.org/2000/svg" onClick={() => handleRemoveImage(i)} height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" /></svg>
                                        </div>
                                    </div>
                                ))}
                                {videos.map((video, i) => (
                                    <div key={i} className="media-item-send">
                                        <video
                                            src={URL.createObjectURL(video)}
                                            controls
                                            className="preview-video"
                                        />
                                        <div className='remove-video'>
                                            <svg xmlns="http://www.w3.org/2000/svg" onClick={() => handleRemoveVideo(i)} height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" /></svg>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <footer class="chat-footer">

                            <button class="icon" onClick={handleVideoClick} ><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                width="24px" fill="#5f6368">
                                <path
                                    d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" />
                            </svg></button>
                            <input
                                type="file"
                                ref={videoInputRef}
                                style={{ display: 'none' }}
                                onChange={handleVideoChange}
                                accept="video/*"
                                multiple
                            />
                            <button class="icon" onClick={handleImageClick}>
                                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="m160-800 80 160h120l-80-160h80l80 160h120l-80-160h80l80 160h120l-80-160h120q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800Zm0 240v320h640v-320H160Zm0 0v320-320Z" />
                                </svg></button>
                            <input
                                type="file"
                                ref={imageInputRef}
                                style={{ display: 'none' }}
                                onChange={handleImageChange}
                                accept="image/*"
                                multiple
                            />
                            <input type="text" class="chat-input" placeholder="Aa"
                                value={message}
                                onChange={(e) => setMessage(e.target.value)} />
                            <button class="icon" onClick={sendMessage}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                width="24px" fill="#5f6368">
                                <path d="M120-160v-640l760 320-760 320Zm80-120 474-200-474-200v140l240 60-240 60v140Zm0 0v-400 400Z" />
                            </svg></button>
                        </footer>
                    </section>
                    <aside class="user-options">
                        <img src={avatarMessage == 'null' ? '/img/avatar.png' : `${avatarMessage}`} alt="Avatar" class="chat-avatar" />
                        <div class="acton-options">
                            <div class="acton-options-one" onClick={() => handleacion(receiverId)}>
                                <div class="action-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                                        fill="#5f6368">
                                        <path
                                            d="M234-276q51-39 114-61.5T480-360q69 0 132 22.5T726-276q35-41 54.5-93T800-480q0-133-93.5-226.5T480-800q-133 0-226.5 93.5T160-480q0 59 19.5 111t54.5 93Zm246-164q-59 0-99.5-40.5T340-580q0-59 40.5-99.5T480-720q59 0 99.5 40.5T620-580q0 59-40.5 99.5T480-440Zm0 360q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q53 0 100-15.5t86-44.5q-39-29-86-44.5T480-280q-53 0-100 15.5T294-220q39 29 86 44.5T480-160Zm0-360q26 0 43-17t17-43q0-26-17-43t-43-17q-26 0-43 17t-17 43q0 26 17 43t43 17Zm0-60Zm0 360Z" />
                                    </svg>
                                </div>
                                <p className='chat-action-options-name'>Trang cá nhân</p>
                            </div>
                            <div class="acton-options-one" onClick={() => blockFriend(receiverId)}>
                                <div class="action-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                                        fill="#5f6368">
                                        <path
                                            d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q54 0 104-17.5t92-50.5L228-676q-33 42-50.5 92T160-480q0 134 93 227t227 93Zm252-124q33-42 50.5-92T800-480q0-134-93-227t-227-93q-54 0-104 17.5T284-732l448 448Z" />
                                    </svg>
                                </div>
                                <p className='chat-action-options-name'>Chặn người dùng</p>
                            </div>
                        </div>

                        {/* <div class="settings-dropdown">
                            <select name="" id="" class="template-select">
                                <option value="">Thay đổi hình nền</option>
                                <option value="">ĐỎ</option>
                                <option value="">Xanh</option>
                                <option value="">Tình yêu </option>
                            </select>
                        </div> */}
                    </aside>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;
