package com.lifenumber.im;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by hanguojing on 2017/7/24 18:15
 */

public class SessionManager {
    private AbstractXMPPConnection connection;
    private String username;
    private String password;
    private static class SessionManagerHolder{
        private static final SessionManager instance = new SessionManager();
    }
    public static SessionManager getInstance() {
        return SessionManagerHolder.instance;
    }

    public XMPPTCPConnection getConnection() {

        return (XMPPTCPConnection) connection;
    }
    public void initializeSession(AbstractXMPPConnection connection, String username, String password) {
        this.connection = connection;
        this.username = username;
        this.password = password;
        // create workgroup session
//        personalDataManager = PrivateDataManager.getInstanceFor( getConnection() );

    }

}
