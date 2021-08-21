package kr.itkoo.voting.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY;

    @Value("${spring.jwt.subject}")
    private String JWT_SUBJECT;

    @Value("${spring.jwt.claims.user-id}")
    private String USER_ID_CLAIM;

    @Value("${spring.jwt.claims.platform-id}")
    private String PLATFORM_ID_CLAIM;

    public static final long JWT_TOKEN_EXPIRATION = 10 * 60 * 60;

    /**
     * 토큰 발급
     * @param platformId String 플랫폼 고유 id
     * @return String
     */
    public String generateToken(long userId, int platformId){
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_CLAIM, userId);
        claims.put(PLATFORM_ID_CLAIM, platformId);

        return Jwts.builder().setHeaderParam("typ", "JWT")
                .setClaims(claims).setSubject(JWT_SUBJECT).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
    }

    /**
     * 토큰에 저장된 정보(claim) 얻기
     * @param token String
     * @param claimsResolver Function<
     * @return T
     */
    public <T> T getClaimByToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsByToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 토큰에 저장된 정보(claim) 얻기
     * @param token String
     * @return Claims
     */
    private Claims getAllClaimsByToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * 토큰 만료일 얻기
     * @param token String
     * @return Date
     */
    public Date getExpirationDateByToken(String token){
        return getClaimByToken(token, Claims::getExpiration);
    }

    /**
     * 토큰값에서 유저 아이디를 가져옴
     * @param token String
     * @return Long
     */
    public Long getUserIdByToken(String token){
        Claims claims = getAllClaimsByToken(token);
        return claims.get(USER_ID_CLAIM, Long.class);
    }

    /**
     * 토큰값에서 플랫폼 아이디를 가져옴
     * @param token String
     * @return Long
     */
    public Long getPlatformIdByToken(String token){
        Claims claims = getAllClaimsByToken(token);
        return claims.get(PLATFORM_ID_CLAIM, Long.class);
    }

    /**
     * 토큰 만료여부 판별
     * @param token String
     * @return boolean
     */
    public Boolean isTokenExpired(String token){
        final Date expirationDate = getExpirationDateByToken(token);
        return expirationDate.before(new Date());
    }
}
