import javax.swing.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: camhenlin
 * Date: 12/20/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadFrame extends JFrame implements ActionListener {
    private JTextArea postsTextField;
    private JTextField newPostTextField;
    private JPanel mainPanel;
    private JScrollPane postsScrollPane;
    private SAThread saThread;
    public String id;

    public ThreadFrame(String title, String id, SAThread saThread) {
        super(title + " : " + id);
        this.saThread = saThread;

        this.id = id;
        setContentPane(this.mainPanel);
        pack();
        newPostTextField.addActionListener(this);


        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // close sockets, etc
                haltThreadUpdate();
            }
        };

        this.addWindowListener(exitListener);
    }

    public void haltThreadUpdate() {
        this.saThread.shouldGetNewPosts = false;
    }

    public void actionPerformed(ActionEvent e) {
        String postResponse = Poster.makePost(id, newPostTextField.getText());
        newPostTextField.setText("");
        if (!postResponse.equals("")) {
            AddPost(new SAPost("YOSPOS COMMANDER", 0, postResponse));
        }
    }

    public void AddPost(SAPost saPost) {
        this.postsTextField.append("\n<" + saPost.poster + "> " + saPost.text);
        postsTextField.setCaretPosition(postsTextField.getDocument().getLength());
    }
}
