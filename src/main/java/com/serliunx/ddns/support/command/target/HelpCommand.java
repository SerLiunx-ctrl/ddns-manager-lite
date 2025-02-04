package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.support.command.AbstractCommand;
import com.serliunx.ddns.support.command.Command;
import com.serliunx.ddns.support.command.CommandDispatcher;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指令: help
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public class HelpCommand extends AbstractCommand {

	public HelpCommand() {
		super("help", null, "查看帮助信息", "help <指令>");
	}

	@Override
	public boolean onCommand(String[] args) {
		final Map<String, Command> commands = getAllCommands();

		log.info("==========================================");
		if (hasArgs(args)) {
			final String cmd = args[0];
			final Command command = commands.get(cmd);
			if (command == null) {
				log.warn("无法找到指令 {} 的相关信息, 请使用 help 查看可用的指令及帮助!", cmd);
			} else {
				log.info("指令:{} - {} - {}", cmd, command.getDescription(), command.getUsage());
			}
		} else {
			commands.forEach((k, v) -> {
				// 忽略 help 自身
				if (k.equals(getName())) {
					return;
				}
				log.info("{} - {} - {}", k, v.getDescription(), v.getUsage());
			});
			log.info("");
			log.info("使用 help <指令> 来查看更详细的帮助信息.");
		}
		log.info("==========================================");
		return true;
	}

	@Override
	public List<String> getArgs() {
		final Map<String, Command> commands = getAllCommands();
		if (commands == null ||
				commands.isEmpty()) {
			return new ArrayList<>();
		}
		return new ArrayList<>(commands.keySet());
	}

	@Override
	public void onComplete(LineReader reader, ParsedLine line, int index, List<Candidate> candidates) {
		final Map<String, Command> commands = getAllCommands();
		if (commands == null ||
				commands.isEmpty() || index < 1) {
			return;
		}

		final String currentWord = line.word();

		if (index == 1) {
			commands.keySet().forEach(k -> {
				if (k.equals("help")) {
					return;
				}
				if (k.startsWith(currentWord)) {
					candidates.add(new Candidate(k));
				}
			});
		}
	}

	/**
	 * 获取所有指令
	 */
	private Map<String, Command> getAllCommands() {
		return CommandDispatcher.getInstance().getCommands();
	}
}