package com._4point.aem.fluentforms.impl.output;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com._4point.aem.fluentforms.api.output.PrintConfig;
import com.adobe.fd.output.api.AcrobatVersion;
import com.adobe.fd.output.api.PaginationOverride;

class PrintedOutputOptionsImplTest {

	private final static com.adobe.fd.output.api.PrintedOutputOptions emptyPrintedOutputOptions = new com.adobe.fd.output.api.PrintedOutputOptions();
	private final PrintedOutputOptionsImpl underTest = new PrintedOutputOptionsImpl();

	@Test
	@DisplayName("Make sure that if nothing was initialized, then the resulting options are the same as an empty options object.")
	void testToAdobePrintedOutputOptions_NoChanges() {
		
		assertEmpty(underTest, null);
	}

	private static void assertEmpty(PrintedOutputOptionsImpl printedOutputOptions, String contentRoot) {
		com.adobe.fd.output.api.PrintedOutputOptions adobePrintedOutputOptions = AdobeOutputServiceAdapter.toAdobePrintedOutputOptions(printedOutputOptions);
		assertEquals(contentRoot, adobePrintedOutputOptions.getContentRoot());	// We modify the content root, so we expect it to be different than the empty version (which would be null).
		assertEquals(emptyPrintedOutputOptions.getCopies(), adobePrintedOutputOptions.getCopies());
		assertEquals(emptyPrintedOutputOptions.getDebugDir(), adobePrintedOutputOptions.getDebugDir());
		assertEquals(emptyPrintedOutputOptions.getLocale(), adobePrintedOutputOptions.getLocale());
		assertEquals(emptyPrintedOutputOptions.getPaginationOverride(), adobePrintedOutputOptions.getPaginationOverride());
		assertNull(adobePrintedOutputOptions.getPrintConfig());	// default for emptyPrintedOutputOptions.getPrintConfig() is 
		assertEquals(emptyPrintedOutputOptions.getXci(), adobePrintedOutputOptions.getXci());
	}
	
	@Test
	@DisplayName("Make sure that if everything was initialized, then the resulting options are the not same as an empty options object.")
	void testToAdobePrintedOutputOptions_AllChanges() {

		underTest.setContentRoot(Paths.get("foo", "bar"));
		underTest.setCopies(2);
		underTest.setDebugDir(Paths.get("bar", "foo"));
		underTest.setLocale(Locale.CANADA_FRENCH);
		underTest.setPaginationOverride(PaginationOverride.duplexShortEdge);
		underTest.setPrintConfig(PrintConfig.ZPL600);
		// Omit the creation of XCI document because that would require a real Adobe implementation to be available.
//		underTest.setXci(new MockDocumentFactory().create(new byte[0]));

		assertNotEmpty(underTest);
	}

	private static void assertNotEmpty(PrintedOutputOptionsImpl printedOutputOptions) {
		com.adobe.fd.output.api.PrintedOutputOptions adobePrintedOutputOptions = AdobeOutputServiceAdapter.toAdobePrintedOutputOptions(printedOutputOptions);
		assertNotEquals(emptyPrintedOutputOptions.getContentRoot(), adobePrintedOutputOptions.getContentRoot());
		assertNotEquals(emptyPrintedOutputOptions.getCopies(), adobePrintedOutputOptions.getCopies());
		assertNotEquals(emptyPrintedOutputOptions.getDebugDir(), adobePrintedOutputOptions.getDebugDir());
		assertNotEquals(emptyPrintedOutputOptions.getLocale(), adobePrintedOutputOptions.getLocale());
		assertNotEquals(emptyPrintedOutputOptions.getPaginationOverride(), adobePrintedOutputOptions.getPaginationOverride());
		assertNotNull(adobePrintedOutputOptions.getPrintConfig());
		assertNotEquals(emptyPrintedOutputOptions.getPrintConfig(), adobePrintedOutputOptions.getPrintConfig());
//		assertNotEquals(emptyPrintedOutputOptions.getXci(), adobePrintedOutputOptions.getXci());
	}

}
