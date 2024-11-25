package com.serliunx.ddns.support.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.config.ConfigurationKeys;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static OkHttpClient CLIENT = null;

	private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
	private static final ObjectMapper JSON_MAPPER = new JsonMapper();
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
	 * 获取本机的ip地址
	 *
	 * @return 响应结果
	 */
	public static IPAddressResponse getIPAddress() {
		Request request = new Request.Builder()
				.url("http://ip-api.com/json")
				.get()
				.build();

		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful() || response.body() == null) {
				return null;
			}

			String body = response.body().string();
			if (body.isEmpty()) {
				return null;
			}

			return JSON_MAPPER.readValue(body, IPAddressResponse.class);
		} catch (Exception e) {
			log.error("ip地址获取异常:", e);
		}
		return null;
	}

	/**
	 * 初始化
	 *
	 * @param configuration 配置信息
	 */
	public static void init(Configuration configuration) {
		Integer overtime = configuration.getInteger(ConfigurationKeys.KEY_HTTP_OVERTIME, DEFAULT_OVERTIME);

		CLIENT = new OkHttpClient.Builder()
				.connectTimeout(overtime, TimeUnit.SECONDS)
				.readTimeout(overtime, TimeUnit.SECONDS)
				.writeTimeout(overtime, TimeUnit.SECONDS)
				.build();
	}
}
