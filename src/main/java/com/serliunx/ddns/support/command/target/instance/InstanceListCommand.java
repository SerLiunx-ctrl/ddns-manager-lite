package com.serliunx.ddns.support.command.target.instance;

import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.ConsoleStyleHelper;
import com.serliunx.ddns.support.SystemInitializer;
import com.serliunx.ddns.support.command.AbstractCommand;

import java.util.ArrayList;
import java.util.Set;

/**
 * 指令: instance list
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class InstanceListCommand extends AbstractCommand {

	private final SystemInitializer systemInitializer;

	public InstanceListCommand(SystemInitializer systemInitializer) {
		super("list", new ArrayList<>(), "列出所有实例", "instance list");
		Assert.notNull(systemInitializer);
		this.systemInitializer = systemInitializer;
	}

	@Override
	public boolean onCommand(String[] args) {
		Set<Instance> instances = systemInitializer.getInstances();
		System.out.println();

		instances.forEach(i -> ConsoleStyleHelper.coloredPrintf("&2%s&r(&3%s&r)", i.getName(), i.getType()));

		System.out.println();
		return true;
	}
}
