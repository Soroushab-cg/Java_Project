import java.util.ArrayList;

public class UserRelation {

    private User Following;
    private User Follower;
    private static ArrayList<UserRelation> UserRelations = new ArrayList<>();


    public UserRelation(User Following, User Follower) {
        this.Following = Following;
        this.Follower = Follower;
    }

    public User getFollowing() {
        return Following;
    }

    public User getFollower() {
        return Follower;
    }

    public static ArrayList<UserRelation> getUserRelations() {
        return UserRelations;
    }

    public static void setUserRelations(UserRelation Userrelations) {
        UserRelations.add(Userrelations);
    }

    public static void DeleteUserRelation(UserRelation Userrelations){
        UserRelations.remove(Userrelations);
    }

}
