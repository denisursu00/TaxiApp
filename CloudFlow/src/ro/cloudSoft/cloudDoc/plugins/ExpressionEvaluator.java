package ro.cloudSoft.cloudDoc.plugins;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.utils.TextWithPlaceholdersHelper;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.PlaceholderValueContext;

/**
 * Evalueaza expresii ce contin conditii pentru valorile metadatelor unui document.
 * 
 * 
 */
public class ExpressionEvaluator {
	
	/** motorul pentru evaluarea expresiilor */
	private static JexlEngine JEXL_ENGINE;
	static {
		JEXL_ENGINE = new JexlEngine();
		Map<String, Object> functions = new HashMap<String, Object>();
		functions.put("date", new JexlDateFunctions());
		functions.put("user", new JexlUserFunctions());
		functions.put("project", new JexlProjectFunctions());
		functions.put("nomenclator", new JexlNomenclatorFunctions());
		JEXL_ENGINE.setFunctions(functions);
	}
	
	/**
	 * Evalueaza conditia din expresia data.
	 * @param conditionExpressionWithPlaceholders expresia
	 * @param metadataWrapperByMetadataName map-ul ce contine date legate de metadate (nume, tip,
	 * valoare / valori)
	 * @return <code>true</code> daca , conditia este adevarata,
	 * <code>false</code> altfel
	 */
	public static boolean evaluateDocumentExpression(String conditionExpressionWithPlaceholders, Map<String, MetadataWrapper> metadataWrapperByMetadataName) {		
		
		PlaceholderValueContext placeholderValueContext = new MetadataForExpressionEvaluationPlaceholderValueContext(metadataWrapperByMetadataName);
		String conditionExpression = TextWithPlaceholdersHelper.replacePlaceholders(conditionExpressionWithPlaceholders, placeholderValueContext);
		Expression expression = JEXL_ENGINE.createExpression(conditionExpression);
		return ((Boolean) expression.evaluate(null));
	}
	
	public static boolean evaluateNomenclatorExpression(String conditionExpressionWithPlaceholders, Map<String, NomenclatorAttributeEvaluationWrapper> attributeWrapperByAttributeKey) {		
		
		PlaceholderValueContext placeholderValueContext = new NomenclatorAttributeForExpressionEvaluationPlaceholderValueContext(attributeWrapperByAttributeKey);
		String conditionExpression = TextWithPlaceholdersHelper.replacePlaceholders(conditionExpressionWithPlaceholders, placeholderValueContext);
		
		Expression expression = JEXL_ENGINE.createExpression(conditionExpression);
		return ((Boolean) expression.evaluate(null));
	}
}