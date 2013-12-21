import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: camhenlin
 * Date: 12/20/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginFrame extends JFrame implements ActionListener {

    private static final String LOGIN_URL = "https://forums.somethingawful.com/account.php";

    private JButton LoginButton;
    private JLabel UserNameLabel;
    private JLabel PasswordLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JPanel jpanel;

    public Boolean loggedIn = false;

    public LoginFrame() {

        setContentPane(this.jpanel);
        this.LoginButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            HttpMethods.readResponse(HttpMethods.doHttpPost(LOGIN_URL, "username=" + usernameTextField.getText() + "&password=" + passwordTextField.getText() +  "&action=login"));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        this.loggedIn = true;
        this.dispose();

    }
}
