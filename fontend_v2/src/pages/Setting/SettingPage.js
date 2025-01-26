import '../../assets/css/setting.css';
import UpdateProfileComponent from '../../components/Setting/Profile/UpdateProfileComponent';
import SecurityProfile from '../../components/Setting/Profile/SecurityProfile';
import BlockList from '../../components/Setting/Profile/BlockList';
import React, { useState } from 'react';
const SettingPage = () => {
    const [actionSettingMenu, setActionSettingMenu] = useState('info');
    const handleChangeSettingMenu = (type) => {
        setActionSettingMenu(type);
    }

    return (
        <div className="setting-container">
            <div className="setting-menu">
                <div className="title-menu">
                    <strong style={{ fontSize: '20px', padding: '10px', color: 'rgb(0, 170, 255)' }}>Trung tâm tài khoản</strong>
                    <p style={{ textAlign: 'left', padding: '10px' }}>
                        Quản lý các cài đặt tài khoản và trải nghiệm kết nối trên nền tảng của chúng tôi, giúp bạn dễ dàng sử dụng và đồng bộ hóa các tính năng trong ứng dụng mạng xã hội dành cho sinh viên Nguyễn Trãi.</p>
                </div>
                <div className="setting-menu-item" onClick={() => handleChangeSettingMenu('info')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M400-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM80-160v-112q0-33 17-62t47-44q51-26 115-44t141-18h14q6 0 12 2-8 18-13.5 37.5T404-360h-4q-71 0-127.5 18T180-306q-9 5-14.5 14t-5.5 20v32h252q6 21 16 41.5t22 38.5H80Zm560 40-12-60q-12-5-22.5-10.5T584-204l-58 18-40-68 46-40q-2-14-2-26t2-26l-46-40 40-68 58 18q11-8 21.5-13.5T628-460l12-60h80l12 60q12 5 22.5 11t21.5 15l58-20 40 70-46 40q2 12 2 25t-2 25l46 40-40 68-58-18q-11 8-21.5 13.5T732-180l-12 60h-80Zm40-120q33 0 56.5-23.5T760-320q0-33-23.5-56.5T680-400q-33 0-56.5 23.5T600-320q0 33 23.5 56.5T680-240ZM400-560q33 0 56.5-23.5T480-640q0-33-23.5-56.5T400-720q-33 0-56.5 23.5T320-640q0 33 23.5 56.5T400-560Zm0-80Zm12 400Z" /></svg>
                    <p>Thông tin cá nhân</p>
                </div>
                <div className="setting-menu-item" onClick={() => handleChangeSettingMenu('security')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m438-338 226-226-57-57-169 169-84-84-57 57 141 141Zm42 258q-139-35-229.5-159.5T160-516v-244l320-120 320 120v244q0 152-90.5 276.5T480-80Zm0-84q104-33 172-132t68-220v-189l-240-90-240 90v189q0 121 68 220t172 132Zm0-316Z" /></svg>
                    <p>Bảo mật</p>
                </div>
                <div className="setting-menu-item" onClick={() => handleChangeSettingMenu('block-list')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M791-55 686-160H160v-112q0-34 17.5-62.5T224-378q45-23 91.5-37t94.5-21L55-791l57-57 736 736-57 57ZM240-240h366L486-360h-6q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm496-138q29 14 46 42.5t18 61.5L666-408q18 7 35.5 14t34.5 16ZM568-506l-59-59q23-9 37-29.5t14-45.5q0-33-23.5-56.5T480-720q-25 0-45.5 14T405-669l-59-59q23-34 58-53t76-19q66 0 113 47t47 113q0 41-19 76t-53 58Zm38 266H240h366ZM457-617Z" /></svg>
                    <p>Danh sách hạn chế</p>
                </div>
                <div className="setting-menu-item" onClick={() => handleChangeSettingMenu('dark-mod')}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M480-120q-150 0-255-105T120-480q0-150 105-255t255-105q14 0 27.5 1t26.5 3q-41 29-65.5 75.5T444-660q0 90 63 153t153 63q55 0 101-24.5t75-65.5q2 13 3 26.5t1 27.5q0 150-105 255T480-120Zm0-80q88 0 158-48.5T740-375q-20 5-40 8t-40 3q-123 0-209.5-86.5T364-660q0-20 3-40t8-40q-78 32-126.5 102T200-480q0 116 82 198t198 82Zm-10-270Z" /></svg>
                    <p>Chế độ tối</p>
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
