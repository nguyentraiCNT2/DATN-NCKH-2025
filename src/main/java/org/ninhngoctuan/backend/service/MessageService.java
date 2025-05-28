package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageDTO message, List<MultipartFile> images, List<MultipartFile> videos);

    List<MessageDTO> getMessagesBetweenUsers(Long receiverId);

    List<RoomDTO> getRoomsBetweenUsers();

    List<MessageImagesDTO> getImagesBetweenMessages(Long id);

    List<MessageVideosDTO> getVideoBetweenMessages(Long id);

    RoomDTO getRoomFriend(Long id);

    MessageDTO sendAudio(Long receiverId, MultipartFile audio);

    List<MessageAudiosDTO> getAudioBetweenMessages(Long id);
}
