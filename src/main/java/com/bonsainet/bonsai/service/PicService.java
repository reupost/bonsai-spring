package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.misc.DocumentStorageException;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.PicRepository;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PicService implements IPicService {

  private static final Logger logger = LoggerFactory.getLogger(PicService.class);

  @Value("${pic.rootfolder}")
  private String picRootFolder;

  private final ApplicationContext context;

  private final PicRepository repository;

  public PicService(ApplicationContext context, PicRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<Pic> findAll() {
    return (List<Pic>) repository.findAll();
  }

  @Override
  public Page<Pic> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public Future<Pic> save(Pic p) throws InterruptedException, IllegalArgumentException {
    CompletableFuture<Pic> completableFuture = new CompletableFuture<>();

    if (StringUtils.isEmpty(p.getFileName())) throw new IllegalArgumentException("Filename is required");
    if (StringUtils.isEmpty(p.getEntityType())) throw new IllegalArgumentException("Entity type is required");
    if (StringUtils.isEmpty(p.getEntityId())) throw new IllegalArgumentException("Entity Id is required");

    try {
      if (StringUtils.isEmpty(p.getRootFolder())) p.setRootFolder(this.picRootFolder);
      p.setDimensions();
      Executors.newCachedThreadPool().submit(() -> {
        p.setThumb();
        Pic pSaved = repository.save(p);
        completableFuture.complete(pSaved);
      });
    } catch (NullPointerException npe) {
      throw new IllegalArgumentException("invalid pic");
    }

    return completableFuture;
  }

  public String storeFile(MultipartFile file) {
    String originalFileName = StringUtils.cleanPath(
        Objects.requireNonNull(file.getOriginalFilename()));
    String fileName = "";
    try {
      // Check if the file's name contains invalid characters
      if (originalFileName.contains("..")) {
        throw new DocumentStorageException(
            "Sorry! Filename contains invalid path sequence " + originalFileName);
      }
      String fileExtension = "";
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch(Exception e) {
        fileExtension = "";
      }
      UUID uuid = UUID.randomUUID();
      fileName = uuid + fileExtension;
      Path rootFolder = Paths.get(picRootFolder).toAbsolutePath().normalize();
      Path targetLocation = rootFolder.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
    return fileName;
  }

  public Resource loadFileAsResource(String fileName) throws Exception {
    try {
      Path rootFolder = Paths.get(picRootFolder).toAbsolutePath().normalize();
      Path filePath = rootFolder.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName);
    }
  }

  @Override
  public void delete(Pic p) {
    repository.delete(p);
  }

  @Override
  public Optional<Pic> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Page<Pic> findByEntityTypeAndEntityId(String entityType, Integer entityId,
      Pageable pageable) {
    return repository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
  }

  @Override
  public Page<Pic> findByEntityType(String entityType, Pageable pageable) {
    return repository.findByEntityType(entityType, pageable);
  }

  @Override
  public Page<Pic> findByEntityTypeAndTitleContaining(String entityType, String title,
      Pageable pageable) {
    return repository.findByEntityTypeAndTitleContaining(entityType, title, pageable);
  }

  @Override
  public Page<Pic> findByEntityTypeAndEntityIdAndTitleContaining(String entityType,
      Integer entityId, String title, Pageable pageable) {
    return repository
        .findByEntityTypeAndEntityIdAndTitleContaining(entityType, entityId, title, pageable);
  }

  @Override
  public Page<Pic> findByTitleContaining(String title, Pageable pageable) {
    return repository.findByTitleContaining(title, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }
}
