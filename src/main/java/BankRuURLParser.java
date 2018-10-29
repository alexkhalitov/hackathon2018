import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankRuURLParser {

    private static final int PAGES = 244;
    //    private static final String PAGE_PREFIX = "http://www.banki.ru/services/responses/bank/sberbank/?rate[]=1&rate[]=2&page=";
    private static final String PAGE_PREFIX = "http://www.banki.ru/services/responses/bank/sberbank/?rate[]=4&rate[]=5&page=";
    private static final String OUT_FILE = "urls45.txt";

    private static final Pattern URL_PATTERN = Pattern.compile("\"header-h3\"(.)*href=\"(.+)\"");

    public static void main(String[] args) throws Exception {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(OUT_FILE)))) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                for (int i = 1; i < PAGES; i++) {
                    HttpGet httpGet = new HttpGet(PAGE_PREFIX + i);
                    try (CloseableHttpResponse response = client.execute(httpGet)) {
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() != HttpStatus.OK.value()) {
                            System.err.println(statusLine.toString());
                            System.err.println(i);
                            return;
                        }
                        HttpEntity httpEntity = response.getEntity();
                        StringBuilder page = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                page.append(line).append("\n");
                            }
                        }
                        Matcher matcher = URL_PATTERN.matcher(page.toString());

                        while (matcher.find()) {
                            writer.println(matcher.group(2));
                        }

                    }
                }
            }
        }
    }
}
