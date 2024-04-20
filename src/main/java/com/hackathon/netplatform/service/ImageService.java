package com.hackathon.netplatform.service;

import static com.hackathon.netplatform.model.Image.FILE_NAME;

import com.hackathon.netplatform.dto.response.ImageResponseDto;
import com.hackathon.netplatform.exception.image.ImageNotFoundException;
import com.hackathon.netplatform.exception.image.MultipartFileContentTypeException;
import com.hackathon.netplatform.exception.image.MultipartFileNotSelectedException;
import com.hackathon.netplatform.exception.image.MultipartFileSizeException;
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

  @Transactional
  public ImageResponseDto uploadImage(MultipartFile multipartFile, UUID userId) throws IOException {

    checkFileIsSelected(multipartFile);
    checkContentType(multipartFile);
    checkFileSize(multipartFile, maxFileSize);

    Path directoryPath = Path.of(folderPath + userId);
    Path imagePath = createFileNamePath(multipartFile, directoryPath);
    User user = getUser(userId);

    Image image = createImageData(multipartFile, user, imagePath);
    setUserImage(user, image);
    saveImageToFileSystem(multipartFile, directoryPath, imagePath);
    logger.info("Uploaded image for User id - {}. Path: {}", user.getId(), imagePath);
    return modelMapper.map(image, ImageResponseDto.class);
  }

  @Transactional
  public byte[] downloadImage(UUID userid) throws IOException {
    User user = getUser(userid);
    checkForAttachedPicture(user);
    logger.debug(
        "Downloaded image for User id - {}. File path: {}", userid, user.getImage().getFilePath());
    return Files.readAllBytes(new File(user.getImage().getFilePath()).toPath());
  }

  @Transactional
  public void deleteImage(UUID userId) throws IOException {
    removeImage(getUser(userId));
    logger.info("Deleted image for User id - {}", userId);
  }

  private void saveImageToFileSystem(
      MultipartFile multipartFile, Path directoryPath, Path imagePath) throws IOException {
    createDirectoryIfNotExists(directoryPath);
    Files.copy(multipartFile.getInputStream(), imagePath);
  }

  private Image createImageData(MultipartFile file, User user, Path imagePath) {
    return imageRepository.save(
        Image.builder()
            .type(file.getContentType())
            .filePath(imagePath.toString())
            .user(user)
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

  private void setUserImage(User user, Image image) throws IOException {
    if (user.getImage() != null) {
      removeImage(user);
    }
    user.setImage(image);
  }

  private void removeImage(User user) throws IOException {
    checkForAttachedPicture(user);
    Image image = user.getImage();
    FileSystemUtils.deleteRecursively(getImageFolderPath(user));
    user.setImage(null);
    imageRepository.delete(image);
  }

  private static Path getImageFolderPath(User user) {
    return Path.of(
        user.getImage()
            .getFilePath()
            .substring(0, user.getImage().getFilePath().indexOf(FILE_NAME)));
  }

  private static void checkForAttachedPicture(User user) {
    if (user.getImage() == null) {
      throw new ImageNotFoundException(user.getId());
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
