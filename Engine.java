/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package socialtrade1;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import org.json.*;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author Harry
 */
public class Engine extends Thread {
    static int[][] ThreadStatus=new int[20][20];
    String hfHiddenFieldID="";
    String userId="";
    String  password="";
    String linksData="";
    String viewstateGenerator="";
    String viewstate="";
    String vs="";
    int status=0;
    int PositionNumber=1;
    int remaing_links=0;
    int state=0;
    int runningSerial=0;
    int timeTaken=0;
    String Cookie="";
    String url="";
    String body="";
    String referer="";
    PostMethod Postmethod=null;
    HttpClient Client=null;
    Document doc=null;
    int sleepingTime=30;
    long previousTime=0;
    String[][] links=new String[500][5];
    String NextWorkID="";
    String WorkID="";
    String response="";
    LocalDate indianTime = LocalDate.now(ZoneId.of("Asia/Kolkata"));
    Engine(String uname,String password,int PositionNumber)
    {
        
        getIndiaTime();
        Utilities.Data[PositionNumber][0]=uname;
        this.PositionNumber=PositionNumber;
        this.password=password;
        state=0;
    }
    
    String  getIndiaTime()
    {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        sd.setTimeZone(TimeZone.getTimeZone("IST"));
        return sd.format(date);
    }
    void restart()
    {
        System.out.println("Restaring.................. "+Utilities.Data[PositionNumber][0]);
        runningSerial=0;
        Utilities.completed_links[PositionNumber]=0;
        Utilities.remaingTime[PositionNumber]=0;
        Utilities.runningState[PositionNumber]=0;
        state=-1;
        
    }
    void login()
    {
        //http://sserp.ablazeerp.com/Default.aspx
        url="https://www.socialtrade.biz";
        body="__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=&txtEmailID="+Utilities.Data[PositionNumber][0]+"&txtPassword="+encode(password)+"&CndSignIn=Sign+In&HFUserNo=&HFECode=&HFMCode=&HFParentID=&HFUserID=&txtNewsName=&txtNewsEmail=&txtName=&txtEmail=&txtMobile=&txtSponsorID=&txtSponsorName=&ddlPosition=0&txtPasswordSignUp=&txtConfirmPassword=&HFPassword=&txtUserIDRecover=";
        body=body.replace("+","%2B").replace("/","%2F");
        if(Utilities.ThreadLogin)
        {
            PostThread();
            Cookie=Utilities.ThreadCookie[PositionNumber];
            userId=Utilities.userID[PositionNumber];
        }
        
        viewstateGenerator=byId("__VIEWSTATEGENERATOR");
        
        
    }
    void dashboard()
    {
        
        url="https://www.socialtrade.biz/User/dashboard.aspx";
        
        
        get();
        viewstate=byId("__VIEWSTATE");
        viewstateGenerator=byId("__VIEWSTATEGENERATOR");;
        save("1");
        url="https://www.socialtrade.biz/User/dashboard.aspx";
        
        body="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24UpPanel1%7Cctl00%24ContentPlaceHolder1%24lnkViewWork2&__EVENTTARGET=ctl00%24ContentPlaceHolder1%24lnkViewWork2&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE="+viewstate+"&__VIEWSTATEGENERATOR="+viewstateGenerator+"&ctl00%24ContentPlaceHolder1%24HFCreateDate="+getIndiaTime()+"&ctl00%24ContentPlaceHolder1%24HFInVoiceNo=&ctl00%24ContentPlaceHolder1%24HFType=&ctl00%24ContentPlaceHolder1%24Accordion1_AccordionExtender_ClientState=0&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtAccountHolderName=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTFromAccount=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTBankName=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTBranch=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24ddlBank=915020024019456&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTToAccount=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTAmmount=57500.00&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTUdrNO=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24txtNEFTDate=&ctl00%24ContentPlaceHolder1%24AccPan2_content%24ddlPlan=0&ctl00%24ContentPlaceHolder1%24AccPOS_content%24txtPOSCardHolderName=&ctl00%24ContentPlaceHolder1%24AccPOS_content%24txtPOSMobile=&ctl00%24ContentPlaceHolder1%24AccPOS_content%24txtPOSPaymentID=PNSOL_&ctl00%24ContentPlaceHolder1%24AccPOS_content%24txtPosDate=&ctl00%24ContentPlaceHolder1%24AccPOS_content%24txtPOSAmount=57500.00&ctl00%24ContentPlaceHolder1%24AccPOS_content%24ddlPosPlan=0&__ASYNCPOST=true&";
        body=body.replace("+","%2B").replace("/","%2F");
        
        PostThread();
        save("2");
    }
    
    void Post()
    {
        
        try{
            
            byte[] postData       = body.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            
            URL    url1            = new URL( url );
            HttpURLConnection con= (HttpURLConnection) url1.openConnection();
            con.setDoOutput( true );
            con.setInstanceFollowRedirects( true );
            con.setRequestMethod( "POST" );
            con.setRequestProperty( "Content-Type", " application/json; charset=UTF-8");
            con.setRequestProperty( "charset", "utf-8");
            con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            con.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Host","www.socialtrade.biz");
            con.setRequestProperty("Cookie",Cookie);
            con.setRequestProperty("Referer","https://www.socialtrade.biz/User/TodayTask179.aspx");
            
            DataOutputStream wr = new DataOutputStream( con.getOutputStream());
            wr.write( postData );
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response1 = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response1.append(inputLine);
            }
            response=response1.toString();
            
            in.close();
        }
        catch(Exception m){
            
            
        }
        
    }
    
    
    public void run()
    {
        Automate();
        
    }
    public void Automate()
    {
        
        
        while(state<7)
        {
            
            Utilities.runningState[PositionNumber]=state;
            
            set(""+state);
            if(state==0)
            {
                login();
                
            }
            
            if(state==1)
            {
                dashboard();
                
            }
            if(state==2)
            {
                
                url="https://www.socialtrade.biz/User/dashboard.aspx";
                referer="https://www.socialtrade.biz/User/dashboard.aspx";
                get();
                save("2_1");
                url="https://www.socialtrade.biz/User/MyePoints.aspx";
                get();
                save("2_2");
                if(Utilities.EnablePendingWork)
                    pendingWork();
                getPoints();
                
                
            }
            if(state==3)
                
            {
                
                url="https://www.socialtrade.biz/User/MyePoints.aspx/ApplyBooster";
                referer="https://www.socialtrade.biz/User/ViewAdvertisements.aspx";
                url="https://www.socialtrade.biz/User/ViewAdvertisements.aspx/ApplyBooster";
                body="{ 'userId':'"+userId+"','flag':'isavailablebooster'}";
                new ChildThread(url, body, Cookie,PositionNumber,state).start();
                
                save("3_4");
                
                url="https://www.socialtrade.biz/User/MyePoints.aspx/GetWorkHistory";
                body="{ 'pageNumber':'1', 'pageSize': '40', 'userId': '"+userId+"'}";
                PostThread();
                
                save("3_6");
                linksData=response;
                ParseJson();
                
                NextWorkID=links[runningSerial][1];
            }
            if(state==4)
            {
                
                previousTime=new Date().getTime();
                long t1=new Date().getTime();
                
                int nn=0;
                try
                {
                    nn=Integer.parseInt(NextWorkID.trim());
                }
                catch(Exception m)
                {
                    
                    if(Utilities.completed_links[PositionNumber]==0)
                    {
                        
                        
                        restart();
                    }
                    else
                    {
                        state=10;
                    }
                }
                
                if(nn!=0)
                {
                    updateWork();
                    if(Utilities.Enable_SuperFast)superFast();
                    if(Utilities.Enable_unlimted[PositionNumber])unlimtedPoints();
                    long t2=new Date().getTime();
                    int t3=(int)(t2-t1)/1000;
                    sleepingTime=Utilities.maxSleepingTime-t3;
                    if(Utilities.EnableFastMode) sleepingTime=1;
                    if(sleepingTime<0)sleepingTime=1;
                    
                }
                
            }
            if(state==5)
            {
                try
                {
                    int nn=Integer.parseInt(NextWorkID.trim());
                    
                    updateWork1();
                    if((status==200&&state>3)&&remaing_links>0)
                    {
                        String t="NextWorkID";
                        int n1=response.indexOf(t);
                        n1=response.indexOf(":",n1+3);
                        int n2=response.indexOf("\",",n1);
                        NextWorkID=response.substring(n1+2, n2);
                        runningSerial++;
                        Utilities.remaingTime[PositionNumber]=sleepingTime;
                        set("5_1");
                        sleep(sleepingTime);
                        timeTaken=timeTaken();
                        remaing_links--;
                        Utilities.completed_links[PositionNumber]++;
                        state=3;
                        if(remaing_links%10==0)getPoints();
                        
                    }
                    if(remaing_links==0)
                    {
                        set("Done");
                        sleep(5000);
                        state=20;
                    }
                    else
                    {
                        state++;
                    }
                    
                }
                catch(Exception m)
                {
                    m.printStackTrace();
                    restart();
                }
                
            }
            else if(state<6)
            {
                state++;
            }
        }
        
    }
    
    void pendingWork()
    {
        set("pending Work");
        String wd="2016-12-12";
        for(int i=10;i<13;i++)
        {
            for(int j=1;j<31;j++)
            {
                wd="2016-"+i+"-"+j;
                url="https://www.socialtrade.biz/User/MyePoints.aspx/RequestPendingTask";
                body="{ 'userId': '"+userId+"', 'pendingWorkDate': '"+wd+"'}";
                PostThread();
                if(response.indexOf("SETPENDINGWORK")!=-1)
                {
                    System.out.println(Utilities.Data[PositionNumber][0]+" Work Found on "+wd);
                }
            }
        }
        set("No pending Work");
        state=20;
    }
    void payOuts()
    {
        
        url="https://www.socialtrade.biz/User/Payouts.aspx";
        referer="https://www.socialtrade.biz/User/dashboard.aspx";
        get();
        doc = Jsoup.parse( response);
        String s=doc.getElementById("ctl00_ContentPlaceHolder1_GvUser_ctl02_lblDate").html();
        
        String d=indianTime.getDayOfMonth()+"-"+indianTime.getMonth().toString().substring(0,3)+"-"+indianTime.getYear();
        if(indianTime.getDayOfMonth()<10)d="0"+d.toLowerCase();
        d=d.toLowerCase();
        s=s.toLowerCase();
        if(d.indexOf(s)!=-1)
        {
            set("submitted");
            
        }
        
        
    }
    
    void save(String s)
    {
        new Save(s,response).start();
    }
    
    void pp(Exception m)
    {
        if(Utilities.EnableStackTrace)
            m.printStackTrace();
        else m.getMessage();
        
    }
    
    void set(String s)
    {
        Utilities.messages[PositionNumber]=s;
    }
    
    int  timeTaken()
    {
        long  currentTime=new Date().getTime();
        long totalTime=currentTime-previousTime;
        int TotalSeconds=(int)totalTime/1000;
        Utilities.messages[PositionNumber]=""+TotalSeconds+" S";
        
        return TotalSeconds;
    }
    
    void exit()
    {
        System.exit(0);
    }
    public String encode(String s)
    {
        String returnString="";
        try {
            returnString=  URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  returnString;
    }
    
    void ParseJson()
    {
        save(Utilities.Data[PositionNumber][0]);
        try {
            JSONObject obj = new JSONObject(response);
            
            String pageName= obj.getJSONObject("d").get("TodayTaskLists").toString();
            JSONArray obj1 = new JSONArray(pageName);
            remaing_links=0;
            String stage="A";
            
            for (int i = 0; i < obj1.length(); i++)
            {
                
                if(obj1.getJSONObject(i).getString("Stage").indexOf(stage)!=-1||Utilities.Enable_unlimted[PositionNumber])
                {
                    
                    links[remaing_links][0]=obj1.getJSONObject(i).getString("WorkID");
                    links[remaing_links][2]=obj1.getJSONObject(i).getString("Link");
                    links[remaing_links][3]=obj1.getJSONObject(i).getString("CampaignID");
                    try
                    {
                        links[remaing_links][1]=obj1.getJSONObject(i+1).getString("WorkID");
                    }
                    catch(Exception m)
                    {
                        pp(m);
                        links[remaing_links][1]=obj1.getJSONObject(i).getString("WorkID");
                    }
                    
                    remaing_links++;
                }
                else
                {
                    Utilities.completed_links[PositionNumber]++;
                }
            }
            if(remaing_links==0&& Utilities.completed_links[PositionNumber]>0)
            {
                if(Utilities.Enable_unlimted[PositionNumber]==false)
                {
                    System.out.println("getting  position "+PositionNumber);
                    System.out.println("No more links "+Utilities.Enable_unlimted[PositionNumber]);
                    state=20;
                    set("Done");
                }
                
                
            }
            
        }
        
        catch (Exception m)
        {
            m.printStackTrace();
            System.out.println(Utilities.Data[PositionNumber][0]+" Error parse json restarting");
            restart();
            
        }
    }
    
    
    void updateWork()
    {
        set("UW");
        url="https://www.socialtrade.biz/User/MyePoints.aspx/UpdateTaskWork";
        body="{_user: {\"Username\":\""+userId+"\",\"WorkID\":"+links[runningSerial][0]+",\"CurrentFlag\":\"hand\",\"PointsType\":250,\"Password\":"+links[runningSerial][3]+",\"Flag\":\"hand\",\"NextWorkID\":\""+links[runningSerial][1]+"\"}}" ;
        PostThread();
        status=ThreadStatus[PositionNumber][state];
        
    }
    
    void superFast()
    {
        
        System.out.println("superfast enabled");
        while(remaing_links-->=0)
        {
            runningSerial++;
            Utilities.completed_links[PositionNumber]++;
            body="{_user: {\"Username\":\""+userId+"\",\"WorkID\":"+links[runningSerial][0]+",\"CurrentFlag\":\"hand\",\"PointsType\":250,\"Password\":"+links[runningSerial][3]+",\"Flag\":\"hand\",\"NextWorkID\":\""+links[runningSerial][1]+"\"}}" ;
            PostThread();
            System.out.println(remaing_links+"running "+runningSerial);
            sleep(sleepingTime);
            
            
        }
        
    }
    
    void unlimtedPoints()
    {
        
        int points=Integer.parseInt(getPoints());
        int remain=Utilities.limit-points;
        
        while(remain>0)
        {
            Utilities.completed_links[PositionNumber]++;
            url="https://www.socialtrade.biz/User/MyePoints.aspx/UpdateTaskWork";
            body="{_user: {\"Username\":\""+userId+"\",\"WorkID\":"+links[runningSerial][0]+",\"CurrentFlag\":\"hand\",\"PointsType\":250,\"Password\":"+links[runningSerial][3]+",\"Flag\":\"hand\",\"NextWorkID\":\""+links[runningSerial][1]+"\"}}" ;
            Post();
            if(points%10==0)getPoints();
            remain--;
            points++;
            Utilities.points[PositionNumber]=""+points;
        }
        
    }
    
    public String  getPoints()
    {
        try
        {
            response="0";
            
            url="https://www.socialtrade.biz/User/BuyEpoints.aspx/CalculateRedeemePoints";
            body="{'userId':'"+userId+"' }";
            Post();
            response=response.replace("{\"d\":\"","").replace("\"}", "");
            Utilities.points[PositionNumber]=response;
        }
        catch(Exception m)
        {
            
        }
        
        return response.trim();
    }
    void updateWork1()
    {
        
        url="https://www.socialtrade.biz/User/TodayTask179.aspx";
        body="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24UpPanel1%7Cctl00%24ContentPlaceHolder1%24Button2&"
                + "hfHiddenFieldID="+byId("hfHiddenFieldID")+"&__EVENTTARGET=&"+byId("__EVENTARGUMENT")+"=&__LASTFOCUS="
                + "&__VIEWSTATE="+byId("__VIEWSTATE")+"&__VIEWSTATEGENERATOR="+byId("__VIEWSTATEGENERATOR")+""
                + "&ctl00%24ContentPlaceHolder1%24HFValue=&ctl00%24ContentPlaceHolder1%24HFWorkDonationID=&"
                + "ctl00%24ContentPlaceHolder1%24txtDonationStart=&ctl00%24ContentPlaceHolder1%24txtDonationEnd=&"
                + "ctl00%24ContentPlaceHolder1%24txtClick=&ctl00%24ContentPlaceHolder1%24hfuserid=&"
                + "ctl00%24ContentPlaceHolder1%24txtRemark=&__ASYNCPOST=true&ctl00%24ContentPlaceHolder1%24Button2=Refresh";
        body=body.replace("+","%2B").replace("/","%2F");
        new ChildThread(url,body,Cookie,PositionNumber,state).start();
        
        
    }
    void waitForThread()
    {
        
        int count=0;
        while(ThreadStatus[PositionNumber][state]==-1&&count<Utilities.PostThreadTimeOut)
            try {
                
                Thread.sleep(1000);
                count++;
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
        response=Utilities.ThreadResponse[PositionNumber][state];
        status= Engine.ThreadStatus[PositionNumber][state];
    }
    
    public void  PostThread()
    {
        new ChildThread(url, body, Cookie,PositionNumber,state).start();
        waitForThread();
    }
    
    private void get()  {
        
        try
        {
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie",Cookie);
            con.setRequestProperty("Referer", referer);
            con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response1 = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response1.append(inputLine);
            }
            in.close();
            response=response1.toString();
            
            
        }
        catch(Exception m)
        {
            pp(m);
        }
        
    }
    void sleep(int se)
    {
        try
        {
            Thread.sleep(se*1000);
        }
        catch(Exception m)
        {
            pp(m);
        }
    }
    
    String byId(String s)
    {
        Element info=null;
        String returnstring="";
        try
        {
            doc = Jsoup.parse( response);
            info = doc.getElementById(s);
            returnstring=""+info.val().replace("=","%3D");
        }
        catch(Exception m)
        {
            //pp(m);
        }
        return returnstring;
    }
    
    
}
