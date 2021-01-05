import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.net.Socket;
public class Nickname_Ui extends JFrame
{
	JFrame Frame=new JFrame("자바 채팅 프로그램");
	JTextField Nickname_Tf2=new JTextField(30);
	String Nickname;
	String People_Num_Str_Temp;
	Socket Client_Socket;
	String People_Num_Str;
	BufferedReader In=null;
	BufferedWriter Out=null;
	
	public Nickname_Ui(String People_Num_Str,Socket Connect_Socket,BufferedWriter Out,BufferedReader In)
	{
		this.Client_Socket=Connect_Socket;
		this.In=In;
		this.Out=Out;
		this.People_Num_Str=People_Num_Str;
		People_Num_Str_Temp=People_Num_Str;
		
		Dimension Dim=new Dimension(550,200);	
		Frame.setResizable(false);
		Frame.setLocationRelativeTo(null);
			
		JLabel Intro_Label=new JLabel("    자바 채팅 프로그램 made by air");
		JLabel Nickname_Label=new JLabel("Nickname:");
		Intro_Label.setFont(Intro_Label.getFont().deriveFont(25.0f));
		
		JButton Connect_Btn=new JButton("Connect");
		Connect_Btn.setPreferredSize(new Dimension(50,60));
			
		Nickname_Label.setFont(Nickname_Label.getFont().deriveFont(25.0f));
		Nickname_Tf2.setPreferredSize(new Dimension(20,30));
			
		JPanel Panel2=new JPanel();
		Panel2.add(Nickname_Label);	
		Panel2.add(Nickname_Tf2);
		
		JPanel Panel4=new JPanel();
		Panel4.setLayout(new BoxLayout(Panel4,BoxLayout.Y_AXIS));
		Panel4.add(Panel2);
		
		Frame.add(Connect_Btn,BorderLayout.SOUTH);
		Frame.add(Intro_Label,BorderLayout.NORTH);
		Frame.add(Panel4,BorderLayout.CENTER);
			
		Frame.pack();
		Frame.setVisible(true);

		Connect_Btn.addMouseListener(new ButtonActionListener());
		
	}
	private class ButtonActionListener implements MouseListener{
		public void mousePressed (MouseEvent e)
		{
			Nickname=Nickname_Tf2.getText();
			if(Nickname.equals(""))
				JOptionPane.showMessageDialog(null,"닉네임을 입력하세요","",JOptionPane.ERROR_MESSAGE);
			else
			{
					System.out.println("닉네임 눌렀을때 버튼 액션 리스너 작동");
					Frame.setVisible(false);
					try {
						Out.write(Nickname+"\n");
						Out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("UI 쓰레드 작동");
					Ui_Thread ui_thread = new Ui_Thread(People_Num_Str,Out,In,Client_Socket,Nickname);
			
			}
		}
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
}


	