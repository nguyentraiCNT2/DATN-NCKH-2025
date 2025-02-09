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
            <div class="home-container">
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

        </div>
    );
}

export default HomePage;