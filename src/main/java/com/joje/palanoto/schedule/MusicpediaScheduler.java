package com.joje.palanoto.schedule;

import com.joje.palanoto.model.dto.musicpedia.SongDto;
import com.joje.palanoto.service.ConfigService;
import com.joje.palanoto.service.MusicpediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MusicpediaScheduler {

    private final String INDEX_KEY = "musicpedia_song_id";

    private final ConfigService configService;
    private final MusicpediaService musicpediaService;

//    @Scheduled(fixedDelay = 5000)
    // @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}") // 문자열 milliseconds 사용 시
    public void scheduleFixedDelayTask() throws InterruptedException {

        Object idConfig = configService.get(INDEX_KEY);
//        log.debug("[idConfig]=[{}]", idConfig);

        long idx = idConfig == null ? 2012282 : Long.parseLong((String) idConfig);
        String songId = String.valueOf(idx);

        int count = musicpediaService.countSongById(songId);
//        log.debug("[count]=[{}]", count);

        try {
            if(count == 0){
                musicpediaService.insertSong(songId);
            }
        } catch (MappingException e) {
        } catch (NullPointerException e) {
        }

        configService.set(INDEX_KEY, String.valueOf(++idx));
    }

}
