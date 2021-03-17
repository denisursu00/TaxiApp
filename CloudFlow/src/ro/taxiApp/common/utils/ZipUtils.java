package ro.taxiApp.common.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

	@SuppressWarnings("deprecation")
	public static void unzip(InputStream zipInputStream, File outputDirectory) {
		List<Closeable> closeables = new LinkedList<>();
		try {
			
			if (!outputDirectory.exists()) {
				boolean created = outputDirectory.mkdir();
				if (!created) {
					throw new RuntimeException("output directory cannot be created");
				}
			}
			
			ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", zipInputStream);
			closeables.add(in);
			
			ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry(); 
			while (entry != null) {
				if (entry.isDirectory()) {
					File dirFile = new File(outputDirectory, entry.getName());
					if (!dirFile.exists()) {
						boolean created = dirFile.mkdir();
						if (!created) {
							throw new RuntimeException("cannot created directory entry [" + entry.getName() + "]");
						}
					}
				} else {
					File outputFile = new File(outputDirectory, entry.getName());				    
					OutputStream os = new FileOutputStream(outputFile);
					closeables.add(os);
				    IOUtils.copy(in, os);
				}
				entry = (ZipArchiveEntry) in.getNextEntry(); 
			}
		} catch (Exception e) {
			throw new RuntimeException("exception while unziping", e);
		} finally {
			for (Closeable closeable : closeables) {
				IOUtils.closeQuietly(closeable);
			}
		}
	}
}
