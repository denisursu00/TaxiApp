package ro.taxiApp.docs.utils.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class QueryUtils {

	public static List executeQueryWithAppropiateParameters(HibernateTemplate hibernateTemplate, String query, Object[] queryParams) {

		if ((queryParams == null) || (queryParams.length == 0)) {
			return hibernateTemplate.find(query);
		} else if (queryParams.length == 1) {
			return hibernateTemplate.find(query, queryParams[0]);
		} else {

			return hibernateTemplate.find(query, queryParams);
		}

	}
	
	public static String joinAsINValues(List<Long> inParameters) {
		String separator = ",";
		StringBuilder formatedString = new StringBuilder("("); 
		boolean isFirstParameter = true;
		for (Object value : inParameters)  {
			if (!isFirstParameter) {
				formatedString.append(separator);
			}
			formatedString.append(value.toString());
			isFirstParameter = false;
		}
		formatedString.append(")");
		return formatedString.toString();
	}

}
