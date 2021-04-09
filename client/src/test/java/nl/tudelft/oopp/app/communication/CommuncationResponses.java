package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.Question;

import java.util.List;
import java.util.UUID;

public abstract class CommuncationResponses {
    /**
     * Method to return post room response body example for tests.
     *
     * @param roomName Name of the room
     * @return Response body
     */
    public static String postRoomBodyResponse(String roomName) {
        return "{\n  \"id\": 2,\n  \"name\": \"" + roomName + "\",\n  "
                + "\"linkIdStudent\": \""
                + "f71b9850-a0b1-4ec6-b85e-ba4fffeaa7c9\",\n  "
                + "\"linkIdModerator\": \""
                + "bea8d9bc-a3fd-4070-9dae-d4ceadfad3a6\",\n  "
                + "\"isOpen\": true,\n  \"permission\": false,\n  \""
                + "createdAt\": \"2021-03-22T14:58:31.109+0000"
                + "\",\n  \"updatedAt\": \""
                + "2021-03-22T14:58:31.109+0000\"\n}";
    }

    /**
     * String with room.
     * @param roomName - room name
     * @return String
     */
    public static String postRoomBodyResponse1(String roomName) {
        return "{\"id\":0,"
                + "\"name\":\"" + roomName + "\","
                + "\"linkIdStudent\":\"f71b9850-a0b1-4ec6-b85e-ba4fffeaa7c9\","
                + "\"linkIdModerator\":\"bea8d9bc-a3fd-4070-9dae-d4ceadfad3a6\","
                + "\"permission\":false,"
                + "\"numberQuestionsInterval\":2147483647,"
                + "\"timeInterval\":2147483647,"
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":true}";
    }

    /**
     * String with room.
     * @return String
     */
    public static String getListOfRooms() {
        return "[{\"id\":0,"
                + "\"name\":\"name\","
                + "\"linkIdStudent\":\"f71b9850-a0b1-4ec6-b85e-ba4fffeaa7c9\","
                + "\"linkIdModerator\":\"bea8d9bc-a3fd-4070-9dae-d4ceadfad3a6\","
                + "\"permission\":false,"
                + "\"numberQuestionsInterval\":2147483647,"
                + "\"timeInterval\":2147483647,"
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":true}]";
    }


    /**
     * Method to return check room response body example for tests.
     *
     * @param roomName Name of the room
     * @param roomLink Link of the room
     * @return Response body
     */
    public static String checkRoomBodyResponse(String roomName, String roomLink) {
        return "{\n  \"id\": 1,\n  \"name\": \"Anonymous\",\n  "
                + "\"isModerator\": false,\n  \"roomId\": {\n    \""
                + "id\": 1,\n    \"name\": \"" + roomName + "\","
                + "\n    \"linkIdStudent\": \"" + roomLink + "\","
                + "\n    \"linkIdModerator\": \"" + roomLink + ""
                + "\",\n    \"isOpen\": true,\n    \"permission\""
                + ": true,\n    \"createdAt\": \""
                + "2021-03-22T16:56:05.303+0000\",\n    \"updatedAt"
                + "\": \"2021-03-22T16:56:05.303+0000\"\n  },\n  "
                + "\"createdAt\": \""
                + "2021-03-22T16:56:16.630+0000\",\n  \"updatedAt\": "
                + "\"2021-03-22T16:56:16.630+0000\"\n}";
    }

    /**
     * Method to set a list pf questions as String.
     * @param questions - list of questions
     * @param roomLink - link of room
     * @param roomName - name of room
     * @param userId - id of user
     * @return String
     */
    public static String getQuestionsBodyResponse(
            List<Question> questions, String roomLink, String roomName, String userId) {
        return "[{\n"
                + "\"id\":1,"
                + "\"upVotes\":0,"
                + "\"room\":"
                + "{\"id\":0,"
                + "\"name\":" + roomName + ","
                + "\"linkIdStudent\":\"" + roomLink + "\","
                + "\"linkIdModerator\":\"" + roomLink + "\","
                + "\"permission\":\"true\","
                + "\"numberQuestionsInterval\":\"2147483647\","
                + "\"timeInterval\":\"2147483647\","
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":\"true\""
                + "},"
                + "\"user\":"
                + "{\"id\":0,"
                + "\"name\":\"nick\","
                + "\"isModerator\":\"true\","
                + "\"roomId\":"
                + "{\"id\":0,"
                + "\"name\":" + roomName + ","
                + "\"linkIdStudent\":\"" + roomLink + "\","
                + "\"linkIdModerator\":" + roomLink + "\","
                + "\"permission\":\"true\","
                + "\"numberQuestionsInterval\":\"2147483647\","
                + "\"timeInterval\":\"2147483647\","
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":\"true\""
                + "},"
                + "\"createdAt\":\"2021-04-07T11:10:46.254+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:46.254+0000\""
                + "},"
                + "\"questionText\":\"" + questions.get(0).getQuestionText() + "\","
                + "\"answerText\":\"null\","
                + "\"answered\":\"true\","
                + "\"duration\":\"0:0:24\","
                + "\"ageSeconds\":\"2\","
                + "\"createdAt\":\"2021-04-07T13:11:06.112525\","
                + "\"updatedAt\":\"2021-04-07T13:11:06.112525\","
                + "\"totalUpVotes\":\"0\""
                + "},\n{"
                + "\"id\":2,"
                + "\"upVotes\":0,"
                + "\"room\":"
                + "{\"id\":0,"
                + "\"name\":" + roomName + ","
                + "\"linkIdStudent\":\"" + roomLink + "\","
                + "\"linkIdModerator\":\"" + roomLink + "\","
                + "\"permission\":\"true\","
                + "\"numberQuestionsInterval\":\"2147483647\","
                + "\"timeInterval\":\"2147483647\","
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":\"true\""
                + "},"
                + "\"user\":"
                + "{\"id\":0,"
                + "\"name\":\"nick\","
                + "\"isModerator\":\"true\","
                + "\"roomId\":"
                + "{\"id\":0,"
                + "\"name\":" + roomName + ","
                + "\"linkIdStudent\":\"" + roomLink + "\","
                + "\"linkIdModerator\":" + roomLink + "\","
                + "\"permission\":\"true\","
                + "\"numberQuestionsInterval\":\"2147483647\","
                + "\"timeInterval\":\"2147483647\","
                + "\"startDate\":\"2021-04-07T11:10:41.850161\","
                + "\"createdAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:41.910+0000\","
                + "\"open\":\"true\""
                + "},"
                + "\"createdAt\":\"2021-04-07T11:10:46.254+0000\","
                + "\"updatedAt\":\"2021-04-07T11:10:46.254+0000\""
                + "},"
                + "\"questionText\":\"" + questions.get(1).getQuestionText() + "\","
                + "\"answerText\":\"null\","
                + "\"answered\":\"true\","
                + "\"duration\":\"0:0:24\","
                + "\"ageSeconds\":\"2\","
                + "\"createdAt\":\"2021-04-07T13:11:06.112525\","
                + "\"updatedAt\":\"2021-04-07T13:11:06.112525\","
                + "\"totalUpVotes\":\"0\""
                + "}]";
    }

    /**
     * Method to set up the response for a list of questions.
     * @param questions - question list
     * @param roomLink - roomLink String
     * @param roomName - roomName
     * @param userId - userId
     * @return String
     */
    public static String getExportedQuestionsBodyResponse(
            List<Question> questions, String roomLink, String roomName, String userId
    ) {
        return "Questions and Answers from Room: sala 17\n"
                + "\n"
                + "Question: \n"
                + "0:0:28: question1\n"
                + "Answers:\n"
                + "0:0:35: This question was answered during the lecture\n"
                + "\n"
                + "Question: \n"
                + "0:0:31: question2\n"
                + "This question was not answered yet\n"
                + "\n";
    }

    /**
     * String with feedback list.
     * @param roomName - name of room
     * @return String
     */
    public static String getFeedBackList(String roomName) {
        return "[{\"id\":1,"
                + "\"comment\":\"Alright\","
                + "\"rating\":4,"
                + "\"room\":"
                + "{\"id\":31,"
                + "\"name\":\"" + roomName + "\","
                + "\"linkIdStudent\":\"77781e9f-97df-456f-a3f3-00fcca9eefe0\","
                + "\"linkIdModerator\":\"96603678-5cd8-4813-9ba5-ec0248b6925c\""
                + ",\"permission\":true,"
                + "\"numberQuestionsInterval\":2147483647,"
                + "\"timeInterval\":2147483647,"
                + "\"startDate\":\"2021-04-08T10:48:23.533074\","
                + "\"createdAt\":\"2021-04-08T10:48:23.548+0000\","
                + "\"updatedAt\":\"2021-04-08T10:48:23.548+0000\","
                + "\"open\":false},"
                + "\"createdAt\":\"2021-04-08T12:50:39.352084\","
                + "\"updatedAt\":\"2021-04-08T12:50:39.352084\""
                + "}]";
    }

    /**
     * Method for getting list of polls.
     * @return String
     */
    public static String getPollsList() {
        return "[{\"id\":2,"
                + "\"room\":"
                + "{\"id\":31,"
                + "\"name\":\"room\","
                + "\"linkIdStudent\":\"77781e9f-97df-456f-a3f3-00fcca9eefe0\","
                + "\"linkIdModerator\":\"96603678-5cd8-4813-9ba5-ec0248b6925c\","
                + "\"permission\":true,"
                + "\"numberQuestionsInterval\":2147483647,"
                + "\"timeInterval\":2147483647,"
                + "\"startDate\":\"2021-04-08T10:48:23.533074\","
                + "\"createdAt\":\"2021-04-08T10:48:23.548+0000\","
                + "\"updatedAt\":\"2021-04-08T10:48:23.548+0000\","
                + "\"open\":true},"
                + "\"pollOptions\":"
                + "[{\"id\":25,"
                + "\"optionText\":\"hi!\","
                + "\"scoreRate\":-1.0,"
                + "\"createdAt\":\"2021-04-08T15:57:08.81109\","
                + "\"updatedAt\":\"2021-04-08T15:57:08.81109\","
                + "\"correct\":true},"
                + "{\"id\":26,"
                + "\"optionText\":\"heyy\","
                + "\"scoreRate\":-1.0,"
                + "\"createdAt\":\"2021-04-08T15:57:08.813089\","
                + "\"updatedAt\":\"2021-04-08T15:57:08.813089\","
                + "\"correct\":false},"
                + "{\"id\":27,"
                + "\"optionText\":\"bye\","
                + "\"scoreRate\":-1.0,"
                + "\"createdAt\":\"2021-04-08T15:57:08.81509\","
                + "\"updatedAt\":\"2021-04-08T15:57:08.81509\","
                + "\"correct\":false},"
                + "{\"id\":28,"
                + "\"optionText\":\"oi\","
                + "\"scoreRate\":-1.0,"
                + "\"createdAt\":\"2021-04-08T15:57:08.81809\","
                + "\"updatedAt\":\"2021-04-08T15:57:08.81809\","
                + "\"correct\":false}],"
                + "\"question\":\"hello\","
                + "\"timeLimit\":null,"
                + "\"createdAt\":\"2021-04-08T15:56:41.245231\","
                + "\"updatedAt\":\"2021-04-08T15:56:41.245231\","
                + "\"open\":true,"
                + "\"finished\":false"
                + "}]";
    }


    /**
     * Set up String for polls answers.
     * @return String
     */
    public static String getPollsAnswers() {
        return "[{"
                + "\"id\": 5,"
                + "\"student\": {"
                + "\"id\": 20,"
                + "\"name\": \"gghjk\","
                + "\"isModerator\": false,"
                + "\"roomId\": {"
                + "\"id\": 1,"
                + "\"name\": \"svcveher\","
                + "\"linkIdStudent\": \"6094beba-10f7-43a0-a3bc-70c705ed8f3b\", "
                + "\"linkIdModerator\": \"0a14ab08-7a3e-471c-aa43-1feb0ca88cc2\", "
                + "\"isOpen\": true,"
                + "\"permission\": true,"
                + "\"numberQuestionsInterval\": 2147483647,"
                + "\"timeInterval\": 2147483647,"
                + "\"startDate\": \"2021-03-31T14:22:43.721675\","
                + "\"endDateForStudents\": null,"
                + "\"createdAt\": \"2021-03-31T14:22:43.813+0000\","
                + "\"updatedAt\": \"2021-03-31T14:22:43.813+0000\", "
                + "  },"
                + "\"createdAt\": \"2021-03-31T16:39:54.101+0000\","
                + "\"updatedAt\": \"2021-03-31T16:39:54.101+0000\"},"
                + "\"pollOption\": {"
                + "\"id\": 3,"
                + "\"optionText\": \"correipacbsocaoef\","
                + "\"scoreRate\": 0.0,"
                + "\"createdAt\": \"2021-03-31T18:41:43.709324\","
                + "\"updatedAt\": \"2021-03-31T18:41:43.709336\","
                + "\"correct\": true},"
                + "\"createdAt\": \"2021-03-31T18:42:12.515732\","
                + "\"updatedAt\": \"2021-03-31T18:42:12.515749\","
                + "\"marked\": true}]";

    }







}

