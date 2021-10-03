package com.bonsainet.bonsai.model;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

@Entity
@Table(name = "pic")
@Data
@Slf4j
public class Pic {
  @Transient
  public static final Integer THUMB_DIM = 200;
  @Transient
  public static final String THUMB_DIR = "thumbs";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  private EntityType entityType;

  private Integer entityId;

  private String title;

  @Enumerated(EnumType.STRING)
  private ViewAngles view;

  private LocalDate dateTaken;
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
  @Setter(AccessLevel.NONE)
  private String imageHash;

  public Pic() {
  }

  public String getMD5HashFromFile(File f) throws IOException {
    HashCode hash = com.google.common.io.Files.hash(f, Hashing.md5());
    return hash.toString().toUpperCase();
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

  public void setDimensions() {
    File f = new File(this.rootFolder, this.fileName);
    BufferedImage image = this.getImageFromFile(f);
    this.dimX = 0;
    this.dimY = 0;
    if (image != null) {
      this.dimX = image.getWidth();
      this.dimY = image.getHeight();
    }
  }

  public void setThumb() {
    BufferedImage thumb;
    try {
      File f = new File(this.rootFolder, this.fileName);
      BufferedImage image = getImageFromFile(f);
      String hash = getMD5HashFromFile(f);
      if (!StringUtils.equals(hash, this.imageHash)) { //image has changed
        this.imageHash = hash;

        thumb = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
            THUMB_DIM, THUMB_DIM, Scalr.OP_ANTIALIAS);

        Path pathThumb = Paths.get(this.rootFolder + File.separatorChar + THUMB_DIR);
        this.fileNameThumb = this.fileName;
        if (Files.notExists(pathThumb)) {
          Files.createDirectory(pathThumb);
        } else {
          setFolderPermission(pathThumb, true);
        }
        File fThumb = new File(pathThumb.toString(), this.fileNameThumb);
        ImageIO.write(thumb, "jpg", fThumb);

        this.dimXThumb = thumb.getWidth();
        this.dimYThumb = thumb.getHeight();
      }
    } catch (Exception ioe) {
      this.dimXThumb = 0;
      this.dimYThumb = 0;
      this.fileNameThumb = "";
      this.imageHash = "";
      ioe.printStackTrace();
    }
  }

  @JsonIgnore
  public BufferedImage getThumbFromImage(BufferedImage image) {
    return Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
        THUMB_DIM, THUMB_DIM, Scalr.OP_ANTIALIAS);
  }

  @JsonIgnore
  public byte[] getImage() throws IOException {
    File file = new File(this.rootFolder, this.fileName);
    byte[] fileContent;
    fileContent = FileUtils.readFileToByteArray(file);

    return fileContent;
  }

  @JsonIgnore
  public byte[] getImageThumb() throws IOException {
    File file = new File(Objects.toString(this.rootFolder,"") + File.separatorChar + THUMB_DIR,
        Objects.toString(this.fileNameThumb, ""));
    byte[] fileContent;
    fileContent = FileUtils.readFileToByteArray(file);

    return fileContent;
  }

  @JsonIgnore
  public void deleteImageIfExists() {
    File file = new File(Objects.toString(this.rootFolder,"") + File.separatorChar + THUMB_DIR,
          Objects.toString(this.fileNameThumb, ""));
    file.delete();
    file = new File(Objects.toString(this.rootFolder,""), Objects.toString(this.fileName, ""));
    file.delete();
  }

  private void setFolderPermission(Path path, boolean writeable) {
    File folder = new File(String.valueOf(path));
    boolean success = folder.setWritable(false, false);
    if (!success) {
      //ok, probably windows...
      try {
        AclFileAttributeView view = Files.getFileAttributeView(
            path, AclFileAttributeView.class);
        List<AclEntry> acls = view.getAcl();
        for (int i = 0; i < acls.size(); i++) {
          UserPrincipal principal = acls.get(i).principal();
          Set<AclEntryPermission> permissions = acls.get(i).permissions();
          if (writeable) {
            permissions.add(AclEntryPermission.WRITE_DATA);
          } else {
            permissions.remove(AclEntryPermission.WRITE_DATA);
          }

          // create a new ACL entry
          AclEntry entry = AclEntry.newBuilder()
              .setType(AclEntryType.ALLOW)
              .setPrincipal(principal)
              .setPermissions(permissions)
              .build();

          // replace the ACL entry for authenticated users
          acls.set(i, entry);
        }
        // set the updated list of ACLs
        view.setAcl(acls);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  /**
   * @param fromPic object to fill-in values from
   * @throws IllegalAccessException
   *
   * Note that if a new filename is set, this is not used to generate a new thumbnail or dimensions -
   * these must be set explicitly from the fromPic object.
   */
  public void supplementWith(Pic fromPic) throws IllegalAccessException {
    Class<? extends Pic> clazz = fromPic.getClass();

    for (Field field : clazz.getDeclaredFields()) {

      Object myValue = field.get(this);
      Object fromValue = field.get(fromPic);

      char first = Character.toUpperCase(field.getName().charAt(0));
      String capitalized = first + field.getName().substring(1);
      try {
        //is this a gettable property? If not, skip (via exception that is thrown)
        clazz.getDeclaredMethod("get" + capitalized);

        if (fromValue != null) {
          field.set(this, (myValue != null) ? myValue : fromValue);
        }
      } catch (NoSuchMethodException | SecurityException e) {
        //ignore and continue with next field
      }
    }
  }
}
