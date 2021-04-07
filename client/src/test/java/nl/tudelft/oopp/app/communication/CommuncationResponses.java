package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.Question;

import java.util.List;

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
}

