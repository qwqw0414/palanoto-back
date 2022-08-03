package com.joje.palanoto.service.impl;

import com.joje.palanoto.component.HttpRequestComponent;
import com.joje.palanoto.model.dto.musicpedia.RankDto;
import com.joje.palanoto.model.dto.musicpedia.SongDto;
import com.joje.palanoto.model.entity.musicpedia.ArtistEntity;
import com.joje.palanoto.model.entity.musicpedia.RankEntity;
import com.joje.palanoto.model.entity.musicpedia.RankKey;
import com.joje.palanoto.model.entity.musicpedia.SongEntity;
import com.joje.palanoto.repository.account.RankRepository;
import com.joje.palanoto.repository.musicpedia.ArtistRepository;
import com.joje.palanoto.repository.musicpedia.SongRepository;
import com.joje.palanoto.service.MusicpediaService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.lang.annotation.ElementType;
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
    private ModelMapper modelMapper;

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
     * 키워드를 이용해 멜론사이트에서 검색 후 곡아이디 값 리턴
     */
    @Override
    public String getSongIdByKeyword(String keyword) {
        String songId = "";
//        HTML 로드
        Document html = httpRequestComponent.requestHtml(SEARCH_URI + keyword);

//        아이디값 크롤링
        Elements tables = html.select("#frm_songList table");
//        log.debug("[tables]=[{}]", tables);

//        해당 검색 테이블이 없을 경우 우회
        if(tables.html().length() < 1){
            tables = html.select("#frm_searchSong table");
//            log.debug("[tables]=[{}]", tables);
        }

        Elements a = tables.eq(0).select("td.t_left a");
        log.debug("[a]=[{}]", a);

//        href에서 아이디값 조회
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
     * 멜론 차트 TOP100 곡 아이디 조회
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
    @Transactional
    public SongDto insertSong(String songId) {

        Document html = httpRequestComponent.requestHtml(DETAIL_URI + songId);

//        신규 아티스트인 경우 디비에 저장
        ArtistEntity rawArtist = this.getArtistByMelon(html);
        ArtistEntity artist = artistRepository.findById(rawArtist.getArtistId()).orElse(null);
        if(artist == null) {
            artist = artistRepository.save(rawArtist);
        }

//        곡 정보 디비에 저장
        SongEntity song = new SongEntity(
                songId,
                this.getSongNameByMelon(html),
                StringUtil.join(this.getLyricsToMelon(html), "\n"),
                artist
        );
        song = songRepository.save(song);

        return modelMapper.map(song, SongDto.class);
    }

    /**
     * 멜론 TOP 100 차트 곡 조회
     * @return
     */
    @Override
    public List<SongDto> getSongTop100(String date) {
        List<SongEntity> songs = songRepository.findAllRank(date);
        return songs.stream().map(p -> modelMapper.map(p, SongDto.class)).collect(Collectors.toList());
    }

    /**
     * HTML에서 곡 가사 크롤링
     */
    private List<String> getLyricsToMelon(Document doc) {
        String FILTER_STR = "<!-- height:auto; 로 변경시, 확장됨 -->";
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
     * HTML에서 곡 명 조회
     */
    private String getSongNameByMelon(Document doc) {
        Element elmt = doc.selectFirst("div.song_name");
        String songTitle = "";
        if (elmt != null) {
            songTitle = elmt.text().replace("곡명", "").trim();
        }
        return songTitle;
    }

    /**
     * HTML에서 아티스트 정보 조회
     */
    private ArtistEntity getArtistByMelon(Document doc) {
        Element elmt = doc.selectFirst("div.artist");

//		가수명 조회
        String artistName = elmt.selectFirst("span").text().trim();

//		가수 아이디 조회
        String artistId = "";
        String href = elmt.selectFirst("a").attr("href");
        Matcher matcher = Pattern.compile("\\d+").matcher(href);
        if(matcher.find()) {
            artistId = matcher.group();
        }
        return new ArtistEntity(artistId, artistName, null);
    }
}
