package com.serliunx.ddns.support.command;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Map;

/**
 * Jline 命令补全器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public class CommandCompleter implements Completer {

	private final CommandDispatcher commandDispatcher;

	public CommandCompleter(CommandDispatcher commandDispatcher) {
		this.commandDispatcher = commandDispatcher;
	}

	@Override
	public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
		final String currentWord = line.word();
		Map<String, Command> commands = commandDispatcher.getCommands();
		if (commands.isEmpty())
			return;

		// 第一个参数补全所有指令
		if (line.wordIndex() == 0) {
			commands.keySet().forEach(k -> {
				if (k.startsWith(currentWord))
					candidates.add(new Candidate(k));
			});
		} else { // 第二个及以后交由具体的指令进行补全逻辑
			final Command command = commands.get(line.words().get(0));
			if (command == null)
				return;
			command.onComplete(reader, line, line.wordIndex(), candidates);
		}
	}
}
