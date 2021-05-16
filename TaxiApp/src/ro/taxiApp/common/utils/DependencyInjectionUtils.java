package ro.taxiApp.common.utils;

/**
 * 
 */
public class DependencyInjectionUtils {

	/**
	 * @throws IllegalStateException daca cel putin o dependenta e nula
	 */
	public static void checkRequiredDependencies(Object... dependencies) {
		for (int i = 0; i < dependencies.length; i++) {
			Object dependency = dependencies[i];
			if (dependency == null) {
				throw new IllegalStateException("Dependency with index [" + i + "] is null.");
			}
		}
	}
}