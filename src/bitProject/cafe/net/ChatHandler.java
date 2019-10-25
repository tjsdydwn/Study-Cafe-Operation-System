package bitProject.cafe.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ChatHandler extends Thread {

	private BufferedReader br;
	private PrintWriter pw;
	private Socket socket;
	private ArrayList<ChatHandler> list;
	private boolean isStaff;
	private String id;

	public ChatHandler(Socket socket, ArrayList<ChatHandler> list) {
		this.socket = socket;
		this.list = list;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String chatStr = null;
			String command = null;
			boolean isChatting = true;
			while (isChatting) {
				chatStr = br.readLine(); // 메시지 대기

				// 메시지 1~10자리는 command, 11~22자리는 보낼 아이디

				if (chatStr == null) { // 온 메시지가 null이 아니라면 command 확인
					break;
				} else {
					command = chatStr.substring(0, 10);
				}
				switch (command) { // command에 따라 명렁 구분
				case "|||MEET|||": // 핸들러에 첫 meet 메시지 도착 시 핸들러에게 1.스태프여부, 아이디 입력 2.채팅방 접속멘트 발송
					isStaff = chatStr.substring(22, 25).equals("stf");
					id = chatStr.substring(10, 22).trim();
					broadcast(chatStr);
					break;
				case "|||SEND|||": // 핸들러에게 온 메시지가 누구에게 보내는 것인지 확인
					String toId = chatStr.substring(10, 22).trim();
					String msg = chatStr.substring(22);
					broadcast(toId, msg); // 받는사람아이디와 메시지 발송
					break;
				case "|||EXIT|||": // 핸들러에게 온 종료메시지가 누구에게 보내는 것인지 확인, 나한테 온거면 핸들러 종료
					if (id.equals(chatStr.substring(10, 22).trim())) {
						broadcast(chatStr);
						list.remove(this);
						br.close();
						pw.close();
						socket.close();
						isChatting = false;
						break;
					} else {
						broadcast(chatStr); // 나한테 온게 아니라면 메시지만 발송
						break;
					}
				default: // 커맨드가 null일 경우가 없지만, 만약 null이면 핸들러 종료
					list.remove(this);
					br.close();
					pw.close();
					socket.close();
					isChatting = false;
					break;
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private String convertId(String id) { // 보낼때 공백 추가하여 아이디 12자리로 맞추기

		StringBuffer cvId = new StringBuffer(id);

		for (int i = cvId.length(); i < 12; i++) {
			cvId.append(" ");
		}

		return cvId.toString();
	}

	public void broadcast(String msg) {
		for (ChatHandler handler : list) {
			handler.pw.println(msg);
			handler.pw.flush();
		}
	}

	public void broadcast(String toId, String msg) {
		for (ChatHandler handler : list) {
			// 1.스태프, 2.받을아이디(toId)의 핸들러만 채팅룸으로 메시지 발송
			if (handler.isStaff || handler.id.equals(toId)) {

				String cvFromId = convertId(this.id); // 12자리 형식 아이디로 변경
				String cvToId = convertId(toId);
				// 발송 시 command, 받을 아이디, 보내는 아이디 형식으로 발송
				handler.pw.println("|||SEND|||" + cvToId + cvFromId.toString() + msg);
				handler.pw.flush();
			} else {

			}
		}
	}
}
