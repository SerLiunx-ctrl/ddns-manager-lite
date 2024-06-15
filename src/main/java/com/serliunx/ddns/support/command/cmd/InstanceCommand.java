package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandManager;

import java.util.Arrays;

/**
 * 实例相关指令
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class InstanceCommand extends AbstractCommand {

	public InstanceCommand(Command[] children, CommandManager commandManager) {
		super("instance", children, commandManager);
	}

	public InstanceCommand(CommandManager commandManager) {
		this(null, commandManager);
	}

	@Override
	public boolean execute(String[] args) {
		System.out.println(Arrays.toString(args));
		return false;
	}
}
