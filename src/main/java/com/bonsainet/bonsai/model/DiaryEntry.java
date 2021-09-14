package com.bonsainet.bonsai.model;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "diary_entry")
@Data
@Slf4j
public class DiaryEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "bonsaiId")
  private Bonsai bonsai;

  private String entryText;

  // @Temporal(TemporalType.TIMESTAMP) ??
  @DateTimeFormat
  private LocalDateTime entryDate;

  public DiaryEntry() {
  }

  public Bonsai getBonsai() {
    return bonsai;
  }
}
