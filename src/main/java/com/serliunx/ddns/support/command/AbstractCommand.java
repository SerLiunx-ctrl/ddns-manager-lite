package com.serliunx.ddns.support.command;

import com.serliunx.ddns.ManagerLite;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指令的抽象实现
 * <li> 实现公共逻辑及定义具体逻辑
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public abstract class AbstractCommand implements Command {

	private final String name;
	private final List<Command> subCommands;
	private final String description;
	private final String usage;

	protected final Logger log = ManagerLite.getLogger();

	public AbstractCommand(String name, List<Command> subCommands, String description, String usage) {
		this.name = name;
		this.subCommands = subCommands;
		this.description = description;
		this.usage = usage;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Command> getSubCommands() {
		return subCommands;
	}

	@Override
	public synchronized void addSubCommand(Command command) {
		subCommands.add(command);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getUsage() {
		return usage;
	}

	/**
	 * 指令逻辑默认实现: 调用子命令
	 *
	 * @param args	当前指令参数
	 * @return	成功执行返回真, 否则返回假. (目前没影响)
	 */
	@Override
	public boolean onCommand(String[] args) {
		if (!hasArgs(args) ||
				args.length < 2) {
			log.warn("用法 => {}", getUsage());
			return true;
		}

		final String subCommand = args[0];
		List<Command> subCommands = getSubCommands();
		for (Command command : subCommands) {
			if (command.getName().equalsIgnoreCase(subCommand)) {
				return command.onCommand(CommandDispatcher.splitArgs(args));
			}
		}
		return false;
	}

	@Override
	public List<String> getArgs() {
		if (subCommands == null ||
				subCommands.isEmpty()) {
			return new ArrayList<>();
		}

		return subCommands.stream()
				.map(Command::getName)
				.collect(Collectors.toList());
	}

	@Override
	public void onComplete(LineReader reader, ParsedLine line, int index, List<Candidate> candidates) {
		if (index < 1) {
			return;
		}

		final String currentWord = line.word();
		// 补全子命令
		final List<Command> subCommands = getSubCommands();
		if (index == 1) {
			subCommands.forEach(c -> {
				if (c.getName().startsWith(currentWord)) {
					candidates.add(new Candidate(c.getName()));
				}
			});
		} else { // 交给子命令补全
			for (Command c : subCommands) {
				if (c.getName().equals(line.words().get(1))) {
					c.onComplete(reader, line, index, candidates);
					return;
				}
			}
		}
	}

	protected boolean hasArgs(String[] args) {
		return args.length > 0;
	}
}
