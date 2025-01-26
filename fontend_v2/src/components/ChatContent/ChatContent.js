import axios from 'axios';
import React, { useEffect, useState } from 'react';

const ChatContent = ({item}) => {
    const token = localStorage.getItem('token');
    const userId = Number(localStorage.getItem('userId')); // Chuyển đổi userId từ chuỗi thành số
    const receiverId = localStorage.getItem('receiverId'); // Lấy `receiverId` từ localStorage
    const [images, setImages] = useState([]);
    const [videos, setVideos] = useState([]);
    const fetchImagesForMessage = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/messages/${item.messageId}/image`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setImages(response.data);
        } catch (error) {
            console.error('Error fetching images:', error);
        }
    };

    const fetchVideosForMessage = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/messages/${item.messageId}/video`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setVideos(response.data);
        } catch (error) {
            console.error('Error fetching videos:', error);
        }
    };

    useEffect(() => {
        fetchImagesForMessage();
        fetchVideosForMessage();
    }, [item.messageId,localStorage.getItem('token')]);

    return (
        <>
                <div  className={`message ${item.sender.userId !== userId ? 'left' : 'right'}`}>{item.content}</div>
                <div  className={`message video-img-list ${item.sender.userId !== userId ? 'left' : 'right'}`}>
        

                    <div className='list-message-video'>
                    {images.map((itemImage, i) => (
                        <img key={i} src={`${itemImage.imagesEntity.url}`} alt="message-img" className="message-image"/>
                    ))}
                  {videos.map((itemVideo, i) => (
                        <div key={i} className="video-wrapper" >
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
        </>
    );
};

export default ChatContent;
