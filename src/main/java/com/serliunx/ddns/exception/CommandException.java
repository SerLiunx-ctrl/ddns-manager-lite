package com.serliunx.ddns.exception;

import com.serliunx.ddns.support.command.cmd.Command;

/**
 * 指令相关异常
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public abstract class CommandException extends RuntimeException {

	private Command command;

	public CommandException() {}

	public CommandException(String message) {
		super(message);
	}

	public CommandException(Command command) {
		this.command = command;
	}

	public CommandException(Command command, String message) {
		super(message);
		this.command = command;
	}

	public CommandException(Command command, String message, Throwable cause) {
		super(message, cause);
		this.command = command;
	}

	public CommandException(Command command, Throwable cause) {
		super(cause);
		this.command = command;
	}

	public CommandException(Command command, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.command = command;
	}
}
