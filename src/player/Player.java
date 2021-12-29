package player;

public class Player {
    String username;
    int hand;//表示所持棋子,1表示黑子，2表示白子
    int state;//当前棋手的状态 0表示等待匹配状态 1表示允许下子，2表示等待对方下子

    public Player(String user,int hand,int state){
        username = user;
        this.hand = hand;
        this.state = state;
    }

    public String getUsername(){
        return username;
    }
    public String getHandInfo(){
        if (hand==0)    return "";
        if (hand==1)    return "执黑子";
        if (hand==2)    return "执白子";
        return null;
    }
    public int getHand(){return hand;}
    public int getState() { return state; }

    public void setHand(int x) {hand = x;}
    public void setState(int state) {this.state = state;}
    public void setUsername(String username) { this.username = username; }

}
