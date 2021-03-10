package ro.cloudSoft.cloudDoc.web.filters;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import ro.cloudSoft.cloudDoc.core.ApplicationVersionHolder;

public class AppVersionFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		String currentVersion = ApplicationVersionHolder.getVersion();
		if (StringUtils.isNotBlank(currentVersion)) {
			response.getHttpHeaders().add("X-BackEnd-App-Version", currentVersion);
		}
		return response;
	}
}
