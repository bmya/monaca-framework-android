package mobi.monaca.framework.test;

import mobi.monaca.framework.MonacaApplication;
import android.content.Context;
import android.test.ApplicationTestCase;

public class MonacaApplicationTest extends ApplicationTestCase<MonacaApplication> {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.createApplication();
    }
    
    public MonacaApplicationTest() {
        super(MonacaApplication.class);
    }
    
    public void testAllowedAccesses() {
        // Http access is allowed.
        assertTrue(MonacaApplication.allowAccess("http://google.com"));
        assertTrue(MonacaApplication.allowAccess("https://google.com"));
        assertTrue(MonacaApplication.allowAccess("http://127.0.0.1"));
        
        // Other application's file access is not allowed.
        assertTrue(!MonacaApplication.allowAccess("file:///data/hogehoge/index.html"));
        
        // Assets file access is not allowed.
        assertTrue(MonacaApplication.allowAccess("file:///android_asset/www/index.html"));
        
        // Application file access is allowed.
        Context context = this.getApplication().getApplicationContext();
        assertTrue(MonacaApplication.allowAccess("file://" + context.getApplicationInfo().dataDir + "/www/index.html"));
        assertTrue(MonacaApplication.allowAccess("file://" + context.getApplicationInfo().dataDir + "/files/hoge.txt"));
        
        // Application preference file access is not allowed.
        assertTrue(!MonacaApplication.allowAccess("file://" + context.getApplicationInfo().dataDir + "/shared_prefs/hoge.xml"));
    }

}
