package com.joje.palanoto.model.entity.musicpedia;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "TB_MUSICPEDIA_WORD")
public class WordEntity {

    @Id
    private String word;

    private Long amount;
    private LocalDate updateDate;

}
