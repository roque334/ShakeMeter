package redes2.p2.shakemeter;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class WaitingThread extends Thread {
//	ProgressDialog progress1;
	int waitingTime;
	
	public WaitingThread(int waitingTime){
		this.waitingTime = waitingTime;
	}
	
//	public WaitingThread(ProgressDialog p1){
//		this.progress1=p1;
//	}
	
	@Override
	public void run(){
		for (int i=0; i<waitingTime; i++){
	    	try {
				Thread.sleep(1000);
//				handleProgress1.sendMessage(handleProgress1.obtainMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}				
	    }
	}
	
//	Handler handleProgress1 = new Handler() {
//	    @Override
//	      public void handleMessage(Message msg) {
//	        super.handleMessage(msg);
//	        progress1.incrementProgressBy(1);
//	      }
//	};
	

}
