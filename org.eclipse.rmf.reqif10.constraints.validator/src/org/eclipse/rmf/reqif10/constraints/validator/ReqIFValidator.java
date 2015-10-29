package org.eclipse.rmf.reqif10.constraints.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.rmf.internal.reqif10.constraints.ReqIFDiagnostician;
import org.eclipse.rmf.reqif10.AttributeValue;
import org.eclipse.rmf.reqif10.Identifiable;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.ReqIFHeader;
import org.eclipse.rmf.reqif10.constraints.validator.Issue.Severity;
import org.eclipse.rmf.reqif10.serialization.ReqIF10LocationStore;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReqIFValidator {

	private ReqIF reqif;
	private boolean schemaValidationDisabled = false;
	private boolean diagnosticianDisabled = false;

	public LinkedList<Issue> validate(ReqIF reqif) {
		this.reqif = reqif;
		LinkedList<Issue> issues = new LinkedList<Issue>();
		
		// 1. run Schema Validation
		
		if (!schemaValidationDisabled){
			String filename = reqif.eResource().getURI().toFileString();
			try {
				validateAgainstSchema(filename, issues);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		

		// 2. run EMF Diagnostician
		if (!diagnosticianDisabled){
			Diagnostic diagnosticResults = ReqIFDiagnostician.INSTANCE
					.validate(reqif);
	
			for (Diagnostic childDiagnostic : diagnosticResults.getChildren()) {
				Issue issue = diagnosticToIssue(childDiagnostic);
	//			if (!isInToolExtension(issue.getTarget())) {
	//			}
				issue.setReqif(reqif);
				issues.add(issue);
			}
		}

		// 3. run EMF Validation Framework
		IBatchValidator validator = ModelValidationService.getInstance()
				.newValidator(EvaluationMode.BATCH);
		validator.setReportSuccesses(false);
		IStatus validationResults = validator.validate(reqif);

		IStatus[] children = validationResults.getChildren();
		if (children.length == 0) {
			// If there is only one result, children is empty and instead
			// the results itself is the Status we need
			if (validationResults.getSeverity() == IStatus.OK
					&& validationResults.getCode() == 10) {
				children = new IStatus[0];
			} else {
				children = new IStatus[1];
				children[0] = validationResults;
			}
		}

		for (IStatus childStatus : children) {
			Issue issue = validationStatusToIssue(childStatus);
//			if (!isInToolExtension(issue.getTarget())) {
//			}
			issue.setReqif(reqif);
			issues.add(issue);
		}

		return issues;
	}

	private Issue validationStatusToIssue(IStatus status) {
		Issue issue = new Issue();

		switch (status.getSeverity()) {
		case IStatus.ERROR:
			issue.setSeverity(Severity.ERROR);
			break;
		case IStatus.INFO:
		case IStatus.OK:
			issue.setSeverity(Severity.INFO);
			break;
		default:
			issue.setSeverity(Severity.WARNING);
			break;
		}

		issue.setMessage(status.getMessage());

		if (status instanceof ConstraintStatus) {
			EObject target = ((ConstraintStatus) status).getTarget();
			Resource targetResource = target.eResource();

			String location = target.toString();

			Integer lineNumber = getLineNumber(target, targetResource);
			if (lineNumber != null) {
				issue.setLine(lineNumber);
				location = "line " + lineNumber + ": " + location;
			}

			issue.setLocation(location);
			issue.setTarget(target);
		}

		return issue;
	}

	private Issue diagnosticToIssue(Diagnostic diagnostic) {
		Issue issue = new Issue();

		int severity = diagnostic.getSeverity();
		if (severity < Diagnostic.WARNING) {
			issue.setSeverity(Severity.INFO);
		} else if (severity < Diagnostic.ERROR) {
			issue.setSeverity(Severity.WARNING);
		} else {
			issue.setSeverity(Severity.ERROR);
		}

		issue.setMessage("[D] " + diagnostic.getMessage());

		Integer lineNumber = null;
		if (diagnostic.getData().size() > 0) {
			EObject object = (EObject) diagnostic.getData().get(0);
			lineNumber = getLineNumber(object, (XMLResource) object.eResource());
			if (lineNumber != null) {
				issue.setLine(lineNumber);
			}
		}

		String location = diagnostic.getData().get(0).toString();
		if (lineNumber != null) {
			location = "line: " + lineNumber + " " + location;
		}
		issue.setLocation(location);

		issue.setTarget(diagnostic.getData().get(0));

		return issue;
	}

	/**
	 * private Helper to retrieve the LineNumber from the store
	 * 
	 * If element is an Identifiable, its position is returned. Otherwise, its
	 * containers position is returned.
	 * 
	 * 
	 * @param element
	 * @param targetResource
	 * @return
	 */
	private Integer getLineNumber(EObject element, Resource targetResource) {
		if (!(targetResource instanceof XMLResource)) {
			return -1;
		}

		String identifier = null;

		if (element instanceof Identifiable) {
			identifier = ((Identifiable) element).getIdentifier();
		} else {
			if (element instanceof AttributeValue) {
				AttributeValue attributeValue = (AttributeValue) element;
				EObject eContainer = attributeValue.eContainer();
				if (eContainer instanceof Identifiable) {
					// If this is not a specObject, something is soo wrong here
					identifier = ((Identifiable) eContainer).getIdentifier();
				}
			} else if (element instanceof ReqIFHeader) {
				identifier = ((ReqIFHeader) element).getIdentifier();
			}
		}

		if (identifier != null) {
			return ReqIF10LocationStore.getInstance().getPosition(
					(XMLResource) targetResource, identifier);
		}

		return null;
	}

	public static boolean isInToolExtension(Object object) {

//		 if (! (object instanceof EObject)){
//		 return false;
//		 }
//		
//		 EObject target = (EObject) object;
//		
//		 if (target instanceof ProrToolExtension){
//		 return true;
//		 }
//		
//		 while (target.eContainer() != null){
//		 EObject container = target.eContainer();
//		 if (container instanceof ProrToolExtension){
//		 return true;
//		 }
//		 target = container;
//		 }

		return false;
	}

	
	/**
	 * validated the file defined by filename against the reqif.xsd schema
	 * 
	 * all found issues are stored in the result list
	 * 
	 * @param filename
	 * @param issues
	 * @throws Exception
	 */
	protected void validateAgainstSchema(String filename, final List<Issue> result) throws Exception {
		StreamSource[] schemaDocuments = new StreamSource[] { new StreamSource("platform:/plugin/org.eclipse.rmf.reqif10/schema/reqif.xsd") };
		
		Source instanceDocument = new StreamSource(filename);

		// the resolver is required to map the schema references to the reqif
		// sub schema files to the local locations
		LSResourceResolver resolver = new LSResourceResolver() {

			public LSInput resolveResource(String type, String namespaceURI,
					String publicId, String systemId, String baseURI) {

				String schemaFileName;
				if (null != systemId) {
					int slashIndex = systemId.lastIndexOf("/");
					if (-1 == slashIndex) {
						schemaFileName = systemId;
					} else if (slashIndex == systemId.length() + 1) {
						schemaFileName = null;
					} else {
						schemaFileName = systemId.substring(slashIndex);
					}
				} else {
					schemaFileName = null;
				}

				InputStream inputStream;
				try {
					URL url;
					url = new URL(
							"platform:/plugin/org.eclipse.rmf.reqif10/schema/"
									+ schemaFileName);
					inputStream = url.openConnection().getInputStream();
				} catch (IOException e) {
					throw new RuntimeException("Can not load Schema " + schemaFileName);
				}


				if (inputStream != null) {
					return new Input(publicId, systemId, inputStream);
				} else {
					throw new RuntimeException("Can not load Schema " + schemaFileName);
				}
			}
		};

		SchemaFactory sf = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		sf.setResourceResolver(resolver);
		Schema s = sf.newSchema(schemaDocuments);
		Validator v = s.newValidator();
		v.setErrorHandler(new ErrorHandler() {

			@Override
			public void warning(SAXParseException exception)
					throws SAXException {
				createIssue(exception);
			}

			@Override
			public void fatalError(SAXParseException exception)
					throws SAXException {
				createIssue(exception);
			}

			@Override
			public void error(SAXParseException exception) throws SAXException {
				createIssue(exception);
			}
			
			private void createIssue(SAXParseException exception){
				Issue issue = new Issue();
				issue.setMessage("[XSD] " + exception.getLocalizedMessage());
				issue.setLine(exception.getLineNumber());
				issue.setSeverity(Severity.ERROR);
				issue.setReqif(reqif);
				result.add(issue);
			}
			
		});
		v.validate(instanceDocument);

	}

	public void disableSchemaValidation() {
		schemaValidationDisabled = true;
	}

	public void disableDiagnostician() {
		diagnosticianDisabled = true;
	}

}
