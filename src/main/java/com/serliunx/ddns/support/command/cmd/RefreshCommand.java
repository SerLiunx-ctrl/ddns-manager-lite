package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandManager;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class RefreshCommand extends AbstractCommand {

	public RefreshCommand(Command[] children, CommandManager commandManager) {
		super("refresh", children, commandManager);
	}

	public RefreshCommand(CommandManager commandManager) {
		this(null, commandManager);
	}

	@Override
	public boolean execute(String[] args) {
		return false;
	}
}
