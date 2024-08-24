package net.superkat.jetlag.compat;

import net.superkat.jetlag.JetLagMain;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;

public class CicadaCompat implements CicadaEntrypoint {

    @Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromResource("cicada/jetlag/conversations.json"),
                JetLagMain.LOGGER::info
        );
    }
}
