import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private Scanner GetInput = new Scanner(System.in);
    private static ArrayList<User> Users = new ArrayList<>();

    private String UserName;
    private String Password;
    private String FullName;
    public Image ProfileImage; // not done.
    public String Email;
    public String PhoneNumber;
    public String Bio;
    public int Age;
    public ArrayList<Post> UserPost;
    public ArrayList<Post> LikedPosts;
    public ArrayList<Chat> UserChats;
    public ArrayList<Comment> UserComments;
    public ArrayList<User> BlockedUsers;


    public User(String UserName , String Password , String Email , String FullName , String PhoneNumber , String Bio , int Age , Image ProfileImage){
        this.Email = Email;
        this.UserName = UserName;
        this.FullName = FullName;
        this.Password = Password;
        this.LikedPosts = new ArrayList<>();
        this.UserChats = new ArrayList<>();
        this.UserComments = new ArrayList<>();
        this.UserPost = new ArrayList<>();
        this.PhoneNumber = PhoneNumber;
        this.Bio=Bio;
        this.Age =Age;
        this.BlockedUsers = new ArrayList<>();
        this.ProfileImage = ProfileImage;
    }

    public static ArrayList<User> getUsers() {
        return Users;
    }

    public static void setUsers(User user) {
        Users.add(user);
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getFullName() {
        return FullName;
    }

    public void AddPost() throws IOException {
        String Caption;
        ArrayList<User> tag = new ArrayList<>();
        Image PostImage = null;
        PostImage = ShowImage.GetFile();
        Caption = JOptionPane.showInputDialog("Please enter a caption for this post:");
        boolean flag = true;
        User user = null;
        while (flag){
            JOptionPane.showMessageDialog(null,"Enter the user name of person to tag in your post (enter 0 to exit)");
            user = SearchUser();
            if(user == null){
                int check = JOptionPane.showConfirmDialog(null,"User Not found do you want to continue?");
                if (check == 1)
                    flag = false;
            }
            else
                tag.add(user);
        }
        Post NewPost = new Post(this,Caption,PostImage,tag);
        this.UserPost.add(NewPost);
        Post.AllPost.add(NewPost);
    }

    public int ShowAllPost(){
        int PostNum = 0;
        for (Post post : UserPost){
            Image scaleImage = post.PostImage;

            ShowImage show = new ShowImage();
            show.SetScaleImage(scaleImage);
            JFrame f = new JFrame();
            f.add(show);

            f.setSize(600, 600);
            f.setVisible(true);

            JOptionPane.showMessageDialog(null,this.UserName + " Posts :\n"+(++PostNum) + " . Post : " + post.PostCaption + " Like Num : " + post.NumOfLikes + " Comment Num : " + post.NumOfComments + "\n");

            f.dispose();
        }
        int num = Integer.parseInt(JOptionPane.showInputDialog("Choose the post num :"));
        return num-1;
    }

    public void SeePost(Post post) throws IOException {

        JOptionPane.showMessageDialog(null,"Post Info:\n" + "User : " + post.User.UserName + " Post : " + post.PostCaption + " Like Num : " + post.NumOfLikes + " Comment Num : " + post.NumOfComments);
        Image scaleImage = post.PostImage;

        ShowImage show = new ShowImage();
        show.SetScaleImage(scaleImage);
        JFrame f = new JFrame();
        f.add(show);

        f.setSize(600, 600);
        f.setVisible(true);

        int command = Main.Command(new String[]{"see likes","see comment","Add comment","Delete comment","Add Like" , "Show tagged users" ,"exit"});

        switch (command){
            case 1: //see like user.
                for (User user : post.PostLikes){
                    System.out.println(user.UserName);
                }
                break;

            case 2: // see comments.
                int select = post.SeeComments();

                JOptionPane.showMessageDialog(null,"You selected comment : " + post.PostComments.get(select));
                int Command = Main.Command(new String[]{"Like comment","See replies","Reply comment","exit"});

                switch (Command){
                    case 1:
                        post.LikeComment(select-1,this);
                        break;

                    case 2:
                        post.PostComments.get(select-1).SeeReply();
                        break;

                    case 3:
                        post.PostComments.get(select-1).AddReply(this);
                        break;

                    case 4:
                        f.dispose();
                        break;
                }
                break;


            case 3: //Add comment
                post.AddComment(this);
                break;

            case 4: //Delete Comment
                post.DeleteComment(this);
                break;

            case 5: // Add like
                post.AddLike(this);
                break;

            case 6:
                int user = post.SeeTaggedUsers();
                if(user<0){
                    break;
                }
                int check = JOptionPane.showConfirmDialog(null,"Do you want to see the user profile?");
                if(check==0){
                    f.dispose();
                    post.TaggedUsers.get(user).UserInfo();
                }
                break;

            case 7://exit
                break;
        }
        f.dispose();

    }

    public void Follow(User user){
        ArrayList<UserRelation> userRelations = UserRelation.getUserRelations();

        for(UserRelation userRelation : userRelations){
            if(userRelation.getFollower() == this && userRelation.getFollowing() == user){
                int check = JOptionPane.showConfirmDialog(null,"Your about to unfollow " + user.UserName + " .\nAre you sure? ");
                if(check == 1)
                    userRelations.remove(userRelation);
                return;
            }
        }
        UserRelation userRelation = new UserRelation(user , this);
        UserRelation.setUserRelations(userRelation);
        JOptionPane.showMessageDialog(null,"You followed "+user.UserName);

    }

    public ArrayList<User> getFollowers(){
        ArrayList<User> followers = new ArrayList<>();
        ArrayList<UserRelation> userRelations = UserRelation.getUserRelations();
        for(UserRelation userRelation : userRelations){
            if(userRelation.getFollower() == this){
                followers.add(userRelation.getFollowing());
            }
        }
        return followers;
    }

    public ArrayList<User> getFollowing(){
        ArrayList<User> followings = new ArrayList<>();
        ArrayList<UserRelation> userRelations = UserRelation.getUserRelations();
        for(UserRelation userRelation : userRelations){
            if(userRelation.getFollowing() == this){
                followings.add(userRelation.getFollower());
            }
        }
        return followings;
    }

    public void UserInfo() throws IOException {
        User user = SearchUser();
        if (user == null){
            JOptionPane.showMessageDialog(null,"User Not  Found");
            return;
        }
        if (user == this){
            this.MyInfo();
            return;
        }
        Image scaleImage = user.ProfileImage;

        ShowImage show = new ShowImage();
        show.SetScaleImage(scaleImage);
        JFrame f = new JFrame();
        f.add(show);
        f.setSize(600, 600);
        f.setVisible(true);

        JOptionPane.showMessageDialog(null,"User Info:\n"+"Username : " + user.UserName +
                "\nFull name : " + user.FullName +
                "\nAge : " + user.Age +
                "\nPage bio : " + user.Bio +
                "\nPhone number : "+user.PhoneNumber +
                "\nEmail address : "+user.Email);

        f.dispose();

        int command = Main.Command(new String[]{"Show posts","Show followers","Show following" , "Follow/UnFollow user" , "Block user" ,"Back"});

        switch (command){
            case 1: // show posts
                if(user.UserPost.size() == 0){
                    JOptionPane.showMessageDialog(null,"There is no post to show");
                    return;
                }
                int PostNum = 1;
                PostNum = user.ShowAllPost();

                if(PostNum < 0){
                    break;
                }
                this.SeePost(user.UserPost.get(PostNum));
                break;

            case 2:
                user.ShowFollowing();
                break;

            case 3:
                user.ShowFollower();
                break;

            case 4:
                this.Follow(user);
                break;

            case 5:
                this.Block(user);
                break;

            case 6:
                break;

        }

    }

    public void MyInfo() throws IOException {
        Image scaleImage = this.ProfileImage;

        ShowImage show = new ShowImage();
        show.SetScaleImage(scaleImage);
        JFrame f = new JFrame();
        f.add(show);

        f.setSize(600, 600);
        f.setVisible(true);

        JOptionPane.showMessageDialog(null,"User Info:\n"+"Username : " + this.UserName +
                "\nFull name : " + this.FullName +
                "\nAge : " + this.Age +
                "\nPage bio : " + this.Bio +
                "\nPhone number : "+this.PhoneNumber +
                "\nEmail address : "+this.Email);

        f.dispose();

        int command = Main.Command(new String[]{"Show posts","Show followers","Show following" , "Add Post" , "Delete Post" , "See Blocked User" ,"Back"});

        switch (command){
            case 1: // show posts
                if(this.UserPost.size() == 0){
                    JOptionPane.showMessageDialog(null,"There is no post to show");
                    return;
                }
                int PostNum = 1;;
                PostNum = this.ShowAllPost();

                if(PostNum < 0){
                    break;
                }
                this.SeePost(this.UserPost.get(PostNum));
                break;

            case 2:
                this.ShowFollowing();
                break;

            case 3:
                this.ShowFollower();
                break;

            case 4:
                this.AddPost();
                break;

            case 5:
                if(this.UserPost.size()==0){
                    JOptionPane.showMessageDialog(null,"No post");
                    break;
                }
                int post = this.ShowAllPost();
                this.UserPost.remove(post );
                break;

            case 6:
                this.UnBlock();
                break;


            case 7:
                break;

        }

    }

    public User SearchUser(){
        String UserName = JOptionPane.showInputDialog("Enter username to search : ");
        for (User user : Users){
            if(user.UserName.equals(UserName) && !this.BlockedUsers.contains(user)){
                return user;
            }
        }
        return null;
    }

    public void HomePage() throws IOException {
        ArrayList<User> MyFollower = getFollowers();
        ArrayList<Post> posts = new ArrayList<>();
        for (Post post : Post.AllPost){
            if(MyFollower.contains(post.User)){
                posts.add(post);
            }
        }
        int NumOfPost = 0;

        if(posts.size() == 0){
            JOptionPane.showMessageDialog(null,"No post to show.");
            return;
        }

        while (NumOfPost != 10 && NumOfPost != posts.size()){
            SeePost(posts.get(NumOfPost));

            NumOfPost++;
        }

//        System.out.println("select a num of post to see the info : (select 0 to exit)");
//        int comm = GetInput.nextInt()-1;
//        GetInput.nextLine();
//        if(comm >=0){
//            SeePost(posts.get(comm));
//        }
//        else
//            return;
    }

    public void ShowFollowing(){
        ArrayList<User> userRelations = this.getFollowing();
        int User = 0;
        String[] UserList = new String[100];
        for (User user : userRelations){
            UserList[User++] = (User + " . UserName : " +user.getUserName());
        }
        JOptionPane.showMessageDialog(null,UserList);
    }

    public void ShowFollower(){
        ArrayList<User> userRelations = this.getFollowers();
        int User = 0;
        String[] UserList = new String[100];
        for (User user : userRelations){
            UserList[User++] = (User + " . UserName : " +user.getUserName());
        }
        JOptionPane.showMessageDialog(null,UserList);
    }

    public void Block(User user){
        ArrayList<User> Follower = this.getFollowers();
        ArrayList<User> Following = this.getFollowing();

        if(user != null){
            this.BlockedUsers.add(user);

            if(Following.contains(user)){
                UserRelation userRelation = new UserRelation(this,user);
                UserRelation.DeleteUserRelation(userRelation);
            }
            if(Follower.contains(user)){
                UserRelation userRelation = new UserRelation(user,this);
                UserRelation.DeleteUserRelation(userRelation);
            }
        }
    }

    public void UnBlock(){
        if(this.BlockedUsers.isEmpty()){
            JOptionPane.showMessageDialog(null,"No user in blocked list");
            return;
        }

        int  BlockUserNum = 0;
        String[] BlockedList = new String[100];
        for (User Block : this.BlockedUsers){
            BlockedList[BlockUserNum++] =((BlockUserNum++) + " . UserName : " + Block.getUserName());
        }

        BlockedList[BlockUserNum] =("Choose User : (enter 0 for cancel)");
        int user = Integer.parseInt(JOptionPane.showInputDialog(BlockedList))-1;

        if(user < 0){
            return;
        }

        if(user >= BlockUserNum){
            JOptionPane.showMessageDialog(null,"wrong input ");
            return;
        }

        this.BlockedUsers.remove(user);

    }
}
