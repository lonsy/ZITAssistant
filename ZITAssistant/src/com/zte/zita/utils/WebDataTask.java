package com.zte.zita.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Web数据访问工具类
 * @author lonsy
 */
public class WebDataTask extends AsyncTask<String, Void, String> {
	private final String LOG_TAG = "WebDataTask";

	private WebDataProcessListener caller;

	public WebDataTask(WebDataProcessListener caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			return downloadUrl(urls[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		this.caller.onPostExecute(result);
	}

	private String downloadUrl(String myurl) throws Exception {
		InputStream is = null;

		try {
			/*
			 * URL url = new URL(myurl); HttpURLConnection conn =
			 * (HttpURLConnection) url.openConnection();
			 * conn.setReadTimeout(10000 milliseconds );
			 * conn.setConnectTimeout(15000 milliseconds );
			 * conn.setRequestMethod("GET"); conn.setDoInput(true);
			 * conn.connect();
			 */
			URL url = new URL(myurl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			/*CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream caInput = WebDataTask.class.getClassLoader().getResourceAsStream("zte.cer");
			Certificate ca;
			try {
				ca = cf.generateCertificate(caInput);
				System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
			} finally {
				caInput.close();
			}

			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);*/

			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSLContext.getInstance("TLS");
			// 绕过证书的验证
			context.init(null, new TrustManager[] { new DefaultTrustManager() }, null);
			conn.setSSLSocketFactory(context.getSocketFactory());
			
			// 忽略主机名验证  
            conn.setHostnameVerifier(new HostnameVerifier() {  
                @Override  
                public boolean verify(String arg0, SSLSession arg1) {  
                    return true;  
                }  
            });  

			// set Timeout and method
			conn.setReadTimeout(7000);
			conn.setConnectTimeout(7000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);

			conn.connect();
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bout.write(buffer, 0, len);
				}
				bout.close();
				return bout.toString();
			} else {
				throw new IOException();
			}
		} catch (Exception ex) {
			Log.v(LOG_TAG, "网络连接错误!");
			ex.printStackTrace();
			throw ex;
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	class DefaultTrustManager implements X509TrustManager {  
		  
        @Override  
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
        }  
  
        @Override  
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
        }  
  
        @Override  
        public X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
    }  
}
