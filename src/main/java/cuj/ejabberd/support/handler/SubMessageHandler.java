package cuj.ejabberd.support.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.PayloadItem;

/**
 * Created by cujamin on 2017/2/20.
 */
public class SubMessageHandler {
    private static final Logger log = Logger.getLogger(SubMessageHandler.class);
    public void handle(ItemPublishEvent evt)
    {
        for (Object obj : evt.getItems()) {
            PayloadItem item = (PayloadItem) obj;
            System.out.println("SUB: " + item.getPayload().toString() + "|||" + "Date:" + evt.getPublishedDate());
        }
        System.out.println(evt.toString());

    }
}
