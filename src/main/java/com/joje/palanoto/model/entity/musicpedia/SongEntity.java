package com.joje.palanoto.model.entity.musicpedia;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "TB_MUSICPEDIA_SONG")
public class SongEntity {

    @Id
    private String songId;

    private String songName;
    private String lyrics;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artistId")
    private ArtistEntity artist;

}
