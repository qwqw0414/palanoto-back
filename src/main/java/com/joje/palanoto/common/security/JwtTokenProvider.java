package com.joje.palanoto.common.security;

import com.joje.palanoto.exception.ExpiredTokenException;
import com.joje.palanoto.exception.InvalidTokenException;
import com.joje.palanoto.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    /**
     * 빈이 생성이 되고 의존성 주입이 되고 난 후에 주입받은 secret 값을 Base64 Decode 해서 key 변수에 할당
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 createToken 메소드 추가
    public String createToken(Authentication authentication) {
//         권한들
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
//        유효 기간
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * token에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드 생성
     */
    public Authentication getAuthentication(String token) {
        // token을 활용하여 Claims 생성
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // claims과 authorities 정보를 활용해 User (org.springframework.security.core.userdetails.User) 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);

        // Authentication 객체를 리턴
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            // 토큰을 파싱해보고 발생하는 exception들을 캐치, 문제가 생기면 false, 정상이면 true를 return
//            log.debug("[token]=[{}]", token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("JWT 토큰이 잘못되었습니다.");
        }
    }

    /**
     * request header에서 토큰 정보를 꺼내오는 메소드
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public long getUserNo(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if(header != null) {
            String token = header.substring(7);
            this.validateToken(token);
            return Long.parseLong(this.getAuthentication(token).getName());
        }
        throw new RuntimeException("헤더에 JWT 정보가 없습니다.");
    }

    public String getUserId(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if(header != null) {
            String token = header.substring(7);
            this.validateToken(token);
            return this.getAuthentication(token).getName();
        }
        throw new RuntimeException("헤더에 JWT 정보가 없습니다.");
    }
}
