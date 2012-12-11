package mobi.monaca.framework.util;

import android.os.AsyncTask.Status;
import android.os.Handler;
/**
 * Used to kick ass Android AsyncTask's bug where doInBackground() is not always get called
 * @param <A> parameter types for execute() method
 * @param <B> type used for onProgressUpdate()
 * @param <C> type returned by doInBackground()
 */
abstract public class MyAsyncTask<A, B, C> {

    protected boolean isExecuted = false;
    protected Thread backgroundThread = null; 
    final Handler handler = new Handler();
	private boolean mIsCancelled = false;
	private Status currentStatus = Status.PENDING;

    public void execute(final A ...a) {
        if (isExecuted) {
            throw new RuntimeException("This task is already executed.");
        }

        onPreExecute();
        isExecuted = true;
        currentStatus = Status.RUNNING;
        
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final C result = doInBackground(a);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                        currentStatus = Status.FINISHED;
                    }
                });
            }
        });

        backgroundThread.start();
    }
    
    protected void onPreExecute() {
	}
    
    protected void publishProgress(final B ...b){
    	handler.post(new Runnable() {
			
			@Override
			public void run() {
				onProgressUpdate(b);
			}
		});
    }

    public void cancel(boolean mayInterruptIfRunning) {
    	if(mayInterruptIfRunning){
    		if (backgroundThread != null) {
                backgroundThread.interrupt();
            }
    	}
        
        mIsCancelled = true;
    }
    
    public Status getStatus(){
    	return currentStatus;
    }
    
    public boolean isCancelled() {
    	return mIsCancelled ;
	}

    public boolean isExecuted() {
        return isExecuted;
    }

    abstract protected C doInBackground(A ...a);

    protected void onPostExecute(C result) {
    }
    
    protected void onProgressUpdate(B ...b) {
	}

}
