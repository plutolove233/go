package go;

import java.util.Stack;
import java.util.Vector;

public class Chess {
    static final int[] dx = {0,0,-1,1};
    static final int[] dy = {1,-1,0,0};

    public int[][] board;//0表示没有棋子，1表示为黑子，2表示为白子
    Stack history = new Stack();
    static private boolean[][] vis;
    static private int[][] block;
    static private int[][] pos;

    public Chess(){
        board = new int[20][20];
        int[][] x = new int[20][20];
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                x[i][j] = board[i][j];
        history.push(x);
    }

    static int getAir(int[][]b,int x,int y){
        int air = 4;
        for (int i = 0; i<4; i++) {
            if (x + dx[i] <= 0 || x + dx[i] > 19 || y + dy[i] <= 0 || y + dy[i] > 19) air--;
            else if (b[x + dx[i]][y + dy[i]]!=0)    air--;
        }
        return air;
    }
    void dfs(int[][] b,int kind,int x,int y,int dfn){
        int nx,ny;
        vis[x][y] = true;
        block[x][y] = dfn;
        for (int i = 0; i<4; i++){
            nx = x+dx[i];ny = y+dy[i];
            if (nx<1 || ny<1 || nx>19 || ny>19) continue;
            if (b[nx][ny]!=kind)    continue;
            if (!vis[nx][ny]) dfs(b,kind,nx,ny,dfn);
        }
    }

    static int count_Air(int[][] b,int who){
        int air = 0;
        for (int i = 1; i<=19; i++) {
            for (int j = 1; j <= 19; j++)
                if (block[i][j] == who) air += getAir(b, i, j);
        }
        return air;
    }

    static void clean_block(int[][] b,int who){
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                if (block[i][j] == who) b[i][j] = 0;
    }

    void update(int[][] b,int order){
        block = new int[20][20];
        vis = new boolean[20][20];
        int block_num = 0;
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                if (b[i][j] == order && (!vis[i][j])) {
                    block_num++;
                    dfs(b, order, i, j,block_num);
                }
        for (int i = 1; i<=block_num; i++)
            if (count_Air(b,i)==0) clean_block(b,i);
    }

    public void Play(int who,int x,int y){//落子
        board[x][y] = who;
        int order;
        if (who==1) order = 2;
        else order = 1;
        update(board,order);
        int[][] temp = new int[20][20];
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                temp[i][j] = board[i][j];
        history.push(temp);
    }

    public void Regret(){//悔棋
        int[][] temp = new int[20][20];
        history.pop();
        history.pop();
        temp = (int[][])history.peek();
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                board[i][j] = temp[i][j];
        if (history.size()==1)  return;
    }

    static void DFS(int[][]b,int x,int y,int camp){
        vis[x][y] = true;
        if (pos[x][y]==camp) pos[x][y] = camp;
        else pos[x][y] += camp;
        for (int i = 0; i<4; i++){
            int nx = x+dx[i];
            int ny = y+dy[i];
            if (nx<1 || ny<1 || nx>19 || ny>19) continue;
            if ((!vis[nx][ny]) && b[nx][ny]==0)    DFS(b,nx,ny,camp);
        }
    }

    public int Finale(){//收官，返回2表示白字胜，返回1表示黑子胜
        int black,white;
        black = 0;white = 0;
        pos = new int[20][20];
        for (int i = 1; i<=19; i++)
            for (int j = 1; j<=19; j++)
                if (board[i][j]!=0){
                    int camp;
                    if (board[i][j]==1) camp = 1;//黑为1
                    else camp = -1;//白为-1
                    vis = new boolean[20][20];
                    DFS(board,i,j,camp);
                }
        for (int i = 1; i<=19; i++) {
            for (int j = 1; j <= 19; j++) {
                if (pos[i][j] == 1) black++;
                if (pos[i][j] == -1) white++;
            }
        }
        System.out.println(black+"  "+white);
        float res = white-176.75f;
        if (res>0)  return 2;
        return 1;
    }
}
