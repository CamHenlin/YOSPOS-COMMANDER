public class PostPostPost {

    private static final String YOSPOS_URL = "http://forums.somethingawful.com/forumdisplay.php?forumid=219";

    public static void main(String[] args) {
        // cookie manager so we can maintain session the whole time we're posting:
        java.net.CookieManager cm = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(cm);

        // LOG IN
        LoginFrame newframe = new LoginFrame();
        newframe.setSize(400, 150);
        newframe.setVisible(true);

        boolean init = true;
        SAThreads saThreads = null;
        while (true) {
            if (newframe.loggedIn) {
                try {
                    String threadList = HttpMethods.readResponse(HttpMethods.doHttpPost(YOSPOS_URL, ""));

                    if (init) {
                    saThreads = new SAThreads(threadList);
                        init = false;
                    }



                    saThreads.updateThreads(threadList);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
