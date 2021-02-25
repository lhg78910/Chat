package 채팅서버;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;




public class Server extends JFrame implements ActionListener{
	private JPanel contentPane;
	private JTextField portinput;
	private JTextArea textArea = new JTextArea();
	private JButton serverstartbutton = new JButton("서버 실행");
	private JButton serverstopbutton = new JButton("서버 중단");
	
	private ServerSocket serversocket;
	private Socket socket;
	private Socket socketfile;
	private ServerSocket serversocketfile;
	private int portnumber;
	private Vector uvector=new Vector();
	private Vector vroom=new Vector();

	Server(){
		window();
		start();
	}
	private void start() {
		serverstartbutton.addActionListener(this);
		serverstopbutton.addActionListener(this);
		
	}
	private void window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 20, 321, 245);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		JLabel lblNewLabel = new JLabel("PORT");
		lblNewLabel.setBounds(12, 281, 33, 15);
		contentPane.add(lblNewLabel);
		
		portinput = new JTextField();
		portinput.setBounds(57, 278, 276, 21);
		contentPane.add(portinput);
		portinput.setColumns(10);
		
		
		serverstartbutton.setBounds(12, 306, 160, 23);
		contentPane.add(serverstartbutton);
		
		
		serverstopbutton.setBounds(184, 306, 149, 23);
		contentPane.add(serverstopbutton);
		serverstopbutton.setEnabled(false);
		
		this.setVisible(true);
	}
	private void serverstart() {
		try {
			serversocket=new ServerSocket(portnumber);
			serversocketfile=new ServerSocket(portnumber+1);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트입니다", "알림", JOptionPane.ERROR_MESSAGE);
		}
		if(serversocket!=null) {
			Connection();
		}
	}
	private void Connection() {
		Thread thread=new Thread(new Runnable() {
			public void run() {
				while(true){
				try {
					textArea.append("사용자 접속 대기 중\n");  
					socket=serversocket.accept();
					textArea.append("사용자 접속\n");
					UserInfo user=new UserInfo(socket);
					user.start();
				} catch (IOException e) {
					break;
				}
				}
			}
		});
		
		thread.start();
		/*Thread thread = new Thread(new Socket_thread());
	    thread.start();
	    Thread thread2 = new Thread(new Socket_file_thread());
	    thread2.start();*/

	}
	public static void main(String[] args) {
		new Server();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==serverstartbutton) {
			System.out.println("서버 실행 버튼 클릭");
			portnumber=Integer.parseInt(portinput.getText().trim());
			serverstart();
			serverstartbutton.setEnabled(false);
			portinput.setEditable(false);
			serverstopbutton.setEnabled(true);
		}
		else if(e.getSource()==serverstopbutton) {
			serverstopbutton.setEnabled(false);
			serverstartbutton.setEnabled(true);
			portinput.setEditable(true);
			try {
				serversocket.close();
				uvector.removeAllElements();
				vroom.removeAllElements();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("서버 중단 버튼 클릭");
		}
		
	}
	class FileReceiver extends UserInfo
	{
	  private String filename;

	  public FileReceiver(Socket so)
	  {
		  super(so);
	  }

	  protected void thread_run()
	  {
	    while (true)
	      try
	      {
	        this.filename = this.dis.readUTF();
	        FileReceive(this.filename);
	        continue;
	      } catch (IOException e) {
	        try {
	          this.is = this.usocket.getInputStream();
	          this.dis = new DataInputStream(this.is);
	          this.os = this.usocket.getOutputStream();
	          this.dos = new DataOutputStream(this.os);
	        }
	        catch (IOException localIOException1)
	        {
	        }
	      }
	  }

	  public void userNetwork()
	  {
	    try
	    {
	      this.is = this.usocket.getInputStream();
	      this.dis = new DataInputStream(this.is);
	      this.os = this.usocket.getOutputStream();
	      this.dos = new DataOutputStream(this.os);
	      this.nickname = this.dis.readUTF();
	    }
	    catch (IOException e) {
	      JOptionPane.showMessageDialog(null, "Stream설정 에러", "알림", 0);
	    }
	  }

	  private void FileReceive(String filename) throws IOException {
	    File file = new File(filename);
	    this.dos.writeUTF(filename);
	    this.fos = new FileOutputStream(file);
	    this.bos = new BufferedOutputStream(this.fos);

	    int size = 4096;
	    byte[] data = new byte[size];
	    int len;
	    while ((len = this.dis.read(data)) != -1)
	    {
	      this.bos.write(data, 0, len);
	    }

	    this.bos.flush();
	    this.bos.close();
	    this.fos.close();
	    this.dis.close();
	  }
	  private void FileSend(File file) {
	    try {
	      this.fis = new FileInputStream(file);
	      this.bis = new BufferedInputStream(this.fis);

	      int size = 4096;
	      byte[] data = new byte[size];
	      int len;
	      while ((len = this.bis.read(data)) != -1)
	      {
	        this.dos.write(data, 0, len);
	      }
	      this.dos.flush();
	      this.dos.close();
	      this.bis.close();
	      this.fis.close();
	    }
	    catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  protected void InMessage(String str)
	  {
	  }

	  public void send_Message(String message)
	  {
	  }
	}
	class RoomInfo{
		private String roomname;
		private Vector roomuservector=new Vector();
		RoomInfo(String str, UserInfo u){
			this.roomname=str;
			this.roomuservector.add(u);
		}
		public void Broadcastroom(String str){
			for(int i=0; i<roomuservector.size(); i++) {
				UserInfo u=(UserInfo)roomuservector.elementAt(i);
				u.sendmessage(str);
			}
		}
		private void adduser(UserInfo u) {
			this.roomuservector.add(u);
		}
	}
	class UserInfo extends Thread{
		protected OutputStream os;
		protected InputStream is;
		protected DataOutputStream dos;
		protected DataInputStream dis;
		protected FileOutputStream fos;
		protected FileInputStream fis;
		protected BufferedOutputStream bos;
		protected BufferedInputStream bis;
		protected Socket usocket;
		protected String nickname="";
		protected String currentroom;
		protected boolean roomcheck=true;
		public UserInfo(Socket so){
			this.usocket=so;
			usernetwork();
		}

		public void usernetwork() {
			try {
				is=usocket.getInputStream();
				dis=new DataInputStream(is);
				os=usocket.getOutputStream();
				dos=new DataOutputStream(os);
				nickname=dis.readUTF();
				textArea.append(nickname+" : 사용자 접속\n");
				Broadcastroom("NewUser/"+nickname);
				for(int i=0; i<uvector.size(); i++) {
					UserInfo u=(UserInfo)uvector.elementAt(i);
					sendmessage("OldUser/"+u.nickname);
				}
				uvector.add(this);
				Broadcastroom("user_list_update/ ");
				SetOldRoom();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Stream 설정 error", "알림", JOptionPane.ERROR_MESSAGE);// TODO Auto-generated catch block
				
			}
			
		}
		private void SetOldRoom(){
			for(int i = 0; i< vroom.size(); i++){
				RoomInfo roomInfo = (RoomInfo) vroom.elementAt(i);
				this.sendmessage("OldRoom/" + roomInfo.roomname);
			}
			this.sendmessage("room_list_update/ ");
		}
		private void inmessage(String str) {
			StringTokenizer tf = new StringTokenizer(str, "/");
			String protocol = tf.nextToken();
			String message = tf.nextToken();
			System.out.println("protocol : " + protocol);
			if(protocol.equals("MakeRoom")) {
				for(int i=0; i<vroom.size(); i++) {
					RoomInfo r=(RoomInfo)vroom.elementAt(i);
					if(r.roomname.equals(message)) {
						sendmessage("MakeRoomFail/ok");
						roomcheck=false;
						break;
					}
				}
				if(roomcheck) {
					RoomInfo newroom=new RoomInfo(message, this);
					vroom.add(newroom);
					currentroom=message;
					sendmessage("MakeRoom/"+message);
					sendmessage("Chat/알림/"+nickname+" 입장");
					Broadcastroom("NewRoom/"+message);
				}
				roomcheck=true;
			}
			else if(protocol.equals("Chat")) {
				String msg=tf.nextToken();
				for(int i=0; i<vroom.size(); i++) {
					RoomInfo r=(RoomInfo)vroom.elementAt(i);
					if(r.roomname.equals(message)) {
						r.Broadcastroom("Chat/"+nickname+"/"+msg);
					}
				}
			}
			else if(protocol.equals("JoinRoom")) {
				for(int i=0; i<vroom.size(); i++) {
					RoomInfo r=(RoomInfo)vroom.elementAt(i);
					if(r.roomname.equals(message)) {
						r.Broadcastroom("Chat/알림/"+nickname+" 입장");
						currentroom=message;
						r.adduser(this);
						sendmessage("JoinRoom/"+message);
					}
				}
			}
			else if (protocol.equals("FileStart")) {
			      for (int i = 0; i < vroom.size(); i++) {
			       RoomInfo roomInfo = (RoomInfo)vroom.elementAt(i);
			        if (roomInfo.roomname.equals(currentroom)) {
			          roomInfo.Broadcastroom("Chatting/알림/"+nickname + "의 파일(" + message + ") 전송이 시작됩니다\n");
			        }
			      }
			    }
			    else if (protocol.equals("FileEnd")) {
			      RoomInfo roomInfo = null;
			      String msg=tf.nextToken();
			      for (int i = 0; i < vroom.size(); i++) {
			        roomInfo = (RoomInfo)vroom.elementAt(i);
			        if (roomInfo.roomname.equals(currentroom))
			          roomInfo.Broadcastroom("전송이 끝났습니다 : " + msg);
			      }
			    }
		
		}
		
		private void Broadcastroom(String str) {
			for(int i=0; i<uvector.size(); i++) {
				UserInfo u=(UserInfo)uvector.elementAt(i);
				u.sendmessage(str);
			}
		}
		private void sendmessage(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			while(true) {
				try {
					String message=dis.readUTF();
					textArea.append(nickname+" : 사용자로부터 들어온 메세지 : "+message+"\n");
					inmessage(message);
				} catch (IOException e) {
					textArea.append(nickname+" 접속 끊어짐\n");
					 try {
						 dos.close();
						 dis.close();
						 socket.close(); 
						 uvector.remove(this);
						 Broadcastroom("Userout/"+nickname);
						 Broadcastroom("user_list_update/ ");
					} catch (IOException e1) {
					}
					 break;
					
				}
				
			}
		}
	
	}

	public class Socket_file_thread
    implements Runnable
  {
    public Socket_file_thread()
    {
    }

    public void run()
    {
      try
      {
        while (true)
        {
          Server.this.textArea.append("파일 전송 대기중\n");
          Server.this.socketfile = Server.this.serversocketfile.accept();
          FileReceiver fileReceiver = new FileReceiver(Server.this.socketfile);
          fileReceiver.start();
        }
      } catch (IOException e) {
        Server.this.textArea.append("파일 전송 대기가 중지 되었습니다\n");
      }
    }
  }

  public class Socket_thread
    implements Runnable
  {
    public Socket_thread()
    {
    }

    public void run()
    {
      try
      {
        while (true)
        {
          Server.this.textArea.append("사용자 접속 대기중\n");
          Server.this.socket = Server.this.serversocket.accept();
          UserInfo userInfo = new UserInfo(Server.this.socket);
          userInfo.start();
        }
      } catch (IOException e) {
        Server.this.textArea.append("서버가 중지 되었습니다\n");
      }
    }
  }
}

