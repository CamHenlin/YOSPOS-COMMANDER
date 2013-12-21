import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Poster {

    private static final String POST_URL = "http://forums.somethingawful.com/newreply.php";
    private static final String NEWPOST_URL = "http://forums.somethingawful.com/newreply.php?action=newreply&threadid=";

    public static String makePost(String threadid, String message) {

        // System.out.println(threadid);
        Keys keys;
        try {
            keys = getStupidKeys(threadid);
        } catch (Exception e) {
            return "ERROR IN: " + threadid + ". THREAD PROBABLY LOCKED. TRY AGAIN.";
        }

        try {
            String data = HttpMethods.readResponse(HttpMethods.doHttpPost(POST_URL,
                    "action=postreply&threadid=" + threadid +
                            "&formkey=" + keys.formKey +
                            "&form_cookie=" + keys.formCookie +
                            "&message=" + message +
                            "&parseurl=yes" +
                            "&bookmark=no" +
                            "&disablesmilies=no" +
                            "&signature=no" +
                            "&MAX_FILE_SIZE=2097152" +
                            "&attachment=\"\"" +
                            "&submit=Submit Reply"));

            if (data.contains("The request you just submitted appears to have been forged in some way!")) {
                //System.out.println(keys.formCookie);
                //System.out.println(keys.formKey);
                // throw new Exception("SHIT GOT FUCKED UP :(");
                return "shit got fucked up";
            }

            if (data.contains("Sorry! The administrator has specified that users can only post one message every 10 seconds.")) {
                return "You tried posting too quickly. You must wait 10 seconds between posts.\n";
            }

            //                      return "SUCCESS IN: " + threadid + " WITH MESSAGE: " + message;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param threadid
     * @return
     */
    public static Keys getStupidKeys(String threadid) {
        // regex for formkey: "formkey"\svalue="[a-z0-9]+"
        // regex for form_cookie: "form_cookie"\svalue="[a-z0-9]+"
        Keys keys = new Keys();

        String data = null;
        try {
            data = HttpMethods.readResponse(HttpMethods.doHttpPost(NEWPOST_URL + threadid, ""));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String regex = "\"formkey\"\\svalue=\"[a-z0-9]+\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        List<String> matches = new ArrayList<String>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        keys.formKey = matches.get(0).replace("\"", "").split("=")[1];

        regex = "\"form_cookie\"\\svalue=\"[a-z0-9]+\"";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(data);
        matches = new ArrayList<String>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        keys.formCookie = matches.get(0).replace("\"", "").split("=")[1];

        return keys;
    }

}
