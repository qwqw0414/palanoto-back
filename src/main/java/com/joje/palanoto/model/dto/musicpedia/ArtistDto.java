package com.joje.palanoto.model.dto.musicpedia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {

    private String artistId;
    private String artistName;

}
