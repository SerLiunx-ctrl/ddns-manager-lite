package com.serliunx.ddns.support.command;

import java.util.List;

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
	public String getDescription() {
		return description;
	}

	@Override
	public String getUsage() {
		return usage;
	}
}
