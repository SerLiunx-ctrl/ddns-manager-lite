package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.ManagerLite;
import com.serliunx.ddns.support.command.AbstractCommand;
import com.serliunx.ddns.support.command.Command;
import com.serliunx.ddns.support.command.CommandDispatcher;
import org.slf4j.Logger;

import java.util.Map;

import static com.serliunx.ddns.support.command.CommandDispatcher.hasArgs;

/**
 * 指令: help
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public class HelpCommand extends AbstractCommand {

	private static final Logger log = ManagerLite.getLogger();

	public HelpCommand() {
		super("help", null, "查看帮助信息", "help <指令>");
	}

	@Override
	public boolean onCommand(String[] args) {
		final Map<String, Command> commands = CommandDispatcher.getInstance().getCommands();

		log.info("==========================================");
		if (hasArgs(args)) {
			final String cmd = args[0];
			final Command command = commands.get(cmd);
			if (command == null) {
				log.warn("无法找到指令 {} 的相关信息, 请使用 help 查看可用的指令及帮助!", cmd);
			} else {
				log.info("指令:\t{}\t-\t{}\t-\t{}", cmd, command.getDescription(), command.getUsage());
			}
		} else {
			commands.forEach((k, v) -> {
				// 忽略 help 自身
				if (k.equals(getName())) {
					return;
				}
				log.info("{}\t-\t{}\t-\t{}", k, v.getDescription(), v.getUsage());
			});
			log.info("exit\t-\t退出程序\t-\texit");
			log.info("");
			log.info("使用 help <指令> 来查看更详细的帮助信息.");
		}
		log.info("==========================================");
		return true;
	}
}