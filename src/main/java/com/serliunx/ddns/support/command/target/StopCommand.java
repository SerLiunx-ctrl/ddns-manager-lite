package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.support.command.AbstractCommand;

/**
 * 指令: stop
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class StopCommand extends AbstractCommand {

	public StopCommand() {
		super("stop", null, "退出程序", "stop");
	}

	@Override
	public boolean onCommand(String[] args) {
		System.exit(0);
		return true;
	}
}
