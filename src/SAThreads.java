import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


public class SAThreads {
    public final List<SAThread> saThreads;

    public SAThreads(String input) {
        List<SAThread> matches = new ArrayList<SAThread>();
        Document doc = Jsoup.parse(input);
        int y, x, counter;
        x = 0;
        y = 0;
        counter = 0;
        for (int i = 0; i < 30; i++) {
            Element threadTitles = doc.select("tr.thread").get(i);
            String title = threadTitles.select("a.thread_title").text();
            String count = threadTitles.select("td.replies").text();
            String id = threadTitles.select("a.thread_title").attr("href").split("threadid=")[1];

            ThreadFrame newFrame;
            newFrame = new ThreadFrame(title, id);
            newFrame.setSize(400, 290);
            newFrame.setLocation(x, y);
            counter++;
            // let's tile windows!
            if (counter < 6) {
                x += 400;
            }
            if (counter == 6) {
                y += 290;
                x = 0;
                counter = 0;
            }

            newFrame.setVisible(true);

            SAThread saThread = new SAThread(title, Integer.parseInt(count), Integer.parseInt(id), newFrame);
            saThread.fillLastPage();

            matches.add(saThread);
        }
        this.saThreads = matches;
    }

    /**
     * @param input
     * @return
     */
    public void updateThreads(String input) {

        List<SAThread> matches = new ArrayList<SAThread>();
        Document doc = Jsoup.parse(input);
        for (int i = 0; i < 30; i++) {
            Element threadTitles = doc.select("tr.thread").get(i);
            String title = threadTitles.select("a.thread_title").text();
            String count = threadTitles.select("td.replies").text();
            String id = threadTitles.select("a.thread_title").attr("href").split("threadid=")[1];

            Boolean oldThread = false;
            for (SAThread saThread : this.saThreads) {
                if (saThread.id == Integer.parseInt(id)) {
                    oldThread = true;

                    if (saThread.postCount != Integer.parseInt(count)) {
                        saThread.getNewPosts(Integer.parseInt(count));
                    }
                }

                matches.add(saThread);
            }

            if (!oldThread) {
                ThreadFrame newframe = new ThreadFrame(title, id);
                newframe.setSize(400, 290);
                newframe.setVisible(true);
                SAThread saThread = new SAThread(title, Integer.parseInt(count), Integer.parseInt(id), newframe);
                saThread.fillLastPage();
                saThreads.add(saThread);
            }

        }
    }
}
