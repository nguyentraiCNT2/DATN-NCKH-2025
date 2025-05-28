import axios from 'axios';
import { ro } from 'date-fns/locale';
import { useEffect, useState } from 'react';

const ChatContent = ({ item }) => {
  const token = localStorage.getItem('token');
  const userId = Number(localStorage.getItem('userId')); // Chuyển đổi userId từ chuỗi thành số
  const receiverId = localStorage.getItem('receiverId'); // Lấy receiverId từ localStorage
  const [images, setImages] = useState([]);
  const [videos, setVideos] = useState([]);
  const [audioList, setAudioList] = useState([]);
  // State Gallery
  const [galleryVisible, setGalleryVisible] = useState(false);
  const [galleryType, setGalleryType] = useState(''); // 'image' hoặc 'video'
  const [currentIndex, setCurrentIndex] = useState(0);
    const [themes, setThemes] = useState([]); // Danh sách themes
    const [selectedTheme, setSelectedTheme] = useState(null); // Theme hiện tại của phòng
    const roomId = localStorage.getItem('roomId');
    const [roomDetail, setRoomDetail] = useState(null);
    // Lấy theme hiện tại của phòng
    const fetchRoomTheme = async () => {
        if (!roomId) return;
        try {
            const response = await axios.get(`http://localhost:8080/theme/room-detail/${roomId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            setRoomDetail(response.data);
                setSelectedTheme(response.data.thememeId);
                console.log('themeData',response.data.thememeId);
        } catch (error) {
            console.error('Error fetching room theme:', error);
        }
    };


  const fetchImagesForMessage = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/messages/${item.messageId}/image`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setImages(response.data);
    } catch (error) {
      console.error('Error fetching images:', error);
    }
  };

  const fetchAudioForMessage = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/messages/${item.messageId}/audio`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAudioList(response.data);
    } catch (error) {
      console.error('Error fetching images:', error);
    }
  };

  const fetchVideosForMessage = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/messages/${item.messageId}/video`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setVideos(response.data);
    } catch (error) {
      console.error('Error fetching videos:', error);
    }
  };

  useEffect(() => {
    fetchRoomTheme()
    fetchImagesForMessage();
    fetchVideosForMessage();
    fetchAudioForMessage();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [item.messageId, token, roomId]);

  // Hàm mở Gallery modal
  const openGallery = (index, type) => {
    setGalleryType(type);
    setCurrentIndex(index);
    setGalleryVisible(true);
  };

  // Hàm chuyển sang ảnh/video trước đó
  const prevGallery = (e) => {
    e.stopPropagation();
    if (galleryType === 'image') {
      setCurrentIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
    } else {
      setCurrentIndex((prev) => (prev === 0 ? videos.length - 1 : prev - 1));
    }
  };

  // Hàm chuyển sang ảnh/video kế tiếp
  const nextGallery = (e) => {
    e.stopPropagation();
    if (galleryType === 'image') {
      setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
    } else {
      setCurrentIndex((prev) => (prev === videos.length - 1 ? 0 : prev + 1));
    }
  };

  const handleCopyLink = () => {
    const shareUrl = `${window.location.origin}/post/${item.messageId}`;
    navigator.clipboard.writeText(shareUrl).then(() => {
      alert("Đường dẫn đã được sao chép vào clipboard!");
    });
  };

  return (
    <>
<div
  className={`message ${item.sender.userId !== userId ? 'left' : 'right'}`}
  style={{
    backgroundColor:
      item.sender.userId === userId
        ? (selectedTheme?.color ? selectedTheme.color : 'rgba(0, 132, 255, 0.95)') 
        : '#ddd' ,
        display: item?.content ? item.content : 'none',
  }}
 >
  {item.content}
</div>
      <div className={`message video-img-list ${item.sender.userId !== userId ? 'left' : 'right'}`}>
        <div className='list-message-video'>
          {audioList.map((itemAudio, i) => (
            <audio
              key={i}
              src={itemAudio.audio.url}
              controls
              className="message-audio"
            />
          ))}
          {images.map((itemImage, i) => (
            <img
              key={i}
              src={`${itemImage.imagesEntity.url}`}
              alt="message-img"
              className="message-image"
              onClick={() => openGallery(i, 'image')}
            />
          ))}
          {videos.map((itemVideo, i) => (
            <div
              key={i}
              className="video-wrapper"
              onClick={() => openGallery(i, 'video')}
            >
              <video
                className="message-video"
                controls
                onClick={(e) => e.stopPropagation()}
              >
                <source
                  src={`${itemVideo.video.url}`}
                  type="video/mp4"
                />
                Your browser does not support the video tag.
              </video>
            </div>
          ))}
        </div>
      </div>

      {/* Gallery Modal */}
      {galleryVisible && (
        <div className="gallery-modal" onClick={() => setGalleryVisible(false)}>
          <div className="gallery-content" onClick={(e) => e.stopPropagation()}>
            <button className="gallery-close" onClick={() => setGalleryVisible(false)}><i class="fa-solid fa-xmark"></i></button>
            <button className="gallery-prev" onClick={prevGallery}><i class="fa-solid fa-caret-left"></i></button>
            <div className="gallery-main-content">
              {galleryType === 'image' ? (
                <img
                  src={images[currentIndex].imagesEntity.url}
                  alt={`Gallery ${currentIndex}`}
                  className="gallery-main"
                />
              ) : (
                <video controls className="gallery-main">
                  <source
                    src={videos[currentIndex].video.url}
                    type="video/mp4"
                  />
                  Your browser does not support the video tag.
                </video>
              )}
            </div>
            <button className="gallery-next" onClick={nextGallery}><i class="fa-solid fa-caret-right"></i></button>
            <div className="gallery-thumbnails">
              {galleryType === 'image'
                ? images.map((img, idx) => (
                  <img
                    key={idx}
                    src={img.imagesEntity.url}
                    alt={`Thumbnail ${idx}`}
                    className={`gallery-thumb ${idx === currentIndex ? 'active' : ''}`}
                    onClick={() => setCurrentIndex(idx)}
                  />
                ))
                : videos.map((vd, idx) => (
                  <video
                    key={idx}
                    className={`gallery-thumb ${idx === currentIndex ? 'active' : ''}`}
                    onClick={() => setCurrentIndex(idx)}
                    muted
                  >
                    <source
                      src={vd.video.url}
                      type="video/mp4"
                    />
                  </video>
                ))
              }
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatContent;
