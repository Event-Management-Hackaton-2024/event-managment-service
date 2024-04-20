package com.hackathon.netplatform.service;

import static com.hackathon.netplatform.model.Image.FILE_NAME;

import com.hackathon.netplatform.dto.response.ImageResponseDto;
import com.hackathon.netplatform.exception.image.ImageNotFoundException;
import com.hackathon.netplatform.exception.image.MultipartFileContentTypeException;
import com.hackathon.netplatform.exception.image.MultipartFileNotSelectedException;
import com.hackathon.netplatform.exception.image.MultipartFileSizeException;
import com.hackathon.netplatform.model.Event;
import com.hackathon.netplatform.model.Image;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.ImageRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class ImageService {
  private static final Logger logger = LogManager.getLogger(ImageService.class);

  @Value("${image.folder.path}")
  private String folderPath;

  @Value("${multipart.file.max.size}")
  private Integer maxFileSize;

  private static final Integer MB = 1024 * 1024;
  private final ModelMapper modelMapper;
  private final ImageRepository imageRepository;
  @Lazy private final UserService userService;
  @Lazy private final EventService eventService;

  @Transactional
  public ImageResponseDto uploadImage(MultipartFile multipartFile, UUID userId, UUID eventId)
      throws IOException {

    checkFileIsSelected(multipartFile);
    checkContentType(multipartFile);
    checkFileSize(multipartFile, maxFileSize);

    Path directoryPath = Path.of(folderPath + (userId != null ? userId : eventId));
    Path imagePath = createFileNamePath(multipartFile, directoryPath);

    Object object = imageOwnerVerification(userId, eventId);
    Image image = null;
    if (object instanceof User user) {
      image = createImageDataForUser(multipartFile, user, imagePath);
      setImageForUser(user, image);
      saveImageToFileSystem(multipartFile, directoryPath, imagePath);

    } else if (object instanceof Event event) {
      image = createImageDataForEvent(multipartFile, event, imagePath);
      setImageForEvent(event, image);
      saveImageToFileSystem(multipartFile, directoryPath, imagePath);
    }
    logger.info("Uploaded image for Object with id - {}. Path: {}", object, imagePath);
    return modelMapper.map(image, ImageResponseDto.class);
  }

  @Transactional
  public byte[] downloadImageForUser(UUID userid) throws IOException {
    User user = getUser(userid);
    checkForAttachedPictureForUser(user);
    logger.debug(
        "Downloaded image for User id - {}. File path: {}", userid, user.getImage().getFilePath());
    return Files.readAllBytes(new File(user.getImage().getFilePath()).toPath());
  }

  @Transactional
  public byte[] downloadImageForEvent(UUID eventId) throws IOException {
    Event event = getEvent(eventId);
    checkForAttachedPictureForEvent(event);
    logger.debug(
        "Downloaded image for Event id - {}. File path: {}",
        eventId,
        event.getImage().getFilePath());
    return Files.readAllBytes(new File(event.getImage().getFilePath()).toPath());
  }

  @Transactional
  public void deleteImageForUser(UUID userId) throws IOException {
    removeImage(getUser(userId));
    logger.info("Deleted image for User id - {}", userId);
  }

  @Transactional
  public void deleteImageForEvent(UUID eventId) throws IOException {
    removeImage(getEvent(eventId));
    logger.info("Deleted image for Event id - {}", eventId);
  }

  private Object imageOwnerVerification(UUID userId, UUID eventId) {
    if (userId != null) {
      return getUser(userId);
    } else if (eventId != null) {
      return getEvent(eventId);
    } else throw new RuntimeException("id not found ");
  }

  private void saveImageToFileSystem(
      MultipartFile multipartFile, Path directoryPath, Path imagePath) throws IOException {
    createDirectoryIfNotExists(directoryPath);
    Files.copy(multipartFile.getInputStream(), imagePath);
  }

  private Image createImageDataForUser(MultipartFile file, User user, Path imagePath) {
    return imageRepository.save(
        Image.builder()
            .type(file.getContentType())
            .filePath(imagePath.toString())
            .user(user)
            .build());
  }

  private Image createImageDataForEvent(MultipartFile file, Event event, Path imagePath) {
    return imageRepository.save(
        Image.builder()
            .type(file.getContentType())
            .filePath(imagePath.toString())
            .event(event)
            .build());
  }

  private void createDirectoryIfNotExists(Path directoryPath) throws IOException {
    if (Files.notExists(directoryPath)) {
      logger.debug("Creating directory: {}", directoryPath);
      Files.createDirectories(directoryPath);
    }
  }

  private User getUser(UUID userId) {
    return userService.getUserById(userId);
  }

  private Event getEvent(UUID eventId) {
    return eventService.getEvent(eventId);
  }

  private void setImageForUser(User user, Image image) throws IOException {
    if (user != null && user.getImage() != null) {
      removeImage(user);
    }
    user.setImage(image);
  }

  private void setImageForEvent(Event event, Image image) throws IOException {
    if (event != null && event.getImage() != null) {
      removeImage(event);
    }
    event.setImage(image);
  }

  private void removeImage(User user) throws IOException {
    checkForAttachedPictureForUser(user);
    Image image = user.getImage();
    FileSystemUtils.deleteRecursively(getImageFolderPath(user));
    user.setImage(null);
    imageRepository.delete(image);
  }

  private void removeImage(Event event) throws IOException {
    checkForAttachedPictureForEvent(event);
    Image image = event.getImage();
    FileSystemUtils.deleteRecursively(getImageFolderPath(event));
    event.setImage(null);
    imageRepository.delete(image);
  }

  private static Path getImageFolderPath(User user) {
    return Path.of(
        user.getImage()
            .getFilePath()
            .substring(0, user.getImage().getFilePath().indexOf(FILE_NAME)));
  }

  private static Path getImageFolderPath(Event event) {
    return Path.of(
        event
            .getImage()
            .getFilePath()
            .substring(0, event.getImage().getFilePath().indexOf(FILE_NAME)));
  }

  private static void checkForAttachedPictureForUser(User user) {
    if (user.getImage() == null) {
      throw new ImageNotFoundException(user.getId());
    }
  }

  private static void checkForAttachedPictureForEvent(Event event) {
    if (event.getImage() == null) {
      throw new ImageNotFoundException(event.getId());
    }
  }

  private boolean isSupportedContentType(String contentType) {
    return contentType.equals("image/png")
        || contentType.equals("image/jpg")
        || contentType.equals("image/jpeg");
  }

  private void checkContentType(MultipartFile file) {
    if (!isSupportedContentType(Objects.requireNonNull(file.getContentType()))) {
      throw new MultipartFileContentTypeException();
    }
  }

  private static void checkFileIsSelected(MultipartFile file) {
    if (file.isEmpty()) {
      throw new MultipartFileNotSelectedException();
    }
  }

  private static void checkFileSize(MultipartFile multipartFile, Integer fileSize) {
    if (multipartFile.getSize() > (long) fileSize * MB) {
      throw new MultipartFileSizeException(fileSize);
    }
  }

  private static Path createFileNamePath(MultipartFile multipartFile, Path directory) {
    StringBuilder sb = new StringBuilder();
    sb.append(directory).append("/");

    switch (Objects.requireNonNull(multipartFile.getContentType())) {
      case "image/png" -> sb.append(FILE_NAME + ".png");
      case "image/jpg" -> sb.append(FILE_NAME + ".jpg");
      case "image/jpeg" -> sb.append(FILE_NAME + ".jpeg");
      default -> throw new MultipartFileContentTypeException();
    }
    return Path.of(sb.toString());
  }
}
