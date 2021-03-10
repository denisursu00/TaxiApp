package ro.cloudSoft.cloudDoc.utils;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.common.utils.DateUtils;

public class ProjectUtils {
	
	public static Integer getLastEstimation(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("project cannot be null");
		}
		List<ProjectEstimation> estimations = project.getEstimations();		
		if (CollectionUtils.isEmpty(estimations)) {			
			return null;
		}
		Integer lastEstimation = null;
		for (ProjectEstimation estimation : estimations) {
			if (estimation.getEndDate() == null) {
				if (lastEstimation != null) {
					throw new IllegalStateException("multiple estimations found");
				}
				lastEstimation = estimation.getEstimationInPercent();
			}
		}
		return lastEstimation;
	}

	public static long getNrZilePerioadaScursa(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("project cannot be null");
		}
		Date dataCurenta = new Date();		
		Date dataInceputProiect = project.getStartDate();
		
		return DateUtils.pozitiveNumberDaysBetween(dataCurenta, dataInceputProiect);
		
	}

	public static long getNrZilePerioadaRamasa(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("project cannot be null");
		}
		
		Date dataCurenta = new Date();		
		Date dataSfarsitProiect = project.getEndDate();
		return DateUtils.pozitiveNumberDaysBetween(dataSfarsitProiect, dataCurenta);
	}

	public static int getNrZileProiect(Project project) {
		// TODO Auto-generated method stub
		return 0;
	}


}
