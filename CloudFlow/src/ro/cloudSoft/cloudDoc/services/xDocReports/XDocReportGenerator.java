package ro.cloudSoft.cloudDoc.services.xDocReports;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReportGenerator {

	private final byte[] templateFileContent;
	private final Map<String, Object> parameters;
	private IXDocReport report;
	private IContext context;

	public XDocReportGenerator(byte[] templateFileContent, Map<String, Object> parameters) {
		this.templateFileContent = templateFileContent;
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		this.parameters = parameters;
	}

	private void prepareReportContext(byte[] inputData) {
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(inputData);
			
			report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
			report.setFieldsMetadata(buildFieldsMetadata());

			context = report.createContext();
			context.putMap(parameters);

		} catch (IOException | XDocReportException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public byte[] generateAsPdf() {
		
		prepareReportContext(generateAsDocx());		

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
			report.convert(context, options, out);
			return out.toByteArray();
		} catch (XDocReportException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public byte[] generateAsDocx() {
		
		prepareReportContext(templateFileContent);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			report.process(context, out);
			return out.toByteArray();
		} catch (XDocReportException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private FieldsMetadata buildFieldsMetadata() {
		FieldsMetadata metadata = new FieldsMetadata();

		parameters.forEach((key, value) -> {
			if (value instanceof List) {
				((List) value).forEach(listValue -> {
					((Map<String, Object>) listValue).forEach((inListkey, inListValue) -> {
						if (inListValue instanceof List) {
							((List) inListValue).forEach(inListValueElement -> {
								((Map<String, Object>) inListValueElement).forEach((inListinListkey, inListListValue) -> {
									metadata.addFieldAsList(key + "." + inListkey + "." + inListinListkey);
								});
							});
						} else {
							metadata.addFieldAsList(key + "." + inListkey);
						}
					});
				});
			}
		});

		return metadata;
	}
}
