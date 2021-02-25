package 채팅클라이언트;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.rmi.UnknownHostException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class Client extends JFrame implements ActionListener, KeyListener{
	//login
	private JPanel loginpane;
	private JTextField serverip;
	private JTextField serverport;
	private JTextField nickname;
	private JFrame login_gui=new JFrame();
	private JButton connectbutton = new JButton("접속");
	
	//main
	private JPanel contentPane;
	private JTextField messagetf;
	private JButton joinroombutton = new JButton("방 참여");
	private JButton makeroombutton = new JButton("방 만들기");
	private JButton sendbutton = new JButton("채팅전송");
	private JButton filesendbutton = new JButton("파일전송");
	private JList clientlist = new JList();
	private JList roomlist = new JList();
	private JTextArea chatarea = new JTextArea();
	
	private Socket socket;
	private String inputip="";
	private int inputport;
	private String id="";
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket socketfile;
	private InputStream isfile;
	private OutputStream osfile;
	private DataOutputStream dosfile;
	private DataInputStream disfile;
	private FileInputStream fis;
	private FileOutputStream fos;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private String filename;
	Vector Clientlist=new Vector();
	Vector Roomlist=new Vector();
	StringTokenizer st;
	private String myroom="";
	Client(){
		login_window();
		main_window();
		start();
	}
	private void main_window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("접속자 목록");
		lblNewLabel.setBounds(12, 10, 64, 15);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 31, 64, 324);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(clientlist);
		
		JLabel lblNewLabel_1 = new JLabel("채팅방 목록");
		lblNewLabel_1.setBounds(102, 10, 70, 15);
		contentPane.add(lblNewLabel_1);
		
		
		joinroombutton.setBounds(88, 305, 96, 23);
		contentPane.add(joinroombutton);
		
		
		makeroombutton.setBounds(87, 332, 97, 23);
		contentPane.add(makeroombutton);
		
		messagetf = new JTextField();
		messagetf.setBounds(184, 306, 263, 49);
		contentPane.add(messagetf);
		messagetf.setColumns(10);
		messagetf.setEnabled(false);
		
		sendbutton.setBounds(453, 305, 99, 23);
		contentPane.add(sendbutton);
		sendbutton.setEnabled(false);
		
		filesendbutton.setBounds(453, 332, 99, 23);
		contentPane.add(filesendbutton);
		filesendbutton.setEnabled(false);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(88, 32, 90, 267);
		contentPane.add(scrollPane_1);
		
		
		scrollPane_1.setViewportView(roomlist);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(184, 5, 368, 291);
		contentPane.add(scrollPane_2);
		
		
		scrollPane_2.setViewportView(chatarea);
		chatarea.setEditable(false);
		
		this.setVisible(false);
	}
	private void start() {
		connectbutton.addActionListener(this);
		joinroombutton.addActionListener(this);
		makeroombutton.addActionListener(this);
		sendbutton.addActionListener(this);
		filesendbutton.addActionListener(this);
		messagetf.addKeyListener(this);
	}
	private void login_window() {
		login_gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login_gui.setBounds(100, 100, 223, 335);
		loginpane = new JPanel();
		loginpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		login_gui.setContentPane(loginpane);
		loginpane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server IP");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 13));
		lblNewLabel.setBounds(12, 40, 70, 15);
		loginpane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server Port");
		lblNewLabel_1.setFont(new Font("Arial Black", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(12, 109, 91, 15);
		loginpane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Nickname");
		lblNewLabel_2.setFont(new Font("Arial Black", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(12, 181, 91, 15);
		loginpane.add(lblNewLabel_2);
		
		serverip = new JTextField();
		serverip.setBounds(12, 60, 183, 21);
		loginpane.add(serverip);
		serverip.setColumns(10);
		
		serverport = new JTextField();
		serverport.setBounds(12, 134, 183, 21);
		loginpane.add(serverport);
		serverport.setColumns(10);
		
		nickname = new JTextField();
		nickname.setBounds(12, 206, 183, 21);
		loginpane.add(nickname);
		nickname.setColumns(10);
		
		
		connectbutton.setBounds(12, 250, 183, 23);
		loginpane.add(connectbutton);
		login_gui.setVisible(true);
	}
	private void network() {
		try {
			socket=new Socket(inputip, inputport);
			socketfile=new Socket(inputip, inputport+1);
			if(socket!=null) {
				connection();
			}
		}catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void connection() {
		
		try {
		is=socket.getInputStream();
		dis=new DataInputStream(is);
		os=socket.getOutputStream();
		dos=new DataOutputStream(os);
		isfile = this.socketfile.getInputStream();
	    disfile = new DataInputStream(this.isfile);
	    osfile = this.socketfile.getOutputStream();
	    dosfile = new DataOutputStream(this.osfile);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
		this.setVisible(true);
		this.login_gui.setVisible(false);
		sendmessage(id);
		sendmessagefile(id);
		Clientlist.add(id);
		//clientlist.setListData(Clientlist);
		Thread thread=new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						String message=dis.readUTF();
						System.out.println("서버로부터 수신된 메세지: "+message);
						inmessage(message);
					} catch (IOException e) {
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							JOptionPane.showMessageDialog(null, "서버와의 접속이 끊어짐", "알림", JOptionPane.ERROR_MESSAGE);
						} catch (IOException e1) {
							
						}
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
	private void inmessage(String str) {
		st=new StringTokenizer(str,"/");
		String protocol=st.nextToken();
		String Mes=st.nextToken();
		System.out.println("프로토콜 : "+protocol);
		System.out.println("내용 : "+Mes);
		if(protocol.equals("NewUser")) {
			Clientlist.add(Mes);
			clientlist.setListData(Clientlist);
		}
		else if(protocol.equals("OldUser")) {
			Clientlist.add(Mes);
			clientlist.setListData(Clientlist);
		}
		else if(protocol.equals("user_list_update")){
			clientlist.setListData(Clientlist);
		}
		else if(protocol.equals("MakeRoom")) {
			myroom=Mes;
			messagetf.setEnabled(true);
			sendbutton.setEnabled(true);
			filesendbutton.setEnabled(true);
			joinroombutton.setEnabled(false);
			makeroombutton.setEnabled(false);
			
		}
		else if(protocol.equals("MakeRoomFail")) {
			JOptionPane.showMessageDialog(null, "방 만들기 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
		else if(protocol.equals("NewRoom")) {
			Roomlist.add(Mes);
			roomlist.setListData(Roomlist);
		}
		else if(protocol.equals("Chat")) {
			String msg=st.nextToken();
			chatarea.append(Mes+":"+msg+"\n");
		}
		else if(protocol.equals("OldRoom")) {
			Roomlist.add(Mes);
		}
		else if(protocol.equals("room_list_update")) {
			roomlist.setListData(Roomlist);
		}
		else if(protocol.equals("JoinRoom")) {
			myroom=Mes;
			messagetf.setEnabled(true);
			sendbutton.setEnabled(true);
			filesendbutton.setEnabled(true);
			joinroombutton.setEnabled(false);
			makeroombutton.setEnabled(false);
			JOptionPane.showMessageDialog(null, "채팅방 입장 성공", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(protocol.equals("Userout")) {
			Clientlist.remove(Mes);
		}
		else if(protocol.equals("FileEnd")) {
			String msg=st.nextToken();
			File a=new File(Mes);
			try{
				this.chatarea.append(a.toURI().toURL()+"'>"+msg);
			}catch(MalformedURLException e1) {
				e1.printStackTrace();
			}
			try {
				URL url=new URL(Mes);
				InputStream is=url.openStream();
				BufferedReader reader=new BufferedReader(new InputStreamReader(is, "utf-8"));
				String line=null;
				while((line=reader.readLine())!=null) {
					System.out.println(line);
				}
				is.close();
			}catch(MalformedURLException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void sendmessage(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==connectbutton) {
			System.out.println("접속 버튼 클릭");
			if(serverip.getText().length()==0) {
				serverip.setText("IP를 입력하세요");
				serverip.requestFocus();
			}
			else if(serverport.getText().length()==0) {
				serverport.setText("PORT 번호를 입력하세요");
				serverport.requestFocus();
			}
			else if(nickname.getText().length()==0) {
				nickname.setText("닉네임을 입력하세요");
				nickname.requestFocus();
			}
			else{
				inputip=serverip.getText().trim();
				inputport=Integer.parseInt(serverport.getText().trim());
				id=nickname.getText().trim();
				network();
			}
		}
		else if(e.getSource()==joinroombutton) {
			String joinroom=(String)roomlist.getSelectedValue();
			sendmessage("JoinRoom/"+joinroom);
			System.out.println("방 참여 버튼 클릭");
		}
		else if(e.getSource()==makeroombutton) {
			String roomname=JOptionPane.showInputDialog("방 이름");
			if(roomname!=null) {
				sendmessage("MakeRoom/"+roomname);
			}
			System.out.println("방 만들기 버튼 클릭");
		}
		else if(e.getSource()==sendbutton) {
			System.out.println("전송 버튼 클릭");
			if(myroom == null){
				JOptionPane.showMessageDialog(null, "채팅방에 참여해주세요", "알림", JOptionPane.ERROR_MESSAGE);
			}
			else {
				sendmessage("Chat/"+myroom+"/"+messagetf.getText().trim());
				messagetf.setText("");
				messagetf.requestFocus();
			}
		}
		else if(e.getSource()==filesendbutton) {
			System.out.println("파일 전송 버튼 클릭");
			if(myroom == null){
				JOptionPane.showMessageDialog(null, "채팅방에 참여해주세요", "알림", JOptionPane.ERROR_MESSAGE);
			}
			else{
				JFileChooser jFileChooser = new JFileChooser("C://");
				jFileChooser.setDialogTitle("파일 선택");
				jFileChooser.setMultiSelectionEnabled(true);
				jFileChooser.setApproveButtonToolTipText("전송할 파일을 선택하세요");
				jFileChooser.showDialog(this, "열기");
				File path = jFileChooser.getSelectedFile();
				if (path != null) {
					sendmessage("FileStart/"+path.getName());
					sendmessagefile(path.getName());
					sendfile(path);
				}
				this.messagetf.requestFocus();
			}
		}
	}
	private void sendmessagefile(String message) {
		try {
			this.dosfile.writeUTF(message);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void sendfile(File file) {
		try {
		      this.fis = new FileInputStream(file);
		      this.bis = new BufferedInputStream(this.fis);

		      int size = 4096;
		      byte[] data = new byte[size];
		      int len;
		      while ((len = this.bis.read(data)) != -1)
		      {
		        this.dosfile.write(data, 0, len);
		      }
		      send_message("FileEnd/"+this.filename);
		      this.dosfile.flush();
		      this.dosfile.close();
		      this.bis.close();
		      this.fis.close();
		      this.dosfile.close();
		      this.socketfile.close();
		      this.socketfile = new Socket(inputip, inputport);
		      if (this.socketfile != null) {
		    	this.filename=disfile.readUTF();
			    this.infile(filename);
		        this.isfile = this.socketfile.getInputStream();
		        this.disfile = new DataInputStream(this.isfile);
		        this.osfile = this.socketfile.getOutputStream();
		        this.dosfile = new DataOutputStream(this.osfile);
		        sendmessagefile(this.id);
		      }
		    }
		    catch (FileNotFoundException e)
		    {
		      e.printStackTrace();
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		    }
		  }

		  private void send_message(String message) {
		    try {
		      this.dos.writeUTF(message);
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	private void infile(String filename) {
		File file=new File(filename);
		try {
			this.fos=new FileOutputStream(file);
			this.bos=new BufferedOutputStream(this.fos);
			int size=4096;
			byte[] data=new byte[size];
			int len;
			while((len=this.disfile.read(data))!=-1) {
				this.bos.write(data, 0, len);
			}
			this.bos.flush();
			this.bos.close();
			this.fos.close();
			this.disfile.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private String getLocalServerIp()
	{
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException ex) {}
		return null;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
		if(e.getKeyCode()==10) {
			sendmessage("Chat/"+myroom+"/"+messagetf.getText().trim());
			messagetf.setText("");
			messagetf.requestFocus();
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

	
	 public class Socket_file_thread implements Runnable{
	    public Socket_file_thread()
	    {
	    }

	    public void run()
	    {
	      while (true)
	        try
	        {
	          Client.this.filename = Client.this.disfile.readUTF();
	          Client.this.infile(Client.this.filename);
	          continue;
	        } catch (IOException e) {
	          try {
	            Client.this.isfile = Client.this.socketfile.getInputStream();
	            Client.this.dis = new DataInputStream(Client.this.is);
	            Client.this.osfile = Client.this.socketfile.getOutputStream();
	            Client.this.dosfile = new DataOutputStream(Client.this.os);
	          }
	          catch (IOException localIOException1)
	          {
	          }
	        }
	    }
	  }
	 public class Socket_thread implements Runnable{
	    public Socket_thread()
	    {
	    }

	    public void run()
	    {
	      try
	      {
	        while (true)
	          Client.this.inmessage(Client.this.dis.readUTF());
	      }
	      catch (IOException e) {
	        try {
	          Client.this.os.close();
	          Client.this.is.close();
	          Client.this.dis.close();
	          Client.this.dos.close();

	          Client.this.osfile.close();
	          Client.this.isfile.close();
	          Client.this.disfile.close();
	          Client.this.dosfile.close();
	          Client.this.socket.close();

	          JOptionPane.showMessageDialog(null, "서버와 접속 끊어짐", "알림", 0);
	        }
	        catch (IOException localIOException1)
	        {
	        }
	      }
	    }
	  }

}
