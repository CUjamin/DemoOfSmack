package cuj.ejabberd.support.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;
import java.io.IOException;

/**
 * Created by cujamin on 2016/12/28.
 */
public class FileTransferHandler {
    private static final Logger log = Logger.getLogger(FileTransferHandler.class);
    public void handle(FileTransferRequest request)
    {
        log.info("\n FileTransferHandler not be Override \n RECV new file : "+request.getFileName()+"\ndescription:"+request.getDescription()+"\n"+
        "the file is in E:/workspace/freelink-im-xmpp/DEMO/download\n");
        IncomingFileTransfer transfer = request.accept();
        try {
            File file = new File("E:/workspace/freelink-im-xmpp/DEMO/download" ,request.getFileName());
            //在目录fileDir目录下新建一个名字为request.getFileName()的文件
            //开始接收文件(将传输过来的文件内容输出到file中)
            transfer.recieveFile(file);
            //此处执行文件传输监听
        } catch (SmackException se) {
            se.printStackTrace();
        }catch (IOException ie)
        {
            ie.printStackTrace();
        }
    }
}
