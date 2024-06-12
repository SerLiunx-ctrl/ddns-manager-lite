package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandArg;
import com.serliunx.ddns.support.command.CommandManager;

import java.util.List;

/**
 * 帮助指令: help
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class HelpCommand extends AbstractCommand {

	private static final String NO_ARGS_INFORMATION = "未找到相关参数信息";

	public HelpCommand(Command[] children, CommandManager commandManager) {
		super("help", children, commandManager);
	}

	public HelpCommand(CommandManager commandManager) {
		this(null, commandManager);
	}

	@Override
	public boolean execute(String[] args) {
		int length = args.length;
		if (length < 1)
			return false;
		Command targetCommand = commandManager.getCommand(args[0]);
		List<CommandArg> commandArgs = targetCommand.getArgs();
		if (commandArgs == null || commandArgs.isEmpty()) {
			System.out.println(NO_ARGS_INFORMATION);
			return true;
		}
		System.out.println(buildArgs(commandArgs));
		return true;
	}

	private String buildArgs(List<CommandArg> args) {
		StringBuilder argsBuilder = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			argsBuilder.append(args.get(i).getArg()).append(" - ").append(args.get(i).getDescription());
			if (i != args.size() - 1)
				argsBuilder.append("\n");
		}
		return argsBuilder.toString();
	}
}
