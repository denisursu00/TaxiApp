package ro.cloudSoft.cloudDoc.utils.placeholderValueContexts;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;

public class CompositeWithPriorityPlaceholderValueContext implements PlaceholderValueContext {
	
	private static final List<Priority> ORDERED_PRIORITIES = ImmutableList.of(
		Priority.HIGH,
		Priority.MEDIUM,
		Priority.LOW
	);
	
	private final ListMultimap<Priority, PlaceholderValueContext> placeholderValueContextsByPriority;

	public CompositeWithPriorityPlaceholderValueContext() {
		placeholderValueContextsByPriority = ArrayListMultimap.create();
	}
	
	public void addPlaceholderValueContext(Priority priority, PlaceholderValueContext placeholderValueContext) {
		placeholderValueContextsByPriority.put(priority, placeholderValueContext);
	}
	
	@Override
	public String getValue(String placeholderName) {
		
		for (Priority priority : ORDERED_PRIORITIES) {
			List<PlaceholderValueContext> placeholderValueContextsOfPriority = placeholderValueContextsByPriority.get(priority);
			for (PlaceholderValueContext placeholderValueContextOfPriority : placeholderValueContextsOfPriority) {
				String placeholderValue = placeholderValueContextOfPriority.getValue(placeholderName);
				if ((placeholderValue != null) && !placeholderValue.equals(PlaceholderValueContext.VALUE_WHEN_NOT_FOUND)) {
					return placeholderValue;
				}
			}
		}
		
		return PlaceholderValueContext.VALUE_WHEN_NOT_FOUND;
	}
	
	public static enum Priority {
		HIGH, MEDIUM, LOW
	}
}