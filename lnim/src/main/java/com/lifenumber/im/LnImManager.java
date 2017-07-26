package com.lifenumber.im;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.parsing.ExceptionLoggingCallback;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hanguojing on 2017/7/24 18:15
 */

public class LnImManager {
    private static final String TAG = "LnImManager";

    private LnChatManager chatManager;
    private SessionManager sessionManager;
    private String host = "ims.186life.com";
    private int port = 5222;

    public void initLnImManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private LnImManager() {
    }

    private static class LnImManagerHolder {
        private static final LnImManager Instance = new LnImManager();
    }

    public static LnImManager getInstance() {
        return LnImManagerHolder.Instance;
    }

    public LnChatManager getLnChatManager() {
        if (chatManager == null) {
            chatManager = LnChatManager.getInstance();
        }
        return chatManager;
    }

    public SessionManager getSessionManager() {
        if (sessionManager == null) {
            sessionManager = SessionManager.getInstance();
        }
        return sessionManager;
    }

    public boolean login(String username, String password) {
        XMPPTCPConnectionConfiguration xmpptcpConnectionConfiguration = retrieveConnectionConfiguration(username, password);


        XMPPTCPConnection xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
        SmackConfiguration.setDefaultReplyTimeout(1000);
        SmackConfiguration.DEBUG = true;
        getSessionManager().initializeSession(xmpptcpConnection, username, password);

        try {
            xmpptcpConnection.connect();
            xmpptcpConnection.login(username, password);
            return true;
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public boolean logout(){
        XMPPTCPConnection connection = getSessionManager().getConnection();
        if(connection != null && connection.isConnected()){
            connection.disconnect();
            return true;
        }
        return false;
    }

    private XMPPTCPConnectionConfiguration retrieveConnectionConfiguration(String username, String password)  {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setUsernameAndPassword(username, password);
        builder.setHost(host).setPort(port);
        try {
            builder.setXmppDomain(host);
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            builder.setDebuggerEnabled(true);
            builder.setSendPresence(false);
            builder.setConnectTimeout(10000);
            SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
            TLSUtils.acceptAllCertificates(builder);

            return builder.build();
        } catch (XmppStringprepException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public XMPPTCPConnection getConnection() {
        return getSessionManager().getConnection();
    }

    public boolean registerAccount(String username, String password) {

        // todo 1. 获取 connection 和 sessionManager中的connection 不一样
        // todo 2. 上传用户名密码进行注册
        // todo 3. 获取注册结果
        // todo 4. 返回注册结果

        final AbstractXMPPConnection connection = getRegConnection();

        return false;
    }

    @Nullable
    private static AbstractXMPPConnection getRegConnection() {
        int port = 5222;
        String serverName = "ims.186life.com";

        final XMPPTCPConnectionConfiguration.Builder builder;
        try {
            builder = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("username", "password")
                    .setXmppDomain(serverName)
                    .setPort(port)
                    .setCompressionEnabled(true);
            final XMPPTCPConnectionConfiguration configuration = builder.build();
            final AbstractXMPPConnection connection = new XMPPTCPConnection(configuration);
            connection.setParsingExceptionCallback(new ExceptionLoggingCallback());
            connection.connect();
            return connection;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return null;
    }


}
