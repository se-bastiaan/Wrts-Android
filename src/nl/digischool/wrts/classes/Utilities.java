package nl.digischool.wrts.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import nl.digischool.wrts.R;
import org.xml.sax.InputSource;

import javax.net.ssl.*;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Utilities {

	/**
     * Send debug LogCat message
     * @param message
     */
	public static void log(Object tag, Object message) {
		if(Params.LOG_ENABLED) Log.d(tag.toString(), message.toString());
	}

    /**
     * First character to uppercase, other characters lowercase
     * @param str Input string
     * @return String first uppercase
     */
    public static String uppercaseFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

    /**
     * First character to uppercase, other characters the same
     * @param str Input string
     * @return String first uppercase
     */
    public static String uppercaseOnlyFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String getLanguageName(Integer i) {
        String alpha = "abcdefghij";
        return "lang-" + alpha.charAt(i);
    }

    public static String getWordName(Integer i) {
        String alpha = "abcdefghij";
        return "word-" + alpha.charAt(i);
    }

    public static String getWordNameByLanguage(String language) {
        Utilities.log("getWordNameByLanguage", language);
        String postfix = language.substring(language.length() - 1);
        return "word-" + postfix;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isOnline(Context context, Boolean showMessage) {
        Boolean online = Utilities.isOnline(context);
        if(!online && showMessage) {

            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        return online;
    }

    /**
     * Stringify Inputstream
     */
    public static String convertToString(InputStreamReader input) {
        try {
            StringBuilder sb = new StringBuilder();
            int c;
            while((c = input.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Trust all hosts in urlconnection
     */
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public static void enableDisableViewGroupClickable(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setClickable(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroupClickable((ViewGroup) view, enabled);
            }
        }
    }

}
