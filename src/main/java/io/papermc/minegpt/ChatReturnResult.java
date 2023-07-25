package io.papermc.minegpt;

public class ChatReturnResult {
    public String response;
    public long tokensUsed;

    public ChatReturnResult(String response, long tokensUsed) {
        this.response = response;
        this.tokensUsed = tokensUsed;
    }
}
