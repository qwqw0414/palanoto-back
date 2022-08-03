package com.joje.palanoto.model.entity.musicpedia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TB_MUSICPEDIA_ARTIST")
@ToString
public class ArtistEntity {

    @Id
    private String artistId;

    private String artistName;

    @JsonBackReference
    @OneToMany
    @JoinColumn(name = "artist")
    private List<SongEntity> songs;

}
