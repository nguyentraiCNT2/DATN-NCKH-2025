import React, { useEffect, useState } from 'react';
import '../assets/css/header.css';
import '../assets/css/home.css';
import CreatePostForm from "../components/CreatePost/CreatePostForm";
import PostCard from "../components/PostCard/PostCard";
import GroupList from "../components/GroupList/GroupList";
import FollowerList from "../components/FollowerList/FollowerList";
import axios from 'axios';
import { UserProfileFuntions } from '../auth/UserProfile';
const HomePage = () => {
    const {userData} = UserProfileFuntions();
    const [postList, setPostList] = useState([]);
        const [errorMessage, setErrorMessage] = useState('');
        const token = localStorage.getItem('token');
        
    const fetchPostList = async () => {
        try {
            const response = await axios.get('http://localhost:8080/home/post/get-all', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setPostList(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
        }
    };

    useEffect(() => {
        fetchPostList();
    }, []);
    const handleacion = (link) => {
        if(link === '/list-friend'){
            window.location.href = '/list-friend';
        }else{
            window.location.href = '/profile';
        }

    }
    
    return (
        <div>

            <div class="container">
                <div class="sidebar">
                    <div class="sidebar-profile" onClick={() => handleacion('/profile')}>
                        <img src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'} alt="Avatar" class="avatar-img" width="40px" />
                        <strong class="sidebar-profile-name">{userData?.user?.fullName}</strong>
                    </div>
                    <ul class="sidebar-option">
                        <li class="sidebar-li" onClick={() => handleacion('/list-friend')}>
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                <path
                                    d="M40-160v-112q0-34 17.5-62.5T104-378q62-31 126-46.5T360-440q66 0 130 15.5T616-378q29 15 46.5 43.5T680-272v112H40Zm720 0v-120q0-44-24.5-84.5T666-434q51 6 96 20.5t84 35.5q36 20 55 44.5t19 53.5v120H760ZM360-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47Zm400-160q0 66-47 113t-113 47q-11 0-28-2.5t-28-5.5q27-32 41.5-71t14.5-81q0-42-14.5-81T544-792q14-5 28-6.5t28-1.5q66 0 113 47t47 113ZM120-240h480v-32q0-11-5.5-20T580-306q-54-27-109-40.5T360-360q-56 0-111 13.5T140-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-640q0-33-23.5-56.5T360-720q-33 0-56.5 23.5T280-640q0 33 23.5 56.5T360-560Zm0 320Zm0-400Z" />
                            </svg>
                            Bạn bè
                        </li>
                        <hr />
                        <li><strong class="title-sidebar">Nhóm của bạn</strong></li>
                        <div style={{ position: 'relative' }}>
                            <GroupList />
                        </div>
                    </ul>
                </div>

                <div class="content">
                    <div class="post-box" style={{ position: 'relative' }}>
                        <CreatePostForm />
                    </div>
                    <div class="list-post">
                    {errorMessage && <p className="error-message">{errorMessage}</p>}
            {postList.length > 0 ? (
                <ul>
                    {postList.map((item) => (
                               <PostCard post={item} />
                    ))}
                </ul>
            ) : (
                <p className="no-groups-message">Không có bài viết nào</p>
            )}
                 

                    </div>
                </div>


                <div class="right-bar">
                    <strong class="right-bar-title">Theo giõi</strong>
                  <FollowerList />
                </div>
            </div>

            <div class="mobile-template">
                <div class="mobile-content">
                    <div class="mobile-post-box">
                        <div class="mobile-post-by">
                            <img src="img/avatar.png" alt="Avatar" class="mobile-avatar-img" width="40px" />
                            <input type="text" class="mobile-post-input" placeholder="Bạn đang nghĩ gì?" />
                        </div>
                        <div class="mobile-actions">
                            <span class="mobile-action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                width="24px" fill="#5f6368">
                                <path
                                    d="m160-800 80 160h120l-80-160h80l80 160h120l-80-160h80l80 160h120l-80-160h120q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800Zm0 240v320h640v-320H160Zm0 0v320-320Z" />
                            </svg> <p class="mobile-action-item-name">Video</p> </span>
                            <span class="mobile-action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                width="24px" fill="#5f6368">
                                <path
                                    d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm40-80h480L570-480 450-320l-90-120-120 160Zm-40 80v-560 560Z" />
                            </svg> <p class="mobile-action-item-name">Ảnh</p> </span>
                            <span class="mobile-action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                width="24px" fill="#5f6368">
                                <path
                                    d="M480-260q68 0 123.5-38.5T684-400H276q25 63 80.5 101.5T480-260ZM312-520l44-42 42 42 42-42-84-86-86 86 42 42Zm250 0 42-42 44 42 42-42-86-86-84 86 42 42ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-400Zm0 320q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Z" />
                            </svg><p class="mobile-action-item-name">Cảm xúc</p> </span>
                        </div>
                    </div>
                    <div class="mobile-list-post">
                        <div class="mobile-post">
                            <div class="mobile-info">
                                <img src="img/friend-img2.png" alt="Avatar" class="avatar-img" width="40px" />
                                <div class="info-user">
                                    <strong>Người dùng 2</strong>
                                    <small>13/12/2024</small>
                                </div>

                            </div>
                            <p class="content-text">test</p>
                            <img src="img/post-img1.png" class="post-img" alt="Merry Christmas" />
                            <div class="like-comment">
                                <span>111 lượt thích</span>
                                <span>103 Bình luận</span>
                            </div>
                            <div class="actions">
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
                                </svg> Thích</span>
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M880-80 720-240H160q-33 0-56.5-23.5T80-320v-480q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v720ZM160-320h594l46 45v-525H160v480Zm0 0v-480 480Z" />
                                </svg> Bình luận</span>
                                <span class="action-item"> <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M680-80q-50 0-85-35t-35-85q0-6 3-28L282-392q-16 15-37 23.5t-45 8.5q-50 0-85-35t-35-85q0-50 35-85t85-35q24 0 45 8.5t37 23.5l281-164q-2-7-2.5-13.5T560-760q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35q-24 0-45-8.5T598-672L317-508q2 7 2.5 13.5t.5 14.5q0 8-.5 14.5T317-452l281 164q16-15 37-23.5t45-8.5q50 0 85 35t35 85q0 50-35 85t-85 35Zm0-80q17 0 28.5-11.5T720-200q0-17-11.5-28.5T680-240q-17 0-28.5 11.5T640-200q0 17 11.5 28.5T680-160ZM200-440q17 0 28.5-11.5T240-480q0-17-11.5-28.5T200-520q-17 0-28.5 11.5T160-480q0 17 11.5 28.5T200-440Zm480-280q17 0 28.5-11.5T720-760q0-17-11.5-28.5T680-800q-17 0-28.5 11.5T640-760q0 17 11.5 28.5T680-720Zm0 520ZM200-480Zm480-280Z" />
                                </svg> Chia sẻ</span>
                            </div>
                        </div>

                        <div class="mobile-post">
                            <div class="mobile-info">
                                <img src="img/friend-img2.png" alt="Avatar" class="avatar-img" width="40px" />
                                <div class="info-user">
                                    <strong>Người dùng 2</strong>
                                    <small>13/12/2024</small>
                                </div>

                            </div>
                            <p class="content-text">test</p>
                            <img src="img/post-img1.png" class="post-img" alt="Merry Christmas" />
                            <div class="like-comment">
                                <span>111 lượt thích</span>
                                <span>103 Bình luận</span>
                            </div>
                            <div class="actions">
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
                                </svg> Thích</span>
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M880-80 720-240H160q-33 0-56.5-23.5T80-320v-480q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v720ZM160-320h594l46 45v-525H160v480Zm0 0v-480 480Z" />
                                </svg> Bình luận</span>
                                <span class="action-item"> <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M680-80q-50 0-85-35t-35-85q0-6 3-28L282-392q-16 15-37 23.5t-45 8.5q-50 0-85-35t-35-85q0-50 35-85t85-35q24 0 45 8.5t37 23.5l281-164q-2-7-2.5-13.5T560-760q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35q-24 0-45-8.5T598-672L317-508q2 7 2.5 13.5t.5 14.5q0 8-.5 14.5T317-452l281 164q16-15 37-23.5t45-8.5q50 0 85 35t35 85q0 50-35 85t-85 35Zm0-80q17 0 28.5-11.5T720-200q0-17-11.5-28.5T680-240q-17 0-28.5 11.5T640-200q0 17 11.5 28.5T680-160ZM200-440q17 0 28.5-11.5T240-480q0-17-11.5-28.5T200-520q-17 0-28.5 11.5T160-480q0 17 11.5 28.5T200-440Zm480-280q17 0 28.5-11.5T720-760q0-17-11.5-28.5T680-800q-17 0-28.5 11.5T640-760q0 17 11.5 28.5T680-720Zm0 520ZM200-480Zm480-280Z" />
                                </svg> Chia sẻ</span>
                            </div>
                        </div>

                        <div class="mobile-post">
                            <div class="mobile-info">
                                <img src="img/friend-img2.png" alt="Avatar" class="avatar-img" width="40px" />
                                <div class="info-user">
                                    <strong>Người dùng 2</strong>
                                    <small>13/12/2024</small>
                                </div>

                            </div>
                            <p class="content-text">test</p>
                            <img src="img/post-img1.png" class="post-img" alt="Merry Christmas" />
                            <div class="like-comment">
                                <span>111 lượt thích</span>
                                <span>103 Bình luận</span>
                            </div>
                            <div class="actions">
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
                                </svg> Thích</span>
                                <span class="action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M880-80 720-240H160q-33 0-56.5-23.5T80-320v-480q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v720ZM160-320h594l46 45v-525H160v480Zm0 0v-480 480Z" />
                                </svg> Bình luận</span>
                                <span class="action-item"> <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
                                    width="24px" fill="#5f6368">
                                    <path
                                        d="M680-80q-50 0-85-35t-35-85q0-6 3-28L282-392q-16 15-37 23.5t-45 8.5q-50 0-85-35t-35-85q0-50 35-85t85-35q24 0 45 8.5t37 23.5l281-164q-2-7-2.5-13.5T560-760q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35q-24 0-45-8.5T598-672L317-508q2 7 2.5 13.5t.5 14.5q0 8-.5 14.5T317-452l281 164q16-15 37-23.5t45-8.5q50 0 85 35t35 85q0 50-35 85t-85 35Zm0-80q17 0 28.5-11.5T720-200q0-17-11.5-28.5T680-240q-17 0-28.5 11.5T640-200q0 17 11.5 28.5T680-160ZM200-440q17 0 28.5-11.5T240-480q0-17-11.5-28.5T200-520q-17 0-28.5 11.5T160-480q0 17 11.5 28.5T200-440Zm480-280q17 0 28.5-11.5T720-760q0-17-11.5-28.5T680-800q-17 0-28.5 11.5T640-760q0 17 11.5 28.5T680-720Zm0 520ZM200-480Zm480-280Z" />
                                </svg> Chia sẻ</span>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;