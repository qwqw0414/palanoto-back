package com.joje.palanoto.model.entity.musicpedia;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TB_MUSICPEDIA_RANK")
public class RankEntity {

    @EmbeddedId
    private RankKey rankId;

    private String songId;

}
