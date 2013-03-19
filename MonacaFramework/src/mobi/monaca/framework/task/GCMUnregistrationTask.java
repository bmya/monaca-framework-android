package mobi.monaca.framework.task;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobi.monaca.framework.MonacaApplication;
import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.APIUtil;
import mobi.monaca.utils.MonacaConst;
import mobi.monaca.utils.MyAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GCMUnregistrationTask extends MyAsyncTask<Void, Void, Integer> {
	private static final String TAG = GCMUnregistrationTask.class.getSimpleName();
	MonacaApplication app;
	String regId;
	public GCMUnregistrationTask(MonacaApplication app, String regId) {
		this.app= app;
		this.regId = regId;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result != null) {
			MyLog.d(TAG, "response :" + result);
			if (result.equals(Integer.valueOf(200))) {
				MyLog.d(TAG, "succeed");
				GCMRegistrationIdSenderTask.clearAlreadyRegisteredPreference(app, regId);
			} else {
				MyLog.d(TAG, "failed");
			}
		}
	}

	@Override
	protected Integer doInBackground(Void... a) {
		MyLog.d(TAG, "start unregistration");
		try {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("registrationId", regId));

			URL url = new URL(MonacaConst.getPushUnegistrationAPIUrl(app, app.getPushProjectId()));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setConnectTimeout(10 * 1000);// 10sec

			OutputStream os = connection.getOutputStream();
			String postStr = APIUtil.getQuery(list);

			PrintStream ps = new PrintStream(os);
			ps.print(postStr);
			ps.close();
			os.close();

			connection.connect();

			return Integer.valueOf(connection.getResponseCode());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
