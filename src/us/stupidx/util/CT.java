package us.stupidx.util;

/**
 * Common Tools
 * 
 * @author erichau
 * 
 */
public class CT {
	public static boolean isEmpty(String v) {
		return v == null || "".equals(v);
	}

	public static boolean notEmpty(String v) {
		return !isEmpty(v);
	}
}
