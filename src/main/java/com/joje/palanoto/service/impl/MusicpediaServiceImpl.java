package com.joje.palanoto.service.impl;

import com.joje.palanoto.common.constants.PoSType;
import com.joje.palanoto.component.HttpRequestComponent;
import com.joje.palanoto.component.NluComponent;
import com.joje.palanoto.model.dto.musicpedia.RankDto;
import com.joje.palanoto.model.dto.musicpedia.SongDto;
import com.joje.palanoto.model.entity.musicpedia.*;
import com.joje.palanoto.repository.musicpedia.RankRepository;
import com.joje.palanoto.repository.musicpedia.ArtistRepository;
import com.joje.palanoto.repository.musicpedia.SongRepository;
import com.joje.palanoto.repository.musicpedia.WordRepository;
import com.joje.palanoto.service.MusicpediaService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("MusicpediaService")
public class MusicpediaServiceImpl implements MusicpediaService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private RankRepository rankRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NluComponent nluComponent;

    @Autowired
    private HttpRequestComponent httpRequestComponent;

    private final String CHART_URI;
    private final String SEARCH_URI;
    private final String DETAIL_URI;

    public MusicpediaServiceImpl(@Value("${palanoto.musicpedia.uri.chart}") String chart,
                                 @Value("${palanoto.musicpedia.uri.search}") String search,
                                 @Value("${palanoto.musicpedia.uri.detail}") String detail) {
        this.CHART_URI = chart;
        this.SEARCH_URI = search;
        this.DETAIL_URI = detail;
    }

    /**
     * ???????????? ????????? ????????????????????? ?????? ??? ???????????? ??? ??????
     */
    @Override
    public String getSongIdByKeyword(String keyword) {
        String songId = "";
//        HTML ??????
        Document html = httpRequestComponent.requestHtml(SEARCH_URI + keyword);

//        ???????????? ?????????
        Elements tables = html.select("#frm_songList table");
//        log.debug("[tables]=[{}]", tables);

//        ?????? ?????? ???????????? ?????? ?????? ??????
        if(tables.html().length() < 1){
            tables = html.select("#frm_searchSong table");
//            log.debug("[tables]=[{}]", tables);
        }

        Elements a = tables.eq(0).select("td.t_left a");
//        log.debug("[a]=[{}]", a);

//        href?????? ???????????? ??????
        if(a.html().length() > 0) {
            Matcher matcher = Pattern.compile("\\d+").matcher(a.attr("href"));
            if(matcher.find()){
                songId = matcher.group();
            }
        }

        return songId;
    }

    @Override
    public SongDto getSongById(String songId) {
        SongEntity song = songRepository.findById(songId).orElse(null);
//        log.debug("[song]=[{}]", song);

        SongDto songDto;
        if (song == null) {
            songDto = this.insertSong(songId);
        } else {
            songDto = modelMapper.map(song, SongDto.class);
        }

        return songDto;
    }

    @Override
    public int insertSong(List<RankDto> ranks) {
        int updated = 0;

        List<RankEntity> rankEntityList = new ArrayList<>();

        for (RankDto rankDto : ranks) {
            String songId = rankDto.getSongId();
            if (songRepository.countBySongId(songId) == 0) {
                this.insertSong(songId);
                updated++;
            }

            RankKey rankId = new RankKey(rankDto.getRank(), rankDto.getRankDate());
            RankEntity rank = new RankEntity(rankId, songId);
            rankEntityList.add(rank);
        }

        rankRepository.saveAll(rankEntityList);
        log.debug("[rankEntityList]=[{}]", rankEntityList);
        return updated;
    }

    /**
     * ?????? ?????? TOP100 ??? ????????? ??????
     */
    @Override
    public List<RankDto> getRanksFromChart() {
        List<RankDto> ranks = new ArrayList<>();
        Document html = httpRequestComponent.requestHtml(CHART_URI);

        int rank = 0;
        LocalDate rankDate = LocalDate.now();
//        log.debug("[rankDate]=[{}]", rankDate);

        Elements elmts = html.select("tbody tr");
        for(Element elmt : elmts) {
            String songId = elmt.attr("data-song-no");
//            log.debug("[songId]=[{}]", songId);

            RankDto rankDto = new RankDto(++rank, rankDate, songId);
//            log.debug("[rankDto]=[{}]", rankDto);
            ranks.add(rankDto);
        }
        return ranks;
    }

    @Override
    public int countSongById(String songId) {
        return songRepository.countBySongId(songId);
    }

    @Override
    @Transactional
    public SongDto insertSong(String songId) throws MappingException {

        Document html = httpRequestComponent.requestHtml(DETAIL_URI + songId);

//        ?????? ??????????????? ?????? ????????? ??????
        ArtistEntity rawArtist = this.getArtistByMelon(html);
        ArtistEntity artist = artistRepository.findById(rawArtist.getArtistId()).orElse(null);
        if(artist == null) {
            artist = artistRepository.save(rawArtist);
        }

//        ??? ?????? ????????? ??????
        SongEntity song = new SongEntity(
                songId,
                this.getSongNameByMelon(html),
                StringUtil.join(this.getLyricsToMelon(html), "\n"),
                artist
        );
        song = songRepository.save(song);

//        ?????? ??????
        List<String> rawWords = nluComponent.get(song.getLyrics(), PoSType.NNG);

        for(String w : rawWords){
            WordEntity wordEntity = wordRepository.findById(w).orElse(null);
            if (wordEntity == null) {
                wordEntity = new WordEntity(w, 1L, null);
            } else {
                wordEntity.setAmount(wordEntity.getAmount() + 1);
            }
            wordRepository.save(wordEntity);
        }

        log.info("Musicpedia Add Song [{} - {}]", song.getArtist().getArtistName(), song.getSongName());
        return modelMapper.map(song, SongDto.class);
    }

    /**
     * ?????? TOP 100 ?????? ??? ??????
     * @return
     */
    @Override
    public List<SongDto> getSongTop100(String date) {
        List<SongEntity> songs = songRepository.findAllRank(date);
        return songs.stream().map(p -> modelMapper.map(p, SongDto.class)).collect(Collectors.toList());
    }

    /**
     * HTML?????? ??? ?????? ?????????
     */
    private List<String> getLyricsToMelon(Document doc) {
        String FILTER_STR = "<!-- height:auto; ??? ?????????, ????????? -->";
        List<String> lyrics = new ArrayList<>();
        Element elmt = doc.selectFirst("div.lyric");
        if (elmt != null) {
            String html = elmt.html().replace(FILTER_STR, "");
            String[] line = html.split("<br>");
            for (String l : line) {
                lyrics.add(l.trim());
            }
        }
        return lyrics;
    }

    /**
     * HTML?????? ??? ??? ??????
     */
    private String getSongNameByMelon(Document doc) {
        Element elmt = doc.selectFirst("div.song_name");
        String songTitle = "";
        if (elmt != null) {
            songTitle = elmt.text().replace("??????", "").trim();
        }
        return songTitle;
    }

    /**
     * HTML?????? ???????????? ?????? ??????
     */
    private ArtistEntity getArtistByMelon(Document doc) {
        Element elmt = doc.selectFirst("div.artist");

//		????????? ??????
        String artistName = elmt.selectFirst("span").text().trim();

//		?????? ????????? ??????
        String artistId = "";
        String href = elmt.selectFirst("a").attr("href");
        Matcher matcher = Pattern.compile("\\d+").matcher(href);
        if(matcher.find()) {
            artistId = matcher.group();
        }
        return new ArtistEntity(artistId, artistName, null);
    }
}
