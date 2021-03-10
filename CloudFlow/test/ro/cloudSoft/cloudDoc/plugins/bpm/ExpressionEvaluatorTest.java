package ro.cloudSoft.cloudDoc.plugins.bpm;

import static junit.framework.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;

import com.google.common.collect.Maps;

/**
 * 
 */
public class ExpressionEvaluatorTest {
	
	@Test
	public void test() {
		
		Map<String, MetadataWrapper> metadataMap = Maps.newHashMap();
		
		String metadataName = "metadata text 1";
		String medatataValue = "abc";
		
		MetadataWrapper metadataWrapper = new MetadataWrapper(MetadataDefinition.TYPE_TEXT, medatataValue);
		metadataMap.put(metadataName, metadataWrapper);
		
		String expression1 = "{" + metadataName + "} == '" + medatataValue + "'";		
		boolean result1 = ExpressionEvaluator.evaluateDocumentExpression(expression1, metadataMap);
		assertTrue(result1);

		String expression2 = "{" + metadataName + "} != '" + "alta valoare" + "'";
		boolean result2 = ExpressionEvaluator.evaluateDocumentExpression(expression2, metadataMap);
		assertTrue(result2);
	}
}