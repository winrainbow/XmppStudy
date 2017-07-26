package com.lifenumber.im;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by hanguojing on 2017/7/24 18:34
 */

public class LnChatManager {
    private static class LnChatManagerHolder{
        private static final LnChatManager instance = new LnChatManager();
    }
    public static LnChatManager getInstance() {
        return LnChatManagerHolder.instance;
    }
    public boolean sendMessage(Message message,String toJid){
        return false;
    }

}
