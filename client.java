import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket; 
import java.util.*;
public class client {
	private static final String Server_Ip="192.168.0.108";
	private static final int Server_Port=9999;
	
	public static void main(String []args)
	{
		BufferedReader In=null;
		BufferedWriter Out=null;
		Socket Connect_Socket=null;
		try 
		{
			
			Connect_Socket=new Socket(Server_Ip,Server_Port);
			System.out.println(Connect_Socket);
			In=new BufferedReader(new InputStreamReader(Connect_Socket.getInputStream(),"UTF-8"));
            Out=new BufferedWriter(new OutputStreamWriter(Connect_Socket.getOutputStream(),"UTF-8"));
            System.out.println("클라이언트 접속됨");             
            
            String People_Num_Str=In.readLine();
            
			Nickname_Ui swing=new Nickname_Ui(People_Num_Str,Connect_Socket,Out,In);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
