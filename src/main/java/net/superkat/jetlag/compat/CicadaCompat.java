package net.superkat.jetlag.compat;

import net.superkat.jetlag.JetLagMain;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;

public class CicadaCompat implements CicadaEntrypoint {

    @Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/Superkat32/Jet-Lag/master/src/main/resources/cicada/jetlag/conversations.json")
                        .or(JsonSource.fromResource("cicada/jetlag/conversations.json")),
                str -> JetLagMain.LOGGER.info("[jetlag] " + str)
        );
    }
}
