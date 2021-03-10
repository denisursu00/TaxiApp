package ro.cloudSoft.cloudDoc.services.project;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ProjectRealizationDegreeProvider {
	
	private static Map<ProjectRealizationDegree, Pair<BigDecimal, BigDecimal>> decisionTable;

	static {
		
		decisionTable = new HashMap<>();
		
		Pair<BigDecimal, BigDecimal> rangeOfProjectRealizationDegreeNesatisfacator = new ImmutablePair<BigDecimal, BigDecimal>(null, new BigDecimal(0.25));
		decisionTable.put(ProjectRealizationDegree.NESATISFACATOR, rangeOfProjectRealizationDegreeNesatisfacator);
		
		Pair<BigDecimal, BigDecimal> rangeOfProjectRealizationDegreeSatisfacator = new ImmutablePair<BigDecimal, BigDecimal>(new BigDecimal(0.25), new BigDecimal(0.5));
		decisionTable.put(ProjectRealizationDegree.SATISFACATOR, rangeOfProjectRealizationDegreeSatisfacator);
		
		Pair<BigDecimal, BigDecimal> rangeOfProjectRealizationDegreeBine = new ImmutablePair<BigDecimal, BigDecimal>(new BigDecimal(0.5), new BigDecimal(0.75));
		decisionTable.put(ProjectRealizationDegree.BINE, rangeOfProjectRealizationDegreeBine);
		
		Pair<BigDecimal, BigDecimal> rangeOfProjectRealizationDegreeFoarteBine = new ImmutablePair<BigDecimal, BigDecimal>(new BigDecimal(0.75), new BigDecimal(1));
		decisionTable.put(ProjectRealizationDegree.FOARTE_BINE, rangeOfProjectRealizationDegreeFoarteBine);
	}
	
	public static ProjectRealizationDegree getProjectRealizationDegree(BigDecimal realizationDegree) {
		
		for (Entry<ProjectRealizationDegree, Pair<BigDecimal, BigDecimal>> entry : decisionTable.entrySet()) {
			if (entry.getValue().getLeft() == null) {
				if (entry.getValue().getRight().compareTo(realizationDegree) >= 0) {
					return entry.getKey();
				}
			} else if (entry.getValue().getLeft().compareTo(realizationDegree) < 0 && entry.getValue().getRight().compareTo(realizationDegree) >= 0) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("Parameter value is not in [-inf, 0] interval.");
	}
	
}