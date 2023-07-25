package io.papermc.minegpt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AskGptCommand implements CommandExecutor {
    private final GptClient gpt3;

    public AskGptCommand(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();

        gpt3 = new GptClient(config.getString("token"), config.getInt("timeout"), config.getStringList("rules"), plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String message = String.join(" ", args);

        TextComponent alert = Component.text("Processing your query, \"" + message +"\"");
        Bukkit.getServer().sendMessage(alert);

        new Thread(() -> {
            String usage =
                    """
                    Tokens used in this response %s
                    """;


            ChatReturnResult result = gpt3.requestResult(message);

            System.out.println("OpenAI responded with " + result.response);

            TextComponent chatPost = Component.empty()
                    .append(Component.text("[").color(TextColor.fromHexString("#57A0D2")))
                    .append(Component.text("OpenAI").color(TextColor.fromHexString("#6693F5")))
                    .append(Component.text("]").color(TextColor.fromHexString("#57A0D2")))
                    .append(Component.text(": ").color(TextColor.fromHexString("#6693F5")))
                    .append(Component.text(result.response + "\n").color(TextColor.fromHexString("#0080FE")))
                    .append(Component.text(String.format(usage, result.tokensUsed)).color(TextColor.fromHexString("#73C2FB")));

            Bukkit.getServer().sendMessage(chatPost);
        }).start();
        return true;
    }
}
