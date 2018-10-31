import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.regex.Pattern;

public class BankRuPageParser45 {

    private final static String resultDir = "result" + File.separatorChar + "result45";
    private final static String urlFile = "urls45.txt";
    private final static int DIRS = 10;
    private final static int FILE_LIMIT = 5000;


    private final static String URL_PREFIX = "http://www.banki.ru";

    private final static Pattern TEXT_PATTERN = Pattern.compile("<div class=\"article-text response-page__text markup-inside-small markup-inside-small--bullet\" itemprop=\"description\" data-test=\"responses-message\">");


    public static void main1(String[] args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(urlFile))) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                String url;
                int j = -1;
                while ((url = reader.readLine()) != null && !url.isEmpty()) {
                    j++;
                    if (j > FILE_LIMIT) {
                        break;
                    }
                    File dir = new File(resultDir + File.separatorChar + (j % DIRS));
                    File file = new File(dir, j + ".txt");
                    if (file.exists()) {
                        continue;
                    }
                    HttpGet httpGet = new HttpGet(URL_PREFIX + url);

                    try (CloseableHttpResponse response = client.execute(httpGet)) {
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() != HttpStatus.OK.value()) {
                            System.err.println(statusLine.toString());
                            System.err.println(j);
                            return;
                        }
                        HttpEntity httpEntity = response.getEntity();
                        StringBuilder page = new StringBuilder();
                        try (BufferedReader pageReader = new BufferedReader(new InputStreamReader(httpEntity.getContent()))) {
                            String line;
                            while ((line = pageReader.readLine()) != null) {
                                page.append(line).append("\n");
                            }
                        }

                        String text = TEXT_PATTERN.split(page.toString())[1].split("</div>")[0];
                        dir.mkdirs();
                        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                            writer.print(text);
                        }
                        Thread.sleep(100); //TODO поспим, чтоб не забанили

                    }
                }
            }
        }
    }
}
