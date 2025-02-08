import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react';
import { useLocation, useParams } from "react-router-dom";
import { UserProfileFuntions } from '../../auth/UserProfile';
const CreatePostForm = () => {
    const { id } = useParams(); // Lấy giá trị id từ URL
    const {userData} = UserProfileFuntions();
    const location = useLocation();
    const [content, setContent] = useState('');
    const [tagId, setTagId] = useState(0);
    const [images, setImages] = useState([]);
    const [videos, setVideos] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showStatusForm, setShowStatusForm] = useState(false);
    const [showStatusList, setShowStatusList] = useState(false); // State quản lý hiển thị danh sách trạng thái
    const [tagsList, setTagList] = useState([]);
    const [loading, setLoading] = useState(false);
    const imageInputRef = useRef(null);
    const videoInputRef = useRef(null);
    const uploadVideoInChunks = async (videoFile) => {
        const chunkSize = 1024 * 1024 * 5; // 5MB mỗi phần
        let start = 0;
        let end = chunkSize;
        let partNumber = 0;

        while (start < videoFile.size) {
            const chunk = videoFile.slice(start, end);
            const formData = new FormData();
            formData.append('videos', chunk, `${videoFile.name}.part${partNumber}`);

            await axios.post('http://localhost:8080/post/create', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            start = end;
            end = start + chunkSize;
            partNumber++;
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); // Bật trạng thái loading
       
          
        const formData = new FormData();
        formData.append('content', content);
        if (tagId !== 0) formData.append('tagId', tagId);
        if (location.pathname.startsWith("/group/")) formData.append('groupId', id);
        // Append multiple images
        Array.from(images).forEach((image) => {
            formData.append('images', image); // Ensure field name matches what backend expects
        });

        // Append multiple videos
        Array.from(videos).forEach((video) => {
            formData.append('videos', video); // Ensure field name matches what backend expects
        });

        try {
            const response = await axios.post('http://localhost:8080/post/create', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            if (response.status === 200) {
                setSuccess('Post created successfully!');
                setContent('');
                setTagId('');
                setImages([]);
                setVideos([]);
                setShowStatusForm(false);
                setShowStatusList(false);
                setLoading(false); // Tắt trạng thái loading

                    window.location.reload();

            }
        } catch (error) {
            setLoading(false); // Tắt trạng thái loading
            if (error.response && error.response.data) {
                alert(error.response.data.error || 'Có lỗi xảy ra khi tạo bài viết');
            } else {
                alert('Có lỗi xảy ra khi tạo bài viết');
            }
        }
    };
    useEffect(() => {
        fetchTags();
    }, []);
    const fetchTags = async () => {
        try {
            const response = await axios.get('http://localhost:8080/home/tag/get-all', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            setTagList(response.data);
        } catch (error) {
            console.error('tag error:', error);
        }

    }
    const handleImageClick = () => {
        imageInputRef.current.click(); // Kích hoạt input ảnh khi nhấn vào icon
    };

    const handleVideoClick = () => {
        videoInputRef.current.click(); // Kích hoạt input video khi nhấn vào icon
    };

    const handleStatusClick = (id) => {
        setTagId(id);
        console.log('tagId', id);
    };

    const handleClear = () => {
        setContent('');
        setTagId('');
        setImages([]);
        setVideos([]);
        setShowStatusForm(false);
        setShowStatusList(false);
    };

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
            <form onSubmit={handleSubmit}>


                <div class="post-by">
                    <img src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'} alt="Avatar" class="avatar-img" width="40px" />
                    <input type="text" class="post-input" placeholder="Bạn đang nghĩ gì?"
                        value={content}
                        onChange={(e) => setContent(e.target.value)} />
                </div>
                <div class="actions">
                    <span class="action-item" onClick={handleVideoClick}>
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                            width="24px" fill="#5f6368">
                            <path
                                d="m160-800 80 160h120l-80-160h80l80 160h120l-80-160h80l80 160h120l-80-160h120q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800Zm0 240v320h640v-320H160Zm0 0v320-320Z" />
                        </svg>
                        <input
                            type="file"
                            ref={videoInputRef}
                            style={{ display: 'none' }}
                            onChange={(e) => setVideos(e.target.files)}
                            accept="video/*"
                            multiple
                        />
                        <p class="action-item-name">Video</p>
                    </span>
                    <span class="action-item" onClick={handleImageClick}>
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                            width="24px" fill="#5f6368">
                            <path
                                d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm40-80h480L570-480 450-320l-90-120-120 160Zm-40 80v-560 560Z" />
                        </svg>
                        <input
                            type="file"
                            ref={imageInputRef}
                            style={{ display: 'none' }}
                            onChange={(e) => setImages(e.target.files)}
                            accept="image/*"
                            multiple
                        />
                        <p class="action-item-name">Ảnh</p>
                    </span>
                    <div class='tag-list'>
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                            width="24px" fill="#5f6368">
                            <path
                                d="M480-260q68 0 123.5-38.5T684-400H276q25 63 80.5 101.5T480-260ZM312-520l44-42 42 42 42-42-84-86-86 86 42 42Zm250 0 42-42 44 42 42-42-86-86-84 86 42 42ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-400Zm0 320q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Z" />
                        </svg>
                        <select class="action-item-select" value={tagId} // Gắn giá trị tagId hiện tại
                            onChange={(e) => handleStatusClick(Number(e.target.value))} >
                            <option>
                                <span class="action-item"><p class="action-item-name">Cảm xúc</p>
                                </span></option>
                            {tagsList.map((item) => (
                                <option key={item.tagId} value={item.tagId}>{item.name}</option>
                            ))}
                        </select>
                    </div>



                </div>

                {/* Hiển thị ảnh và video đã chọn */}
                <div className="media-previews">
                    {Array.from(images).map((image, index) => (
                        <img key={index} src={URL.createObjectURL(image)} className='media-previews-img' alt={`Selected ${index}`} />
                    ))}
                    {Array.from(videos).map((video, index) => (
                        <video key={index} controls className='media-previews-img' >
                            <source src={URL.createObjectURL(video)} type={video.type} />
                            Your browser does not support the video tag.
                        </video>
                    ))}
                </div>

                <div className='button-create-post-group'>

                    <button type="submit" className="mt-3 create-post-button">
                        Tạo bài đăng
                    </button>

                    <button type="button"  className="mt-3 float-right clear-post-button" onClick={() => handleClear()}>Hủy</button>
                </div>

            </form>

        </div>

    );
};

export default CreatePostForm;
