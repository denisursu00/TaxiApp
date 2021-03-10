package ro.cloudSoft.cloudDoc.exporters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Clasa ajutatoare pentru exportul schemei necesare Hibernate in fisiere SQL
 * 
 * 
 */
public class HibernateSchemaSqlExporter implements InitializingBean {
	
	private final boolean generateFiles;
	
	private final String createFilePath;
	private final String dropFilePath;

	public HibernateSchemaSqlExporter(boolean generateFiles, String createFilePath, String dropFilePath) {
		
		this.generateFiles = generateFiles;
		
		this.createFilePath = createFilePath;
		this.dropFilePath = dropFilePath;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			generateFiles,
			
			createFilePath,
			dropFilePath
		);
	}

	/**
	 * Salveaza script-urile necesare pentru crearea si stergerea structurii bazei de date necesare pentru configuratia Hibernate data.
	 * 
	 * @param configuration configuratia Hibernate
	 */
	public void doExport(Configuration configuration) {
		
		if (!generateFiles) {
			return;
		}
		
		Dialect dialect = Dialect.getDialect(configuration.getProperties());
		
		String[] sqlCreateStatements = configuration.generateSchemaCreationScript(dialect);
		exportSqlStatementsToFile(sqlCreateStatements, createFilePath);
		
		String[] sqlDropStatements = configuration.generateDropSchemaScript(dialect);
		exportSqlStatementsToFile(sqlDropStatements, dropFilePath);
	}
	
	private void exportSqlStatementsToFile(String[] sqlStatements, String filePath) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(filePath));
			for (String sqlStatement : sqlStatements) {
				writer.println(sqlStatement + ";");
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
}