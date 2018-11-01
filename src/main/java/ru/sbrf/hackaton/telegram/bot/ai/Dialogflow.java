package ru.sbrf.hackaton.telegram.bot.ai;

import com.google.cloud.dialogflow.v2.*;

public class Dialogflow {
    public static final String PROJECT_ID = "newagent-5738a";
    public static final String CASH_BACK_INTENT = "cashBack";
    public static final String DEFAULT_FALLBACK = "Default Fallback Intent";
    /*public static void main(String[] args) throws Exception {
        System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        detectIntentTexts("newagent-5738a", Arrays.asList("Где cashback?", "pppppp"), UUID.randomUUID().toString(), "ru-ru");
    }*/

    public static QueryResult detectIntentTexts(
            String projectId,
            String text,
            String sessionId,
            String languageCode) throws Exception {
        QueryResult queryResults = null;
        // Instantiates a client
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());

            // Detect intents for each text input
            //for (String text : texts) {
            // Set the text (hello) and language code (en-US) for the query
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

            // Build the query with the TextInput
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            // Display the query result
            com.google.cloud.dialogflow.v2.QueryResult queryResult = response.getQueryResult();

            System.out.println("====================");
            System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
            System.out.format("Detected Intent: %s (confidence: %f)\n",
                    queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
            System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

            return queryResult;
        }
        //}
    }
}
