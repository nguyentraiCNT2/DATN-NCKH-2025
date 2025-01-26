// src/components/PrivateRoute.js
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import CreatePostForm from '../components/CreatePost/CreatePostForm';
import PostCard from '../components/PostCard/PostCard';
const GroupProfilePage = () => {
    const getToken = () => {
        return localStorage.getItem('token') || '';
    };
    const [groupPostList, setGroupPostList] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    // Khai báo state để lưu trữ dữ liệu nhóm
    const [group, setGroup] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [member, setMember] = useState([]);
    const [groupPost, setGroupPost] = useState([]);
    const { id } = useParams();
    useEffect(() => {
        // Hàm gọi API để lấy dữ liệu nhóm
        const fetchGroupData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/groups/detail/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${getToken()}`,
                    },
                });
                setGroup(response.data);
                setLoading(false);
            } catch (err) {
                setError('Không thể tải dữ liệu nhóm');
                setLoading(false);
            }
        };

        fetchGroupData();
        fetchGroupMemberData();
        fetchGroupPosts();
    }, [id, getToken()]);
    const fetchGroupMemberData = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/groups/${id}/members`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });
            setMember(response.data);
            setLoading(false);
        } catch (err) {
            setError('Không thể tải dữ liệu nhóm');
            setLoading(false);
        }
    };
    const fetchGroupPosts = async () => {
        setLoading(true);
        try {

            const response = await axios.get(`http://localhost:8080/post/group/${id}`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });
            setGroupPostList(response.data);
            setLoading(false);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
            setLoading(false)
        }
    };

    return (
        <div>
            <div class="profile-info-contaiter">
                <div class="profile-banner">
                    <img src={group?.groupCoverImage ? `${group?.groupCoverImage}` : '/img/Cover_img.png'} alt="Profile Banner" />
                </div>
                <div class="profile-info">
                    <img src={group?.groupImage ? `${group?.groupImage}` : '/img/avatar.png'} alt="Profile Picture" />
                    <div class="profile-detail">
                        <h2 class="profile-name">{group?.name}</h2>
                        <p class="profile-follower">{member.length} Thành viên</p>
                    </div>
                </div>
                <div class="profile-options">
                    <a href="#" class="follow-friend-action">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M720-400v-120H600v-80h120v-120h80v120h120v80H800v120h-80Zm-360-80q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM40-160v-112q0-34 17.5-62.5T104-378q62-31 126-46.5T360-440q66 0 130 15.5T616-378q29 15 46.5 43.5T680-272v112H40Zm80-80h480v-32q0-11-5.5-20T580-306q-54-27-109-40.5T360-360q-56 0-111 13.5T140-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-640q0-33-23.5-56.5T360-720q-33 0-56.5 23.5T280-640q0 33 23.5 56.5T360-560Zm0-80Zm0 400Z" /></svg>

                        <p>Tham gia</p>
                    </a>
                    <a href="#" class="chat-friend-action">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF"><path d="M440-280h80v-160h160v-80H520v-160h-80v160H280v80h160v160Zm40 200q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z" /></svg>
                        <p>Mời</p>
                    </a>
                </div>
            </div>

            <div class="tabs">
                <button>Bài viết</button>
                <button>Thành viên</button>
                <button>Giới thiệu</button>
            </div>


            <div class="post-area">
                <div class="profile-contact">
                    <h3>Giới thiệu</h3>
                    <p>test giới thiệu</p>
                </div>
                <div class="group-content">
                    <div class="post-box" style={{ position: 'relative'}}>
                        <CreatePostForm />
                    </div>
                    <div class="list-post">
                        {groupPostList.length > 0 ? (
                            <>
                                {
                                    groupPostList.map((item) => (
                                        <PostCard post={item} />
                                    ))
                                }
                            </>
                        ) : (
                            <p className="no-groups-message">Không có bài viết nào</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default GroupProfilePage;
