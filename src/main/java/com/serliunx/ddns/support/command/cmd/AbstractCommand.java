package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandArg;
import com.serliunx.ddns.support.command.CommandManager;

import java.util.Collections;
import java.util.List;

/**
 * 指令抽象实现
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public abstract class AbstractCommand implements Command {

	protected final String name;
	protected final Command[] children;
	protected final CommandManager commandManager;

	public AbstractCommand(String name, Command[] children, CommandManager commandManager) {
		this.name = name;
		this.children = children;
		this.commandManager = commandManager;
	}

	public AbstractCommand(String name, CommandManager commandManager) {
		this(name, null, commandManager);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Command[] getChildren() {
		return children;
	}

	@Override
	public List<CommandArg> getArgs() {
		return Collections.emptyList();
	}

	@Override
	public abstract boolean execute(String[] args);

	@Override
	public String toString() {
		return "Command{" +
				"name='" + name + '\'' +
				'}';
	}
}
