package ro.taxiApp.docs.web.filters;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import ro.taxiApp.docs.config.environment.AppEnvironment;
import ro.taxiApp.docs.config.environment.AppEnvironmentConfig;

public class JerseyCORSFilter implements ContainerResponseFilter {
	
	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		if (AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.DEVELOPMENT)) {
			if (!response.getHttpHeaders().containsKey("Access-Control-Allow-Origin")) {
				response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
			}
			if (!response.getHttpHeaders().containsKey("Access-Control-Allow-Headers")) {
				response.getHttpHeaders().add("Access-Control-Allow-Headers", "origin,content-type,accept,authorization");
			}
			if (!response.getHttpHeaders().containsKey("Access-Control-Allow-Methods")) {
				response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
			}
			if (!response.getHttpHeaders().containsKey("Access-Control-Allow-Credentials")) {
				response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
			}
		}
        return response;
	}
}
