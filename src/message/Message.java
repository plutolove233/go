package message;

import java.io.Serializable;

public class Message implements Serializable {
    int action; //请求动作 -1表示请求悔棋,0表示匹配成功,1表示下子,2表示允许悔棋,
                // 3表示结束,4表示请求结束,5表示游戏对局结果返回
    int px;
    int py;//请求动作的位置
    String context;//请求内容

    public Message(int action,int px,int py,String c){
        this.action = action;this.px = px;this.py = py;this.context = c;
    }

    public int getAction() { return action; }

    public int getPx() { return px; }

    public int getPy() { return py; }

    public String getContext() { return context; }
}
