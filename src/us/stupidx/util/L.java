package us.stupidx.util;

import android.util.Log;

/**
 * My Logger 
 * @author erichau
 *
 */
public class L {
	private static final String TAG = "DG";
	
	public static void d(String pos, String...vs ){
		String msg = pos + " [[[ ";
		for (String v : vs) {
			msg += v + "; ";
		}
		msg  += " ]]]";
		Log.d(TAG, msg);
	}
}
