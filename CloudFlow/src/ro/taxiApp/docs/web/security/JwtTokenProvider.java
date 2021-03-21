package ro.taxiApp.docs.web.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import ro.taxiApp.docs.utils.log.LogHelper;

public class JwtTokenProvider {
	
	private static final String CLAIM_NAME_AUTHORITIES = "authorities";
	private static final String CLAIM_NAME_USER_ID = "user_id";
	
	private static final LogHelper logger = LogHelper.getInstance(JwtTokenProvider.class);
	
	private JwtSettings jwtSettings;
	
    private Key key;
    
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSettings.getSecretKey().getBytes());
    }

    public String createToken(UserWithAccountAuthentication authentication, boolean rememberMe) {
    	
    	String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    	
        Calendar validityAsCalendar = Calendar.getInstance();
        if (rememberMe) {        	
        	validityAsCalendar.add(Calendar.DATE, this.jwtSettings.getTokenValidityInDaysForRememberMe());
        } else {
        	validityAsCalendar.add(Calendar.DATE, this.jwtSettings.getTokenValidityInDays());
        }
        validityAsCalendar.set(Calendar.HOUR_OF_DAY, 23);
        validityAsCalendar.set(Calendar.MINUTE, 59);
        validityAsCalendar.set(Calendar.SECOND, 59);
        
        return Jwts.builder()
        		.setSubject(authentication.getName())
        		.claim(CLAIM_NAME_USER_ID, authentication.getUserId())
        		.claim(CLAIM_NAME_AUTHORITIES, authorities)
        		.signWith(key, SignatureAlgorithm.HS512)
        		.setExpiration(validityAsCalendar.getTime())
        		.compact();
    }

    public UserWithAccountAuthentication getAuthentication(String token) {
        
    	Claims claims = Jwts.parser()
    			.setSigningKey(key)
    			.parseClaimsJws(token)
    			.getBody();
    	
    	Long userId = claims.get(CLAIM_NAME_USER_ID, Long.class);    	
    	Collection<GrantedAuthority> authorities = Arrays.stream(claims.get(CLAIM_NAME_AUTHORITIES).toString().split(","))
        		.map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    	
        return new UserWithAccountAuthentication(userId, claims.getSubject(), authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
        }
        return false;
    }
    
	public void setJwtSettings(JwtSettings jwtSettings) {
		this.jwtSettings = jwtSettings;
	}
}
