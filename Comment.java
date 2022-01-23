import java.util.ArrayList;
import java.util.Scanner;

public class Comment {

    private Scanner GetInput = new Scanner(System.in);

    public String Text;
    public Post Post;
    public User UserComment;
    public int LikeNum;
    public ArrayList<User> LikeUser;
    public int ReplyNum;
    public ArrayList<Reply> CommentReply ;

    public Comment(String text , Post post , User UserComment ){
        this.Text = text;
        this.Post = post;
        this.UserComment = UserComment;
        this.ReplyNum = 0;
        this.CommentReply = new ArrayList<>();
        this.LikeNum = 0;
        this.LikeUser = new ArrayList<>();
    }

    public void SeeReply(){
        if(this.CommentReply.size()==0){
            System.out.println("There is no reply");
            return;
        }
        System.out.println(this.ReplyNum);
        for (Reply replyComment : this.CommentReply){
            System.out.println(replyComment.ReplyUser.getUserName() + " : " +replyComment.Text);
        }
    }

    public void AddReply(User user){
        this.ReplyNum++;
        System.out.println("Please enter the text you want to reply");
        String text = GetInput.nextLine();
        Reply reply = new Reply(text,user);
        this.CommentReply.add(reply);
    }

}
