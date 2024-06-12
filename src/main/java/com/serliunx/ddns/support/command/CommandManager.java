package com.serliunx.ddns.support.command;

import com.serliunx.ddns.exception.CommandExceptionNotExistsException;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.command.cmd.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令管理
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public final class CommandManager {

	private static final Logger log = LoggerFactory.getLogger(CommandManager.class);

	private final Map<String, Command> commandMap = new HashMap<>();
	private final SystemInitializer initializer;

	public CommandManager(SystemInitializer systemInitializer) {
		this.initializer = systemInitializer;
	}

	public Command getCommand(String commandName) {
		Command command = commandMap.get(commandName);
		if (command == null)
			throw new CommandExceptionNotExistsException(String.format("指令 %s 不存在!", commandName));
		return command;
	}

	public SystemInitializer getInitializer() {
		return initializer;
	}

	/**
	 * 注册指令
	 * @param command 指令
	 */
	public void register(Command command) {
		boolean result = register0(command);
		if (result)
			log.debug("成功注册指令 => {}", command.getName());
		else
			log.warn("指令注册失败 => {}", command);
	}

	public void handle(String commandString) {
		String[] args = commandString.split(" ");
		if (args.length < 1) {
			log.warn("未找到相关指令! 输入 help 查看指令手册.");
			return;
		}
		Command cmd = commandMap.get(args[0]);
		if (cmd == null) {
			log.warn("未找到相关指令! 输入 help 查看指令手册.");
			return;
		}
		String[] subArgs = new String[args.length - 1];
		System.arraycopy(args, 1, subArgs, 0, args.length - 1);
		cmd.execute(subArgs);
	}

	private boolean register0(Command command) {
		if (command == null) return false;
		String cmdName = command.getName();
		if (cmdName == null || cmdName.isEmpty())
			return false;
		if (commandMap.containsKey(cmdName))
			return false;
		commandMap.put(cmdName, command);
		return true;
	}
}
