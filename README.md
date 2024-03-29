21800511 이민재
# SimpleChat
Simple Chatting Program (java)
# HOW TO USE?


## Client Compile 방법
 javac ChatClient.java
 
 ![스크린샷 2019-06-06 오후 6 13 33](https://user-images.githubusercontent.com/42690774/59021479-d5794300-8886-11e9-9059-eb33c273326a.png)


## Server Compile 방법
 javac ChatServer.java
 
 ![스크린샷 2019-06-06 오후 6 15 43](https://user-images.githubusercontent.com/42690774/59021671-34d75300-8887-11e9-96ba-69a9aaebec4c.png)



## Test 방법
### Terminal #1
  java ChatServer
### Terminal #2
  java ChatClient \<username1> \<server--ip-address>
### Terminal #3
  java ChatClient \<username2> \<server--ip-address>

## Lab5: Customizing 1
- 1. ChatClient 실행 구문 변경하기
- 2. broadcast(), sendmsg()에서 클라이언트에게 보내는 메시지 앞부분에 현재시간을 보여주는 기능 추가

## Lab6: Customizing 2
- 1. 현재 접속한 사용자 목록 보기 기능
- 2. 자신이 보낸 채팅 문장은 자신에게는 나타나지 않도록 할 것
- 3. 금지어 경고 기능

## Lab7: Customizing 3
- 1. 클라이언트에서 '/spamlist' 를 입력하면 현재 서버에 등록된 금지어의 목록 출력 기능 구현 (미리 금지어가 등록되어 있을 필요 없음)

<img width="966" alt="스크린샷 2019-06-06 오후 10 54 21" src="https://user-images.githubusercontent.com/42690774/59038794-95c65180-88ae-11e9-9b0c-aa1b096a2d7c.png">

- 2. 클라이언트에서 '/addspam 단어'를 입력하면 해당 <단어>가 서버에 금지어로 추가되도록 하는 기능 구현

<img width="963" alt="스크린샷 2019-06-06 오후 10 54 52" src="https://user-images.githubusercontent.com/42690774/59038796-95c65180-88ae-11e9-9fb5-1e44064a53ae.png">

- 3. 금지어 파일 관리 기능 구현 - 서버를 시작하면 금지어 리스트는 특정 파일에서 불러오고, 서버가 종료되면 새로 추가된 금지어를 포함한 현재 리스트가 파일에 저장되도록 기능 구현

<img width="1155" alt="스크린샷 2019-06-06 오후 11 02 49" src="https://user-images.githubusercontent.com/42690774/59039166-42a0ce80-88af-11e9-8773-217bcf9453cc.png">

-4. client는 시작 할때 아이디, ip, 그리고 방이름을 입력한다.
client가 입력한 방에 들어가 있는 사람끼리 대화를 주고 받을 수 있다.

<img width="451" alt="image" src="https://user-images.githubusercontent.com/42690774/59107774-4f3b2a80-8974-11e9-8881-3a24abbde56f.png">

> Q&A: 21400564@handong.edu
