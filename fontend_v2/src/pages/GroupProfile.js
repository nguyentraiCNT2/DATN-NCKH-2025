// src/components/PrivateRoute.js
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CreatePostForm from '../components/CreatePost/CreatePostForm';
import PostCard from '../components/PostCard/PostCard';
import { tr } from 'date-fns/locale';
import { set } from 'date-fns';
const GroupProfilePage = () => {
    const getToken = () => {
        return localStorage.getItem('token') || '';
    };
    const [groupName, setGroupName] = useState("");
    const [groupDescription, setGroupDescription] = useState("");
    const [groupPostList, setGroupPostList] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    // Khai báo state để lưu trữ dữ liệu nhóm
    const [group, setGroup] = useState(null);
    const [groupRole, setGroupRole] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [member, setMember] = useState([]);
    const [memberNotActive, setMemberNotActive] = useState([]);
    const [groupPost, setGroupPost] = useState([]);
    const [groupActionModel, setGroupActionModel] = useState('group-posts')
    const { id } = useParams();
    const token = localStorage.getItem('token');
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
                setGroupDescription(response.data.description);
                setGroupName(response.data.name);
                setLoading(false);
            } catch (err) {
                setError('Không thể tải dữ liệu nhóm');
                setLoading(false);
            }
        };

        fetchGroupData();
        fetchGroupMemberData();
        fetchGroupPosts();
        fetchGroupRoles();
        fetchGroupMemberNotActiveData();
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
    const fetchGroupMemberNotActiveData = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/groups/${id}/members/not-active`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });
            setMemberNotActive(response.data);
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
    const fetchGroupRoles = async () => {
        setLoading(true);
        try {

            const response = await axios.get(`http://localhost:8080/groups/role/${id}`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });
            setGroupRole(response.data);
            setLoading(false);
        } catch (error) {
            setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
            setLoading(false)
        }
    };

    const handleGroupAction = (model) => {
        setGroupActionModel(model);
    }

    const outGroup = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/groups/out/${id}`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });

            alert('Bạn đã thành công rời khỏi nhóm');
            window.location.reload();
        } catch (error) {
            console.error(error);
            alert(error.response.data || 'Có lỗi xảy ra khi rời nhóm');

        }
    }
    const joinGroup = async (event) => {


        try {
            const response = await axios.post(
                `http://localhost:8080/groups/${id}/join`,
                {}, // Body rỗng (nếu cần dữ liệu, thêm vào đây)
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                }
            );

            alert('Bạn đã thành công tham gia nhóm');
            window.location.reload();
        } catch (error) {
            console.error(error);
            alert(error.response?.data || 'Có lỗi xảy ra khi tham gia nhóm');
        }
    };


    const activeMember = async (memberId) => {


        try {
            const response = await axios.post(
                `http://localhost:8080/groups/${id}/member/active/${memberId}`,
                {}, // Body rỗng (nếu cần dữ liệu, thêm vào đây)
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                }
            );

            alert('Duyệt thành công yêu cầu ');
            window.location.reload();
        } catch (error) {
            console.error(error);
            alert(error.response?.data || 'Có lỗi xảy ra khi duyệt yêu cầu tham gia nhóm');
        }
    };
    const quitMember = async (memberId) => {
        try {
            const response = await axios.delete(`http://localhost:8080/groups/${id}/delete/members/${memberId}`, {
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                },
            });
            alert('Xóa thành công người dùng ra khỏi nhóm');
            window.location.reload();
        } catch (error) {
            console.error(error);
            alert(error.response?.data || 'Có lỗi xảy ra khi xóa người dùng ra khỏi nhóm');
        }
    };

    // Hàm sao chép đường dẫn
    const copyToClipboard = () => {
        // Lấy đường dẫn hiện tại của trang
        const currentUrl = window.location.href;

        // Sao chép đường dẫn vào clipboard
        navigator.clipboard.writeText(currentUrl)
            .then(() => {
                alert('Đường dẫn nhóm đã được sao chép!');
            })
            .catch((err) => {
                console.error('Không thể sao chép đường dẫn: ', err);
            });
    };
 // Hàm xử lý khi người dùng nhấn nút "Lưu"
 const handleSave = async () => {
    try {
      // Tạo đối tượng GroupDTO để gửi lên API
      const groupDTO = {
        name: groupName,
        description: groupDescription,
      };

      // Gọi API để cập nhật thông tin nhóm
      const response = await axios.put(
        `http://localhost:8080/groups/update/${id}`,
        groupDTO , {
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }
      );

      // Hiển thị thông báo thành công
      alert("Cập nhật nhóm thành công!");
      console.log("API Response:", response.data);
      window.location.reload();
    } catch (error) {
      // Hiển thị thông báo lỗi nếu có lỗi xảy ra
      alert("Có lỗi xảy ra khi cập nhật nhóm.");
      console.error("API Error:", error);
    }
  };
  
  const fetchUpdateCoverPicture = async (file) => {
    if (!file) return;
    const formData = new FormData();
    formData.append('file', file);

    try {
        await axios.post(`http://localhost:8080/groups/${id}/upload-cover-image`, formData, {
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


const fetchUpdateProfilePicture = async (file) => {
    if (!file) return;
    const formData = new FormData();
    formData.append('file', file);
  
    try {
      await axios.post(`http://localhost:8080/groups/${id}/upload-image`, formData, {
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
    return (
        <div>
            <div class="profile-info-contaiter">
                <div class="profile-banner">
                    <img src={group?.groupCoverImage ? `${group?.groupCoverImage}` : '/img/Cover_img.png'} alt="Profile Banner" />
                    {groupRole?.groupRole === 'GROUP_ADMIN' && (
                        <>
                    <button className="other-cover-picture" onClick={() => document.getElementById('cover-photo-input').click()}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" /></svg></button>
                    <input
                        id="cover-photo-input"
                        type="file"
                        onChange={handleCoverPictureChange}
                        style={{ display: 'none' }}
                    />
                    </>
                    )}
                </div>
                <div class="profile-info">
                    <img src={group?.groupImage ? `${group?.groupImage}` : '/img/avatar.png'} alt="Profile Picture" />
                    <div class="profile-detail">
                        <h2 class="profile-name">{group?.name}</h2>
                        <p class="profile-follower">{member.length} Thành viên</p>
                    </div>
                    {groupRole?.groupRole === 'GROUP_ADMIN' && (
                        <>
                    <button className="other-avatar"   onClick={() => document.getElementById('profile-photo-input').click()}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M480-260q75 0 127.5-52.5T660-440q0-75-52.5-127.5T480-620q-75 0-127.5 52.5T300-440q0 75 52.5 127.5T480-260Zm0-80q-42 0-71-29t-29-71q0-42 29-71t71-29q42 0 71 29t29 71q0 42-29 71t-71 29ZM160-120q-33 0-56.5-23.5T80-200v-480q0-33 23.5-56.5T160-760h126l74-80h240l74 80h126q33 0 56.5 23.5T880-680v480q0 33-23.5 56.5T800-120H160Zm0-80h640v-480H638l-73-80H395l-73 80H160v480Zm320-240Z" /></svg></button>
                    <input
                        id="profile-photo-input"
                        type="file"
                        onChange={handleProfilePictureChange}
                        style={{ display: 'none' }}
                    />
                        </>
                    )}
                </div>
                <div class="profile-options">
                    {groupRole ? (
                        <>

                            <a href="#" class="follow-friend-action" onClick={() => outGroup()}>
                                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M806-440H320v-80h486l-62-62 56-58 160 160-160 160-56-58 62-62ZM600-600v-160H200v560h400v-160h80v160q0 33-23.5 56.5T600-120H200q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h400q33 0 56.5 23.5T680-760v160h-80Z" /></svg>
                                <p>Rời nhóm</p>
                            </a>
                            <a href="#" class="chat-friend-action" onClick={() => copyToClipboard()}>
                                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF"><path d="M440-280h80v-160h160v-80H520v-160h-80v160H280v80h160v160Zm40 200q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z" /></svg>
                                <p>Mời</p>
                            </a>
                        </>


                    ) : (
                        <a href="#" class="follow-friend-action" onClick={() => joinGroup()}>
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M720-400v-120H600v-80h120v-120h80v120h120v80H800v120h-80Zm-360-80q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM40-160v-112q0-34 17.5-62.5T104-378q62-31 126-46.5T360-440q66 0 130 15.5T616-378q29 15 46.5 43.5T680-272v112H40Zm80-80h480v-32q0-11-5.5-20T580-306q-54-27-109-40.5T360-360q-56 0-111 13.5T140-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-640q0-33-23.5-56.5T360-720q-33 0-56.5 23.5T280-640q0 33 23.5 56.5T360-560Zm0-80Zm0 400Z" /></svg>

                            <p>Tham gia</p>
                        </a>
                    )
                    }
                </div>
            </div>

            <div class="tabs">
                <button onClick={() => handleGroupAction('group-posts')}>Bài viết</button>
                <button onClick={() => handleGroupAction('members')}>Thành viên</button>
                {groupRole?.groupRole === 'GROUP_ADMIN' && (
                    <>
                        <button onClick={() => handleGroupAction('member-active')}>Duyệt</button>
                        <button onClick={() => handleGroupAction('group-setting')}>Cài đặt nhóm</button>
                    </>

                )}
            </div>


            <div class="post-area">

                {groupActionModel === 'group-posts' && (
                    <>
                        <div class="profile-contact">
                            <h3>Giới thiệu</h3>
                            <p>{group?.description}</p>
                        </div>
                        <div class="group-content">
                            <div class="post-box" style={{ position: 'relative' }}>
                                {groupRole ? (
                                    <CreatePostForm />
                                ) : (
                                    null
                                )}
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
                    </>
                )}
                {groupActionModel === 'members' && (
                    <div className='list-member'>
                        <strong >Danh sách thành viên </strong>
                        <div className='member-content' >

                            {member.map((item) => (
                                <div className='member-item'>
                                    <div className='member-info'>
                                        <img src={item?.profilePicture ? `${item?.profilePicture}` : '/img/avatar.png'} alt='name' className='member-item-avatar' />
                                        <p className='member-item-name'>{item?.fullName}</p>
                                    </div>
                                    {groupRole?.groupRole === 'GROUP_ADMIN' && (
                                        <button className='group-member-action-button' onClick={() => quitMember(item?.userId)}>Xóa khỏi nhóm</button>

                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {groupActionModel === 'member-active' && (
                    <div className='list-member'>
                        <strong >Danh sách yêu cầu vào nhóm</strong>
                        <div className='member-content' >
                            {memberNotActive.length > 0 ? (
                                <>
                                    {

                                        memberNotActive.map((item) => (
                                            <div className='member-item'>
                                                <div className='member-info'>
                                                    <img src={item?.profilePicture ? `${item?.profilePicture}` : '/img/avatar.png'} alt='name' className='member-item-avatar' />
                                                    <p className='member-item-name'>{item?.fullName}</p>
                                                </div>
                                                <button className='group-member-action-button' onClick={() => activeMember(item?.userId)} >Xác nhận</button>
                                            </div>
                                        ))}
                                </>
                            ) : (
                                <p className="no-groups-message">Không có yêu cầu nào</p>
                            )

                            }
                        </div>
                    </div>
                )}

                {groupActionModel === 'group-setting' && (
                    <div className='list-member'>
                        <strong >Cài đặt nhóm</strong>
                        <div className='group-setting-input-groups' >
                            <label className='group-setting-label'>Tên nhóm</label>
                            <input type="text"
                                placeholder="Tên nhóm"
                                className="group-setting-input"
                                value={groupName}
                                onChange={(e) => setGroupName(e.target.value)} />
                        </div>
                        <div className='group-setting-input-groups' >
                            <label className='group-setting-label'>Mô tả</label>
                            <input type="text"
                                placeholder="Mô tả"
                                className="group-setting-input"
                                value={groupDescription}
                                onChange={(e) => setGroupDescription(e.target.value)} />
                        </div>
                        <button className='group-setting-save-button' onClick={() => handleSave()} >Lưu</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default GroupProfilePage;
