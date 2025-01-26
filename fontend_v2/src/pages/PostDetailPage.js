import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import '../assets/css/postDetail.css';
import { UserProfileFuntions } from '../auth/UserProfile';
const PostDetailPage = () => {
  const { id } = useParams();
  const {userData} = UserProfileFuntions();
  const shareUrl = `${window.location.origin}/post/${id}`;
  const [showShareModal, setShowShareModal] = useState(false);
  const [newComment, setNewComment] = useState('');
  const [post, setPost] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [comments, setComments] = useState([]);
  const token = localStorage.getItem('token');
  const [images, setImages] = useState([]);
  const [videos, setVideos] = useState([]);
  const [selectedMedia, setSelectedMedia] = useState( localStorage.getItem('selectedMedia'));
  const [typeMainMedia, setMainMedia] = useState(localStorage.getItem('typeMedia'));
  const [liked, setLiked] = useState(false);
  const [likes, setLikes] = useState(0);
  const fetchLikesList = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/likes/list/post/${post.postId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setLikes(response.data.length);
    } catch (error) {
      console.error("Error fetching likes:", error);
    }
  };
  const fetchLikes = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/likes/check/like/${post.postId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setLiked(response.data);
    } catch (error) {
      console.error("Error fetching likes:", error);
    }
  };
  const handleLike = async () => {
    try {
      await axios.post(`http://localhost:8080/likes/like/post/${id}`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      await fetchLikesList();
      await fetchLikes();
    } catch (error) {
      console.error("Error liking the post:", error);
    }
  };
  const fetchPost = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/post/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setPost(response.data);
      setLikes(response.data.totalLike);
    } catch (error) {
      setErrorMessage(error.response?.data || 'Có lỗi xảy ra khi lấy dữ liệu');
    }
  };
  const fetchComments = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/comment/getbypost/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setComments(response.data);
    } catch (error) {
      console.error("Error fetching comments:", error);
    }
  };

  const fetchImages = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/post/${id}/image`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setImages(response.data);
      setMainMedia('image');
      setSelectedMedia(response.data[0]?.images.url); // Chọn ảnh đầu tiên mặc định
      console.log('selectedMedia',selectedMedia);
    } catch (error) {
      console.error('Error fetching images:', error);
    }
  };

  const fetchVideos = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/post/${id}/video`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setVideos(response.data);
      console.log('selectedMedia',selectedMedia);
      setSelectedMedia(selectedMedia !== null ? selectedMedia :  response.data[0]?.videos.url   );
      setMainMedia('video');
    } catch (error) {
      console.error('Error fetching videos:', error);
    }
  };
  const handleCommentSubmit = async () => {
    if (!newComment.trim()) return;

    try {
      await axios.post(`http://localhost:8080/comment/create`, {
        post: { postId: id },
        content: newComment,
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setNewComment('');
      fetchComments();
    } catch (error) {
      console.error("Error creating comment:", error);
    }
  };
  useEffect(() => {
    fetchImages();
    fetchVideos();
    fetchPost();
    fetchComments();
    fetchLikes();
  }, [id, liked, likes]);
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
  }; const formatLikes = (likes) => {
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
  const handleSelectMedia = (url, type) => {
    setSelectedMedia(url);
    setMainMedia(type);
  }
  const handleShareClick = () => {
    setShowShareModal(!showShareModal);
  };

  return (
    <div className="post-detail-content">
      
      <div className="post-detail-img">
        {/* Phần hiển thị media chính */}
        {selectedMedia ? (
          <>
            
        <div className="main-post">
         
            {typeMainMedia === 'video' ? (
              <video controls className="main-post-video">
                <source src={selectedMedia} type="video/mp4" />
              </video>
            ) : (
              <img src={selectedMedia} alt="Selected" className="main-post-img" />
            )}
         
        </div>

        {/* Phần danh sách ảnh/video */}
        <div className="list-second-post">
          {images.map((img, index) => (
            <div key={index} className="second-post" onClick={() => handleSelectMedia(img.images.url, 'img')}>
              <img src={img.images.url} alt={`Image ${index + 1}`} className="second-post-img" />
            </div>
          ))}
          {videos.map((video, index) => (
            <div key={index} className="second-post" onClick={() => handleSelectMedia(video.videos.url, 'video')}>
              <video className="second-post-video">
                <source src={video.videos.url} type="video/mp4" />
              </video>
            </div>
          ))}
        </div>
        </>
         ) : (
          <p>Không có Hình ảnh hiển thị</p>
        )}
      </div>

      <div class="post-detail-info">
      {post?.groupId?.groupId ? (
          <div class="group-info">
            <div class="info-group">
              <img src={post?.groupId?.groupImage || '/img/avatar.png'}
              alt={post?.groupId?.name} class="group-avatar-img" width="40px" />
            </div>
            <div class="info-detail-member-group">
              <img src={post.user.profilePicture || '/img/avatar.png'}
              alt={post.user.fullName} class="member-avatar-img" width="40px" />
            </div>
            <div class="info-user">
              <div>
                <strong>{post?.groupId?.name}</strong>
                <br />
                <small class="member-name">{post.user.fullName}</small>
                <br />
                <small>{formatTime(post.createdAt)}</small>
              </div>

            </div>
          </div>

        ) : (

          <div class="detail-info">
          <img src={post?.user?.profilePicture || '/img/avatar.png'}
            alt={post?.user?.fullName} class="detail-avatar-img" width="40px" />
          <div class="detail-info-user">
            <strong>{post?.user?.fullName}  {post?.tagId ? ` - Đang cảm thấy ${post?.tagId?.name}` : ""}</strong>
            <br />
            <small>{formatTime(post?.createdAt)}</small>
          </div>

        </div>
        )}

  



        <p class="detail-content-text">{post?.content}</p>
        <div class="post-detail-like-comment">
          <span>{formatLikes(likes)} lượt thích</span>
          <span>103 Bình luận</span>
        </div>
        <div class="post-detail-actions">
          {liked ? (
            <span class="detail-action-item  unliked-icon" onClick={handleLike}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#1877f2"><path d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
            </svg> Thích</span>

          ) : (
            <span class="detail-action-item " onClick={handleLike}><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960"
              width="24px" fill="#5f6368">
              <path
                d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z" />
            </svg> Thích</span>
          )}

          <span class="detail-action-item"><svg xmlns="http://www.w3.org/2000/svg" height="24px"
            viewBox="0 -960 960 960" width="24px" fill="#5f6368">
            <path
              d="M880-80 720-240H160q-33 0-56.5-23.5T80-320v-480q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v720ZM160-320h594l46 45v-525H160v480Zm0 0v-480 480Z" />
          </svg> Bình luận</span>
          <span class="detail-action-item" onClick={handleShareClick}> <svg xmlns="http://www.w3.org/2000/svg" height="24px"
            viewBox="0 -960 960 960" width="24px" fill="#5f6368">
            <path
              d="M680-80q-50 0-85-35t-35-85q0-6 3-28L282-392q-16 15-37 23.5t-45 8.5q-50 0-85-35t-35-85q0-50 35-85t85-35q24 0 45 8.5t37 23.5l281-164q-2-7-2.5-13.5T560-760q0-50 35-85t85-35q50 0 85 35t35 85q0 50-35 85t-85 35q-24 0-45-8.5T598-672L317-508q2 7 2.5 13.5t.5 14.5q0 8-.5 14.5T317-452l281 164q16-15 37-23.5t45-8.5q50 0 85 35t35 85q0 50-35 85t-85 35Zm0-80q17 0 28.5-11.5T720-200q0-17-11.5-28.5T680-240q-17 0-28.5 11.5T640-200q0 17 11.5 28.5T680-160ZM200-440q17 0 28.5-11.5T240-480q0-17-11.5-28.5T200-520q-17 0-28.5 11.5T160-480q0 17 11.5 28.5T200-440Zm480-280q17 0 28.5-11.5T720-760q0-17-11.5-28.5T680-800q-17 0-28.5 11.5T640-760q0 17 11.5 28.5T680-720Zm0 520ZM200-480Zm480-280Z" />
          </svg> Chia sẻ</span>
        </div>
        <div class="detail-list-comment">
          {comments.map((cm) => (

            <div class="comment-info">
              <div class="comment-info-user">
                <img src={cm?.user?.profilePicture|| '/img/avatar.png'}
            alt={cm?.user?.fullName} class="detail-avatar-img" width="40px" 
                  height="40px" />
                <div class="comment-content">
                  <strong>{cm?.user?.fullName}</strong>
                  <br />
                  <p>{cm.content}</p>
                </div>
              </div>

              <p class="comment-time">{formatTime(cm.createdAt)}</p>
            </div>
          ))}
        </div>
        <div class="create-comment-group">
          <img src={userData?.user?.profilePicture ? `${userData.user?.profilePicture}` : '/img/avatar.png'}alt="Avatar" class="detail-avatar-img" width="40px" height="40px" />
          <input type="text" placeholder="Aa" class="comment-input"   
           value={newComment}
            onChange={(e) => setNewComment(e.target.value)} />
          <button class="comment-send-button" onClick={handleCommentSubmit}><svg xmlns="http://www.w3.org/2000/svg" height="24px"
            viewBox="0 -960 960 960" width="24px" fill="#000000">
            <path
              d="M120-160v-640l760 320-760 320Zm80-120 474-200-474-200v140l240 60-240 60v140Zm0 0v-400 400Z" />
          </svg></button>
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

export default PostDetailPage;
