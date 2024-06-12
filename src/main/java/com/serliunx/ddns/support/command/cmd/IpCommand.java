package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.NetworkContextHolder;
import com.serliunx.ddns.support.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ip 相关指令
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class IpCommand extends AbstractCommand {

	private static final Logger log = LoggerFactory.getLogger(IpCommand.class);

	public IpCommand(Command[] children, CommandManager commandManager) {
		super("ip", children, commandManager);
	}

	public IpCommand(CommandManager commandManager) {
		this(null, commandManager);
	}

	@Override
	public boolean execute(String[] args) {
		String newestIp = NetworkContextHolder.getIpAddress();
		log.info("当前最新IP地址为: {}", newestIp);
		return true;
	}
}
