import axios from 'axios';
import React, { useEffect, useState } from 'react';

const PostCard = ({ post }) => {
  const [images, setImages] = useState([]);
  const [videos, setVideos] = useState([]);
  const [liked, setLiked] = useState(false);
  const [likes, setLikes] = useState(post.totalLike);
  
  // State để quản lý số lượng ảnh và video hiển thị
  const [showAllImages, setShowAllImages] = useState(false);
  const [showAllVideos, setShowAllVideos] = useState(false);
  const userId = Number(localStorage.getItem('userId')); // Chuyển đổi userId từ chuỗi thành số
  // Hàm toggle để hiển thị hoặc ẩn các ảnh, video còn lại
  const toggleImages = () => setShowAllImages(!showAllImages);
  const toggleVideos = () => setShowAllVideos(!showAllVideos);
  const getToken = () => {
    return localStorage.getItem('token') || '';
  };
  const [showDeleteAction, setShowDeleteAction] = useState(false);
  const fetchLikesList = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/likes/list/post/${post.postId}`, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      setLikes(response.data.length);
    } catch (error) {
      console.error("Error fetching likes:", error);
    }
  };

  const fetchImages = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/post/${post.postId}/image`, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      setImages(response.data);
    } catch (error) {
      console.error("Error fetching images:", error);
    }
  };

  const fetchVideos = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/post/${post.postId}/video`, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      setVideos(response.data);
    } catch (error) {
      console.error("Error fetching videos:", error);
    }
  };
  const fetchLikes = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/likes/check/like/${post.postId}`, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      setLiked(response.data);
    } catch (error) {
      console.error("Error fetching likes:", error);
    }
  };
  const handleLike = async () => {
    try {
      await axios.post(`http://localhost:8080/likes/like/post/${post.postId}`, {}, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      await fetchLikesList();
      await fetchLikes();
    } catch (error) {
      console.error("Error liking the post:", error);
    }
  };
  useEffect(() => {
    if (post?.postId) {
      fetchVideos();
      fetchImages();
      fetchLikes();
    }
  }, [post?.postId, liked]);

  const formatLikes = (likes) => {
    if (likes >= 1_000_000_000) {
      return (likes / 1_000_000_000).toFixed(1).replace(/\.0$/, '') + 'B';
    }
    if (likes >= 1_000_000) {
      return (likes / 1_000_000).toFixed(1).replace(/\.0$/, '') + 'M';
    }
    if (likes >= 1_000) {
      return (likes / 1_000).toFixed(1).replace(/\.0$/, '') + 'K';
    }
    return likes;
  };
  const [showShareModal, setShowShareModal] = useState(false);

  const shareUrl = `${window.location.origin}/post/${post.postId}`;

  const handleShareClick = () => {
    setShowShareModal(!showShareModal);
  };

  const handleCopyLink = () => {
    navigator.clipboard.writeText(shareUrl).then(() => {
      alert("Đường dẫn đã được sao chép vào clipboard!");
    });
  };

  const formatTime = (createdAt) => {
    const postDate = new Date(createdAt);
    const now = new Date();
    const diffInMilliseconds = now - postDate;
    const diffInMinutes = Math.floor(diffInMilliseconds / (1000 * 60)); // Tính số phút
    const diffInHours = Math.floor(diffInMilliseconds / (1000 * 60 * 60)); // Tính số giờ
    const diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24)); // Tính số ngày

    if (diffInMinutes < 60) {
      return `${diffInMinutes} phút trước`;
    } else if (diffInHours < 24) {
      return `${diffInHours} giờ trước`;
    } else if (diffInDays < 7) {
      return `${diffInDays} ngày trước`;
    } else {
      return postDate.toLocaleDateString("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      });
    }
  };
  const handleDetailAction = (id, selectedMedia, type) => {
    const url = `/post/${id}?selectedMedia=${encodeURIComponent(selectedMedia)}`;
    localStorage.setItem('selectedMedia', selectedMedia);
    localStorage.setItem('typeMedia', type);
    window.location.href = url;
  };
  const handleActionUserProfile = (id) => {
    if (id === userId) {
      window.location.href = `/profile`;
    } else {
      window.location.href = `/friend/${id}`;
    }
  }
  const handleActionGroupProfile = (id) => {

    window.location.href = `/group/${id}`;

  }
  const handleHidenPost = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/post/delete/${id}`, {}, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      });
      alert("Xóa bỏ bài viết thành công!")
      window.location.reload();
    } catch (error) {
      alert(error.response.data.error);
    }
  };
  return (
    <div className='post-item-container'>

      <div class="post" >
        {post?.groupId?.groupId ? (
          <div class="group-info">
            <div class="info-group">
              <img src={post?.groupId?.groupImage || '/img/avatar.png'}
                alt={post?.groupId?.name} class="group-avatar-img" width="40px" onClick={() => handleActionGroupProfile(post.groupId.groupId)} />
            </div>
            <div class="info-member-group">
              <img src={post.user.profilePicture || '/img/avatar.png'}
                alt={post.user.fullName} class="member-avatar-img" width="40px" onClick={() => handleActionUserProfile(post.user.userId)} />
            </div>
            <div class="info-user">
              <div>
                <strong onClick={() => handleActionGroupProfile(post.groupId.groupId)}>{post?.groupId?.name}</strong>
                <br />
                <small class="member-name" onClick={() => handleActionUserProfile(post.user.userId)}>{post.user.fullName}</small>
                <br />
                <small>{formatTime(post.createdAt)}</small>
              </div>

            </div>

            {post.user.userId === userId && (

              <div className='post-action-list' onClick={() => setShowDeleteAction(!showDeleteAction)}>
                <i class="fa-solid fa-ellipsis"></i>
              </div>
            )}



            {showDeleteAction && (
              <div className='post-action-delete' >
                <button className='post-action-delete-button' onClick={() => handleHidenPost(post?.postId)}>xóa bỏ</button>
              </div>
            )}
          </div>

        ) : (

          <div class="info">
            <img src={post.user.profilePicture || '/img/avatar.png'}
              alt={post.user.fullName}
              class="avatar-img" width="40px" onClick={() => handleActionUserProfile(post.user.userId)}/>
            <div class="info-user">
              <strong onClick={() => handleActionUserProfile(post.user.userId)}>{post.user.fullName}  {post?.tagId ? ` - Đang cảm thấy ${post.tagId.name}` : ""}</strong>
              <small>{formatTime(post.createdAt)}</small>
            </div>

            {post.user.userId === userId && (

              <div className='post-action-list' onClick={() => setShowDeleteAction(!showDeleteAction)}>
                <i class="fa-solid fa-ellipsis"></i>
              </div>
            )}



            {showDeleteAction && (
              <div className='post-action-delete' >
                <button className='post-action-delete-button' onClick={() => handleHidenPost(post?.postId)}>xóa bỏ</button>
              </div>
            )}

          </div>
        )}
        <p class="content-text">{post.content}</p>
        <div className="list-image-video-post">
          {/* Hiển thị phần ảnh nếu có ảnh */}
          {images && images.length > 0 && (
            <div className="list-image-post">
              {images.slice(0, showAllImages ? images.length : 5).map((img, index) => (
                <img key={index} src={img.images.url} className="post-img" alt={`Image ${index + 1}`} onClick={() => handleDetailAction(post.postId, img.images.url, 'image')} />
              ))}
              {images.length > 5 && (
                <button className="show-more-btn" onClick={toggleImages}>
                  {showAllImages ? "Ẩn bớt" : "Xem thêm"}
                </button>
              )}
            </div>
          )}

          {/* Hiển thị phần video nếu có video */}
          {videos && videos.length > 0 && (
            <div className="list-video-post">
              {videos.slice(0, showAllVideos ? videos.length : 5).map((vd, index) => (
                <video key={index} controls className="post-video" onClick={() => handleDetailAction(post.postId, vd.videos.url, 'video')}>
                  <source src={vd.videos.url} type="video/mp4" />
                  Your browser does not support the video tag.
                </video >
              ))}
              {videos.length > 5 && (
                <button className="show-more-btn" onClick={toggleVideos}>
                  {showAllVideos ? "Ẩn bớt" : "Xem thêm"}
                </button>
              )}
            </div>
          )}
        </div>

        <div class="like-comment">
          <span>{formatLikes(likes)} lượt thích</span>
          <span>{formatLikes(post?.totalComment ? post?.totalComment : 0)} Bình luận</span>
        </div>
        <div class="actions">
          {liked ? (
            <span class="action-item unliked-icon" onClick={handleLike}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#1877f2"><path d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
            </svg> <p class='action-item-name'>Thích</p> </span>

          ) : (
            <span class="action-item " onClick={handleLike}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
              width="24px" fill="#5f6368">
              <path
                d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
            </svg> <p class='action-item-name'>Thích</p></span>
          )}

          <span class="action-item" onClick={() => handleDetailAction(post.postId)}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
            width="24px" fill="#5f6368">
            <path
              d="M880-80 720-240H160q-33 0-56.5-23.5T80-320v-480q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v720ZM160-320h594l46 45v-525H160v480Zm0 0v-480 480Z" />
          </svg>  <p class='action-item-name'>Bình luận</p></span>
          <span class="action-item" onClick={handleShareClick}> <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
            width="24px" fill="#5f6368">
            <path
              d="M680-80q-50 0-85-35t-35-85q0-6 3-28L282-392q-16 15-37 23.5t-45 8.5q-50 0-85-35t-35-85q0-50 35-85t85-35q24 0 45 8.5t37 23.5l281-164q-2-7-2.5-13.5T560-760q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35q-24 0-45-8.5T598-672L317-508q2 7 2.5 13.5t.5 14.5q0 8-.5 14.5T317-452l281 164q16-15 37-23.5t45-8.5q50 0 85 35t35 85q0 50-35 85t-85 35Zm0-80q17 0 28.5-11.5T720-200q0-17-11.5-28.5T680-240q-17 0-28.5 11.5T640-200q0 17 11.5 28.5T680-160ZM200-440q17 0 28.5-11.5T240-480q0-17-11.5-28.5T200-520q-17 0-28.5 11.5T160-480q0 17 11.5 28.5T200-440Zm480-280q17 0 28.5-11.5T720-760q0-17-11.5-28.5T680-800q-17 0-28.5 11.5T640-760q0 17 11.5 28.5T680-720Zm0 520ZM200-480Zm480-280Z" />
          </svg> <p class='action-item-name'>Chia sẻ</p></span>
        </div>
      </div>
      {showShareModal && (
        <div className="share-modal">
          <div className="share-modal-content">
            <h3 className="share-modal-title">Chia sẻ bài viết</h3>
            <ul className="share-options">
              <li className="share-option">
                <a
                  className="share-option-link"
                  href={`https://www.facebook.com/sharer/sharer.php?u=${shareUrl}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <img src='https://phuongwanh.com/wp-content/uploads/2021/08/Lich-su-hinh-thanh-thay-doi-logo-facebook-16.png' width='50px' />
                  Facebook
                </a>
              </li>
              <li className="share-option">
                <a
                  className="share-option-link"
                  href={`https://t.me/share/url?url=${shareUrl}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <img src='https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Telegram_2019_Logo.svg/512px-Telegram_2019_Logo.svg.png' width='25px' />

                  Telegram
                </a>
              </li>

              <li className="share-option">
                <a
                  className="share-option-link"
                  href={`https://www.messenger.com/`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <img src='https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Facebook_Messenger_logo_2020.svg/2048px-Facebook_Messenger_logo_2020.svg.png' width='20px' />

                  Messenger
                </a>
              </li>
              <li
                className="share-option copy-link-option"
                onClick={handleCopyLink}
              >
                <img src='https://png.pngtree.com/element_our/20190529/ourlarge/pngtree-cartoon-link-icon-download-image_1196815.jpg' width='30px' />

                Sao chép liên kết
              </li>
            </ul>
            <button
              className="close-modal-button"
              onClick={() => setShowShareModal(false)}
            >
              Đóng
            </button>
          </div>
        </div>
      )}

    </div>
  );
};

export default PostCard;
