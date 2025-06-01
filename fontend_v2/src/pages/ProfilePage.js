import axios from "axios";
import { useEffect, useState } from 'react';
import '../assets/css/profile.css';
import { UserProfileFuntions } from '../auth/UserProfile';
import CreatePostForm from "../components/CreatePost/CreatePostForm";
import PostCard from "../components/PostCard/PostCard";
const ProfilePage = () => {
    const { userData } = UserProfileFuntions();
    const token = localStorage.getItem('token');
    const [postList, setPostList] = useState([]);
    const [connectionRequests, setConnectionRequests] = useState([]);
    const [userRelationships, setUserRelationships] = useState([]);
    const [RelationShips, setRelationShips] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [actionModel, setActionModel] = useState('list-post');
    const [actionContactModel, setActionContactModel] = useState('info');
    const [actionRelationShipModel, setActionRelationShipModel] = useState('info-relationship');
    const [profilePictureFile, setProfilePictureFile] = useState(null);
    const [coverPictureFile, setCoverPictureFile] = useState(null);
    const [selectProfilepicture, setSelectProfilepicture] = useState('');
    const [relationshipType, setRelationshipType] = useState('');
    const [requestMessage, setRequestMessage] = useState('');
    const [selectedUserId, setSelectedUserId] = useState('');

    const handleSendConnectionRequest = async (e) => {
        e.preventDefault();
        if (!selectedUserId || !relationshipType) {
            setRequestMessage('Vui lòng chọn người dùng và loại quan hệ');
            return;
        }

        try {
            const selectedUser = userRelationships.find(user => user.userId === parseInt(selectedUserId));
            if (!selectedUser) {
                setRequestMessage('Người dùng không hợp lệ');
                return;
            }

            const response = await axios.post('http://localhost:8080/relations/send-request', null, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                params: {
                    name: relationshipType,
                    secondPeopleId: selectedUserId,
                },
            });
            setRequestMessage(response.data); // "Yêu cầu kết nối đã được gửi"
            setSelectedUserId('');
            setRelationshipType('');
        } catch (error) {
            setRequestMessage(error.response?.data || 'Có lỗi khi gửi yêu cầu kết nối');
        }
    };

    const fetchPostList = async () => {
        try {
            const response = await axios.get('http://localhost:8080/post/getbyuser', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setPostList(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
        }
    };
    const fetchUserRelationship = async () => {
        try {
            const response = await axios.get('http://localhost:8080/relations/get-all/user-relationship', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setUserRelationships(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
        }
    };
    const fetchConnectionRequest = async () => {
        try {
            const response = await axios.get('http://localhost:8080/relations/getall-connectionrequest', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setConnectionRequests(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
        }
    };
    const fetchRelationShips = async () => {
        try {
            const response = await axios.get('http://localhost:8080/relations/me', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setRelationShips(response.data);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
        }
    };

    const handleConfirmConnection = async (id, userId) => {
        try {
            const response = await axios.put(`http://localhost:8080/relations/confirm/${id}/user?userId=${userId}`, null, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setRequestMessage(response.data); // "Kết nối đã được xác nhận"
            setConnectionRequests(connectionRequests.filter(req => req.id !== id));
        } catch (error) {
            setRequestMessage(error.response?.data || 'Có lỗi khi xác nhận kết nối');
        }
    };

    const handleRejectConnection = async (id) => {
        try {
            const response = await axios.delete(`http://localhost:8080/relations/reject/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setRequestMessage(response.data); // "Yêu cầu kết nối đã bị từ chối"
            setConnectionRequests(connectionRequests.filter(req => req.id !== id));
        } catch (error) {
            setRequestMessage(error.response?.data || 'Có lỗi khi từ chối kết nối');
        }
    };
    const handleDeleteConnection = async (id) => {
        try {
            const response = await axios.delete(`http://localhost:8080/relations/delete/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setRequestMessage(response.data); // "Yêu cầu kết nối đã bị từ chối"
            setRelationShips();
        } catch (error) {
            setRequestMessage(error.response?.data || 'Có lỗi khi từ chối kết nối');
        }
    };
    // Hàm format ngày sinh
    const formatBirthday = (timestamp) => {
        const date = new Date(Number(timestamp)); // Chuyển timestamp thành đối tượng Date
        const day = String(date.getDate()).padStart(2, '0'); // Lấy ngày, đảm bảo luôn có 2 chữ số
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Lấy tháng (tháng bắt đầu từ 0, nên +1)
        const year = date.getFullYear(); // Lấy năm
        return `${day}/${month}/${year}`; // Định dạng ngày tháng năm
    };
    const formatTimestamp = (timestamp) => {
        const date = new Date(Number(timestamp));
        return date.toLocaleString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    useEffect(() => {
        fetchPostList();
        fetchUserRelationship();
        fetchConnectionRequest();
        fetchRelationShips();
    }, [token]);

    const handleFollowAction = () => {
        window.location.href = '/list-friend';
    }
    const handleContactPostAction = (text) => {
        setActionModel(text);
    }
    const handleRelationShipAction = (text) => {
        setActionRelationShipModel(text);
    }
    const handleContactAction = (text) => {
        setActionContactModel(text);
    }
    const handleCoverPictureChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            // Hiển thị hộp thoại xác nhận
            const confirm = window.confirm('Bạn có muốn sử dụng ảnh này làm ảnh bìa không?');
            if (confirm) {
                // Nếu người dùng đồng ý, thực hiện API
                fetchUpdateCoverPicture(file);
            } else {
                // Nếu người dùng không đồng ý, không thực hiện API
                alert('Bạn đã hủy việc cập nhật ảnh bìa');
            }
        }
    };

    const fetchUpdateCoverPicture = async (file) => {
        if (!file) return;
        const formData = new FormData();
        formData.append('picture', file);

        try {
            await axios.post('http://localhost:8080/user/upload/cover/picture', formData, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('Cập nhật ảnh bìa thành công');
            window.location.reload();
        } catch (err) {
            alert(err.response?.data || 'Có lỗi xảy ra khi cập nhật ảnh bìa');
        }
    };

    const handleProfilePictureChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            // Hiển thị hộp thoại xác nhận
            const confirm = window.confirm('Bạn có muốn sử dụng ảnh này làm ảnh đại diện không?');
            if (confirm) {
                // Nếu người dùng đồng ý, thực hiện API
                fetchUpdateProfilePicture(file);
            } else {
                // Nếu người dùng không đồng ý, không thực hiện API
                alert('Bạn đã hủy việc cập nhật ảnh đại diện');
            }
        }
    };

    const fetchUpdateProfilePicture = async (file) => {
        if (!file) return;
        const formData = new FormData();
        formData.append('picture', file);

        try {
            await axios.post('http://localhost:8080/user/upload/profile/picture', formData, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('Cập nhật ảnh đại diện thành công');
            window.location.reload();
        } catch (err) {
            alert(err.response?.data || 'Có lỗi xảy ra khi cập nhật ảnh đại diện');
        }
    };

    return (
        <div>
            <div class="profile-info-contaiter">
                <div class="profile-banner">
                    <img src={userData?.user?.coverPicture ? `${userData.user?.coverPicture}` : '/img/Cover_img.png'} alt="Profile Banner" />
                    <button className="other-cover-picture" onClick={() => document.getElementById('cover-photo-input').click()}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" /></svg></button>
                    <input
                        id="cover-photo-input"
                        type="file"
                        onChange={handleCoverPictureChange}
                        style={{ display: 'none' }}
                    />
                </div>
                <div class="profile-info">
                    <img src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'} alt="Profile Picture" />
                    <div class="profile-detail">
                        <h2 class="profile-name">{userData?.user?.fullName}   {userData?.nickName ? `( ${userData.nickName} )` : ''}</h2>
                    </div>
                    <button className="other-avatar" onClick={() => document.getElementById('profile-photo-input').click()}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" /></svg></button>
                    <input
                        id="profile-photo-input"
                        type="file"
                        onChange={handleProfilePictureChange}
                        style={{ display: 'none' }}
                    />
                </div>
                <div class="edit-profile">
                    <a href="/setting" class="edit-profile-action">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                            fill="#5f6368">
                            <path
                                d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h357l-80 80H200v560h560v-278l80-80v358q0 33-23.5 56.5T760-120H200Zm280-360ZM360-360v-170l367-367q12-12 27-18t30-6q16 0 30.5 6t26.5 18l56 57q11 12 17 26.5t6 29.5q0 15-5.5 29.5T897-728L530-360H360Zm481-424-56-56 56 56ZM440-440h56l232-232-28-28-29-28-231 231v57Zm260-260-29-28 29 28 28 28-28-28Z" />
                        </svg>
                    </a>
                </div>
            </div>

            <div class="tabs">

                <button onClick={() => handleContactPostAction('list-post')}>Bài viết</button>
                <button onClick={() => handleFollowAction()}>Bạn bè</button>
                <button onClick={() => handleContactPostAction('list-contact')}>Giới thiệu</button>
                <button onClick={() => handleContactPostAction('list-relationship')}>Kết nối</button>
            </div>

            <div class="post-area">

                {actionModel === 'list-relationship' && (
                    <div className='contact-options'>
                        <div className='contact-menu'>
                            <button className='contact-menu-item' onClick={() => handleRelationShipAction('info-relationship')}>Thông tin kết nối</button>
                            <button className='contact-menu-item' onClick={() => handleRelationShipAction('relationship-conecttion-request')}>Yêu cầu kết nối </button>
                            <button className='contact-menu-item' onClick={() => handleRelationShipAction('relationship-conecttion-list')}>Lời mời kết nối</button>
                        </div>
                        <div className='contact-content'>
                            {actionRelationShipModel === 'info-relationship' && (
                                <div className="relationship-container">
                                    <h3 className="relationship-title">Danh sách mối quan hệ</h3>
                                    {RelationShips.length > 0 ? (

                                        <div className="relationship-list">
                                            {RelationShips.map((item) => (
                                                <div key={item.id} className="relationship-card">
                                                    <img
                                                        src={item?.userDTO?.profilePicture ? `${item.userDTO?.profilePicture}` : '/img/avatar.png'}
                                                        alt={item?.userDTO?.fullName || 'Avatar'}
                                                        className="relationship-avatar"
                                                    />
                                                    <h3 className="relationship-name">{item?.userDTO?.fullName}</h3>
                                                    <div className="relationship-type">
                                                        <p>{item?.name}</p>
                                                    </div>
                                                    <div className="request-actions">
                                                        <button
                                                            className="delete-btn"
                                                            onClick={() => handleDeleteConnection(item.id)}
                                                        >
                                                            Bỏ kết nối
                                                        </button>
                                                    </div>

                                                </div>
                                            ))}

                                        </div>
                                    ) : (
                                        <p className="no-requests">Không có mối quan hệ nào</p>
                                    )}
                                </div>
                            )}
                            {actionRelationShipModel === 'relationship-conecttion-request' && (
                                <div className="connection-form bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
                                    <h3 className="text-lg font-semibold mb-4">Gửi yêu cầu kết nối</h3>
                                    {requestMessage && <p className={`mb-4 ${requestMessage.includes('lỗi') ? 'text-red-500' : 'text-green-500'}`}>{requestMessage}</p>}
                                    <form onSubmit={handleSendConnectionRequest}>
                                        <div className="mb-4">
                                            <label htmlFor="userSelect" className="block text-sm font-medium text-gray-700">Chọn người dùng</label>
                                            <select
                                                id="userSelect"
                                                value={selectedUserId}
                                                onChange={(e) => setSelectedUserId(e.target.value)}
                                                className="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                            >
                                                <option value="">-- Chọn người dùng --</option>
                                                {userRelationships.map((user) => (
                                                    <option key={user.userId} value={user.userId}>
                                                        {user.fullName}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                        <div className="mb-4">
                                            <label htmlFor="relationshipType" className="block text-sm font-medium text-gray-700">Loại quan hệ</label>
                                            <select
                                                id="relationshipType"
                                                value={relationshipType}
                                                onChange={(e) => setRelationshipType(e.target.value)}
                                                className="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                            >
                                                <option value="">-- Chọn loại quan hệ --</option>
                                                <option value="Bạn bè">Bạn bè</option>
                                                <option value="Người yêu">Người yêu</option>
                                                <option value="Bạn thân">Bạn thân</option>
                                                <option value="Gia đình">Gia đình</option>
                                                <option value="Bố">Bố</option>
                                                <option value="Mẹ">Mẹ</option>
                                                <option value="Anh Trai">Anh Trai</option>
                                                <option value="Chị gái">Chị gái</option>
                                                <option value="Em trai">Em trai</option>
                                                <option value="Em gái">Em gái</option>
                                                <option value="Đồng nghiệp">Đồng nghiệp</option>
                                            </select>
                                        </div>
                                        <button
                                            type="submit"
                                            className="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600 transition duration-200"
                                        >
                                            Gửi yêu cầu
                                        </button>
                                    </form>
                                </div>
                            )}
                            {actionRelationShipModel === 'relationship-conecttion-list' && (
                                <div className="connection-requests">
                                    <h3>Lời mời kết nối</h3>
                                    {requestMessage && (
                                        <p className={`message ${requestMessage.includes('lỗi') ? 'error' : 'success'}`}>
                                            {requestMessage}
                                        </p>
                                    )}
                                    {connectionRequests.length > 0 ? (
                                        <div className="request-list">
                                            {connectionRequests.map((request) => (
                                                <div key={request.id} className="request-card">
                                                    <div className="request-info">
                                                        <img
                                                            src={request.sender?.profilePicture || '/img/avatar.png'}
                                                            alt="Avatar"
                                                            className="request-avatar"
                                                        />
                                                        <div className="request-details">
                                                            <p className="request-name">{request.userDTO?.fullName || 'Người dùng'}</p>
                                                            <p className="request-type">Loại quan hệ: {request.name}</p>
                                                            <p className="request-time">Gửi lúc: {formatBirthday(request.createAt)}</p>
                                                        </div>
                                                    </div>
                                                    <div className="request-actions">
                                                        <button
                                                            className="confirm-btn"
                                                            onClick={() => handleConfirmConnection(request.id, request?.userDTO?.userId)}
                                                        >
                                                            Xác nhận
                                                        </button>
                                                        <button
                                                            className="reject-btn"
                                                            onClick={() => handleRejectConnection(request.id)}
                                                        >
                                                            Từ chối
                                                        </button>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    ) : (
                                        <p className="no-requests">Không có lời mời kết nối nào</p>
                                    )}
                                </div>
                            )}

                        </div>
                    </div>
                )}
                {actionModel === 'list-post' && (
                    <>
                        <div class="profile-contact">
                            <h3>Giới thiệu</h3>
                            <p className="contact-info">
                           {userData?.description ? userData.description : 'Không có mô tả để hiển thị'}
                            </p>
                            {/* <p className="contact-info">
                                Sinh nhật: {userData?.birthday ? formatBirthday(userData.birthday) : 'Không có ngày sinh để hiển thị'}
                            </p>
                            <p className="contact-info">
                                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m798-322-62-62q44-41 69-97t25-119q0-63-25-118t-69-96l62-64q56 53 89 125t33 153q0 81-33 153t-89 125ZM670-450l-64-64q18-17 29-38.5t11-47.5q0-26-11-47.5T606-686l64-64q32 29 50 67.5t18 82.5q0 44-18 82.5T670-450Zm-310 10q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM40-120v-112q0-33 17-62t47-44q51-26 115-44t141-18q77 0 141 18t115 44q30 15 47 44t17 62v112H40Zm80-80h480v-32q0-11-5.5-20T580-266q-36-18-92.5-36T360-320q-71 0-127.5 18T140-266q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-600q0-33-23.5-56.5T360-680q-33 0-56.5 23.5T280-600q0 33 23.5 56.5T360-520Zm0-80Zm0 400Z" /></svg>
                                {userData?.description ? userData.description : 'Không có mô tả bản thân để hiển thị'}
                            </p> */}


                        </div>
                        <div class="profile-content">
                            <div class="post-box">
                                <CreatePostForm />
                            </div>
                            <div class="list-post">
                                {postList.length > 0 ? (

                                    postList.map((item) => (
                                        <PostCard post={item} />
                                    ))

                                ) : (
                                    <p className="no-groups-message">Không có bài viết nào</p>
                                )}
                            </div>
                        </div>
                    </>
                )}
                {actionModel === 'list-contact' && (
                    <div className='contact-options'>
                        <div className='contact-menu'>
                            <button className='contact-menu-item' onClick={() => handleContactAction('info')}>Thông tin cá nhân</button>
                            <button className='contact-menu-item' onClick={() => handleContactAction('hometown')}>Quê Quán </button>
                            <button className='contact-menu-item' onClick={() => handleContactAction('study')}>Học vấn</button>
                            <button className='contact-menu-item' onClick={() => handleContactAction('relationship')}>Tinh trạng quan hệ </button>
                        </div>
                        <div className='contact-content'>
                            {actionContactModel === 'info' && (
                                <>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M560-440h200v-80H560v80Zm0-120h200v-80H560v80ZM200-320h320v-22q0-45-44-71.5T360-440q-72 0-116 26.5T200-342v22Zm160-160q33 0 56.5-23.5T440-560q0-33-23.5-56.5T360-640q-33 0-56.5 23.5T280-560q0 33 23.5 56.5T360-480ZM160-160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800h640q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160Zm0-80h640v-480H160v480Zm0 0v-480 480Z" /></svg>
                                        {userData?.nickName ? userData.nickName : 'Không có biệt danh để hiển thị'}
                                    </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M160-80q-17 0-28.5-11.5T120-120v-200q0-33 23.5-56.5T200-400v-160q0-33 23.5-56.5T280-640h160v-58q-18-12-29-29t-11-41q0-15 6-29.5t18-26.5l56-56 56 56q12 12 18 26.5t6 29.5q0 24-11 41t-29 29v58h160q33 0 56.5 23.5T760-560v160q33 0 56.5 23.5T840-320v200q0 17-11.5 28.5T800-80H160Zm120-320h400v-160H280v160Zm-80 240h560v-160H200v160Zm80-240h400-400Zm-80 240h560-560Zm560-240H200h560Z" /></svg>
                                        {userData?.birthday ? formatBirthday(userData.birthday) : 'Không có ngày sinh để hiển thị'}</p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M360-390q-21 0-35.5-14.5T310-440q0-21 14.5-35.5T360-490q21 0 35.5 14.5T410-440q0 21-14.5 35.5T360-390Zm240 0q-21 0-35.5-14.5T550-440q0-21 14.5-35.5T600-490q21 0 35.5 14.5T650-440q0 21-14.5 35.5T600-390ZM480-160q134 0 227-93t93-227q0-24-3-46.5T786-570q-21 5-42 7.5t-44 2.5q-91 0-172-39T390-708q-32 78-91.5 135.5T160-486v6q0 134 93 227t227 93Zm0 80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm-54-715q42 70 114 112.5T700-640q14 0 27-1.5t27-3.5q-42-70-114-112.5T480-800q-14 0-27 1.5t-27 3.5ZM177-581q51-29 89-75t57-103q-51 29-89 75t-57 103Zm249-214Zm-103 36Z" /></svg>
                                        {userData?.gender ? userData.gender : 'Không có giới tính để hiển thị'} </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m798-322-62-62q44-41 69-97t25-119q0-63-25-118t-69-96l62-64q56 53 89 125t33 153q0 81-33 153t-89 125ZM670-450l-64-64q18-17 29-38.5t11-47.5q0-26-11-47.5T606-686l64-64q32 29 50 67.5t18 82.5q0 44-18 82.5T670-450Zm-310 10q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM40-120v-112q0-33 17-62t47-44q51-26 115-44t141-18q77 0 141 18t115 44q30 15 47 44t17 62v112H40Zm80-80h480v-32q0-11-5.5-20T580-266q-36-18-92.5-36T360-320q-71 0-127.5 18T140-266q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-600q0-33-23.5-56.5T360-680q-33 0-56.5 23.5T280-600q0 33 23.5 56.5T360-520Zm0-80Zm0 400Z" /></svg>
                                        {userData?.description ? userData.description : 'Không có mô tả bản thân để hiển thị'} </p>
                                </>
                            )}
                            {actionContactModel === 'hometown' && (
                                <>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M480-480q33 0 56.5-23.5T560-560q0-33-23.5-56.5T480-640q-33 0-56.5 23.5T400-560q0 33 23.5 56.5T480-480Zm0 294q122-112 181-203.5T720-552q0-109-69.5-178.5T480-800q-101 0-170.5 69.5T240-552q0 71 59 162.5T480-186Zm0 106Q319-217 239.5-334.5T160-552q0-150 96.5-239T480-880q127 0 223.5 89T800-552q0 100-79.5 217.5T480-80Zm0-480Z" /></svg>
                                        {userData?.address ? userData.address : 'Không có địa chỉ để hiển thị'}
                                    </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M120-120v-560h160v-160h400v320h160v400H520v-160h-80v160H120Zm80-80h80v-80h-80v80Zm0-160h80v-80h-80v80Zm0-160h80v-80h-80v80Zm160 160h80v-80h-80v80Zm0-160h80v-80h-80v80Zm0-160h80v-80h-80v80Zm160 320h80v-80h-80v80Zm0-160h80v-80h-80v80Zm0-160h80v-80h-80v80Zm160 480h80v-80h-80v80Zm0-160h80v-80h-80v80Z" /></svg>
                                        {userData?.city ? userData.city : 'Không có tỉnh/thành phố để hiển thị'}</p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M720-520q-83 0-141.5-58.5T520-720q0-83 58.5-141.5T720-920q83 0 141.5 58.5T920-720q0 83-58.5 141.5T720-520ZM144-144q-11-11-17.5-25t-6.5-31v-560q0-33 23.5-56.5T200-840h268q-14 27-21 57.5t-7 62.5q0 59 22 109.5t60 88.5L144-144Zm336 24v-216q0-42 25.5-75.5T572-454q35-8 72-12t76-4q32 0 61.5 2.5T840-460v260q0 33-23.5 56.5T760-120H480Z" /></svg>
                                        {userData?.state ? userData.state : 'Không có quận/huyện để hiển thị'} </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm-40-82v-78q-33 0-56.5-23.5T360-320v-40L168-552q-3 18-5.5 36t-2.5 36q0 121 79.5 212T440-162Zm276-102q20-22 36-47.5t26.5-53q10.5-27.5 16-56.5t5.5-59q0-98-54.5-179T600-776v16q0 33-23.5 56.5T520-680h-80v80q0 17-11.5 28.5T400-560h-80v80h240q17 0 28.5 11.5T600-440v120h40q26 0 47 15.5t29 40.5Z" /></svg>
                                        {userData?.country ? userData.country : 'Không có quốc gia để hiển thị'} </p>
                                </>
                            )}
                            {actionContactModel === 'study' && (
                                <>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M480-160q-48-38-104-59t-116-21q-42 0-82.5 11T100-198q-21 11-40.5-1T40-234v-482q0-11 5.5-21T62-752q46-24 96-36t102-12q58 0 113.5 15T480-740v484q51-32 107-48t113-16q36 0 70.5 6t69.5 18v-480q15 5 29.5 10.5T898-752q11 5 16.5 15t5.5 21v482q0 23-19.5 35t-40.5 1q-37-20-77.5-31T700-240q-60 0-116 21t-104 59Zm80-200v-380l200-200v400L560-360Zm-160 65v-396q-33-14-68.5-21.5T260-720q-37 0-72 7t-68 21v397q35-13 69.5-19t70.5-6q36 0 70.5 6t69.5 19Zm0 0v-396 396Z" /></svg>
                                        {userData?.school ? userData.school : 'Không có trường học để hiển thị'}
                                    </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M480-120 200-272v-240L40-600l440-240 440 240v320h-80v-276l-80 44v240L480-120Zm0-332 274-148-274-148-274 148 274 148Zm0 241 200-108v-151L480-360 280-470v151l200 108Zm0-241Zm0 90Zm0 0Z" /></svg>
                                        {userData?.university ? userData.university : 'Không có trường đại học để hiển thị'}</p>

                                </>
                            )}
                            {actionContactModel === 'relationship' && (
                                <>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M160-80q-17 0-28.5-11.5T120-120v-200q0-33 23.5-56.5T200-400v-160q0-33 23.5-56.5T280-640h160v-58q-18-12-29-29t-11-41q0-15 6-29.5t18-26.5l56-56 56 56q12 12 18 26.5t6 29.5q0 24-11 41t-29 29v58h160q33 0 56.5 23.5T760-560v160q33 0 56.5 23.5T840-320v200q0 17-11.5 28.5T800-80H160Zm120-320h400v-160H280v160Zm-80 240h560v-160H200v160Zm80-240h400-400Zm-80 240h560-560Zm560-240H200h560Z" /></svg>
                                        {userData?.birthday ? formatBirthday(userData.birthday) : 'Không có ngày sinh để hiển thị'}</p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M360-390q-21 0-35.5-14.5T310-440q0-21 14.5-35.5T360-490q21 0 35.5 14.5T410-440q0 21-14.5 35.5T360-390Zm240 0q-21 0-35.5-14.5T550-440q0-21 14.5-35.5T600-490q21 0 35.5 14.5T650-440q0 21-14.5 35.5T600-390ZM480-160q134 0 227-93t93-227q0-24-3-46.5T786-570q-21 5-42 7.5t-44 2.5q-91 0-172-39T390-708q-32 78-91.5 135.5T160-486v6q0 134 93 227t227 93Zm0 80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm-54-715q42 70 114 112.5T700-640q14 0 27-1.5t27-3.5q-42-70-114-112.5T480-800q-14 0-27 1.5t-27 3.5ZM177-581q51-29 89-75t57-103q-51 29-89 75t-57 103Zm249-214Zm-103 36Z" /></svg>
                                        {userData?.gender ? userData.gender : 'Không có giới tính để hiển thị'} </p>
                                    <p className='contact-content-item'>
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Zm0-108q96-86 158-147.5t98-107q36-45.5 50-81t14-70.5q0-60-40-100t-100-40q-47 0-87 26.5T518-680h-76q-15-41-55-67.5T300-774q-60 0-100 40t-40 100q0 35 14 70.5t50 81q36 45.5 98 107T480-228Zm0-273Z" /></svg>
                                        {userData?.relationship ? userData.relationship : 'Chưa thiết lập mối quan hệ'}</p>

                                </>
                            )}
                        </div>
                    </div>
                )}

            </div>


        </div>
    );
};

export default ProfilePage;
