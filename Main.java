import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        while (true){
            User NowUser = null;
            boolean IsWorking = false;
            int Entry = Command(new String[]{"Login","Sign up"});
            switch (Entry){
                case 1:
                    NowUser = Entrance.Login();
                    IsWorking = true;
                    break;
                case 2:
                    NowUser = Entrance.SignUp();
                    IsWorking = true;
                    break;
            }
            if(NowUser == null)
                return;

            while (IsWorking){
                int Command = Main.Command(new String[]{"See my user page","See my home page","See my chat page" , "See a User Page" , "Logout"});

                switch (Command){
                    case 1: // user page
                        NowUser.MyInfo();
                        break;

                    case 2: // home page
                        NowUser.HomePage();
                        break;

                    case 3:
                        boolean StartChat = true;

                        while (StartChat) {
                            Command = Main.Command(new String[]{"Create new chat", "Show old chats", "Back"});

                            switch (Command) {
                                case 1:
                                    Chat.CreatGroup(NowUser);
                                    break;

                                case 2:
                                    Chat.OpenGroups(NowUser);
                                    break;

                                case 3:
                                    StartChat = false;
                                    break;
                            }
                        }
                        break;

                    case 4:
                        NowUser.UserInfo();
                        break;

                    case 5:
                        IsWorking = false;
                        NowUser = null;
                        break;
                }
            }

        }
    }

    static int Command(String[] Options) {
        String[] comm = new String[Options.length+2];
        comm[0] = "What do you want to do:\n";
        int option = 0;
        while (option != Options.length){
            comm[option+1] = ((option+1) + " . " +Options[option] +"\n");
            option++;
        }
        return Integer.parseInt(JOptionPane.showInputDialog(comm));
    }
}
