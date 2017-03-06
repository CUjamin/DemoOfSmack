package cuj.ejabberd.support.listener;

import cuj.ejabberd.support.handler.FileTransferHandler;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;
import java.io.IOException;

/**
 * Created by cujamin on 2016/12/16.
 */
public class TransferListener implements FileTransferListener
{
    static final private Logger log = Logger.getLogger(TransferListener.class);
//    private String fileaddress;
    private FileTransferHandler fileTransferHandler;

    public TransferListener(FileTransferHandler fileTransferHandler)
    {
        this.fileTransferHandler = fileTransferHandler;
    }

    @Override
    public void fileTransferRequest(final FileTransferRequest request)
    {
        fileTransferHandler.handle(request);
//        log.info("RECV new file : "+request.getFileName());
//        IncomingFileTransfer transfer = request.accept();
//        try {
//            String description = request.getDescription();
//            File file = new File("E:/workspace/freelink-im-xmpp/DEMO/download" ,request.getFileName());
//
//            //在目录fileDir目录下新建一个名字为request.getFileName()的文件
//            //开始接收文件(将传输过来的文件内容输出到file中)
//            transfer.recieveFile(file);
//            //此处执行文件传输监听
//        } catch (SmackException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
