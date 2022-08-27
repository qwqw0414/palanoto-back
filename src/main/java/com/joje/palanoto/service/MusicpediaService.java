package com.joje.palanoto.service;

import com.joje.palanoto.model.dto.musicpedia.RankDto;
import com.joje.palanoto.model.dto.musicpedia.SongDto;

import java.util.List;

public interface MusicpediaService {

    String getSongIdByKeyword(String keyword);

    SongDto getSongById(String songId);

    SongDto insertSong(String songId);

    List<SongDto> getSongTop100(String date);

    int insertSong(List<RankDto> songId);

    List<RankDto> getRanksFromChart();

    int countSongById(String songId);
}
