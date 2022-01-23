
import java.util.ArrayList;

public class Message {
    public String Text;
    public User Sender;
    public ArrayList<User> Receivers;
    public ArrayList<Reply> Replies;

    public Message(String text, User sender, ArrayList<User> receivers) {
        this.Text = text;
        this.Sender = sender;
        this.Receivers = receivers;
        this.Replies = new ArrayList<>();
    }



}
