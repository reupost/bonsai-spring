package com.bonsainet.bonsai.model;

import java.time.LocalDate;
import javax.persistence.Transient;
import lombok.Data;

@Data
public class PicDTO {

  @Transient
  public static final Integer THUMB_DIM = 200;
  @Transient
  public static final String THUMB_DIR = "thumbs";

  private Integer id;

  private EntityType entityType;
  private Integer entityId;

  private String title;
  private ViewAngles view;

  private LocalDate dateTaken;
  private String rootFolder;
  private String fileName;

  private Integer dimX;
  private Integer dimY;
  private String fileNameThumb;
  private Integer dimXThumb;
  private Integer dimYThumb;
  private String imageHash;

  public PicDTO() {
  }

  public PicDTO(Integer picId) {
    this.id = picId;
  }
}
