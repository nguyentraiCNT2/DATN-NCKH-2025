import axios from 'axios';
import React, { useState } from 'react';
import '../../assets/css/setting.css';
import BlockList from '../../components/Setting/Profile/BlockList';
import SecurityProfile from '../../components/Setting/Profile/SecurityProfile';
import UpdateProfileComponent from '../../components/Setting/Profile/UpdateProfileComponent';
import { UserProfileFuntions } from '../../auth/UserProfile';
const SettingPage = () => {
 const {userData} = UserProfileFuntions();
    const token = localStorage.getItem('token');
    const [actionSettingMenu, setActionSettingMenu] = useState('info');
    const handleChangeSettingMenu = (type) => {
        setActionSettingMenu(type);
    }
    const handleLogOut = async () => {
        try {
            await axios.post(
                'http://localhost:8080/auth/logout',
                {}, // Body trống vì chỉ cần gửi header
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    }
                }
            );
            window.location.href = '/login';
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };
    const handleacion = (link) => {
        if(link === '/list-friend'){
            window.location.href = '/list-friend';
        }else{
            window.location.href = '/profile';
        }

    }

    return (
        <div className="setting-container">
            <div className="setting-menu">
                <div className="title-menu">
                    <strong style={{ fontSize: '20px', padding: '10px', color: 'rgb(0, 170, 255)' }}>Trung tâm tài khoản</strong>
                    <p style={{ textAlign: 'left', padding: '10px' }}>
                        Quản lý các cài đặt tài khoản và trải nghiệm kết nối trên nền tảng của chúng tôi, giúp bạn dễ dàng sử dụng và đồng bộ hóa các tính năng trong ứng dụng mạng xã hội dành cho sinh viên Nguyễn Trãi.</p>
                </div>
                <div class="sidebar-profile" onClick={() => handleacion('/profile')}>
                        <img src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'} alt="Avatar" class="avatar-img" width="40px" />
                        <strong class="sidebar-profile-name">{userData?.user?.fullName}</strong>
                    </div>
                <div className={actionSettingMenu === 'info' ? 'setting-menu-item active' : 'setting-menu-item' } onClick={() => handleChangeSettingMenu('info')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M400-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM80-160v-112q0-33 17-62t47-44q51-26 115-44t141-18h14q6 0 12 2-8 18-13.5 37.5T404-360h-4q-71 0-127.5 18T180-306q-9 5-14.5 14t-5.5 20v32h252q6 21 16 41.5t22 38.5H80Zm560 40-12-60q-12-5-22.5-10.5T584-204l-58 18-40-68 46-40q-2-14-2-26t2-26l-46-40 40-68 58 18q11-8 21.5-13.5T628-460l12-60h80l12 60q12 5 22.5 11t21.5 15l58-20 40 70-46 40q2 12 2 25t-2 25l46 40-40 68-58-18q-11 8-21.5 13.5T732-180l-12 60h-80Zm40-120q33 0 56.5-23.5T760-320q0-33-23.5-56.5T680-400q-33 0-56.5 23.5T600-320q0 33 23.5 56.5T680-240ZM400-560q33 0 56.5-23.5T480-640q0-33-23.5-56.5T400-720q-33 0-56.5 23.5T320-640q0 33 23.5 56.5T400-560Zm0-80Zm12 400Z" /></svg>
                    <p className='setting-action-name'>Thông tin cá nhân</p>
                </div>
                <div className={actionSettingMenu === 'security' ? 'setting-menu-item active' : 'setting-menu-item' } onClick={() => handleChangeSettingMenu('security')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m438-338 226-226-57-57-169 169-84-84-57 57 141 141Zm42 258q-139-35-229.5-159.5T160-516v-244l320-120 320 120v244q0 152-90.5 276.5T480-80Zm0-84q104-33 172-132t68-220v-189l-240-90-240 90v189q0 121 68 220t172 132Zm0-316Z" /></svg>
                    <p  className='setting-action-name'>Bảo mật</p>
                </div>
                <div className={actionSettingMenu === 'block-list' ? 'setting-menu-item active' : 'setting-menu-item' } onClick={() => handleChangeSettingMenu('block-list')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M791-55 686-160H160v-112q0-34 17.5-62.5T224-378q45-23 91.5-37t94.5-21L55-791l57-57 736 736-57 57ZM240-240h366L486-360h-6q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm496-138q29 14 46 42.5t18 61.5L666-408q18 7 35.5 14t34.5 16ZM568-506l-59-59q23-9 37-29.5t14-45.5q0-33-23.5-56.5T480-720q-25 0-45.5 14T405-669l-59-59q23-34 58-53t76-19q66 0 113 47t47 113q0 41-19 76t-53 58Zm38 266H240h366ZM457-617Z" /></svg>
                    <p  className='setting-action-name'>Danh sách hạn chế</p>
                </div>
                <div className="setting-menu-item" onClick={() => handleLogOut()}>
                <i class="fa-solid fa-right-from-bracket"></i>
                    <p  className='setting-action-name'>Đăng xuất</p>
                </div>
            </div>
            <div className="setting-content">

                {actionSettingMenu === 'info' && (
                    <>
                        <p className='title-content'>
                            Thông tin cá nhân
                        </p>
                        <UpdateProfileComponent />

                    </>
                )}
                  {actionSettingMenu === 'security' && (
                    <>
                        <p className='title-content'>
                            Thay đổi mật khẩu
                        </p>
                        <SecurityProfile />

                    </>
                )}
                 {actionSettingMenu === 'block-list' && (
                    <>
                        <p className='title-content'>
                           Danh sách hạn chế
                        </p>
                        <BlockList />

                    </>
                )}
                {actionSettingMenu === 'dark-mod' && (
                    <>
                        <p className='title-content'>
                            Chế độ tối
                        </p>
                        <p>Chức năng này hiện tại đang bảo trì.</p>

                    </>
                )}
            </div>
        </div>
    );
};

export default SettingPage;
