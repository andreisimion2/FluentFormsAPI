package com._4point.aem.fluentforms.api;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com._4point.aem.fluentforms.impl.CrxUrlHandler;

class PathOrUrlTest {

	@BeforeAll
	static void setUpAll() throws Exception {
		CrxUrlHandler.enableCrxProtocol();
	}
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@ParameterizedTest
	@ValueSource(strings = { "C:/foo/bar", "foo", "foo/bar", "C:\\foo\\bar", "\\\\foo\\bar", "C:/foo/bar/", "\\\\foo\\bar\\" })
	void testFromString_Path(String path) {
		PathOrUrl result = PathOrUrl.from(path);
		assertTrue(result.isPath(), "Expected that isPath() would be true");
		assertFalse(result.isUrl(), "Expected that isUrl() would be false");
		assertFalse(result.isCrxUrl(), "Expected that isCrxUrl() would be false");
		Path expected = Paths.get(path);
		assertEquals(expected, result.getPath());
		assertEquals(expected.toString(), result.toString());
	}

	@ParameterizedTest
	@ValueSource(strings = { "http://example.com", "https://example.com", "file:///~/calendar" })
	void testFromString_Url(String url) throws MalformedURLException {
		PathOrUrl result = PathOrUrl.from(url);
		assertFalse(result.isPath(), "Expected that isPath() would be false");
		assertTrue(result.isUrl(), "Expected that isUrl() would be true");
		assertFalse(result.isCrxUrl(), "Expected that isCrxUrl() would be false");
		URL expected = new URL(url);
		assertEquals(expected, result.getUrl());
		assertEquals(expected.toString(), result.toString());
	}

	@ParameterizedTest
	@ValueSource(strings = { "crx:/content/dam/formsanddocument", "crx://content/dam/formsanddocument", "crx://foo./?foo?#?#%foo/bar?foo" })
	void testFromString_CrxUrl(String url) throws MalformedURLException {
		PathOrUrl result = PathOrUrl.from(url);
		assertFalse(result.isPath(), "Expected that isPath() would be false");
		assertFalse(result.isUrl(), "Expected that isUrl() would be false");
		assertTrue(result.isCrxUrl(), "Expected that isCrxUrl() would be true");
		String crxUrl = result.getCrxUrl();
		assertEquals(url, crxUrl);
		assertEquals(url, result.toString());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })	// I'd like to find a crx: example that causes an invalid URL, but have been unable to find one.
	void testFromString_Invalid(String str) {
		IllegalArgumentException iaex = assertThrows(IllegalArgumentException.class, ()->PathOrUrl.from(str));
	}

	@EnabledOnOs(OS.WINDOWS)	// Only execute on Windows, because crap://more/crap is a valid unix path.
	@ParameterizedTest
	@ValueSource(strings = { "crap://more/crap" })
	void testFromString_InvalidWindows(String str) {
		IllegalArgumentException iaex = assertThrows(IllegalArgumentException.class, ()->PathOrUrl.from(str));
	}

	@Test
	void testFromString_Null() {
		assertThrows(NullPointerException.class, ()->PathOrUrl.from((String)null));
	}
	
	@Test
	void testFromPath_Null() {
		assertThrows(NullPointerException.class, ()->PathOrUrl.from((Path)null));
	}
	
	@Test
	void testFromUrl_Null() {
		assertThrows(NullPointerException.class, ()->PathOrUrl.from((URL)null));
	}
	
	private enum FilenameScenario {
		HAPPY_PATH("/foo/bar/test.txt", "test.txt", "\\foo\\bar", TestType.PATH),
		HAPPY_PATH2("test.txt", "test.txt", null, TestType.PATH),
		HAPPY_URL("http://foo/bar/testUrl.html", "testUrl.html", "http://foo/bar/", TestType.URL),
		HAPPY_URL_FILE("file:///~/calendar", "calendar", "file:/~/", TestType.URL),
		HAPPY_CRXURL("crx:/foo/bar/testForm.xdp", "testForm.xdp", "crx:/foo/bar/", TestType.CRX); 

		private enum TestType {
			PATH, URL, CRX;
		}
		private final String input;
		private final String expectedFilename;
		private final String expectedParent;
		private final TestType testType;

		private FilenameScenario(String input, String expectedFilename, String expectedParent, TestType testType) {
			this.input = input;
			this.expectedFilename = expectedFilename;
			this.expectedParent = expectedParent;
			this.testType = testType;
		}
	}
	
	/**
	 * Test PathOrUrl that was constructed from a String and constructed from a strongly typed object.
	 * 
	 */
	@ParameterizedTest
	@EnumSource
	void test_Filename(FilenameScenario scenario) throws Exception {
		PathOrUrl underTest = PathOrUrl.from(scenario.input);
		assertEquals(scenario.expectedFilename, underTest.getFilename().get());
		if (scenario.testType == FilenameScenario.TestType.PATH) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(Paths.get(scenario.input));
			assertEquals(scenario.expectedFilename, underTestOtherTest.getFilename().get());
			if (scenario.expectedParent == null) {
				assertFalse(underTestOtherTest.getParent().isPresent());
			} else {
				assertEquals(scenario.expectedParent, underTestOtherTest.getParent().get().toString());
			}
		}
		if (scenario.testType == FilenameScenario.TestType.URL) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(new URL(scenario.input));
			assertEquals(scenario.expectedFilename, underTestOtherTest.getFilename().get());
			if (scenario.expectedParent == null) {
				assertFalse(underTestOtherTest.getParent().isPresent());
			} else {
				assertEquals(scenario.expectedParent, underTestOtherTest.getParent().get().toString());
			}
		}
		if (scenario.testType == FilenameScenario.TestType.CRX) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(scenario.input);
			assertEquals(scenario.expectedFilename, underTestOtherTest.getFilename().get());
			if (scenario.expectedParent == null) {
				assertFalse(underTestOtherTest.getParent().isPresent());
			} else {
				assertEquals(scenario.expectedParent, underTestOtherTest.getParent().get().toString());
			}
		}
	}
	
	private enum EmptyFilenameScenario {
		HAPPY_PATH("\\\\foo\\bar", TestType.PATH),
		HAPPY_PATH2("\\\\foo\\bar\\", TestType.PATH),
		HAPPY_URL("https://example.com/foo/bar/", TestType.URL),
		HAPPY_CRXURL("crx://content/dam/formsanddocument/", TestType.CRX); 

		private enum TestType {
			PATH, URL, CRX;
		}
		private final String input;
		private final TestType testType;

		private EmptyFilenameScenario(String input, TestType testType) {
			this.input = input;
			this.testType = testType;
		}
	}
	
	/**
	 * Test PathOrUrl that was constructed from a String and constructed from a strongly typed object.
	 * 
	 */
	@ParameterizedTest
	@EnumSource
	void test_EmptyFilename(EmptyFilenameScenario scenario) throws Exception {
		PathOrUrl underTest = PathOrUrl.from(scenario.input);
		assertFalse(underTest.getFilename().isPresent(), ()->"Expected to be empty, but was '" + underTest.getFilename().get() + "'.");
		if (scenario.testType == EmptyFilenameScenario.TestType.PATH) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(Paths.get(scenario.input));
			assertFalse(underTestOtherTest.getFilename().isPresent(), ()->"Expected filename to be empty, but was '" + underTest.getFilename().get() + "'.");
			// The Path object does not seem to handle UNC names very well.  It returns null.
			// The docs for PathOrUrl.getParent() says that it returns Path.getParent(), so that's what I am testing for even though
			// that's not really what I would expect.  Not sure if UNC names are going to be a problem for anyone, so I am just following the docs.
			assertFalse(underTestOtherTest.getParent().isPresent(), ()->"Expected parent to be empty, but was '" + underTest.getFilename().get() + "'.");
		}
		if (scenario.testType == EmptyFilenameScenario.TestType.URL) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(new URL(scenario.input));
			assertFalse(underTestOtherTest.getFilename().isPresent(), ()->"Expected to be empty, but was '" + underTest.getFilename().get() + "'.");
			assertTrue(underTestOtherTest.getParent().isPresent(), ()->"Expected parent to not be empty.");
			assertSame(underTestOtherTest.getParent().get(), underTestOtherTest, "Expected parent would match the PathOrUrl under test.");
		}
		if (scenario.testType == EmptyFilenameScenario.TestType.CRX) {
			PathOrUrl underTestOtherTest = PathOrUrl.from(scenario.input);
			assertFalse(underTestOtherTest.getFilename().isPresent(), ()->"Expected to be empty, but was '" + underTest.getFilename().get() + "'.");
			assertTrue(underTestOtherTest.getParent().isPresent(), ()->"Expected parent to not be empty.");
			assertSame(underTestOtherTest.getParent().get(), underTestOtherTest, "Expected parent would match the PathOrUrl under test.");
		}
	}

	@Test
	void test_toPath() throws Exception {
		Path testDataPathRel = Paths.get("foo", "bar");
		Path testDataPathAbs = Paths.get("/foo/bar");
		URL testDataUrl = new URL("ftp", "host", 2323, testDataPathAbs.toString().replace('\\', '/'));
		
		assertAll(
				()->assertEquals(testDataPathRel, PathOrUrl.from(testDataPathRel).toPath()),
				()->assertEquals(testDataPathAbs, PathOrUrl.from(testDataPathAbs).toPath()),
				()->assertEquals(testDataPathAbs, PathOrUrl.from(testDataUrl).toPath()),
				()->assertEquals(testDataPathAbs, PathOrUrl.from("crx:" + testDataPathAbs.toString().replace('\\', '/')).toPath()),
				()->assertEquals(testDataPathAbs, PathOrUrl.from("https://foo" + testDataPathAbs.toString().replace('\\', '/')).toPath()),
				()->assertEquals(testDataPathAbs, PathOrUrl.from("file://" + testDataPathAbs.toString().replace('\\', '/')).toPath())
		);

	}
}
