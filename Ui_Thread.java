import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



class Ui_Thread extends JFrame
{
	JScrollPane ScrollPane;
	JFrame Container=new JFrame();
	JTextField ChatInput_Tf=new JTextField();
	JTextArea ChatScreen_Tf=new JTextArea();
	JLabel PeopleNum_Label;
	JTextArea Tf3=new JTextArea("o1",20,10);
	
	Socket Connect_Socket;
	BufferedWriter Out;
	BufferedReader In;
	ReciveThread Recive_Thread=new ReciveThread();
	
	int Num=0;
	int People_Num_Int;
	String Nickname;
	String Msg;
	String MyNickname;
	String MyPeople_Num;
	String People_Num;
	String Nickname_Arr[]=new String[10];

	
	//사람 수,소켓 정보,접속한 사람의 닉네임,배열을 저장
	
	public Ui_Thread(String People_Num,BufferedWriter Out,BufferedReader In,Socket socket,String MyNickname)
	{
		
		Container.setTitle("채팅 프로그램 ver 0.1");
		Container.setLayout(null);
		Container.setLocationRelativeTo(null);
		this.MyPeople_Num=People_Num;
		this.Nickname=MyNickname;
		this.In=In;
		this.Out=Out;
		this.People_Num_Int=Integer.valueOf(People_Num);
		this.Connect_Socket=socket;
		
		PeopleNum_Label=new JLabel("인원:"+People_Num);
		PeopleNum_Label.setBounds(680,4,150,20);
		Container.add(PeopleNum_Label);
		
		//인원,누가 들어왔는지 UI에 출력
		PeopleNum_Label.setBounds(680,4,150,20);
		Container.add(PeopleNum_Label);
		String Nickname_Temp="";
		
		
		Tf3.setEditable(false);
		Tf3.setBounds(660,30,100,540);
		Container.add(Tf3);

		
		//닉네임
		JLabel Nickname_Label=new JLabel("닉네임:");
		Nickname_Label.setBounds(4,4,200,20);
		Container.add(Nickname_Label);
		
		JTextField Nickname_Tf=new JTextField(Nickname);
		Nickname_Tf.setBounds(50,4,600,20);
		Nickname_Tf.setEditable(false);
		Container.add(Nickname_Tf);
		
		//채팅창
	
		ChatScreen_Tf.setEditable(false);
		ScrollPane = new JScrollPane(ChatScreen_Tf, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollPane.setBounds(6,30,645,540);
		Container.add(ScrollPane);
		
		
		//입력:
		JLabel TextLabel=new JLabel("대화: ");
		TextLabel.setBounds(15,575,250,20);
		Container.add(TextLabel);
		
		//입력창
		ChatInput_Tf.setBounds(50,575,535,20);
		Container.add(ChatInput_Tf);
		
		//버튼
		JButton Exit_Btn=new JButton("EXIT");
		Exit_Btn.setBounds(660,575,100,20);
		Container.add(Exit_Btn);
		
		//버튼2
		JButton Send_Btn=new JButton("전송");
		Send_Btn.setBounds(590,575,60,20);
		Container.add(Send_Btn);
		//이벤트처리 엔터치면 메시지 출력
		Container.setSize(800,650);
		Container.setResizable(false);
		Container.setVisible(true);
		
		Recive_Thread.start();
		ChatInput_Tf.addActionListener(new MyActionListener());
		Exit_Btn.addActionListener(new ExitListener());
		Send_Btn.addActionListener(new SendActionListener());
		
	}
		private class MyActionListener implements ActionListener{
			public void  actionPerformed(ActionEvent e){
				Msg=ChatInput_Tf.getText();
				SendThread send_thread=new SendThread();
				send_thread.start();
				ChatInput_Tf.setText("");
			}
		}
		private class SendActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e2)	{
				Msg=ChatInput_Tf.getText();
				SendThread send_thread=new SendThread();
				send_thread.start();
			}
		}
		private class ExitListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				try {
					Out.write("/DebugMode Exit"+"\n");
					Out.flush();
					Out.write(MyPeople_Num+"\n");
					Out.flush();
					System.out.println("서버에 전송한 나의 번호:"+MyPeople_Num);
					Connect_Socket.close();
					System.exit(0);
				}
				catch(IOException e2){
					e2.printStackTrace();
				}
			
			}
		}
	
		
		private class ReciveThread extends Thread{
			String Msg_Temp;
			String Nickname_Tf3="";
			String Nickname_Temp;
			public void run(){
				try{
					while(true){
							//클라이언트가 접속한 경우가 아니라면 Msg_Temp에는 메시지가 들어감
							Msg_Temp=In.readLine();
							if(Msg_Temp.equals("/DebugMode ClientAdd")||Msg_Temp.equals("/DebugMode ClientExit"))
							{
								//클라이언트 접속 시 사람 수 증가
								if(Msg_Temp.equals("/DebugMode ClientAdd")) {
									People_Num_Int++;
								}
								//종료 시 사람 수 감소
								else
								{
									People_Num_Int--;
								}
								//인원의 숫자와 누가 들어와 있는지 처리
								PeopleNum_Label.setText("인원: "+People_Num_Int);
								for(Num=0; Num<People_Num_Int; Num++)
								{
									Nickname_Arr[Num]=In.readLine();
									Nickname_Tf3=Nickname_Tf3.concat(Nickname_Arr[Num]+"\n");
								}
								Tf3.setText(Nickname_Tf3);
								//나간 사람의 닉네임 혹은 들어온 사람의 닉네임
								Nickname_Tf3=In.readLine();
								if(Msg_Temp.equals("/DebugMode ClientAdd")) {
									ChatScreen_Tf.append(Nickname_Tf3+"님이 접속하였습니다 \n");
								}
								else
								{
									
									ChatScreen_Tf.append(Nickname_Tf3+"님이 퇴장하였습니다 \n");
									//나간 사람의 순서에 따라 자신의 순서를  조정해줍니다
									String Temp_Exit_Order=In.readLine();
									int Exit_Order_Int=Integer.parseInt(Temp_Exit_Order);
									int temp=Integer.parseInt(MyPeople_Num);
									if( Exit_Order_Int<temp)
									{
										temp--;
										MyPeople_Num=Integer.toString(temp);
									}
								}
								Nickname_Tf3="";
							}
							else{
								Nickname_Temp=In.readLine();
								ChatScreen_Tf.append(Nickname_Temp+">>"+Msg_Temp+"\n");
							}
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		//서버에 메시지와 보낸 사람의 닉네임을 보내는 쓰레드
		private class SendThread extends Thread{
			public void run(){
				try{	
					
						 Out.write(Msg+"\n");
						 Out.flush();
						 Out.write(Nickname+"\n");
						 Out.flush();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
}