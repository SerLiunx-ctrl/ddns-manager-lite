package com.serliunx.ddns.support.command.target;

import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.support.command.AbstractCommand;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指令: config
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/22
 */
public class ConfigCommand extends AbstractCommand {

    /**
     * 配置信息
     */
    private final Configuration configuration;

    public ConfigCommand(Configuration configuration) {
        super("config", null, "调整配置信息", "config <配置项> 新的值");
        this.configuration = configuration;
    }

    @Override
    public boolean onCommand(String[] args) {
        if (!hasArgs(args) ||
                args.length < 2) {
            log.warn("用法 => {}", getUsage());
            return true;
        }
        final String target = args[0];
        final String value = args[1];
        return configuration.modify(target, value);
    }

    @Override
    public List<String> getArgs() {
        final Map<String, String> allKeyAndValue;
        if (configuration == null ||
                (allKeyAndValue = configuration.getAllKeyAndValue()) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allKeyAndValue.keySet());
    }

    @Override
    public void onComplete(LineReader reader, ParsedLine line, int index, List<Candidate> candidates) {
        if (index < 1) {
            return;
        }

        final String currentWord = line.word();

        // 补全配置键
        if (index == 1) {
            final Map<String, String> allKeyAndValue;
            if (configuration == null ||
                    (allKeyAndValue = configuration.getAllKeyAndValue()) == null) {
                return;
            }
            allKeyAndValue.keySet().forEach(k -> {
                if (k.startsWith(currentWord)) {
                    candidates.add(new Candidate(k));
                }
            });
        }
    }
}
