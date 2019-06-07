import java.io.PrintWriter;

public class ClientInfo {
	public String chatRoom;
	public PrintWriter pw;

	ClientInfo(String chatRoom, PrintWriter pw) {
		this.chatRoom = chatRoom;
		this.pw = pw;
	}
}
