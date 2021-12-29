package client;

import go.Chess;
import message.Contract;
import message.Message;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientUI {//window即客户端界面
    JFrame frame;
    Chess go;
    Player player;
    JButton finish,regret;
    JLabel notice,context,chess;
    BoardPanel centerBoard;
    int x,y;//x,y是确认时所处行列。
    Socket socket;

    public ClientUI(Chess x,Socket host){//实现棋盘初始布局
        socket = host;

        String username = JOptionPane.showInputDialog(null,"请输入你的昵称",
                "围棋小程序",JOptionPane.QUESTION_MESSAGE);
        Player p1 = new Player(username,0,0);
        player = p1;

        go = x;
        frame = new JFrame("围棋小程序");
        frame.setSize(800,600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel player;//加载棋局信息
        player = new JPanel();
        player.setPreferredSize(new Dimension(80,300));
        JLabel l1 = new JLabel(p1.getUsername());
        chess = new JLabel(p1.getHandInfo());

        notice = new JLabel("系统信息");
        context = new JLabel("等待中");

        finish = new JButton("结束");
        regret = new JButton("悔棋");
        player.add(l1);player.add(chess);player.add(regret);player.add(finish);
        player.add(notice);player.add(context);
        frame.add(BorderLayout.WEST,player);

        centerBoard = new BoardPanel(go);
        frame.add(BorderLayout.CENTER,centerBoard);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    int Rounding(int x,int y){
        float ans = (float)x/y;
        int temp = x/y;
        if (ans-(float)temp>=0.5f)  return temp+1;
        else return temp;
    }

    public void Run(){
        centerBoard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (player.getState()!=1)   return;
                player.setState(2);
                int px = e.getX();int py = e.getY();//px,py是用户点击是所在的坐标
                System.out.println(px+" "+py);
                if (px>=26 && px<=520 && py>=26 && py<=520) {
                    x = (Rounding(px - 30, 27)) + 1;
                    y = (Rounding(py - 30, 27)) + 1;
                    if (go.board[y][x] != 0) return;
                    System.out.println(x + " " + y);
                    String msg = player.getHandInfo() + "落子";
                    go.Play(player.getHand(), y, x);
                    frame.repaint();
                    Contract.sendMessage(socket, new Message(1, x, y, msg));
                }
            }
        });
        finish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (player.getState()!=1)   return;
                Contract.sendMessage(socket,new Message(4,0,0,"申请结束本局游戏"));
            }
        });
        regret.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (player.getState()!=1)   return;
                Contract.sendMessage(socket,new Message(-1,0,0,"申请一次悔棋"));
            }
        });

        while(true){
            System.out.println("循环阶段:"+ socket.getInetAddress().getHostAddress());
            Message msg = Contract.getMessage(socket);
            if (msg.getAction()==-1){
                int res = JOptionPane.showConfirmDialog(frame,"是否同意对方悔棋",
                        "Go",JOptionPane.YES_NO_OPTION);
                if (res==JOptionPane.YES_OPTION) {
                    go.Regret();
                    Contract.sendMessage(socket, new Message(2, 0, 0, "同意悔棋"));
                }
            }
            if (msg.getAction()==0){
                System.out.println("game start");
                context.setText("游戏开始");
                String  gin = msg.getContext();
                if (gin.equals("0")) {
                    player.setHand(1);
                    player.setState(1);
                }
                else {
                    player.setHand(2);
                    player.setState(2);
                }
                chess.setText(player.getHandInfo());
            }
            if (msg.getAction()==1) {
                System.out.println("play the game");
                if ("执白子落子".equals(msg.getContext()))  go.Play(2,msg.getPy(),msg.getPx());
                if ("执黑子落子".equals(msg.getContext()))  go.Play(1,msg.getPy(),msg.getPx());
                player.setState(1);
            }
            if (msg.getAction()==2)  go.Regret();
            if (msg.getAction()==3){
                int result = go.Finale();
                if (result==player.getHand())
                    JOptionPane.showMessageDialog(frame,"你赢了");
                else JOptionPane.showMessageDialog(frame,"你输了");
                Contract.sendMessage(socket,new Message(5,0,0,""+(result==player.getHand())));
            }
            if (msg.getAction()==4){
                int res = JOptionPane.showConfirmDialog(frame,"是否同意结束本局游戏",
                        "Go",JOptionPane.YES_NO_OPTION);
                if (res==JOptionPane.YES_OPTION){
                    Contract.sendMessage(socket,new Message(3,0,0,"同意结束本局游戏"));
                }
            }
            if (msg.getAction()==5){
                if ("false".equals(msg.getContext()))
                    JOptionPane.showMessageDialog(frame,"你赢了");
                else JOptionPane.showMessageDialog(frame,"你输了");
            }
            frame.repaint();
        }
    }
}
