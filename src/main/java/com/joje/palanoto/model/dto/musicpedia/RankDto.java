package com.joje.palanoto.model.dto.musicpedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankDto {

    private Integer rank;
    private LocalDate rankDate;
    private String songId;

}
