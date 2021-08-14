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
    private String secretKey;

    @Value("${spring.jwt.subject}")
    private String subject;

    public static final long JWT_TOKEN_EXPIRATION = 10 * 60 * 60;

    /**
     * 토큰 발급
     * @param platformId String 플랫폼 고유 id
     * @return String
     */
    public String generateToken(long userId, int platformId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", userId);
        claims.put("pid", platformId);

        return Jwts.builder().setHeaderParam("typ", "JWT")
                .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
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
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * 토큰 만료일 얻기
     * @param token String
     * @return Date
     */
    public Date getExpirationDateByToken(String token){
        return getClaimByToken(token, Claims::getExpiration);
    }

    public Long getUserIdByToken(String token){
        return 1L;
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
