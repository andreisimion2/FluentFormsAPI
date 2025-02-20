package com._4point.aem.docservices.rest_services.client.docassurance;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com._4point.aem.docservices.rest_services.client.helpers.AemServerType;
import com._4point.aem.docservices.rest_services.client.helpers.Builder;
import com._4point.aem.docservices.rest_services.client.helpers.BuilderImpl;
import com._4point.aem.docservices.rest_services.client.helpers.MultipartTransformer;
import com._4point.aem.docservices.rest_services.client.helpers.RestServicesServiceAdapter;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.docassurance.DocAssuranceService.DocAssuranceServiceException;
import com._4point.aem.fluentforms.api.docassurance.EncryptionOptions;
import com._4point.aem.fluentforms.api.docassurance.ReaderExtensionOptions;
import com._4point.aem.fluentforms.impl.SimpleDocumentFactoryImpl;
import com._4point.aem.fluentforms.impl.docassurance.TraditionalDocAssuranceService;
import com.adobe.fd.docassurance.client.api.SignatureOptions;
import com.adobe.fd.encryption.client.EncryptionTypeResult;
import com.adobe.fd.readerextensions.client.GetUsageRightsResult;
import com.adobe.fd.readerextensions.client.ReaderExtensionsOptionSpec;
import com.adobe.fd.signatures.client.types.FieldMDPOptionSpec;
import com.adobe.fd.signatures.client.types.PDFDocumentVerificationInfo;
import com.adobe.fd.signatures.client.types.PDFSeedValueOptionSpec;
import com.adobe.fd.signatures.client.types.PDFSignature;
import com.adobe.fd.signatures.client.types.PDFSignatureField;
import com.adobe.fd.signatures.client.types.PDFSignatureFieldProperties;
import com.adobe.fd.signatures.client.types.PDFSignatureVerificationInfo;
import com.adobe.fd.signatures.client.types.PositionRectangle;
import com.adobe.fd.signatures.client.types.VerificationTime;
import com.adobe.fd.signatures.pdf.inputs.UnlockOptions;
import com.adobe.fd.signatures.pdf.inputs.ValidationPreferences;
import com.adobe.fd.signatures.pki.client.types.common.RevocationCheckStyle;

public class RestServicesDocAssuranceServiceAdapter extends RestServicesServiceAdapter implements TraditionalDocAssuranceService {

	private static final String SECURE_DOCUMENT_SERVICE_NAME = "DocAssuranceService";
	private static final String SECURE_DOCUMENT_METHOD_NAME = "SecureDocument";

	private static final String CREDENTIAL_ALIAS_PARAM = "credentialAlias";
	private static final String DOCUMENT_PARAM = "inDoc";
	private static final String MESSAGE_PARAM = "message";
	private static final String IS_MODE_FINAL_PARAM = "isModeFinal";
	private static final String ENABLED_BARCODED_DECODING_PARAM = "usageRights.enabledBarcodedDecoding";
	private static final String ENABLED_COMMENTS_PARAM = "usageRights.enabledComments";
	private static final String ENABLED_COMMENTS_ONLINE_PARAM = "usageRights.enabledCommentsOnline";
	private static final String ENABLED_DIGITAL_SIGNATURES_PARAM = "usageRights.enabledDigitalSignatures";
	private static final String ENABLED_DYNAMIC_FORM_FIELDS_PARAM = "usageRights.enabledDynamicFormFields";
	private static final String ENABLED_DYNAMIC_FORM_PAGES_PARAM = "usageRights.enabledDynamicFormPages";
	private static final String ENABLED_EMBEDDED_FILES_PARAM = "usageRights.enabledEmbeddedFiles";
	private static final String ENABLED_FORM_DATA_IMPORT_EXPORT_PARAM = "usageRights.enabledFormDataImportExport";
	private static final String ENABLED_FORM_FILL_IN_PARAM = "usageRights.enabledFormFillIn";
	private static final String ENABLED_ONLINE_FORMS_PARAM = "usageRights.enabledOnlineForms";
	private static final String ENABLED_SUBMIT_STANDALONE_PARAM = "usageRights.enabledSubmitStandalone";

	// Only callable from Builder
	private RestServicesDocAssuranceServiceAdapter(WebTarget target, Supplier<String> correlationId, AemServerType aemServerType) {
		super(target, correlationId, aemServerType);
	}

	@Override
	public Document secureDocument(Document inDocument, EncryptionOptions encryptionOptions, SignatureOptions signatureOptions, ReaderExtensionOptions readerExtensionOptions,
			UnlockOptions unlockOptions) throws DocAssuranceServiceException {
		WebTarget secureDocTarget = baseTarget.path(constructStandardPath(SECURE_DOCUMENT_SERVICE_NAME, SECURE_DOCUMENT_METHOD_NAME));
		
		try (final FormDataMultiPart multipart = new FormDataMultiPart()) {
			if (encryptionOptions != null) {
				// TODO Auto-generated method stub
			}
			
			if (signatureOptions != null) {
				// TODO Auto-generated method stub
			}

			if (readerExtensionOptions != null) {
				String credentialAlias = readerExtensionOptions.getCredentialAlias();
				ReaderExtensionsOptionSpec reOptionsSpec = readerExtensionOptions.getReOptions();
				
				multipart.field(DOCUMENT_PARAM, inDocument.getInputStream(), APPLICATION_PDF);
				multipart.field(CREDENTIAL_ALIAS_PARAM, credentialAlias);

				if (reOptionsSpec != null) {
					String message = reOptionsSpec.getMessage();
					Boolean isModeFinal = reOptionsSpec.isModeFinal();
					Boolean enabledBarcodeDecoding = reOptionsSpec.getUsageRights().isEnabledBarcodeDecoding();
					Boolean enabledComments = reOptionsSpec.getUsageRights().isEnabledComments();
					Boolean enabledCommentsOnline = reOptionsSpec.getUsageRights().isEnabledCommentsOnline();
					Boolean enabledDigitalSignatures = reOptionsSpec.getUsageRights().isEnabledDigitalSignatures();
					Boolean enabledDynamicFormFields = reOptionsSpec.getUsageRights().isEnabledDynamicFormFields();
					Boolean enabledDynamicFormPages = reOptionsSpec.getUsageRights().isEnabledDynamicFormPages();
					Boolean enabledEmbeddedFiles = reOptionsSpec.getUsageRights().isEnabledEmbeddedFiles();
					Boolean enabledFormDateImportExport = reOptionsSpec.getUsageRights().isEnabledFormDataImportExport();
					Boolean enabledFormFillIn = reOptionsSpec.getUsageRights().isEnabledFormFillIn();
					Boolean enabledOnlineForms = reOptionsSpec.getUsageRights().isEnabledOnlineForms();
					Boolean enabledSubmitStandalone = reOptionsSpec.getUsageRights().isEnabledSubmitStandalone();

					// Set fields for non-null values. 
					MultipartTransformer.create(multipart)
										.transform((t)->message == null ? t : t.field(MESSAGE_PARAM, message))
										.transform((t)->isModeFinal == null ? t : t.field(IS_MODE_FINAL_PARAM, isModeFinal.toString()))
										.transform((t)->enabledBarcodeDecoding == null ? t : t.field(ENABLED_BARCODED_DECODING_PARAM, enabledBarcodeDecoding.toString()))
										.transform((t)->enabledComments == null ? t : t.field(ENABLED_COMMENTS_PARAM, enabledComments.toString()))
										.transform((t)->enabledCommentsOnline == null ? t : t.field(ENABLED_COMMENTS_ONLINE_PARAM, enabledCommentsOnline.toString()))
										.transform((t)->enabledDigitalSignatures == null ? t : t.field(ENABLED_DIGITAL_SIGNATURES_PARAM, enabledDigitalSignatures.toString()))
										.transform((t)->enabledDynamicFormFields == null ? t : t.field(ENABLED_DYNAMIC_FORM_FIELDS_PARAM, enabledDynamicFormFields.toString()))
										.transform((t)->enabledDynamicFormPages == null ? t : t.field(ENABLED_DYNAMIC_FORM_PAGES_PARAM, enabledDynamicFormPages.toString()))
										.transform((t)->enabledEmbeddedFiles == null ? t : t.field(ENABLED_EMBEDDED_FILES_PARAM, enabledEmbeddedFiles.toString()))
										.transform((t)->enabledFormDateImportExport == null ? t : t.field(ENABLED_FORM_DATA_IMPORT_EXPORT_PARAM, enabledFormDateImportExport.toString()))
										.transform((t)->enabledFormFillIn == null ? t : t.field(ENABLED_FORM_FILL_IN_PARAM, enabledFormFillIn.toString()))
										.transform((t)->enabledOnlineForms == null ? t : t.field(ENABLED_ONLINE_FORMS_PARAM, enabledOnlineForms.toString()))
										.transform((t)->enabledSubmitStandalone == null ? t : t.field(ENABLED_SUBMIT_STANDALONE_PARAM, enabledSubmitStandalone.toString()));
				}
			}
			
			if (unlockOptions != null) {
				// TODO Auto-generated method stub
			}

			Response result = postToServer(secureDocTarget, multipart, APPLICATION_PDF);
			
			StatusType resultStatus = result.getStatusInfo();
			if (!Family.SUCCESSFUL.equals(resultStatus.getFamily())) {
				String msg = "Call to server failed, statusCode='" + resultStatus.getStatusCode() + "', reason='" + resultStatus.getReasonPhrase() + "'.";
				if (result.hasEntity()) {
					InputStream entityStream = (InputStream) result.getEntity();
					msg += "\n" + inputStreamtoString(entityStream);
				}
				throw new DocAssuranceServiceException(msg);
			}
			
			if (!result.hasEntity()) {
				throw new DocAssuranceServiceException("Call to server succeeded but server failed to return document.  This should never happen.");
			}
			
			String responseContentType = result.getHeaderString(HttpHeaders.CONTENT_TYPE);
			if ( responseContentType == null || !APPLICATION_PDF.isCompatible(MediaType.valueOf(responseContentType))) {
				String msg = "Response from AEM server was not a PDF.  " + (responseContentType != null ? "content-type='" + responseContentType + "'" : "content-type was null") + ".";
				InputStream entityStream = (InputStream) result.getEntity();
				msg += "\n" + inputStreamtoString(entityStream);
				throw new DocAssuranceServiceException(msg);
			}
			
			Document resultDoc = SimpleDocumentFactoryImpl.getFactory().create((InputStream) result.getEntity());
			resultDoc.setContentType(APPLICATION_PDF.toString());
			return resultDoc;
			
		} catch (IOException e) {
			throw new DocAssuranceServiceException("I/O Error while reader extending the document. (" + baseTarget.getUri().toString() + ").", e);
		} catch (RestServicesServiceException e) {
			throw new DocAssuranceServiceException("Error while POSTing to server", e);
		}
	}

	@Override
	public GetUsageRightsResult getDocumentUsageRights(Document inDocument, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getDocumentUsageRights is not implemented yet.");
	}

	@Override
	public GetUsageRightsResult getCredentialUsageRights(String credentialAlias) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getCredentialUsageRights is not implemented yet.");
	}

	@Override
	public Document addInvisibleSignatureField(Document inDoc, String signatureFieldName,
			FieldMDPOptionSpec fieldMDPOptionsSpec, PDFSeedValueOptionSpec seedValueOptionsSpec,
			UnlockOptions unlockOptions) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("addInvisibleSignatureField is not implemented yet.");
	}

	@Override
	public Document addSignatureField(Document inDoc, String signatureFieldName, Integer pageNo,
			PositionRectangle positionRectangle, FieldMDPOptionSpec fieldMDPOptionsSpec,
			PDFSeedValueOptionSpec seedValueOptionsSpec, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("addSignatureField is not implemented yet.");
	}

	@Override
	public Document clearSignatureField(Document inDoc, String signatureFieldName, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("clearSignatureField is not implemented yet.");
	}

	@Override
	public PDFSignatureField getCertifyingSignatureField(Document inDoc, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getCertifyingSignatureField is not implemented yet.");
	}

	@Override
	public PDFSignature getSignature(Document inDoc, String signatureFieldName, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getSignature is not implemented yet.");
	}

	@Override
	public List<PDFSignatureField> getSignatureFieldList(Document inDoc, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getSignatureFieldList is not implemented yet.");
	}

	@Override
	public Document modifySignatureField(Document inDoc, String signatureFieldName,
			PDFSignatureFieldProperties pdfSignatureFieldProperties, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("modifySignatureField is not implemented yet.");
	}

	@Override
	public Document removeSignatureField(Document inDoc, String signatureFieldName, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("removeSignatureField is not implemented yet.");
	}

	@Override
	public PDFSignatureVerificationInfo verify(Document inDoc, String signatureFieldName,
			RevocationCheckStyle revocationCheckStyle, VerificationTime verificationTime,
			ValidationPreferences dssPrefs) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("verify is not implemented yet.");
	}

	@Override
	public EncryptionTypeResult getPDFEncryption(Document inDoc) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("getPDFEncryption is not implemented yet.");
	}

	@Override
	public Document removePDFCertificateSecurity(Document inDoc, String alias) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("removePDFCertificateSecurity is not implemented yet.");
	}

	@Override
	public Document removePDFPasswordSecurity(Document inDoc, String password) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("removePDFPasswordSecurity is not implemented yet.");
	}

	@Override
	public PDFDocumentVerificationInfo verifyDocument(Document inDoc, RevocationCheckStyle revocationCheckStyle,
			VerificationTime verificationTime, ValidationPreferences prefStore) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("verifyDocument is not implemented yet.");
	}

	@Override
	public Document removeUsageRights(Document inDoc, UnlockOptions unlockOptions) throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("removeUsageRights is not implemented yet.");
	}

	@Override
	public Document applyDocumentTimeStamp(Document inDoc, VerificationTime verificationTime,
			ValidationPreferences validationPreferences, UnlockOptions unlockOptions)
			throws DocAssuranceServiceException {
		throw new UnsupportedOperationException("applyDocumentTimeStamp is not implemented yet.");
	}

	/**
	 * Creates a Builder object for building a RestServicesFormServiceAdapter object.
	 * 
	 * @return build object
	 */
	public static DocAssuranceServiceBuilder builder() {
		return new DocAssuranceServiceBuilder();
	}
	
	public static class DocAssuranceServiceBuilder implements Builder {
		private BuilderImpl builder = new BuilderImpl();
//		private final static Supplier<Client> defaultClientFactory = ()->ClientBuilder.newClient();
		
		private DocAssuranceServiceBuilder() {
			super();
		}

		@Override
		public DocAssuranceServiceBuilder machineName(String machineName) {
			builder.machineName(machineName);
			return this;
		}

		@Override
		public DocAssuranceServiceBuilder port(int port) {
			builder.port(port);
			return this;
		}

		@Override
		public DocAssuranceServiceBuilder useSsl(boolean useSsl) {
			builder.useSsl(useSsl);
			return this;
		}

		@Override
		public DocAssuranceServiceBuilder clientFactory(Supplier<Client> clientFactory) {
			builder.clientFactory(clientFactory);
			return this;
		}

		@Override
		public DocAssuranceServiceBuilder basicAuthentication(String username, String password) {
			builder.basicAuthentication(username, password);
			return this;
		}
		
		@Override
		public DocAssuranceServiceBuilder correlationId(Supplier<String> correlationIdFn) {
			builder.correlationId(correlationIdFn);
			return this;
		}

		@Override
		public Supplier<String> getCorrelationIdFn() {
			return builder.getCorrelationIdFn();
		}

		@Override
		public WebTarget createLocalTarget() {
			return builder.createLocalTarget();
		}

		@Override
		public DocAssuranceServiceBuilder aemServerType(AemServerType serverType) {
			builder.aemServerType(serverType);
			return this;
		}
		
		@Override
		public AemServerType getAemServerType() {
			return builder.getAemServerType();
		}

		public RestServicesDocAssuranceServiceAdapter build() {
			return new RestServicesDocAssuranceServiceAdapter(this.createLocalTarget(), this.getCorrelationIdFn(), this.getAemServerType());
		}
	}
}
