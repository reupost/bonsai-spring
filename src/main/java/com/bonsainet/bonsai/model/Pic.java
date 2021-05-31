package com.bonsainet.bonsai.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;

@Entity
@Table(name = "pic")
@Data
public class Pic {
  @Transient
  public static final Integer THUMB_DIM = 200;
  @Transient
  public static final String THUMB_DIR = "thumbs";
  @Transient
  @Setter(AccessLevel.NONE)
  public String workStatus;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String entityType; //TODO refactor into enum
  private Integer entityId;
  private String title;
  private Date dateTaken;
  private String rootFolder;
  private String fileName;

  @Setter(AccessLevel.NONE)
  private Integer dimX;
  @Setter(AccessLevel.NONE)
  private Integer dimY;
  @Setter(AccessLevel.NONE)
  private String fileNameThumb;
  @Setter(AccessLevel.NONE)
  private Integer dimXThumb;
  @Setter(AccessLevel.NONE)
  private Integer dimYThumb;

  //@Transient
  //private byte[] image;

  public Pic() {
  }

  public BufferedImage getImageFromFile(File f) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(f);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return image;
  }

  public void setDimensionsFromImage(BufferedImage image, boolean forThumb) {
    if (forThumb) {
      this.dimXThumb = 0;
      this.dimYThumb = 0;
    } else {
      this.dimX = 0;
      this.dimY = 0;
    }
    if (image != null) {
      if (forThumb) {
        this.dimXThumb = image.getWidth();
        this.dimYThumb = image.getHeight();
      } else {
        this.dimX = image.getWidth();
        this.dimY = image.getHeight();
      }
    }
  }

  public void setDimensions() {
    File f = new File(this.rootFolder, this.fileName);
    setDimensionsFromImage(this.getImageFromFile(f), false);
  }


  public Future<String> setThumb() throws InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    this.dimXThumb = 0;
    this.dimYThumb = 0;
    this.fileNameThumb = "";
    this.workStatus = "Calculating";

    Executors.newCachedThreadPool().submit(() -> {
      BufferedImage thumb;
      try {
        File f = new File(this.rootFolder, this.fileName);
        BufferedImage image = getImageFromFile(f);
        thumb = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
                this.THUMB_DIM, this.THUMB_DIM, Scalr.OP_ANTIALIAS);

        Path pathThumb = Paths.get(this.rootFolder + File.separatorChar + this.THUMB_DIR);
        this.fileNameThumb = this.fileName;
        if (Files.notExists(pathThumb)) {
          Files.createDirectory(pathThumb);
        }
        File fThumb = new File(pathThumb.toString(), this.fileNameThumb);
        ImageIO.write(thumb, "jpg", fThumb);

        this.dimXThumb = thumb.getWidth();
        this.dimYThumb = thumb.getHeight();

      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      //TODO catch errors a bit better?
      completableFuture.complete("Done");
    });

    return completableFuture;
  }

  public BufferedImage getThumbFromImage(BufferedImage image) {
    BufferedImage thumb = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
        this.THUMB_DIM, this.THUMB_DIM, Scalr.OP_ANTIALIAS);
    return thumb;
  }

  public byte[] getImage() throws IOException {
    File file = new File(this.rootFolder, this.fileName);
    byte[] fileContent;
    fileContent = FileUtils.readFileToByteArray(file);

    return fileContent;
  }

  public byte[] getImageThumb() throws IOException {
    File file = new File(this.rootFolder + File.separatorChar + this.THUMB_DIR, this.fileNameThumb);
    byte[] fileContent;
    fileContent = FileUtils.readFileToByteArray(file);

    return fileContent;
  }

}
