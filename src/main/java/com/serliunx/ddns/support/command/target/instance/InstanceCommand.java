package com.serliunx.ddns.support.command.target.instance;

import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.command.AbstractCommand;

import java.util.ArrayList;

/**
 * 指令: instance
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class InstanceCommand extends AbstractCommand {

	public InstanceCommand(SystemInitializer systemInitializer) {
		super("instance", new ArrayList<>(), "实例相关指令", "instance list/add/...");
		// 子命令: list
		addSubCommand(new InstanceListCommand(systemInitializer));
	}
}
