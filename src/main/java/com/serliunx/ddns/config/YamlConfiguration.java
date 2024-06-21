package com.serliunx.ddns.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * yml/yaml格式的配置文件，目前用于语言文件
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/6/17
 */
public class YamlConfiguration extends FileConfiguration {

	public YamlConfiguration(String path, boolean refresh) {
		super(path);
		if (refresh)
			refresh();
	}

	public YamlConfiguration(String path) {
		this(path, false);
	}

	@Override
	protected void refresh0() {
		try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
			Yaml yaml = new Yaml();
			Map<String, String> keyAndValue = yaml.load(inputStream);
			if (keyAndValue != null)
				valueMap.putAll(keyAndValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void load0() {
		// do nothing.
	}
}
