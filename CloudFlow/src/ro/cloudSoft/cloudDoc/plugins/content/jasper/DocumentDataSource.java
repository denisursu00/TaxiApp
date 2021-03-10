package ro.cloudSoft.cloudDoc.plugins.content.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DocumentDataSource implements JRDataSource {
	
	private boolean finished;
	
	public DocumentDataSource() {
		this.finished = false;
	}

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		return null;
	}

	@Override
	public boolean next() throws JRException {
		if (this.finished) {
			return false;
		}
		this.finished = true;
		return true;
	}
}