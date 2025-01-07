package com.serliunx.ddns.support.okhttp;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.constant.ConfigurationKeys;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Http 客户端工具类
 * <li> 使用okhttp实现
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.2
 * @since 2024/11/6
 */
public final class HttpClient {

	private static OkHttpClient CLIENT;

	private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
	private static final int DEFAULT_OVERTIME = 3;

	private HttpClient() {throw new UnsupportedOperationException();}

	static {
		CLIENT = new OkHttpClient.Builder()
				.connectTimeout(DEFAULT_OVERTIME, TimeUnit.SECONDS)
				.readTimeout(DEFAULT_OVERTIME, TimeUnit.SECONDS)
				.writeTimeout(DEFAULT_OVERTIME, TimeUnit.SECONDS)
				.build();
	}

	/**
	 * 发送GET请求
	 *
	 * @param url 请求地址
	 * @return 响应
	 */
	public static String httpGet(String url) {
		final Request request = new Request.Builder()
				.url(url)
				.get()
				.build();

		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()
					|| response.body() == null) {
				return null;
			}

			final ResponseBody responseBody = response.body();

            return responseBody.string();
		} catch (Exception e) {
			log.error("http 接口异常:", e);
		}
		return null;
	}

	/**
	 * 初始化
	 *
	 * @param configuration 配置信息
	 */
	public static void init(Configuration configuration) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		Integer overtime = configuration.getInteger(ConfigurationKeys.KEY_HTTP_OVERTIME, DEFAULT_OVERTIME);
		boolean proxyEnable = configuration.getBoolean(ConfigurationKeys.KEY_HTTP_PROXY_ENABLE, false);
		boolean authEnable = configuration.getBoolean(ConfigurationKeys.KEY_HTTP_PROXY_AUTH, false);
		String host = configuration.getString(ConfigurationKeys.KEY_HTTP_PROXY_HOST);
		Integer port = configuration.getInteger(ConfigurationKeys.KEY_HTTP_PROXY_PORT);
		String username = configuration.getString(ConfigurationKeys.KEY_HTTP_PROXY_USERNAME);
		String password = configuration.getString(ConfigurationKeys.KEY_HTTP_PROXY_PASSWORD);

		// 配置代理
		if (proxyEnable) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));

			builder.proxy(proxy);

			// 是否需要认证
			if (authEnable) {
				Authenticator authenticator = (route, response) -> {
                    String credential = Credentials.basic(username, password);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                };
				builder.proxyAuthenticator(authenticator);
			}
		}

		CLIENT = builder.connectTimeout(overtime, TimeUnit.SECONDS)
				.readTimeout(overtime, TimeUnit.SECONDS)
				.writeTimeout(overtime, TimeUnit.SECONDS)
				.build();
	}
}
