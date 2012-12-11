package mobi.monaca.framework.template.test;

import mobi.monaca.framework.template.AssetTemplateResource;
import mobi.monaca.framework.template.TemplateCompiler;
import mobi.monaca.framework.template.TemplateExecuter;
import mobi.monaca.framework.template.TemplateResource;
import android.test.InstrumentationTestCase;
import android.content.res.AssetManager;

public class AssetTemplateResourceTest extends InstrumentationTestCase {
	
	public AssetTemplateResource createTemplateResource() {
		AssetManager assetManager = getInstrumentation().getContext().getResources().getAssets();
		AssetTemplateResource templateResource = new AssetTemplateResource(assetManager);
		return templateResource;
	}
	
	public void testBasicOperation() throws Throwable {
		TemplateResource templateResource = createTemplateResource();
		TemplateCompiler compiler = new TemplateCompiler(templateResource);
		
		assertEquals(
			TemplateExecuter.build(templateResource).execute(compiler.compileFrom("pathtest/a/a.html")),
			"hoge"
		);
	}
	
	public void testPathResolver() {
		TemplateResource templateResource = createTemplateResource();
		
		assertEquals("hoge.html", templateResource.resolve("hoge.html", "hoge.html"));
		assertEquals("hoge.html", templateResource.resolve("./hoge.html", "hoge.html"));
		assertEquals("hoge/fuga.html", templateResource.resolve("../hoge/fuga.html", "hoge/index.html"));
		assertEquals("hoge/fuga.html", templateResource.resolve("./hoge/fuga.html", "index.html"));
	}
	
}
