package mobi.monaca.framework;

import java.io.IOException;
import java.io.Reader;

import java.io.StringReader;
import mobi.monaca.framework.template.TemplateResource;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.util.Log;

/** An implementation of TemplateResource retrieves a template over HTTP. */
public class RemoteTemplateResource implements TemplateResource {

    protected HttpClient httpClient;

    public RemoteTemplateResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public boolean exists(String url) {
        return true;
    }

    @Override
    public Reader get(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException(
                    "This class doesn't support such scheme: " + url);
        }
        return new StringReader(fetch(url));
    }

    @Override
    public String resolve(String path, String currentUrl) {
        // resolve relative path.
        return Uri.withAppendedPath(Uri.parse(currentUrl), "../" + path)
                .toString();
    }

    /** Do fetch. */
    protected String fetch(String httpUrl) {
        MonacaApplication.closeStaleConnections();
        HttpGet request = new HttpGet(httpUrl);

        try {
            String result = httpClient.execute(request,
                    new ResponseHandler<String>() {
                        @Override
                        public String handleResponse(HttpResponse response)
                                throws ClientProtocolException, IOException {
                            if (response.getStatusLine().getStatusCode() > 0) {
                                try {
                                    return EntityUtils.toString(
                                            response.getEntity(), "UTF8");
                                } catch (Exception e) {
                                    Log.d(getClass().getSimpleName(),
                                            "Request fail.", e);
                                    throw new RuntimeException(e);
                                }
                            }
                            return "Request fail: "
                                    + response.getStatusLine().getStatusCode();
                        }

                    });
            MonacaApplication.closeStaleConnections();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
