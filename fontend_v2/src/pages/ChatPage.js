import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import io from 'socket.io-client';
import '../assets/css/chatPage.css';
import ChatContent from '../components/ChatContent/ChatContent';

const ChatPage = () => {
    const [images, setImages] = useState([]);
    const [videos, setVideos] = useState([]);
    const [isRecording, setIsRecording] = useState(false);
    const [recordingTime, setRecordingTime] = useState(0);
    const [rooms, setRooms] = useState([]);
    const [roomDetail, setRoomDetail] = useState(null);
    const [errorRoom, setErrorRoom] = useState('');
    const userId = Number(localStorage.getItem('userId'));
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const imageInputRef = useRef(null);
    const videoInputRef = useRef(null);
    const audioRecorderRef = useRef(null);
    const audioContextRef = useRef(null);
    const analyserRef = useRef(null);
    const dataArrayRef = useRef(null);
    const animationFrameRef = useRef(null);
    const [themes, setThemes] = useState([]);
    const [selectedTheme, setSelectedTheme] = useState(null);
    const socketRef = useRef(null);
    const token = localStorage.getItem('token');
    const [currentRoomId, setCurrentRoomId] = useState(localStorage.getItem('roomId'));

    // Kết nối WebSocket
    useEffect(() => {
    const token = localStorage.getItem('token');
    console.log('Token from localStorage:', token); // Log token để kiểm tra

    if (!token) {
        setErrorMessage('Không tìm thấy token. Vui lòng đăng nhập lại.');
        console.error('No token found in localStorage');
        return;
    }

    const socket = io('http://localhost:8081', {
        query: { token }, // Gửi token qua query parameter
        transports: ['websocket', 'polling'], // Thử cả WebSocket và polling
        withCredentials: true, // Gửi credentials để hỗ trợ header
    });
    socketRef.current = socket;

    socket.on('connect', () => {
        console.log('Connected to WebSocket:', socket.id);
        if (currentRoomId) {
            socket.emit('joinRoom', currentRoomId.toString());
            console.log(`Joined room ${currentRoomId}`);
        }
    });

    socket.on('newMessage', (newMessage) => {
        console.log('Received new message:', newMessage);
        setMessages((prev) => {
            if (!prev.some((msg) => msg.messageId === newMessage.messageId)) {
                return [...prev, newMessage];
            }
            return prev;
        });
    });

    socket.on('connect_error', (error) => {
        console.error('WebSocket connection error:', error);
        setErrorMessage(`Lỗi kết nối WebSocket: ${error.message}`);
    });

    socket.on('error', (error) => {
        console.error('WebSocket error:', error);
        setErrorMessage(`Lỗi WebSocket: ${error}`);
    });


    return () => {
        socket.disconnect();
    };
    }, [currentRoomId, token]);

    // Lấy danh sách themes
    const fetchThemes = async () => {
        try {
            const response = await axios.get('http://localhost:8080/theme/getAll', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            setThemes(response.data);
        } catch (error) {
            console.error('Error fetching themes:', error);
            setErrorMessage(error.response?.data || 'Lỗi khi tải danh sách theme');
        }
    };

    // Lấy theme hiện tại của phòng
    const fetchRoomTheme = async (roomId = currentRoomId) => {
        if (!roomId) return;
        try {
            const response = await axios.get(`http://localhost:8080/theme/room-detail/${roomId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            setRoomDetail(response.data);
            setSelectedTheme(response.data.thememeId);
        } catch (error) {
            console.error('Error fetching room theme:', error);
        }
    };

    // Thay đổi theme cho phòng
    const changeRoomTheme = async (themeId) => {
        try {
            await axios.put(`http://localhost:8080/theme/room/${currentRoomId}/theme/${themeId}`, {}, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            fetchRoomTheme();
            window.location.reload();
        } catch (error) {
            console.error('Error changing room theme:', error);
            setErrorMessage(error.response?.data || 'Lỗi khi thay đổi theme');
        }
    };

    // Lấy danh sách phòng chat
    const fetchRooms = async () => {
        try {
            const response = await axios.get('http://localhost:8080/messages/message/room', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            setRooms(response.data);
        } catch (error) {
            setErrorRoom(
                error.response?.data?.message || error.response?.data || 'Đã xảy ra lỗi không mong muốn'
            );
            console.error('Error fetching rooms:', error);
        }
    };

    // Lấy tin nhắn của phòng
    const fetchMessages = async (roomId = currentRoomId) => {
        if (!roomId) return;
        try {
            const response = await axios.get(`http://localhost:8080/messages/between/${roomId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            setMessages(response.data);
        } catch (error) {
            console.error('Error fetching messages:', error);
        }
    };

    // Gửi tin nhắn
    const sendMessage = async () => {
        if (!currentRoomId) {
            setErrorMessage('Vui lòng chọn một phòng chat');
            return;
        }
        if (message.trim() || images.length > 0 || videos.length > 0) {
            const formData = new FormData();
            formData.append('content', message);
            formData.append('receiverId', localStorage.getItem('receiverId'));
            images.forEach((image) => formData.append('images', image));
            videos.forEach((video) => formData.append('videos', video));
            try {
                const response = await axios.post('http://localhost:8080/messages/send', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });
                const newMessage = response.data;
                setMessage('');
                setImages([]);
                setVideos([]);
                // Gửi sự kiện newMessage qua WebSocket
                socketRef.current.emit('sendMessage', {
                    roomId: currentRoomId,
                    message: newMessage,
                });
            } catch (error) {
                setErrorMessage(
                    error.response?.data?.message || error.response?.data || 'Lỗi khi gửi tin nhắn'
                );
                console.error('Error sending message:', error);
            }
        }
    };

    // Gửi âm thanh
    const sendAudio = async (audio) => {
        if (!currentRoomId) {
            setErrorMessage('Vui lòng chọn một phòng chat');
            return;
        }
        const formData = new FormData();
        formData.append('receiverId', localStorage.getItem('receiverId'));
        formData.append('audio', audio);
        try {
            const response = await axios.post('http://localhost:8080/messages/send-audio', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            const newMessage = response.data;
            // Gửi sự kiện newMessage qua WebSocket
            socketRef.current.emit('sendMessage', {
                roomId: currentRoomId,
                message: newMessage,
            });
        } catch (error) {
            setErrorMessage(
                typeof error.response?.data === 'string'
                    ? error.response.data
                    : error.response?.data?.message || 'Lỗi khi gửi âm thanh'
            );
            console.error('Error sending audio:', error);
        }
    };

    // Xử lý chọn phòng chat
    const handleRoom = async (roomId, receiverId, username, avatar) => {
        localStorage.setItem('roomId', roomId);
        localStorage.setItem('receiverId', receiverId);
        localStorage.setItem('receiverAvatar', avatar);
        localStorage.setItem('messageUser', username);

        setCurrentRoomId(roomId);
        if (socketRef.current) {
            socketRef.current.emit('joinRoom', roomId.toString());
            console.log(`Joined room ${roomId}`);
        }
        await fetchMessages(roomId);
        await fetchRoomTheme(roomId);
    };

    // Các hàm khác giữ nguyên
    const startRecording = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            audioRecorderRef.current = new MediaRecorder(stream);
            audioRecorderRef.current.chunks = [];
            audioRecorderRef.current.ondataavailable = (e) => {
                if (e.data.size > 0) {
                    audioRecorderRef.current.chunks.push(e.data);
                }
            };
            audioContextRef.current = new (window.AudioContext || window.webkitAudioContext)();
            const source = audioContextRef.current.createMediaStreamSource(stream);
            analyserRef.current = audioContextRef.current.createAnalyser();
            analyserRef.current.fftSize = 256;
            dataArrayRef.current = new Uint8Array(analyserRef.current.frequencyBinCount);
            source.connect(analyserRef.current);
            const updateWave = () => {
                if (analyserRef.current) {
                    analyserRef.current.getByteFrequencyData(dataArrayRef.current);
                    animationFrameRef.current = requestAnimationFrame(updateWave);
                }
            };
            updateWave();
            audioRecorderRef.current.start();
            setIsRecording(true);
            setRecordingTime(0);
        } catch (error) {
            console.error('Error starting recording:', error);
            setErrorMessage('Không thể truy cập micro');
        }
    };

    const stopRecording = () => {
        if (audioRecorderRef.current && isRecording) {
            audioRecorderRef.current.stop();
            audioRecorderRef.current.onstop = () => {
                const blob = new Blob(audioRecorderRef.current.chunks, { type: 'audio/webm' });
                const file = new File([blob], `recording-${Date.now()}.webm`, { type: 'audio/webm' });
                sendAudio(file);
                cleanupRecording();
            };
        }
    };

    const cancelRecording = () => {
        if (audioRecorderRef.current && isRecording) {
            audioRecorderRef.current.stop();
            audioRecorderRef.current.onstop = () => {
                cleanupRecording();
            };
        }
    };

    const cleanupRecording = () => {
        if (audioRecorderRef.current) {
            audioRecorderRef.current.stream.getTracks().forEach((track) => track.stop());
        }
        if (audioContextRef.current) {
            audioContextRef.current.close();
        }
        if (animationFrameRef.current) {
            cancelAnimationFrame(animationFrameRef.current);
        }
        setIsRecording(false);
        setRecordingTime(0);
    };

    const handleImageChange = (e) => {
        const newImages = Array.from(e.target.files);
        setImages((prevImages) => [...prevImages, ...newImages]);
    };

    const handleVideoChange = (e) => {
        const newVideos = Array.from(e.target.files);
        setVideos((prevVideos) => [...prevVideos, ...newVideos]);
    };

    const handleRemoveImage = (index) => {
        setImages(images.filter((_, i) => i !== index));
    };

    const handleRemoveVideo = (index) => {
        setVideos(videos.filter((_, i) => i !== index));
    };

    const blockFriend = async (id) => {
        try {
            await axios.put(`http://localhost:8080/friend/block/${id}`, {}, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            alert('Chặn người dùng thành công');
        } catch (error) {
            alert('Lỗi khi chặn người dùng');
            console.error(error);
        }
    };

    const handleImageClick = () => {
        imageInputRef.current.click();
    };

    const handleVideoClick = () => {
        videoInputRef.current.click();
    };

    const handleAction = (id) => {
        window.location.href = `/friend/${id}`;
    };

    const getWaveData = () => {
        if (dataArrayRef.current) {
            const bars = [];
            for (let i = 0; i < 10; i++) {
                const index = Math.floor((i / 10) * dataArrayRef.current.length);
                const value = dataArrayRef.current[index] || 0;
                const height = (value / 255) * 50;
                bars.push(height);
            }
            return bars;
        }
        return Array(10).fill(10);
    };

    useEffect(() => {
        fetchRooms();
        fetchThemes();
        if (currentRoomId) {
            fetchMessages(currentRoomId);
            fetchRoomTheme(currentRoomId);
        }
    }, [currentRoomId]);

    useEffect(() => {
        let timer;
        if (isRecording) {
            timer = setInterval(() => {
                setRecordingTime((prev) => prev + 1);
            }, 1000);
        }
        return () => clearInterval(timer);
    }, [isRecording]);

    const handleThemeChange = (e) => {
        const themeId = e.target.value;
        if (themeId) {
            changeRoomTheme(themeId);
        }
    };

    return (
        <div>
            <div className="chat-container">
                <div className="chat-body">
                    <aside className="chat-list">
                        {/* <div className="chat-search">
                            <input type="text" className="chat-search-input" placeholder="Tìm kiếm đoạn chat" />
                            <button className="chat-search-submit">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    height="24px"
                                    viewBox="0 -960 960 960"
                                    width="24px"
                                    fill="#5f6368"
                                >
                                    <path d="M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z" />
                                </svg>
                            </button>
                        </div> */}
                        <ul className="list-room">
                            {rooms.map((room) => (
                                <li
                                    className="room-item"
                                    key={room.id}
                                    onClick={() =>
                                        handleRoom(
                                            room.id,
                                            room.member1.userId !== userId ? room.member1.userId : room.member2.userId,
                                            room.member1.userId !== userId
                                                ? room.member1.fullName
                                                : room.member2.fullName,
                                            room.member1.userId !== userId
                                                ? room.member1.profilePicture
                                                : room.member2.profilePicture
                                        )
                                    }
                                >
                                    <img
                                        src={
                                            room.member1.userId !== userId
                                                ? room.member1.profilePicture || '/img/avatar.png'
                                                : room.member2.profilePicture || '/img/avatar.png'
                                        }
                                        alt="Người dùng"
                                        className="room-avatar"
                                    />
                                    <p className="room-chat-name">
                                        {room.member1.userId !== userId ? room.member1.fullName : room.member2.fullName}
                                    </p>
                                </li>
                            ))}
                        </ul>
                    </aside>
                    <section
                        className="chat-window"
                        style={{
                            backgroundImage: selectedTheme?.image ? `url(${selectedTheme.image})` : 'none',
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                        }}
                    >
                        <header className="chat-header">
                            <img
                                src={localStorage.getItem('receiverAvatar') === 'null' ? '/img/avatar.png' : localStorage.getItem('receiverAvatar')}
                                alt="Người dùng"
                                className="chat-box-avatar"
                            />
                            <h3>{localStorage.getItem('messageUser')}</h3>
                        </header>
                        <div className="chat-messages">
                            {messages.map((message, index) => (
                                <ChatContent key={index} item={message} />
                            ))}
                            <div id="last-chat"></div>
                        </div>
                        <div className="error-message-container">
                            {errorMessage && <p className="error-message">{errorMessage}</p>}
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
                                        <div className="remove-img">
                                            <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                onClick={() => handleRemoveImage(i)}
                                                height="24px"
                                                viewBox="0 -960 960 960"
                                                width="24px"
                                                fill="#000000"
                                            >
                                                <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
                                            </svg>
                                        </div>
                                    </div>
                                ))}
                                {videos.map((video, i) => (
                                    <div key={i} className="media-item-send">
                                        <video src={URL.createObjectURL(video)} controls className="preview-video" />
                                        <div className="remove-video">
                                            <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                onClick={() => handleRemoveVideo(i)}
                                                height="24px"
                                                viewBox="0 -960 960 960"
                                                width="24px"
                                                fill="#000000"
                                            >
                                                <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
                                            </svg>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <footer className="chat-footer">
                            <button className="icon" onClick={handleVideoClick}>
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    height="24px"
                                    viewBox="0 -960 960 960"
                                    width="24px"
                                    fill="#5f6368"
                                >
                                    <path d="m160-800 80 160h120l-80-160h80l80 160h120l-80-160h80l80 160h120l-80-160h120q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800Zm0 240v320h640v-320H160Zm0 0v320-320Z" />
                                </svg>
                            </button>
                            <input
                                type="file"
                                ref={videoInputRef}
                                style={{ display: 'none' }}
                                onChange={handleVideoChange}
                                accept="video/*"
                                multiple
                            />
                            <button className="icon" onClick={handleImageClick}>
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    height="24px"
                                    viewBox="0 -960 960 960"
                                    width="24px"
                                    fill="#5f6368"
                                >
                                    <path d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" />
                                </svg>
                            </button>
                            <input
                                type="file"
                                ref={imageInputRef}
                                style={{ display: 'none' }}
                                onChange={handleImageChange}
                                accept="image/*"
                                multiple
                            />
                            <button className="icon" onClick={startRecording}>
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    height="24px"
                                    viewBox="0 -960 960 960"
                                    width="24px"
                                    fill={isRecording ? '#ff0000' : '#5f6368'}
                                >
                                    <path d="M480-360q-50 0-85-35t-35-85v-240q0-50 35-85t85-35q50 0 85 35t35 85v240q0 50-35 85t-85 35Zm-40-440v240q0 17 11.5 28.5T480-520q17 0 28.5-11.5T520-560v-240q0-17-11.5-28.5T480-840q-17 0-28.5 11.5T440-800ZM320-160v-120q-59-32-99.5-92.5T180-520h80q0 50 35 85t85 35q50 0 85-35t35-85h80q0 77-40.5 137.5T440-280v120h-120Z" />
                                </svg>
                            </button>
                            <input
                                type="text"
                                className="chat-input"
                                placeholder="Aa"
                                value={message}
                                onChange={(e) => setMessage(e.target.value)}
                            />
                            <button className="icon" onClick={sendMessage}>
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    height="24px"
                                    viewBox="0 -960 960 960"
                                    width="24px"
                                    fill="#5f6368"
                                >
                                    <path d="M120-160v-640l760 320-760 320Zm80-120 474-200-474-200v140l240 60-240 60v140Zm0 0v-400 400Z" />
                                </svg>
                            </button>
                        </footer>
                        {isRecording && (
                            <div className="recording-modal">
                                <div className="recording-content">
                                    <div className="wave-animation">
                                        {getWaveData().map((height, i) => (
                                            <span
                                                key={i}
                                                style={{ height: `${height}px` }}
                                                className="wave-bar"
                                            ></span>
                                        ))}
                                    </div>
                                    <p className="recording-text">Đang ghi âm...</p>
                                    <p className="recording-time">
                                        {Math.floor(recordingTime / 60)}:{(recordingTime % 60).toString().padStart(2, '0')}
                                    </p>
                                    <div className="recording-buttons">
                                        <button className="cancel-recording-btn" onClick={cancelRecording}>
                                            Hủy
                                        </button>
                                        <button className="stop-recording-btn" onClick={stopRecording}>
                                            Dừng
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </section>
                    <aside className="user-options">
                        <img
                            src={localStorage.getItem('receiverAvatar') === 'null' ? '/img/avatar.png' : localStorage.getItem('receiverAvatar')}
                            alt="Avatar"
                            className="chat-avatar"
                        />
                        <div className="acton-options">
                            <div className="acton-options-one" onClick={() => handleAction(localStorage.getItem('receiverId'))}>
                                <div className="action-icon">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        height="24px"
                                        viewBox="0 -960 960 960"
                                        width="24px"
                                        fill="#5f6368"
                                    >
                                        <path d="M234-276q51-39 114-61.5T480-360q69 0 132 22.5T726-276q35-41 54.5-93T800-480q0-133-93.5-226.5T480-800q-133 0-226.5 93.5T160-480q0 59 19.5 111t54.5 93Zm246-164q-59 0-99.5-40.5T340-580q0-59 40.5-99.5T480-720q59 0 99.5 40.5T620-580q0 59-40.5h-120Z" />
                                    </svg>
                                </div>
                                <p className="chat-action-options-name">Trang cá nhân</p>
                            </div>
                            <div className="acton-options-one" onClick={() => blockFriend(localStorage.getItem('receiverId'))}>
                                <div className="action-icon">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        height="24px"
                                        viewBox="0 -960 960 960"
                                        width="24px"
                                        fill="#5f6368"
                                    >
                                        <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q54 0 104-17.5t92-50.5L228-676q-33 42-50.5 92T160-480q0 134 93 227t227 93Zm252-124q33-42 50.5-92T800-480q0-134-93-227t-227-93q-54 0-104 17.5T284-732l448 448Z" />
                                    </svg>
                                </div>
                                <p className="chat-action-options-name">Chặn người dùng</p>
                            </div>
                        </div>
                        <div className="settings-dropdown">
                            <select
                                name="theme"
                                id="theme-select"
                                className="template-select"
                                onChange={handleThemeChange}
                                value={selectedTheme?.id || ''}
                            >
                                <option value="">Chọn hình nền</option>
                                {themes.map((theme) => (
                                    <option
                                        key={theme.id}
                                        value={theme.id}
                                        selected={roomDetail?.thememeId?.id === theme.id}
                                    >
                                        {theme.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </aside>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;