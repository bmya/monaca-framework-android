package mobi.monaca.framework.template;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetTemplateResource implements TemplateResource {

    protected AssetManager assetManager;

    public AssetTemplateResource(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public AssetTemplateResource(Context context) {
        this(context.getResources().getAssets());
    }

    protected String normalize(String path) {
        if (path.startsWith("file:///android_asset/")) {
            path = path.substring("file:///android_asset/".length());
        }
        return path;
    }

    @Override
    public boolean exists(String path) {
        path = normalize(path);
        try {
            InputStream stream = assetManager.open(path);
            stream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Reader get(String path) {
        path = normalize(path);
        try {
            return new InputStreamReader(new BufferedInputStream(
                    assetManager.open(path)), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String resolve(String path, String from) {
        from = normalize(from);
        try {
            URI uri = new URI("file:///" + from);
            return uri.resolve(path).getPath().substring(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
