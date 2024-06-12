package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandManager;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class ExitCommand extends AbstractCommand {

	public ExitCommand(Command[] children, CommandManager commandManager) {
		super("exit", children, commandManager);
	}

	public ExitCommand(CommandManager commandManager) {
		this(null, commandManager);
	}

	@Override
	public boolean execute(String[] args) {
		System.exit(0);
		return false;
	}
}
