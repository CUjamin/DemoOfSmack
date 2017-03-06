package cuj.ejabberd.support.thread.manager;

import cuj.ejabberd.support.handler.SubMessageHandler;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.pubsub.*;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.HashMap;

/**
 * Created by cujamin on 2017/1/3.
 */
public class PubSubManagerThread extends Thread {
    private static final Logger log = Logger.getLogger(PubSubManagerThread.class);
    private static PubSubManagerThread pubSubManagerThread = null;
    private String pubIntervalTimeInSec;
    private XMPPTCPConnection connection = null;
    private PubSubManager pubSubManager = null;
    private SubMessageHandler subMessageHandler = null;
    private String nodename = "time";

    /**
     * @param connection
     */
    private PubSubManagerThread(XMPPTCPConnection connection) {
        this.connection = connection;
        pubSubManager = new PubSubManager(connection);
    }

    public static PubSubManagerThread getInstance(XMPPTCPConnection connection) {
        if (null == pubSubManagerThread) {
            pubSubManagerThread = new PubSubManagerThread(connection);
        }
        return pubSubManagerThread;
    }

    public void run() {
        while (true) ;
    }

    public void sendMessageOnNode(String[] infos) {
        String pubSubNodeName = null;
        String message = null;
        if(null==infos)
        {
            pubSubNodeName = nodename;
            message = "" + System.currentTimeMillis();
        }
        else
        {
            switch (infos.length) {
                case 1:
                    pubSubNodeName = nodename;
                    message = infos[0];
                    break;
                case 2:
                    pubSubNodeName = infos[0];
                    message = infos[1];
                    break;
            }
        }
        try {
            if (null == pubSubNodeName) {
                log.info("nodeId is null");
                return;
            }

            LeafNode node = getPubNode(pubSubNodeName);

            if (null == message) {
                log.info("the pubsub message is timestamp");
            } else {
                log.info("the pubsub message is " + message);
            }
            publishMessage(node, message);
            log.info("---Publish ---: node name :" + pubSubNodeName + "; pubIntervalTimeInSec :" + pubIntervalTimeInSec);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private LeafNode getPubNode(String pubSubNodeName) {
        LeafNode leafNode = null;
        try {
            leafNode = pubSubManager.getNode(pubSubNodeName);
            log.info("get the old pub node :" + pubSubNodeName + " success");
        } catch (Exception e) {
            log.error("get the old pub node error, ERROR!!!!!");
        }
        if (null == leafNode) {
            leafNode = createnode(pubSubNodeName);
        }
        return leafNode;
    }

    private LeafNode createnode(String NodeID) {
        LeafNode leafnode = null;
//        ConfigureForm configureForm = getConfigureForm();
        try {
            leafnode = pubSubManager.createNode(NodeID);
//            leafnode.sendConfigurationForm(configureForm);
            log.info("create pub node :" + NodeID + "sucess");
        } catch (Exception e) {
            log.error("create pub node ERROR!!!!!!!!!!");
            e.printStackTrace();
        }
        return leafnode;
    }

    private ConfigureForm getConfigureForm() {
        ConfigureForm configureForm = new ConfigureForm(DataForm.Type.submit);
        configureForm.setNodeType(NodeType.leaf);
        configureForm.setAccessModel(AccessModel.open);
        configureForm.setPublishModel(PublishModel.open);
        configureForm.setPersistentItems(false);
        configureForm.setNotifyRetract(true);
        configureForm.setMaxItems(65535);
        return configureForm;
    }

    private void publishMessage(LeafNode node, String message) {
        String pubMessage = message;
        if (null == pubMessage) {
            pubMessage = "" + System.currentTimeMillis();
        }
        PayloadItem<SimplePayload> item = new PayloadItem<SimplePayload>(null, new SimplePayload("heartbeat", "pubsub:heartbeat", "<heartbeat xmlns='pubsub:heartbeat'><body>" + pubMessage + "</body></heartbeat>"));
        log.info("public message, time:" + System.currentTimeMillis());
        try {
            node.publish(item);
        } catch (SmackException.NotConnectedException s) {
            log.info("Not Connected Exception : " + s);
        }
    }

    public boolean sub(String nodeName)
    {
        return sub(nodeName , null);
    }
    public boolean sub(String nodeName , SubMessageHandler subMessageHandler) {
        SubMessageHandler handler = null;
        if(null==subMessageHandler) handler = new SubMessageHandler();
        else handler = subMessageHandler;
        LeafNode node = null;
        try {
            node = pubSubManager.getNode(nodeName);
            if (null == node) {
                log.info("get node error ,can not sub the node : " + nodeName);
                return false;
            }
            node.addItemEventListener(new ItemEventListener<PayloadItem>() {
                public void handlePublishedItems(ItemPublishEvent evt) {
                    subMessageHandler.handle(evt);
                }
            });
            node.subscribe(connection.getUser());
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            log.error("NotConnectedException", e);
            return false;
        }
        return true;
    }
}
