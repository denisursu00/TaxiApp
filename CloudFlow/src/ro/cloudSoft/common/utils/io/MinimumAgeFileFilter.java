package ro.cloudSoft.common.utils.io;

import java.io.File;
import java.io.FileFilter;

/**
 * Gaseste fisiere care sunt mai vechi de o varsta specificata.
 */
public class MinimumAgeFileFilter implements FileFilter {
	
	private final long currentTimeInMilliseconds;
	private final long minimumFileAgeInMilliseconds;
	
	public MinimumAgeFileFilter(int minimumFileAgeInMinutes) {
		currentTimeInMilliseconds = System.currentTimeMillis();
		minimumFileAgeInMilliseconds = (minimumFileAgeInMinutes * 60 * 1000);
	}
	
	@Override
	public boolean accept(File file) {
		
		if (!file.isFile()) {
			return false;
		}
		
		long fileLastModifiedDateInMilliseconds = file.lastModified();
		long fileAgeInMilliseconds = (currentTimeInMilliseconds - fileLastModifiedDateInMilliseconds);
		
		return (fileAgeInMilliseconds >= minimumFileAgeInMilliseconds);
	}
}