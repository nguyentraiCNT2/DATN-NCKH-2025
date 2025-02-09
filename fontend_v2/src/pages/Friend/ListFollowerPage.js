import axios from 'axios';
import React, { useEffect, useState } from 'react';
import '../../assets/css/friend.css';
const ListFollowerPage = () => {
  const handleAction = (id) => {
    window.location.href = `/friend/${id}`;
  };

  const [friends, setFriends] = useState([]);
  const [error, setError] = useState('');
  const token = localStorage.getItem('token');

  // Lấy danh sách follower
  const fetchFriends = async () => {
    try {
      const response = await axios.get('http://localhost:8080/friend/list/follower', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      const friendList = response.data;

      // Kiểm tra trạng thái theo dõi cho tất cả bạn bè
      const updatedFriends = await Promise.all(
        friendList.map(async (friend) => {
          const isFollowing = await checkFriend(friend.user.userId);
          return { ...friend, isFollowing };
        })
      );

      setFriends(updatedFriends);
    } catch (error) {
      setError('Lỗi khi lấy danh sách bạn bè');
      console.error(error);
    }
  };

  // Hàm kiểm tra trạng thái theo dõi
  const checkFriend = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/friend/checkfriend/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      return response.data; // Trả về trạng thái true/false
    } catch (error) {
      console.error('Lỗi khi kiểm tra trạng thái theo dõi:', error);
      return false; // Mặc định là chưa theo dõi nếu lỗi xảy ra
    }
  };

  // Thêm theo dõi
  const addFriend = async (id) => {
    try {
      await axios.post(`http://localhost:8080/friend/add/${id}`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      alert('Bạn đã theo dõi thành công!');
      fetchFriends(); // Cập nhật danh sách sau khi thêm
    } catch (error) {
      alert('Lỗi khi theo dõi');
      console.error(error);
    }
  };

  // Hủy theo dõi
  const cancelFriend = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/friend/cancel/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      alert('Bạn đã bỏ theo dõi thành công!');
      fetchFriends(); // Cập nhật danh sách sau khi hủy
    } catch (error) {
      alert('Lỗi khi bỏ theo dõi');
      console.error(error);
    }
  };
  const blockFriend = async (id) => {
    try {
      await axios.put(`http://localhost:8080/friend/block/${id}`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      alert('Chặn người dùng thành công');
      setError('');
      fetchFriends();
    } catch (error) {
      alert('Lỗi khi chặn người dùng');
      console.error(error);
    }
  };
  useEffect(() => {
    fetchFriends();
  }, []);
  const handleacion = (id) => {
    window.location.href = `/friend/${id}`;
  }

  return (
    <div>

      <div class="friend-container">
        <div class="friend-menu">
          <h2 class="friend-title">Người hâm mộ</h2>
          <a href='/list-friend' class="friend-actions">
                <div class="friend-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                        fill="#000000">
                        <path
                            d="M40-160v-112q0-34 17.5-62.5T104-378q62-31 126-46.5T360-440q66 0 130 15.5T616-378q29 15 46.5 43.5T680-272v112H40Zm720 0v-120q0-44-24.5-84.5T666-434q51 6 96 20.5t84 35.5q36 20 55 44.5t19 53.5v120H760ZM360-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47Zm400-160q0 66-47 113t-113 47q-11 0-28-2.5t-28-5.5q27-32 41.5-71t14.5-81q0-42-14.5-81T544-792q14-5 28-6.5t28-1.5q66 0 113 47t47 113ZM120-240h480v-32q0-11-5.5-20T580-306q-54-27-109-40.5T360-360q-56 0-111 13.5T140-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-640q0-33-23.5-56.5T360-720q-33 0-56.5 23.5T280-640q0 33 23.5 56.5T360-560Zm0 320Zm0-400Z" />
                    </svg>
                </div>
                <p  class="friend-action">Trang chủ</p>
            </a>
            <a href="/list-follow"  class="friend-actions">
                <div class="friend-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                        fill="#000000">
                        <path
                            d="M500-482q29-32 44.5-73t15.5-85q0-44-15.5-85T500-798q60 8 100 53t40 105q0 60-40 105t-100 53Zm220 322v-120q0-36-16-68.5T662-406q51 18 94.5 46.5T800-280v120h-80Zm80-280v-80h-80v-80h80v-80h80v80h80v80h-80v80h-80Zm-480-40q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM0-160v-112q0-34 17.5-62.5T64-378q62-31 126-46.5T320-440q66 0 130 15.5T576-378q29 15 46.5 43.5T640-272v112H0Zm320-400q33 0 56.5-23.5T400-640q0-33-23.5-56.5T320-720q-33 0-56.5 23.5T240-640q0 33 23.5 56.5T320-560ZM80-240h480v-32q0-11-5.5-20T540-306q-54-27-109-40.5T320-360q-56 0-111 13.5T100-306q-9 5-14.5 14T80-272v32Zm240-400Zm0 400Z" />
                    </svg>
                </div>
                <p class="friend-action">Follow</p>
            </a>
            <a href="/list-follower"  class="friend-actions">
                <div class="friend-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                        fill="#000000">
                        <path
                            d="M560-680v-80h320v80H560Zm0 160v-80h320v80H560Zm0 160v-80h320v80H560Zm-240-40q-50 0-85-35t-35-85q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35ZM80-160v-76q0-21 10-40t28-30q45-27 95.5-40.5T320-360q56 0 106.5 13.5T522-306q18 11 28 30t10 40v76H80Zm86-80h308q-35-20-74-30t-80-10q-41 0-80 10t-74 30Zm154-240q17 0 28.5-11.5T360-520q0-17-11.5-28.5T320-560q-17 0-28.5 11.5T280-520q0 17 11.5 28.5T320-480Zm0-40Zm0 280Z" />
                    </svg>
                </div>
                <p class="friend-action">Follower</p>
            </a>
        </div>
        <div class="friend-box">
          <div class="friend-list">
            {
              friends.map(friend => (
                <div class="friend-item">
                  <div class="friend-imgs">
                    <img src={friend?.user?.profilePicture || '/img/avatar.png'} alt="" class="friend-avatar" onClick={() => handleacion(friend?.user?.userId)} />

                  </div>

                  <div class="follow-actions">
                    <p onClick={() => handleacion(friend?.user?.userId)}>{friend?.user?.fullName}</p>
                    {friend.isFollowing ? (
                      <a
                        href="#"
                        className="follow-action"
                        onClick={() => cancelFriend(friend?.user?.userId)}
                      >
                        <i class="fa-solid fa-user-minus"></i>
                        <p className='follow-action-button-name'>Bỏ theo giõi</p>
                      </a>
                    ) : (
                      <a
                        href="#"
                        className="follow-action"
                        onClick={() => addFriend(friend?.user?.userId)}
                      >
                   <p className='follow-action-button-name'>Theo giõi</p>
                      </a>
                    )}
                    <a href="#" class="follow-action block-user" onClick={() => blockFriend(friend?.user?.userId)}><i class="fa-solid fa-ban"></i>  <p className='follow-action-button-name'>Chặn người dùng</p></a>
                  </div>
                </div>
              ))}

          </div>
        </div>
      </div>
    </div>
  );
};

export default ListFollowerPage;
