package client;

import go.Chess;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class BoardPanel extends JPanel {//棋盘绘制块
    private static final int N = 19;
    int[][] chess;
    BoardPanel(Chess go){
        chess = go.board;
    }

    public void paint(Graphics g) {
        int width = getWidth();int height = getHeight();
        int margin = 30;//设置绘图边距
        int x1 = margin;int x2 = width- margin;
        int y1 = margin;int y2 = height- margin;
        int y_span = (y2-y1)/(N-1);
        int x_span = (x2-x1)/(N-1);
        int span = y_span>x_span ? x_span : y_span;//格子间距

        int X1,X2,Y1,Y2;
        X1 = Y1 = Y2 = margin;
        X2 = x1+( N - 1 ) * span;

        for (int i = 0; i<N; i++){//画横线
            g.drawLine(X1,Y1,X2,Y2);
            Y2 += span;
            Y1 = Y2;
        }
        X1 = X2 = Y1 = margin;
        Y2 = Y1 + ( N - 1) * span;
        for (int i = 0; i<N; i++){//画竖线
            g.drawLine(X1,Y1,X2,Y2);
            X1 += span;
            X2 = X1;
        }

        y2 = y1+(N-1)*span;x2 = x1+(N-1)*span;

        g.fillOval(x1+3*span-4,y1+3*span-4,8,8);
        g.fillOval(x2-3*span-4,y2-3*span-4,8,8);
        g.fillOval(x1+3*span-4,y2-3*span-4,8,8);
        g.fillOval(x2-3*span-4,y1+3*span-4,8,8);
        g.fillOval(x1+9*span-4,y1+9*span-4,8,8);

        //绘制棋盘中的棋子
        for (int i = 1; i<=N; i++)
            for (int j = 1; j<=N; j++){
                if (chess[i][j]==1){//绘制黑子
                    g.fillOval(x1 + ( j-1 ) * span - 12,y1 + ( i-1 ) * span - 12,
                            24,24);
                }
                if (chess[i][j]==2){//绘制白子
                    g.setColor(Color.WHITE);
                    g.fillOval(x1 + ( j-1 ) * span - 12,y1 + ( i-1 ) * span - 12,
                            24,24);
                    g.setColor(Color.BLACK);
                }
            }
    }
}
