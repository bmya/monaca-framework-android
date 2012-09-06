package mobi.monaca.framework.util;

import android.os.Handler;

abstract public class MyAsyncTask<B> {

    protected boolean isExecuted = false;
    protected Thread backgroundThread = null;

    public void execute() {
        if (isExecuted) {
            throw new RuntimeException("This task is already executed.");
        }

        isExecuted = true;

        final Handler handler = new Handler();
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final B result = doInBackground();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                    }
                });
            }
        });

        backgroundThread.start();
    }

    public void cancel() {
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    abstract protected B doInBackground();

    protected void onPostExecute(B result) {
    }

}
