package com.bonsainet.bonsai.model;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import org.apache.commons.io.FileUtils;

@Entity
@Table(name = "pic")
@Data
public class Pic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String entityType; //TODO refactor into enum
  private Integer entityId;
  private String title;
  private Date dateTaken;
  private String filePath;

  //@Transient
  //private byte[] image;

  public Pic() {
  }

  public byte[] getImage(String rootFolder) throws IOException {
    File file = new File(rootFolder, this.filePath);
    byte[] fileContent;
    fileContent = FileUtils.readFileToByteArray(file);

    return fileContent;
  }
}
