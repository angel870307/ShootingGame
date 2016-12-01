package com.key;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// F1 키는 바탕화면 초록색, % 키는 노란색으로 변경
public class KeyCodeTest extends JFrame{

	JPanel contentPane = new JPanel();
	JLabel jla = new JLabel();
	
	public KeyCodeTest() {

		setTitle("키코드 테스트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setContentPane(contentPane);
		contentPane.addKeyListener(new MyKeyListener());
		contentPane.add(jla);
		
		setSize(300, 300);
		setVisible(true);
		// JPanel이 키 입력받을 수 있도록 포커스를 줌
		contentPane.requestFocus();
	}
	
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			jla.setText((e.getKeyText(e.getKeyCode())));
			// % 키를 판별하기 위해 e.getKeyChar() 이용
			if (e.getKeyChar() == '%') {
				contentPane.setBackground(Color.green);
			// 유니코드가 아닌 경우 e.getKeyCode() 이용 
			} else if (e.getKeyCode() == KeyEvent.VK_F1) {
				contentPane.setBackground(Color.yellow);
			}
		}
	}
	
	public static void main(String[] args) {

		new KeyCodeTest();
	}

}