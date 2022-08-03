package com.joje.palanoto.repository.musicpedia;

import com.joje.palanoto.model.entity.musicpedia.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongRepository extends JpaRepository<SongEntity, String> {

    int countBySongId(String songId);

    @Query(value = "SELECT MS.*, MR.RANK FROM TB_MUSICPEDIA_SONG MS JOIN (SELECT SONG_ID, RANK FROM TB_MUSICPEDIA_RANK WHERE RANK_DATE = ?) MR ON MS.SONG_ID = MR.SONG_ID ORDER BY RANK;", nativeQuery = true)
    List<SongEntity> findAllRank(String date);
}