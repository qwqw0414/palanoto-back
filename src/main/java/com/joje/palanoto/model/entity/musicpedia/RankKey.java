package com.joje.palanoto.model.entity.musicpedia;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RankKey implements Serializable {
    private Integer rank;
    private LocalDate rankDate;
}
