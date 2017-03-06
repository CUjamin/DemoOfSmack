package cuj.ejabberd.domain;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by cujamin on 2017/2/6.
 */
public class IQMessage extends IQ{
    public static final String ELEMENT = "pubsub";
    public static final String NAMESPACE = "http://jabber.org/protocol/pubsub";

    public IQMessage() {
        super(ELEMENT, NAMESPACE);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        // N.B. We could use SimpleIQ here, but PubSub IQs will nearly *always* have packet extensions, which means that
        // SimpleIQs xml.setEmptyElement() is counter-productive in this case and we use xml.rightAngleBracket()
        // instead, as there are likely sub-elements to follow.
        xml.rightAngleBracket();
        return xml;
    }

}
