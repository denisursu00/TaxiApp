package ro.cloudSoft.cloudDoc.services.export;

public class ExportDataAsSqlService {
	
	private DocumentTypeDataAsSqlExporter documentTypeDataAsSqlExporter;
	private WorkflowDataAsSqlExporter workflowDataAsSqlExporter;
	
	public String exportAll() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("-- >>> DOCUMENT TYPES");
		sql.append("\n\n");
		sql.append(exportDocumentTypes());
		sql.append("\n\n");
		sql.append("-- >>> WORKFLOWS");
		sql.append("\n\n");
		sql.append(exportWorkflows()) ;
		
		return sql.toString();
	}
	
	public String exportDocumentTypes() {
		return documentTypeDataAsSqlExporter.export();
	}

	public String exportWorkflows() {		
		return workflowDataAsSqlExporter.export();
	}
	
	public void setWorkflowDataAsSqlExporter(WorkflowDataAsSqlExporter workflowDataAsSqlExporter) {
		this.workflowDataAsSqlExporter = workflowDataAsSqlExporter;
	}
	
	public void setDocumentTypeDataAsSqlExporter(DocumentTypeDataAsSqlExporter documentTypeDataAsSqlExporter) {
		this.documentTypeDataAsSqlExporter = documentTypeDataAsSqlExporter;
	}
}
