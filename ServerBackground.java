import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerBackground {
    public static final int PORT = 9999;
    String nickname_arr[]=new String[10];
	int people_num=0;
	int Client_Add_Trigger=0;
	int say_hi_count=0;
    BufferedReader in=null;
    BufferedWriter out=null;
    ServerSocket serverSocket=null;
    Socket socket=null;
    BufferedWriter BufferedWriter_arr[]=new BufferedWriter[10];
    BufferedReader BufferedReader_arr[]=new BufferedReader[10];
    Socket Socket_Arr[]=new Socket[10];
    public void ServerBackground()
	{
        try {
            // 1. 서버 소켓 생성          
            serverSocket = new ServerSocket(PORT);
            // 2. 바인딩
            
            
            // 3. 요청 대기
            while(true) 
            {
            	System.out.println("대기중");
                socket = serverSocket.accept();
                Socket_Arr[people_num]=socket;
                System.out.println("연결됨"); 
                in=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
              
                String num_String=String.valueOf(people_num);
                out.write(num_String+"\n");
                out.flush();
                people_num=people_num+1;
                nickname_arr[people_num-1]=in.readLine();
                BufferedWriter_arr[people_num-1]=out;
                BufferedReader_arr[people_num-1]=in;
             
                Server_Thread server_thread =new Server_Thread();
                Client_Add_Trigger=1;
                server_thread.start();
            }
        }
      
        
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if( serverSocket != null && !serverSocket.isClosed() ) {	
                	serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }
	}
    public class Server_Thread extends Thread{
    	String msg="";
    	BufferedReader Server_In;
    	String Nickname;
    	String Exit_Order;
    	String Exit_Nickname;
    	int Order=0;
    	int Num=0;
    	public void run()
    	{
    		try 
    		{
    			Server_In=in;
    			while(true)
    			{
    				
    				//클라이언트 접속 및 종료시에 작동
    				if(Client_Add_Trigger==1||msg.equals("/DebugMode Exit"))
    				{
 
    					//종료시에 버퍼,닉네임 배열을 재정비
    					if(msg.equals("/DebugMode Exit"))
    	    			{
    	    				Exit_Order=Server_In.readLine();
    	    				Order=Integer.parseInt(Exit_Order);
    	    				Exit_Nickname=nickname_arr[Order]; 
    	    				for(Num=Order+1;Num<people_num;Num++)
    	    				{
    	    					BufferedWriter_arr[Num-1]=BufferedWriter_arr[Num];
    	    					nickname_arr[Num-1]=nickname_arr[Num];
    	    				}
    	    			
    	    				people_num--;
    	    				//클라이언트가 그에 맞게 작동 하도록 명령어 보냄
    	    				for(Num=0;Num<people_num;Num++)
    	    				{
    	    					BufferedWriter_arr[Num].write("/DebugMode ClientExit"+"\n");
    	        				BufferedWriter_arr[Num].flush();
    	    				}
    					}
    					for(int i=0;i<people_num;i++)
    					{
    						//임의로 정한 "명령어"로써 작동하도록
    						if(Client_Add_Trigger==1)
    						{
    							BufferedWriter_arr[i].write("/DebugMode ClientAdd"+"\n");
            					BufferedWriter_arr[i].flush();
    						}
    						//인원에 들어갈 닉네임 정보 전송
        					for(int j=0;j<people_num;j++)
        					{
        						BufferedWriter_arr[i].write(nickname_arr[j]+"\n");
            					BufferedWriter_arr[i].flush();
        					}
    					}
    					for(int i=0;i<people_num;i++)
    					{
    						if(msg.equals("/DebugMode Exit")){
    							//누구가 나갔습니다의 누구를 보내줌
    							BufferedWriter_arr[i].write(Exit_Nickname+"\n");
    							BufferedWriter_arr[i].flush();
    							//나간사람의 순서를 보내줌
    							BufferedWriter_arr[i].write(Exit_Order+"\n");
    							BufferedWriter_arr[i].flush();
    						}
    						else{
    							//누구가 들어왔습니다의 누구를 보내줌
    							BufferedWriter_arr[i].write(nickname_arr[people_num-1]+"\n");
    							BufferedWriter_arr[i].flush();
    						}
    						
    					}
    					Client_Add_Trigger=0;
    					msg="";
    				}
    				
    				else{
    					msg=Server_In.readLine();
    					
        				if(msg.equals("/DebugMode Exit")){
        				}
        				//일반적인 메시지를 보내는 경우 닉네임과 메시지를 보내줌
        				else	{
        					Nickname=Server_In.readLine();
        					for(int i=0;i<people_num;i++){
            					BufferedWriter_arr[i].write(msg+"\n");
            					BufferedWriter_arr[i].flush();
                				BufferedWriter_arr[i].write(Nickname+"\n");
                				BufferedWriter_arr[i].flush();
                			}
        				}
    				}
    			}
    		}
    		 catch (IOException e) {
    			 System.out.println("null에러");
    			e.printStackTrace();
    		}
    	}
    			
    		
    		
    		
    	}
    

   
    public static void main(String[] args) {
    	ServerBackground serverbackground;
    	serverbackground=new ServerBackground();
    	serverbackground.ServerBackground();
    }
    	
    private static void consoleLog(String log) {
        System.out.println("[server " + Thread.currentThread().getId() + "] " + log);
    }
}
