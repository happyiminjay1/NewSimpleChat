import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Set;
public class ChatServer {

	public static void main(String[] args) {
		try{
			ServerSocket server = new ServerSocket(10001);
			/*ServerSocket(int port)
			Creates a server socket, bound to the specified port.
			https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
			*/
			System.out.println("Waiting connection...");
			HashMap<String,ClientInfo> hm = new HashMap();
			ArrayList<String> banWord = new ArrayList<>();
			File file = new File("banword.txt");
			BufferedReader br = null;
		       try {
		    	   br = new BufferedReader(new FileReader(file));
		           String str;
		           while ((str = br.readLine()) != null) {
		                banWord.add(str);
		            }
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }finally {
		            if(br != null) try {br.close(); } catch (IOException e) {}
		        }
			/*
			HashMap()
			Constructs an empty HashMap with the default initial capacity (16) and the default load factor (0.75).
			*/
			while(true){
				Socket sock = server.accept();
				/*Socket
				server.accept() : Listens for a connection to be made to this socket and accepts it.
								  The method blocks until a connection is made.
				Returns: the new Socket
				접속 대기 -> Returns: the new Socket

				*/
				ChatThread chatthread = new ChatThread(sock, hm, banWord);
				chatthread.start();
				/* public void start()
				  	Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
					The result is that two threads are running concurrently: the current thread (which returns from the call to the start method) and the other thread (which executes its run method).
					It is never legal to start a thread more than once. In particular, a thread may not be restarted once it has completed execution.*/
			} // while
		}catch(Exception e){
			System.out.println(e);
		}
	} // main
}

class ChatThread extends Thread{
	private Socket sock;
	private String chatRoom;
	private String id;
	private BufferedReader br;
	private HashMap<String,ClientInfo> hm;
	private ArrayList<String> banWord;
	private boolean initFlag = false;
	private ClientInfo clientInfo;
	public ChatThread(Socket sock, HashMap<String,ClientInfo> hm, ArrayList<String> banWord){
		this.sock = sock;
		this.hm = hm;
		this.banWord =banWord;
		try{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			id = br.readLine(); // read id //read()
			chatRoom = br.readLine();
			System.out.println("[Server] User (" + id + ") entered."); // tell sever (id) has entered
			clientInfo = new ClientInfo(chatRoom,pw);
			synchronized(hm){ //synchronize hm to all of thread
				hm.put(this.id, this.clientInfo); // put new (id) to hashmap, key : id , value : pw (PrinWriter)
			}
			broadcast(id + " entered.");//broadcast(id + "entered")

			initFlag = true; //
		}catch(Exception ex){
			System.out.println(ex);
		}
	} // construcor
	public void run(){
		try{
			String line = null;
			while((line = br.readLine()) != null){
				if(line.equals("/quit"))//if the text is equal to /quit break the loop
					break;
				if(line.equals("/userlist"))//if the test is euqal to '/userlist' run sendmsg_userlist()
				{
					sendmsg_userlist();
				}
				else if(line.equals("/spamlist"))
				{
					sendmsg_spamlist();
				}
				else if(line.indexOf("/addspam") == 0)
				{
					int start = line.indexOf(" ") +1;
					String str = line.substring(start);
					addspam(str);
					addBanWordList(str);
				}
				else if(line.indexOf("/to ") == 0){
					sendmsg(line);
					//public int indexOf(String str)
					//Returns the index within this string of the first occurrence of the specified substring.
					//https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#indexOf(java.lang.String)
				}
				else
					broadcast(id + " : " + line); //broadcast to every client
			}
		}catch(Exception ex){
			System.out.println(ex); //handling error
		}finally{
			synchronized(hm){
				hm.remove(id); //remove (id) from hashmap
			}
			broadcast(id + " exited."); // boradcast that (id) has exited to all client
			try{
				if(sock != null)
					sock.close();//close
			}catch(Exception ex){}
		}
	} // run

	public void sendmsg(String msg){
		Date date = new Date();//clss date
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[a h:mm]",Locale.KOREAN);//make foramt of clock [오후/오전 시간:분] language into Korean
    	String datemsg = simpleDateFormat.format(date);//give data to simpleDateForamte and make datemsg
		int start = msg.indexOf(" ") +1;
		int end = msg.indexOf(" ", start);
		if(end != -1){
			String to = msg.substring(start, end); //get (id = someone to send)
			String msg2 = msg.substring(end+1); //get message to send
			Object obj = hm.get(to).pw; // get value from hashmap
			if(checkBanWord(msg2)) // check if msg2 contains banWord
			{
				Object obj2 = hm.get(id).pw; //get Users printWriter Object
				PrintWriter pwUser = (PrintWriter)obj2;
				pwUser.println("Becareful of using Ban words!!");//send to User who write banWord
				pwUser.flush();//send meassage
				return;
			}
			if(obj != null){
				PrintWriter pw = (PrintWriter)obj; // ready to send message to object
				pw.println(datemsg + " " + id + " whisphered : " + msg2); // write message in printwriter
				pw.flush(); // send message
			} // if
		}
	} // sendms

	public void broadcast(String msg){
		Date date = new Date();//make clss date
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[a h:mm]",Locale.KOREAN);//make foramt of clock [오후/오전 시간:분] language into Korean
    	String datemsg = simpleDateFormat.format(date);//give data to simpleDateForamte and make datemsg
		msg = datemsg + " " + msg; //put datemsg in msg
		synchronized(hm){ //synchronize hashmap
			Object obj = hm.get(id).pw;
			PrintWriter pwUser = (PrintWriter)obj;
			String chatRoomUser = hm.get(id).chatRoom;
			Collection collection = hm.values(); //collection to
			Iterator iter = collection.iterator();//iterator to acces collection
			if(checkBanWord(msg))
			{
				pwUser.println("Becareful of using Ban words!!");//send massage to specified person(which iterator has given)
				pwUser.flush();//send meassage
				return;
			}
			while(iter.hasNext()){//check iterator is empty (to send message to all user)
				ClientInfo clientInfo = (ClientInfo)iter.next();
				PrintWriter pw = clientInfo.pw;
				if(!chatRoomUser.equals(clientInfo.chatRoom)) continue;
				if(pw.equals(pwUser)) continue;
				pw.println(msg);//send massage to specified person(which iterator has given)
				pw.flush();//send meassage
			}
		}
	} // broadcast

	public void sendmsg_userlist(){
		String msg = "";
		synchronized(hm){
			Set<String> keys = hm.keySet(); //make String list contain of keys
			for(String key : keys) {
				msg += (key + "\n"); //make String contain of  all keys
			}
			msg = msg.substring(0,msg.length()-1);
			Object obj = hm.get(id).pw;
			PrintWriter pw = (PrintWriter)obj;
			pw.println(msg);//send message only to the User
			pw.flush();//send meassage
		}
	}

	public void sendmsg_spamlist(){
		String msg = "";
		synchronized(hm){
			for(String key : banWord) {
				msg += (key + "\n"); //make String contain of  all keys
			}
			msg = msg.substring(0,msg.length()-1);
			Object obj = hm.get(id).pw;
			PrintWriter pw = (PrintWriter)obj;
			pw.println(msg);//send message only to the User
			pw.flush();//send meassage
		}
	}

	public boolean checkBanWord(String msg){
		for(String word : banWord)
		{
			if(msg.indexOf(word)!=-1) return true; // check if the message contains banWord if true return true
		}
		return false;
	}//check message contain banWord
	public void addspam(String str) {
			banWord.add(str);
	}
	public void addBanWordList(String str)
	{
	  File file = new File("banword.txt");
	  BufferedWriter bw = null;
      try {
          bw = new BufferedWriter(new FileWriter(file,true));
          bw.write(str);
          bw.newLine();
          bw.flush();
      } catch (IOException e) {
          e.printStackTrace();
      }finally {
    	  System.out.println("file saved");
          if(bw != null) try {bw.close(); } catch (IOException e) {}
      }

	}
}
