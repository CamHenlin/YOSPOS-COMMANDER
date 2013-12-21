import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


public class SAThread {
    private static final String YOSPOS_URL = "http://forums.somethingawful.com/forumdisplay.php?forumid=219";
    private static final String BASE_SA_URL = "http://forums.somethingawful.com/";

    public final String title;
    public int postCount;
    public final int id;
    public final List<SAPost> posts;
    public final ThreadFrame threadframe;

    public SAThread(String title, int postCount, int id, ThreadFrame threadframe) {
        this.threadframe = threadframe;
        this.title = title;
        this.postCount = postCount;
        this.id = id;
        this.posts = new ArrayList<SAPost>();
    }

    public void getNewPosts(int newPostCount) {
        int newPosts = newPostCount - this.postCount;
        int postsOnPage = this.postCount % 40;
        int pages = this.postCount / 40;
        int newPages = newPostCount / 40;
        this.postCount = newPostCount;

        for (int i = 0; i <= newPages - pages; i++) {
            try {

                String postList = HttpMethods.readResponse(HttpMethods.doHttpPost(BASE_SA_URL + "showthread.php?threadid=" + this.id + "&goto=lastpost", ""));
                Document doc = Jsoup.parse(postList);//Jsoup.connect("http://en.wikipedia.org/").get();

                List<Element> posts = doc.select(".post");
                int numPostsOnPage = posts.size();

                int localPostCount = 0;
                for (Element post : posts) {
                        // on same page                    on new page
                    if (localPostCount > postsOnPage || numPostsOnPage < newPosts) {
                        // String poster, int number, String text
                        String postText = "";
                        List<Element> postElements = post.select(".postbody");
                        for (Element postElement : postElements) {
                            List<Element> quotesInPost = postElement.select(".bbc-block");
                            for (Element quoteInPost : quotesInPost) {
                                postText = "\"<" + quoteInPost.text().replace(" posted:", "> ") + "\"";
                                quoteInPost.remove();
                            }

                            postText += " " + postElement.removeClass(".bbc-block").text();
                        }


                        SAPost saPost = new SAPost(post.select(".author").text(), this.postCount + localPostCount, postText);
                        this.posts.add(saPost);
                        threadframe.AddPost(saPost);
                    }

                    localPostCount++;
                }

                // this.postCount = newPostCount;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void fillLastPage() {
        String threadList = "";
        try {
            threadList = HttpMethods.readResponse(HttpMethods.doHttpPost(YOSPOS_URL, ""));

            Document doc = Jsoup.parse(threadList);//Jsoup.connect("http://en.wikipedia.org/").get();
            String pageUrl = "";
            for (int i = 0; i < 30; i++) { // 30 is worst case
                Element threadTitles = doc.select("tr.thread").get(i);


                int id = Integer.parseInt(threadTitles.select("a.thread_title").attr("href").split("threadid=")[1]);
                if (id == this.id) {
                    // System.out.println("got correct id");
                    pageUrl = threadTitles.select("a.thread_title").attr("href");
                    pageUrl += "&goto=lastpost";

                    break;
                }
            }

            if (!pageUrl.equals("")) {
                String postList = HttpMethods.readResponse(HttpMethods.doHttpPost(BASE_SA_URL + pageUrl, ""));
                doc = Jsoup.parse(postList);//Jsoup.connect("http://en.wikipedia.org/").get();

                List<Element> posts = doc.select(".post");

                for (Element post : posts) {
                    // String poster, int number, String text

                    String postText = "";
                    List<Element> postElements = post.select(".postbody");
                    for (Element postElement : postElements) {
                        List<Element> quotesInPost = postElement.select(".bbc-block");
                        for (Element quoteInPost : quotesInPost) {
                            postText = "\"<" + quoteInPost.text().replace(" posted:", "> ") + "\"";
                            quoteInPost.remove();
                        }
                        postText += " " + postElement.removeClass(".bbc-block").text();
                    }

                    SAPost saPost = new SAPost(post.select(".author").text(), this.postCount, postText);
                    this.posts.add(saPost);
                    threadframe.AddPost(saPost);

                }

                threadframe.AddPost(new SAPost("YOSPOS COMMANDER", 0, "YOU HAVE NOW TAKEN COMMAND."));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
