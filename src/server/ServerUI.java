package server;

import javax.swing.*;
import java.awt.*;

public class ServerUI {
    JFrame frame;
    JTextArea context;
    public ServerUI(){
        JFrame frame = new JFrame("服务端程序");
        frame.setLayout(new BorderLayout());
        frame.setSize(300,640);

        context = new JTextArea();
        context.setSize(150,500);
        JScrollPane sc = new JScrollPane(context);
        sc.setBorder(BorderFactory.createLineBorder(Color.gray,4));
        frame.add(BorderLayout.CENTER,sc);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
