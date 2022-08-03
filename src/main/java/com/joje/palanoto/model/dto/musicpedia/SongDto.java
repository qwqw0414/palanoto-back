package com.joje.palanoto.model.dto.musicpedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {

    private String songId;
    private String songName;
    private String lyrics;

    private ArtistDto artist;

}
