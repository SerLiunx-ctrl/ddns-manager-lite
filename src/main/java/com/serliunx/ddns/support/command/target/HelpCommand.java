package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.ManagerLite;
import com.serliunx.ddns.support.command.AbstractCommand;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * 指令: help
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2025/1/15
 */
public class HelpCommand extends AbstractCommand {

	private static final Logger log = ManagerLite.getLogger();

	public HelpCommand() {
		super("help", null, "查看帮助信息", "help cmd");
	}

	@Override
	public boolean onCommand(String[] args) {
		System.out.println(Arrays.toString(args));
		return true;
	}
}