package com.serliunx.ddns.support.command.target.config;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

import static com.serliunx.ddns.support.ConsoleStyleHelper.coloredPrintf;

/**
 * 指令: config set
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class ConfigSetCommand extends AbstractCommand {

	/**
	 * 配置信息
	 */
	private final Configuration configuration;

	public ConfigSetCommand(Configuration configuration) {
		super("set", null, "设置指定配置项的值", "config set <配置项> <新的值>");
		this.configuration = configuration;
	}

	@Override
	public boolean onCommand(String[] args) {
		if (!hasArgs(args) ||
				args.length < 2) {
			System.out.println();
			coloredPrintf("&2用法 =>&r &6%s", getUsage());
			System.out.println();
			return true;
		}
		final String target = args[0];
		final String value = args[1];
		return configuration.modify(target, value);
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
