package com.joje.palanoto.controller;

import com.joje.palanoto.component.NluComponent;
import com.joje.palanoto.model.dto.musicpedia.RankDto;
import com.joje.palanoto.model.dto.musicpedia.SongDto;
import com.joje.palanoto.model.vo.ResultVo;
import com.joje.palanoto.service.MusicpediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/palanoto/musicpedia")
public class MusicpediaController {

    private final MusicpediaService musicpediaService;

    @GetMapping("/chart/{date}")
    public ResponseEntity<ResultVo> getChartTop100(@PathVariable("date") String date) throws Exception {

//        String date = LocalDate.now().toString();

        List<SongDto> songs = musicpediaService.getSongTop100(date);

        ResultVo resultVo = new ResultVo();
        resultVo.put("songs", songs);

        return new ResponseEntity<>(resultVo, HttpStatus.OK);
    }

    @PostMapping("/chart")
    public ResponseEntity<ResultVo> insertChartTop100() throws Exception {

        List<RankDto> ranks = musicpediaService.getRanksFromChart();
        log.debug("[ranks]=[{}]", ranks);

//        Song 정보 디비 추가
        int updateCount = musicpediaService.insertSong(ranks);

//        랭킹 업데이트
//        int rankUpdated = musicpediaService.updateRank

        ResultVo resultVo = new ResultVo();
        resultVo.put("updated", updateCount);

        return new ResponseEntity<>(resultVo, HttpStatus.OK);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<ResultVo> searchSong(@PathVariable("keyword") String keyword) throws Exception {
//        곡 아이디 조회
        String songId = musicpediaService.getSongIdByKeyword(keyword);
        log.debug("[songId]=[{}]", songId);

//        곡 정보 조회
        SongDto song = StringUtils.hasText(songId) ? musicpediaService.getSongById(songId) : new SongDto();

//        결과 셋
        ResultVo resultVo = new ResultVo();
        resultVo.put("song", song);
        
        return new ResponseEntity<>(resultVo, HttpStatus.OK);
    }

}
