package main_service.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import main_service.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Extract username from token
     *
     * @param token token
     * @return username
     */
    public int extractUserId(String token) {
        return Integer.parseInt(extractClaim(token, Claims::getSubject));
    }

    /**
     * Token's generation
     *
     * @param userDetails user's data
     * @return token
     */
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getEmail());
        return generateToken(claims, userDetails);
    }

    /**
     * Token's validation
     *
     * @param token       token
     * @param userDetails user's data
     * @return true, if token is valid
     */
    public boolean isTokenValid(String token, User userDetails) {
        final int userId = extractUserId(token);
        return (userId == userDetails.getId()) && !isTokenExpired(token);
    }

    /**
     * extract data from token
     *
     * @param token           token
     * @param claimsResolvers extract function
     * @param <T>             data type
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Generate token
     *
     * @param extraClaims extra data
     * @param userDetails user details
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, User userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(String.valueOf(userDetails.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Checking the token for expiration
     *
     * @param token token
     * @return true, if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * extract token expiration date
     *
     * @param token jwt token
     * @return expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * extract all data from token
     *
     * @param token token
     * @return data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get signing key
     *
     * @return key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}