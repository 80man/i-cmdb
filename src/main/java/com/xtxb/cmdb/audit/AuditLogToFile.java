package com.xtxb.cmdb.audit;

import com.xtxb.cmdb.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月13日-下午5:53
 * <p>
 * <p>
 * 将审计日志保存于文件中
 */
@Component
public class AuditLogToFile  implements AuditLog, ApplicationRunner {

    @Autowired
    private LoggerUtil log;

    private LinkedList logList=new LinkedList();
    private File file=null;
    private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    private String currentDate="";

    public AuditLogToFile(){
        initLog();
    }

    @Override
    public void log(String hostName, String ip, String user, String sessionid, String requestID, String type, String uri, String note) {
        logList.add(new String[]{hostName,ip,user,sessionid,requestID,type,uri,note});
    }

    public void writeToFile(){
        String[][] temp=null;
        synchronized (logList){
            if(logList.size()>0) {
                temp = new String[logList.size()][];
                int i = 0;
                while (logList.size() > 0) {
                    temp[i++] = (String[])logList.poll();
                }
            }
        }

        if(temp!=null){
            try(
                    BufferedWriter bw=new BufferedWriter(new FileWriter(file,true));
                    ) {
                for(String[] str: temp)
                    bw.write(format(str)+"\n");
                bw.flush();
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    writeToFile();
                    if(!df.format(new Date()).equals(currentDate)){
                        initLog();
                    }
                }
            }
        }).start();
    }

    private String format(String[] array){
        return System.currentTimeMillis()+" "+array[5]+"  Client:[IP:"+array[1]+"  Name:"+array[0]+" User:"+array[2]+"] SID:"+array[3]+" RID:"+array[4]+"  URI:"+array[6]+"  Msg:"+array[7];
    }


    private void initLog(){
        currentDate=df.format(new Date());
        file=new File(currentDate+"_api.log");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }
}
