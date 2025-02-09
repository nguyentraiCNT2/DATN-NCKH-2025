import React, { useEffect, useState } from "react";
import '../assets/css/groupList.css';
import PostCard from "../components/PostCard/PostCard";
import axios from "axios";
const ListGroupPage = () => {
    const [groupList, setGroupList] = useState([]);
    const [groupPostList, setGroupPostList] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const token = localStorage.getItem('token');
    const [loading, setLoading] = useState(false);
    const [viewCreateGroupForm, setViewCreateGroupForm] = useState(false);
    const [newGroup, setNewGroup] = useState({
        name: '',
        description: '',
    });
    
    const fetchGroups = async () => {
        setLoading(true);
        try {

            const response = await axios.get('http://localhost:8080/home/group/user', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setGroupList(response.data);
            setLoading(false);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
            setLoading(false)
        }
    };
    const fetchGroupPosts = async () => {
        setLoading(true);
        try {

            const response = await axios.get('http://localhost:8080/post/group/get-all', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setGroupPostList(response.data);
            setLoading(false);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
            setLoading(false)
        }
    };

    useEffect(() => {
        fetchGroups();
        fetchGroupPosts();
    }, []);

    const handleCreateGroup = async (e) => {
        e.preventDefault();
    
        // Tạo đối tượng JSON chứa thông tin nhóm
        const groupData = {
            name: newGroup.name,
            description: newGroup.description,
        };
    
        setLoading(true);
        try {
            // Gửi yêu cầu POST với body là JSON và Authorization header
            await axios.post('http://localhost:8080/groups/create', groupData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json', // Đảm bảo gửi đúng loại content
                },
            });
            alert('Tạo nhóm thành công');
            setLoading(false);
            setViewCreateGroupForm(false); // Ẩn form sau khi tạo nhóm thành công
            fetchGroups();
            window.location.reload();
        } catch (error) {
            console.error('Error creating group:', error);
            setLoading(false);
        }
    };
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewGroup((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };
    const handleacion = (id) =>{
        window.location.href = `/group/${id}`;
    }
    return (
        <div>
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
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <div class="group-container">
                <div class="group-list">
                    <p class="group-title">Nhóm</p>
                    <button  class="create-new-group-button" onClick={() => setViewCreateGroupForm(true)}><i class="fa-solid fa-plus"></i> <p className="create-new-group-button-name"> Tạo nhóm</p></button>
                    {viewCreateGroupForm && (
                        <div class="overlay">
                            <form class="create-group-form" onSubmit={handleCreateGroup}>
                                <div className="close-create-group-form" onClick={() => setViewCreateGroupForm(false)}>
                                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/></svg>
                                </div>
                                <p className="create-group-title">Tạo nhóm</p>
                                <div className="create-group-div">
                                    <label className="create-group-label">Tên nhóm</label>
                                    <br/>
                                    <input
                                     type="text"
                                      placeholder="Nhập tên nhóm của bạn"
                                       className="create-group-input"
                                       name="name"
                                       value={newGroup.name}
                                       onChange={handleInputChange}
                                        />
                                </div>
                                <div className="create-group-div">
                                    <label className="create-group-label">Mô tả</label>
                                    <br/>
                                    <input
                                     type="text"
                                      placeholder="Nhập mô tả nhóm của bạn"
                                       className="create-group-input input-description"
                                       name="description"
                                       value={newGroup.description}
                                       onChange={handleInputChange}
                                       />
                                </div>
                                <input type='submit' class="create-group-form-button" value="Xác nhận" />
                            </form>
                        </div>

                    )}

                    <div class="group-list-item">
                        {groupList.length > 0 ? (
                            <>
                                {
                                    groupList.map((group) => (
                                        <div class="group-item" onClick={() => handleacion(group?.groupId)}>
                                            <img src={group.groupImage || "/img/avatar.png"} alt="" class="group-avatar" />
                                            <p class="group-name">{group.name} </p>
                                        </div>
                                    ))
                                }
                            </>
                        ) : (
                            <p className="no-groups-message">Bạn chưa theo dõi nhóm nào</p>
                        )}
                    </div>

                </div>
                <div class="group-post-wrapper">
                    <div class="group-post">
                        <p class="group-post-title">Bài viết mới</p>
                        <div class="group-post-list">
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
        </div>

    );
}

export default ListGroupPage;