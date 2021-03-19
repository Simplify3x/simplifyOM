package com.simplifyOM.HttpUtility;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApiUtil {

	final static Logger logger = LoggerFactory.getLogger(ApiUtil.class);

	public static JSONObject get(String url, Map<String, String> queryParam, Map<String, String> headers)
			throws JSONException {
		JSONObject resp = null;
		try {
			CloseableHttpClient httpClient = getHttpClient(url);
			URIBuilder uri = new URIBuilder(url);
			HttpGet get = new HttpGet(uri.build());

			if (null != queryParam && !queryParam.isEmpty()) {
				queryParam.forEach((key, value) -> {
					uri.addParameter(key, value);
				});
			}

			if (null != headers && !headers.isEmpty()) {
				headers.forEach((k, v) -> {
					get.addHeader(k, v);
				});
			}
			CloseableHttpResponse response = httpClient.execute(get);
			int statusLine = response.getStatusLine().getStatusCode();
			logger.debug("In get {} {}", url, statusLine);
			HttpEntity entity = response.getEntity();
			resp = new JSONObject(EntityUtils.toString(entity));
		} catch (Exception e) {
			resp = new JSONObject().put("error", e.getMessage());
		}
		return resp;
	}

	public static CloseableHttpClient getHttpClient(String url)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		int timeout = 500; // seconds
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		if (url.startsWith("https://")) {
			SSLContext ssl = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
			return HttpClients.custom().setSSLContext(ssl).setSSLHostnameVerifier(new NoopHostnameVerifier())
					.setDefaultRequestConfig(config).build();
		} else {
			return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		}
	}
}
