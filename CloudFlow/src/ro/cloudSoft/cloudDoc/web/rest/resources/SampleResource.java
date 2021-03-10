package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.dto.SampleDTO;
import ro.cloudSoft.cloudDoc.dto.SampleDTO.DetailDTO;

@Component
@Path("/sample")
public class SampleResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSamples() {
		
		List<SampleDTO> list = new ArrayList<SampleDTO>();
		
		SampleDTO s = new SampleDTO();
		s.setName("Jersey Test");
		s.setAge(15);
		s.setBirthDate(new Date());
		
		DetailDTO ds = new DetailDTO();
		ds.setProperty1("aaa");
		ds.setProperty2(true);
		ds.setProperty3(BigDecimal.valueOf(23.45));		
		s.setDetail(ds);
		List<DetailDTO> details = new ArrayList<SampleDTO.DetailDTO>();
		details.add(ds);
		s.setDetails(details);		
		list.add(s);
		
		s = new SampleDTO();
		s.setName("Jersey Test2");
		s.setAge(16);
		//s.setBirthDate(new Date());
		//s.setDetail(ds);
		//s.setDetails(details);
		list.add(s);
		
		return Response.status(Status.BAD_REQUEST)
				.entity(list)
				.build();
	}
	
	@GET
	@Path("{sampleId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSampleById(@PathParam("sampleId") Integer sampleId) {
		
		System.out.println("Sample id - path: " + sampleId);
		
		SampleDTO s = new SampleDTO();
		s.setName("Jersey Test Simple");
		s.setAge(15);
		s.setBirthDate(new Date());
		
		DetailDTO ds = new DetailDTO();
		ds.setProperty1("bbb");
		ds.setProperty2(true);
		ds.setProperty3(BigDecimal.valueOf(23.45));		
		s.setDetail(ds);
		List<DetailDTO> details = new ArrayList<SampleDTO.DetailDTO>();
		details.add(ds);
		s.setDetails(details);		
		
		return Response.status(200)
				.entity(s)
				.build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSampleById(SampleDTO sample) {
		if (sample != null) {
			System.out.println("name: " + sample.getName());
			System.out.println("birthDate: " + sample.getBirthDate());
			if (sample.getDetail() != null) {
				System.out.println("detail prop1: " + sample.getDetail().getProperty1());
			} else {
				System.out.println("detail is null");
			}
		} else {
			System.out.println("sample este null");
		}	
		
		return Response.status(200).build();
	}
	
	
}


