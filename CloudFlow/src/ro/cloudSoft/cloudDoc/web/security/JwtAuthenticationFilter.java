package ro.cloudSoft.cloudDoc.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;


public class JwtAuthenticationFilter extends GenericFilterBean {
	
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtTokenProvider jwtTokenProvider;
    private SecurityManagerFactory securityManagerFactory;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	
    	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    	HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    	
        String token = resolveToken(httpServletRequest);
        
        if (StringUtils.hasText(token)) {
    		try {
        		
    			boolean tokenValid = jwtTokenProvider.validateToken(token);
        		if (!tokenValid) {
        			throw new JwtInvalidTokenException("Invalid jwt");
        		}
        		
        		UserWithAccountAuthentication authentication = this.jwtTokenProvider.getAuthentication(token);
        		
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                
                SecurityManager securityManager = securityManagerFactory.getSecurityManager(authentication.getUserId());
        		SecurityManagerHolder.setSecurityManager(securityManager);
        		
        	} catch (AuthenticationException authException) {
        		SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, authException);
                return;
			}
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    public void setJwtAuthenticationEntryPoint(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}
    
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
    
    public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}
    
    private static class JwtInvalidTokenException extends AuthenticationException {
    	
    	private static final long serialVersionUID = 1L;
    	
    	public JwtInvalidTokenException(String msg) {
    		super(msg);
    	}
    }
}
