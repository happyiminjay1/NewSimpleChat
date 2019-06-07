import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {
  String serverIp;
  String username;
  String chatRoom;
  boolean endflag = false;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("java ChatClient");
    System.out.print("your name >> "); //get usersNickName
    String username = sc.nextLine();
    System.out.print("server ip >> "); //get userIp
    String serverIp = sc.nextLine();
    System.out.print("Enter room (main) >> ");
    String chatRoom = sc.nextLine();

    ChatClient chatClient = new ChatClient(serverIp,username,chatRoom);
    chatClient.start();
  }

  ChatClient (String serverIp, String username, String chatRoom)
  {
      this.serverIp = serverIp;
      this.username = username;
      this.chatRoom = chatRoom;
  }//constructor

  public void start()
  {
    Socket sock = null;
    BufferedReader br = null;
    PrintWriter pw = null;
    try{
      sock = new Socket(serverIp, 10001);
      pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
      br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
      // send username.
      pw.println(username);
      pw.flush();
      pw.println(chatRoom);
      pw.flush();
      InputThread it = new InputThread(sock, br);
      it.start();
      String line = null;
      while((line = keyboard.readLine()) != null){
        pw.println(line);
        pw.flush();
        if(line.equals("/quit")){
          endflag = true;
          break;
        }
      }
      System.out.println("Connection closed.");
    }catch(Exception ex){
      if(!endflag)
        System.out.println(ex);
    }finally{
      try{
        if(pw != null)
          pw.close();
      }catch(Exception ex){}
      try{
        if(br != null)
          br.close();
      }catch(Exception ex){}
      try{
        if(sock != null)
          sock.close();
      }catch(Exception ex){}
    } // finally
  }
   // main
} // class

class InputThread extends Thread{
  private Socket sock = null;
  private BufferedReader br = null;
  public InputThread(Socket sock, BufferedReader br){
    this.sock = sock;
    this.br = br;
  }
  public void run(){
    try{
      String line = null;
      while((line = br.readLine()) != null){
        System.out.println(line);
      }
    }catch(Exception ex){
    }finally{
      try{
        if(br != null)
          br.close();
      }catch(Exception ex){}
      try{
        if(sock != null)
          sock.close();
      }catch(Exception ex){}
    }
  } // InputThread!
}
