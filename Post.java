import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Post {

    private Scanner Get_Input = new Scanner(System.in);

    public static ArrayList<Post> AllPost = new ArrayList<>();

    public User User;
    public int NumOfLikes;
    public int NumOfComments;
    public String PostCaption;
    public Image PostImage;
    public ArrayList<Comment> PostComments;
    public ArrayList<User> PostLikes;
    public ArrayList<User> TaggedUsers;

    public Post(User user , String PostCaption , Image PostImage,ArrayList<User> TaggedUsers){
        this.User = user;
        this.PostCaption = PostCaption;
        this.NumOfLikes = 0;
        this.NumOfComments = 0;
        this.PostComments = new ArrayList<>();
        this.PostLikes = new ArrayList<>();
        this.PostImage = PostImage;
        this.TaggedUsers = TaggedUsers;
    }


    public void AddLike(User user){
        for(Post likePost : user.LikedPosts){
            if(this == likePost){
                user.LikedPosts.remove(this);
                this.RemoveLike(user);
                return;
            }
        }

        user.LikedPosts.add(this);
        this.PostLikes.add(user);
        this.NumOfLikes++;
    }

    public void RemoveLike(User user){
        NumOfLikes--;
        PostLikes.remove(user);
    }

    public void AddComment(User user){
        String Comment = JOptionPane.showInputDialog("Write you're comment for this post:");
        Comment comment = new Comment(Comment,this,user);
        this.PostComments.add(comment);
        this.NumOfComments++;
    }

    public void DeleteComment(User user){
        for(Comment comment : this.PostComments){
            if(comment.UserComment == user){
                this.PostComments.remove(comment);
                return;
            }
        }
        System.out.println("Comment not found");
    }

    public int SeeComments(){
        int CommentNum = 1;
        String[] Comments = new String[100];
        if(this.PostComments == null){
            JOptionPane.showMessageDialog(null,"There is no comment");
            return -1;
        }
        for (Comment comment : this.PostComments){

            Comments[CommentNum-1] = (CommentNum++ + " . User : " + comment.UserComment.getUserName() +  ", Text : " + comment.Text + ", like Num : " + comment.LikeNum + ", Reply Num : " + comment.ReplyNum + "\n");

        }
        Comments[CommentNum] = "Chose the post num: (enter 0 to exit)";
        int num = parseInt(JOptionPane.showInputDialog(Comments));
        return num-1;
    }

    public void LikeComment(int CommentNum , User user){
        Comment comment = this.PostComments.get(CommentNum);
        for (User LikeUser : comment.LikeUser){
            if (LikeUser == user){
                comment.LikeUser.remove(LikeUser);
                comment.LikeNum--;
                return;
            }
        }
        comment.LikeNum++;
        comment.LikeUser.add(user);

    }

    public int SeeTaggedUsers(){
        int tag =  0;
        String[] TaggedUsers = new String[100];

        for (User user : this.TaggedUsers){
            TaggedUsers[tag++] = tag + " . UserName = " + user.getUserName();
        }
        TaggedUsers[tag] = "Chose your user or enter 0 to exit";
        int num = Integer.parseInt(JOptionPane.showInputDialog(TaggedUsers));

        return num-1;
    }

}
