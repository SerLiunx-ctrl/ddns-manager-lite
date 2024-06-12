package com.serliunx.ddns.exception;

/**
 * 指令相关异常: 指令不存在.
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class CommandExceptionNotExistsException extends CommandException {

	public CommandExceptionNotExistsException(String message) {
		super(message);
	}
}
