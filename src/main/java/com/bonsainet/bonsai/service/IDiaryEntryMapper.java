package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.model.DiaryEntryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface IDiaryEntryMapper {
  //TODO is there a way of using the @Mapping to convert instead of doing it manually?
  DiaryEntryDTO toDTO(DiaryEntry diaryEntry);

  DiaryEntry toDiaryEntry(DiaryEntryDTO diaryEntryDto);
}

