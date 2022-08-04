package com.joje.palanoto.model.dto.musicpedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {

    private String word;
    private Long amount;
    private LocalDate updateDate;

}
