package cuj.ejabberd.support.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by cujamin on 2016/12/28.
 */
public class DeliveryReceiptHandler {
    private static final Logger log = Logger.getLogger(DeliveryReceiptHandler.class);
    public void handle(String fromJid, String toJid, String receiptId, Stanza receipt)
    {
        log.info("\nDeliveryReceiptHandler not be Override\n收到回执：fromJid:"+fromJid+";toJid: "+toJid+";receiptId:"+receiptId+";receipt:"+receipt+"\n");
    }
}
