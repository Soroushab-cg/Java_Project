
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat {
    private static Scanner GetInput = new Scanner(System.in);

    public String GroupName;
    public ArrayList<User> Admins;
    public ArrayList<User> Members;
    public ArrayList<Message> Messages;

    private Chat(String GroupName,ArrayList<User> Admins,ArrayList<User> Members , ArrayList<Message> messages){
        this.GroupName=GroupName;
        this.Admins = Admins;
        this.Members = Members;
        this.Messages = messages;
    }

    public static Message SendMessage(User sender, ArrayList<User> receivers){
        String text = JOptionPane.showInputDialog("Enter your message:");
        Message message = new Message(text, sender, receivers);

        return message;
    }

    public static void OpenGroups(User user){

        boolean Done = true;

        while (Done) {

            if (user.UserChats.size() == 0) {
                int check = JOptionPane.showConfirmDialog(null,"There is no chat available! would you like to create a new chat? (yes/no)");
                if (check==1) {
                    CreatGroup(user);
                    break;
                }
                else
                    return;
            }
            String[] ChatsList = new String[100];

            int ChatNum = 0;
            ChatsList[ChatNum++] = ("Your chat list :");
            for (Chat UserChat : user.UserChats) {
                ChatsList[ChatNum] = ((ChatNum++) + " . Name : " + UserChat.GroupName +"\n");
            }

            ChatsList[ChatNum] = ("Enter the chat's number you want to open : ");
            ChatNum = Integer.parseInt(JOptionPane.showInputDialog(ChatsList));

            if (ChatNum > user.UserChats.size() + 1) {
                JOptionPane.showMessageDialog(null,"Wrong input!");
                break;
            }

            int numofchat = 0;

            Chat chat = user.UserChats.get(ChatNum - 1);

            if (chat.Messages.size() == 0) {
                int check = JOptionPane.showConfirmDialog(null,"There is no message available. Would you like to send a new message ? (yes/no)");

                if (check==1) {
                    ArrayList<User> receivers = new ArrayList<>();
                    receivers.addAll(chat.Admins);
                    receivers.addAll(chat.Members);
                    Message messages = chat.SendMessage(user, receivers);
                    chat.Messages.add(messages);
                    return;
                } else
                    return;
            }

            String[] Chats = new String[100];
            while (numofchat != 10 && numofchat != chat.Messages.size()) {
                Chats[numofchat++] = ((numofchat ) + " . User : " + chat.Messages.get(numofchat-1).Sender.getUserName() +
                        "\ntext : " + chat.Messages.get(numofchat-1).Text + "\n");

            }

            Chats[numofchat] = ((numofchat + 1) + " : Add a new message\n" + (numofchat + 2) + " : Chat setting\n" + (numofchat + 3) + " : Back\nselect a chat:");

            int chose = Integer.parseInt(JOptionPane.showInputDialog(Chats));

            if (chose == numofchat + 1) {
                ArrayList<User> receivers = new ArrayList<>();
                receivers.addAll(chat.Admins);
                receivers.addAll(chat.Members);
                chat.Messages.add(Chat.SendMessage(user, receivers));
                break;
            } else if (chose == numofchat + 2) {
                chat.GroupSetting(user, chat);
                break;
            }
            else if(chose == numofchat+3){
                return;
            }

            chose--;

            String[] ChatsText = new String[100];

            JOptionPane.showMessageDialog(null,"you chose : \n"+"User : " + chat.Messages.get(chose).Sender.getUserName() + " " +
                    "text : " + chat.Messages.get(chose).Text);

            int message = Main.Command(new String[]{"reply message", "forward message", "see replies", "exit"});

            switch (message) {
                case 1:
                    String reply = JOptionPane.showInputDialog("Enter your reply text :");
                    chat.Messages.get(chose).Replies.add(new Reply(reply, user));
                    break;

                case 2:
                    ChatNum = 0;
                    for (Chat UserChats : user.UserChats) {
                        ChatsText[ChatNum++] = (ChatNum + " . Group Name : " + UserChats.GroupName+ "\n");
                    }

                    ChatsText[ChatNum] = ("Select chat number to open : ");
                    ChatNum = Integer.parseInt(JOptionPane.showInputDialog(ChatsText))-1;

                    String text = "Forwarded message \n" + chat.Messages.get(chose).Text;
                    ArrayList<User> receivers = new ArrayList<>();
                    receivers.addAll(chat.Admins);
                    receivers.addAll(chat.Members);
                    Message message1 = new Message(text, user, receivers);
                    user.UserChats.get(ChatNum ).Messages.add(message1);

                    break;

                case 3:
                    int ReplyNum = 0;
                    String[] ReplyText = new String[100];
                    for (Reply replymessage : chat.Messages.get(chose).Replies) {
                        ReplyText[ReplyNum++] = ("User : " + replymessage.ReplyUser.getUserName() +
                                "\nText : " + replymessage.Text + "\n");
                    }
                    JOptionPane.showMessageDialog(null,ReplyText);
                    break;

                case 4:
                    Done = false;
                    break;
            }
        }

    }
    
    public void GroupSetting(User user ,Chat chat){
        boolean Done = true;

        while (Done) {
            if (chat.Admins.contains(user)) { // admin setting.

                int Setting = Main.Command(new String[]{"See admins", "See members", "Add member", "Add admin", "Remove member", "Exit"});

                switch (Setting) {
                    case 1:
                        int AdminNum = 0;
                        String[] Admins = new String[100];
                        for (User admin : chat.Admins) {
                            Admins[AdminNum++] = (admin.getUserName());
                        }
                        JOptionPane.showMessageDialog(null,Admins);
                        break;

                    case 2:
                        int MemberNum = 0;
                        String[] Member = new String[100];
                        for (User member : chat.Members) {
                            Member[MemberNum++] = (member.getUserName());
                        }
                        JOptionPane.showMessageDialog(null,Member);
                        break;

                    case 3:
                        User NewMember = user.SearchUser();
                        if (chat.Members.contains(NewMember) || chat.Admins.contains(NewMember)) {
                            JOptionPane.showMessageDialog(null,"This user is already in this chat.");
                            break;
                        } else if (NewMember == null) {
                            JOptionPane.showMessageDialog(null,"user not found");
                            break;
                        }

                        chat.Members.add(NewMember);
                        NewMember.UserChats.add(chat);
                        ArrayList<User> receivers = new ArrayList<>();
                        receivers.addAll(chat.Admins);
                        receivers.addAll(chat.Members);
                        String text = "Say hi to " + NewMember.getUserName();
                        chat.Messages.add(new Message(text, user, receivers));
                        break;

                    case 4: ;
                        String Add = JOptionPane.showInputDialog("Choose 1 to add admin from members or 2 to choose from all users ");

                        if (Add.equals("1")) {
                            int member = 0;
                            String[] Members = new String[100];
                            for (User members : chat.Members) {
                                Members[member++] = (member + " . " + members.getUserName() + "\n");
                            }

                            if (chat.Members.size() == 0) {
                                JOptionPane.showMessageDialog(null,"There is no member");
                                break;
                            }

                            Members[member] = ("Enter user's number :");
                            member = Integer.parseInt(JOptionPane.showInputDialog(Members)) - 1;
                            chat.Admins.add(chat.Members.get(member));
                            chat.Members.remove(member);
                        } else {
                            User admin = user.SearchUser();

                            if (admin != null) {
                                chat.Admins.add(admin);
                                admin.UserChats.add(chat);
                                receivers = new ArrayList<>();
                                receivers.addAll(chat.Admins);
                                receivers.addAll(chat.Members);
                                text = "Say hi to " + admin.getUserName();
                                chat.Messages.add(new Message(text, user, receivers));
                            } else {
                                JOptionPane.showMessageDialog(null,"User not found !");
                                break;
                            }

                        }

                        break;

                    case 5:
                        if (chat.Members.size() == 0) {
                            JOptionPane.showMessageDialog(null,"There is no member in this group");
                            break;
                        }
                        int member = 0;
                        String[] Members = new String[100];
                        Members[member++] = ("Choose a member to remove:\n");
                        for (User members : chat.Members) {
                            Members[member] = (member + " . username :" + members.getUserName()+"\n");
                            member++;
                        }
                        Members[member] = ("Choose member's number:");
                        member = Integer.parseInt(JOptionPane.showInputDialog(Members));
                        chat.Members.get(member - 1).UserChats.remove(chat);
                        chat.Members.remove(member - 1);
                        break;

                    case 6:
                        Done = false;
                        break;
                }


            }

            else { // is member.
                int Setting = Main.Command(new String[]{"See Admins","See Members","Left a group","Back"});

                switch (Setting){
                    case 1:
                        int AdminNum = 0;
                        String[] Admins = new String[100];
                        for (User admin : chat.Admins) {
                            Admins[AdminNum++] = (admin.getUserName());
                        }
                        JOptionPane.showMessageDialog(null,Admins);
                        break;

                    case 2:
                        int MemberNum = 0;
                        String[] Member = new String[100];
                        for (User member : chat.Members) {
                            Member[MemberNum++] = (member.getUserName());
                        }
                        JOptionPane.showMessageDialog(null,Member);
                        break;

                    case 3:
                        chat.Members.remove(user);
                        user.UserChats.remove(chat);
                        break;

                    case 4:
                        Done = false;
                        break;

                }
            }
        }
    }

    public static void CreatGroup(User user){
        String name = JOptionPane.showInputDialog(("Enter a name for your chat :"));

        JOptionPane.showMessageDialog(null,"Enter the username of user you want to make admin (enter 0 when you are finished)");
        User admin = user;
        boolean flag =false;
        ArrayList<User> newAdmins = new ArrayList<>();
        while(admin != null || flag == true) {
            flag = false;
            if(!newAdmins.contains(admin)) {
                newAdmins.add(admin);
            }
            admin = user.SearchUser();
            if(admin == null){
                int check = JOptionPane.showConfirmDialog(null,"User not found. Do you want to continue?");
                if(check==0){
                    flag = true;
                    admin = user.SearchUser();
                }
            }
        }


        JOptionPane.showMessageDialog(null,"Enter the username of user you want to make member (enter 0 when you are finished)");
        User member = user;
        boolean flag2 =false;
        ArrayList<User> newMembers = new ArrayList<>();
        while(member != null || flag2 == true) {
            flag2 = false;
            if(!newAdmins.contains(member) && !newMembers.contains(member) ) {
                newMembers.add(member);
            }
            member = user.SearchUser();
            if(member == null){
                int check = JOptionPane.showConfirmDialog(null,"User not found. Do you want to continue?");

                if(check == 0){
                    flag2 = true;
                    member = user.SearchUser();
                }
            }
        }


        JOptionPane.showMessageDialog(null,"Write the first message to send:");
        ArrayList<User> receivers = new ArrayList<>();
        receivers.addAll(newMembers);
        receivers.addAll(newAdmins);
        ArrayList<Message> newMessages = new ArrayList<>();
        newMessages.add(SendMessage(user, receivers));
        Chat chat = new Chat(name, newAdmins, newMembers, newMessages);
        for(User newUser : receivers ){
            newUser.UserChats.add(chat);
        }
    }


}




