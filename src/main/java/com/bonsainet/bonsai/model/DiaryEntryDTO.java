package com.bonsainet.bonsai.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DiaryEntryDTO {

  private Integer id;
  private Integer bonsaiId;
  private String entryText;
  @DateTimeFormat
  private LocalDateTime entryDate;

  public DiaryEntryDTO() {
  }
}
