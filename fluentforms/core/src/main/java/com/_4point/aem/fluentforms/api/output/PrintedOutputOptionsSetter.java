package com._4point.aem.fluentforms.api.output;

import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.PathOrUrl;
import com.adobe.fd.output.api.PaginationOverride;

public interface PrintedOutputOptionsSetter {

	PrintedOutputOptionsSetter setContentRoot(PathOrUrl pathOrUrl);

	default PrintedOutputOptionsSetter setContentRoot(Path path) {
		return setContentRoot(new PathOrUrl(path));
	}

	default PrintedOutputOptionsSetter setContentRoot(URL url) {
		return setContentRoot(new PathOrUrl(url));
	}

	PrintedOutputOptionsSetter setCopies(int copies);

	PrintedOutputOptionsSetter setDebugDir(Path debugDir);

	PrintedOutputOptionsSetter setLocale(Locale locale);

	PrintedOutputOptionsSetter setPaginationOverride(PaginationOverride paginationOverride);

	PrintedOutputOptionsSetter setPrintConfig(PrintConfig printConfig);

	PrintedOutputOptionsSetter setXci(Document xci);

}