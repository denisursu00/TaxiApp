package ro.cloudSoft.common.utils.spring;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Permite request-urilor non-Spring (care nu sunt preluate de servlet-ul Spring) sa beneficieze de obtinerea limbii curente.
 * Astfel, se poate folosi, de exemplu, tag-ul spring:message intr-un JSP care nu a fost afisat cu Spring.
 * 
 * 
 */
public class LocaleResolverInNonSpringRequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		LocaleResolver localeResolver = SpringUtils.getBean(LocaleResolver.class);
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
		
		chain.doFilter(request, response);
	}
}