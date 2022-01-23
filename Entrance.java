import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public  class Entrance {

    public static User SignUp() throws NoSuchAlgorithmException, IOException {
        String UserName = JOptionPane.showInputDialog(("Please Enter your username :")).toLowerCase();

        while(Find_Username(UserName) != -1){
            UserName = JOptionPane.showInputDialog(("the username has been already chosen.\nPlease Enter Another UserName: ")).toLowerCase();
        }

        String Password = SecurePassword.GetHash(JOptionPane.showInputDialog(("Please enter the password:")));
        ;
        String FullName = JOptionPane.showInputDialog(("Please enter your FullName:"));

        boolean flag = true;

        String Email = null;

        while (flag) {

            Email = JOptionPane.showInputDialog(("Please enter your Email:"));
            if(EmailValidation(Email)){
                flag = false;
            }
            else
                JOptionPane.showMessageDialog(null,"InValid Email! enter a valid email !");
        }


        String PhoneNumber =  JOptionPane.showInputDialog(("Please enter your PhoneNumber:"));

        String Bio =  JOptionPane.showInputDialog(("Please enter your Page Bio:"));

        int Age = Integer.parseInt(JOptionPane.showInputDialog("Please enter your Age :"));

        if(Age < 12){
            JOptionPane.showMessageDialog(null,"You must be at least 12 years old to use this app !");
            return SignUp();
        }

        Image ProfileImage = null;
        ProfileImage = ShowImage.GetFile();


        User user = new User(UserName,Password,Email,FullName,PhoneNumber,Bio,Age,ProfileImage);
        User.setUsers(user);

        return user;


    }

    public static User Login() throws NoSuchAlgorithmException, IOException {
        int flag = 0;
        int user = -1;


        String UserName = JOptionPane.showInputDialog("Please enter your username:");

        String Password = JOptionPane.showInputDialog("Please enter your password:");

        while (flag != 1){
            user = Find_Username(UserName);
            if(user >= 0 && Password.equals(User.getUsers().get(user).getPassword())){
                flag = 1;
            }
            else {
                if ( user < 0) {

                    int check = JOptionPane.showConfirmDialog(null,"User not found. Do you want to creat a new account?");


                    if (check == 0) {
                        return SignUp();
                    }
                    JOptionPane.showMessageDialog(null,"username or password is incorrect. Please try again.");

                }
                break;
            }
        }
        if(user<0)
            return null;
        JOptionPane.showMessageDialog(null,"welcome " + User.getUsers().get(user).getFullName());
        return User.getUsers().get(user);
    }

    static int Find_Username(String Username){
        ArrayList<User> users = User.getUsers();
        for (int user = 0; user < users.size() ; user++)
            if(Username.equals(users.get(user).getUserName()))
                return user;

        return -1;
    }

    public static boolean EmailValidation(String Email){
        String EmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(EmailRegex);
        if (Email == null)
            return false;
        return pat.matcher(Email).matches();
    }
}
