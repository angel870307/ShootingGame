package com.game.shooting;

import java.awt.*;
import java.awt.event.*;//keyListener(Interface) 쓰는데 사용
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;  //JFrame 쓰는데 사용

//GUI를 구현하려면  JFrame을 꼭 상속해야함
//keyListener : 키보드에서 키 눌린거 처리할 때 사용함
//Runnable : 쓰레드를 만들려면 implements 해야함
public class Shoot extends JFrame implements Runnable, KeyListener {
	private BufferedImage bi = null;
	private ArrayList msList = null;// ArrayList 객체를 담을 변수 선언
	private ArrayList enList = null;// ArrayList 객체를 담을 변수 선언
	//방향키 발사키 정보 담는 변수인듯
	private boolean left = false, right = false, up = false, down = false, fire = false;
	private boolean start = false, end = false;
	//w : 창 너비?, h : 창 높이?, x : 플레이어 좌표 x?, y : 플레이어 좌표y?, xw : 플레이어 폭? xh : 플레이어 높이?
	private int w = 300, h = 500, x = 150, y = 450, xw = 20, xh = 20;

	public Shoot() { //Shoot클래스 생성자
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		msList = new ArrayList();//ArrayList 객체로 만듬
		enList = new ArrayList();//ArrayList 객체로 만듬
		this.addKeyListener(this);
		this.setSize(w, h);//창 크기를 결정 하나보다...혹시 가로/세로? -->확실하군 ㅎㅎ
		this.setTitle("Shooting Game");//창의 제목!
		this.setResizable(false);//창크기 조절 가능한지 결정(true:가능, false:불가능)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//창의 X버튼 누르면 프로그램도 같이 종료되게 하는 방법
		this.setVisible(true);//창의 화면에 보여줄지 말지 결정(true:보여줌 false:안보여줌-및에에서 예외 발생하던데.-->예외처리좀 잘해놓지..쯔쯔
	}
	
	
	public void run() {
		try {
			int msCnt = 0;
			int enCnt = 0;
			while (true) {//무한루프 -_-a 아직도 이런식으로 짜다니
				Thread.sleep(10); //잠시 정지시키는 메소드, 왜쓰지? -> 게임 전체 속도에 영향(작으면 빨라지고 크면 느려진다.)
				
				if (start) {//start가 참이면 아래로
					if (enCnt > 200) {//enCnt가 2000보다 크면?
						enCreate();//이 놈은 뭐야???
						enCnt = 0;
					}
					if (msCnt >= 100) {
						fireMs();
						msCnt = 0;
					}
					msCnt += 10;
					enCnt += 10;
					keyControl();
					crashChk();
				}
				draw();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireMs() {
		if (fire) {
			if (msList.size() < 100) {
				Ms m = new Ms(this.x, this.y);
				msList.add(m);
			}
		}
	}

	public void enCreate() {//9번 반복
		for (int i = 0; i < 9; i++) {
			double rx = Math.random() * (w - xw);//지역 변수&Scope
			double ry = Math.random() * 50;
			Enemy en = new Enemy((int) rx, (int) ry);
			enList.add(en);
		}
	}

	public void crashChk() {
		Graphics g = this.getGraphics();
		Polygon p = null;
		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			for (int j = 0; j < enList.size(); j++) {
				Enemy e = (Enemy) enList.get(j);
				int[] xpoints = { m.x, (m.x + m.w), (m.x + m.w), m.x };
				int[] ypoints = { m.y, m.y, (m.y + m.h), (m.y + m.h) };
				p = new Polygon(xpoints, ypoints, 4);
				if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
					msList.remove(i);
					enList.remove(j);
				}
			}
		}
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			int[] xpoints = { x, (x + xw), (x + xw), x };
			int[] ypoints = { y, y, (y + xh), (y + xh) };
			p = new Polygon(xpoints, ypoints, 4);
			if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
				enList.remove(i);
				start = false;
				end = true;
			}
		}
	}

	public void draw() {
		Graphics gs = bi.getGraphics();
		gs.setColor(Color.white);
		gs.fillRect(0, 0, w, h);
		gs.setColor(Color.black);
		gs.drawString("Enemy 객체수 : " + enList.size(), 180, 50);
		gs.drawString("Ms 객체수 : " + msList.size(), 180, 70);
		gs.drawString("게임시작 : Enter", 180, 90);

		if (end) {
			gs.drawString("G A M E     O V E R", 100, 250);
		}

		gs.fillRect(x, y, xw, xh);

		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			gs.setColor(Color.blue);
			gs.drawOval(m.x, m.y, m.w, m.h);
			if (m.y < 0)
				msList.remove(i);
			m.moveMs();
		}
		gs.setColor(Color.black);
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			gs.fillRect(e.x, e.y, e.w, e.h);
			if (e.y > h)
				enList.remove(i);
			e.moveEn();
		}

		Graphics ge = this.getGraphics();
		try{
			ge.drawImage(bi, 0, 0, w, h, this);
			
		}catch(java.lang.NullPointerException e){
			//java.lang.NullPointerException 예외가 생기면 실행할 코드
			//하지만 난 안짤꺼야
			//아니다..컨설에 메세지는 출력해볼까!!
			System.out.println("java.lang.NullPointerException 이라는 예외 발생--시간나면 멋있게 바꾸지뭐!!");
		}
	}

	public void keyControl() {
		if (0 < x) {
			if (left)
				x -= 3;
		}
		if (w > x + xw) {
			if (right)
				x += 3;
		}
		if (25 < y) {
			if (up)
				y -= 3;
		}
		if (h > y + xh) {
			if (down)
				y += 3;
		}
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_A:
			fire = true;
			break;
		case KeyEvent.VK_ENTER:
			start = true;
			end = false;
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_A:
			fire = false;
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {
	}

	public static void main(String[] args) {
		Thread t = new Thread(new Shoot());//쓰레드 하나 생성,Shoot클래스를 가지고 쓰레드 만듬?
		t.start();// 쓰레드를 시작하는거
	}
}

class Ms {
	int x;
	int y;
	int w = 5;
	int h = 5;

	public Ms(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveMs() {
		y--;
	}
}

class Enemy {
	int x;
	int y;
	int w = 10;
	int h = 10;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveEn() {
		y++;
	}
}