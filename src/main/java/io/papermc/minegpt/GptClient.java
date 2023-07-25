package io.papermc.minegpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import retrofit2.Retrofit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

public class GptClient {
    private final OpenAiService service;
    private final List<String> rules;
    private final Plugin plugin;

    public GptClient(String token, long timeout, List<String> rules, Plugin plugin) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(token, Duration.ofSeconds(timeout))
                .newBuilder()
                .build();

        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);

        this.rules = rules;
        this.plugin = plugin;
        service = new OpenAiService(api);
    }

    public ChatReturnResult requestResult(String question) {
        List<ChatMessage> messages = new ArrayList<>();
        FileConfiguration configuration = plugin.getConfig();
        rules.forEach(rule -> messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), rule)));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), question));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(configuration.getString("model"))
                .messages(messages)
                .maxTokens(configuration.getInt("maxTokens"))
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);

        return new ChatReturnResult(
                result.getChoices().get(0).getMessage().getContent(),
                result.getUsage().getTotalTokens()
        );
    }
}