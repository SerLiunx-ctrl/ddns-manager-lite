package com.serliunx.ddns.support.command.target.config;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

/**
 * 指令: config get
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class ConfigGetCommand extends AbstractCommand {

	/**
	 * 配置信息
	 */
	private final Configuration configuration;

	public ConfigGetCommand(Configuration configuration) {
		super("get", null, "获取指定配置项的值", "config get <配置项>");
		this.configuration = configuration;
	}

	@Override
	public boolean onCommand(String[] args) {
		if (!hasArgs(args) ||
				args.length < 1) {
			log.warn("用法 => {}", getUsage());
			return true;
		}
		System.out.println(configuration.getString(args[0]));
		return true;
	}

	@Override
	public List<String> getArgs() {
		return ConfigCommandHelper.getArgs(configuration);
	}

	@Override
	public void onComplete(LineReader reader, ParsedLine line, int index, List<Candidate> candidates) {
		if (index == 2)
			ConfigCommandHelper.completeConfigKeys(configuration, line.word(), candidates);
	}
}
