package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.Question;

import java.util.List;

public abstract class CommuncationResponses {
    /**Method to return post room response body example for tests.
     * @param roomName Name of the room
     * @return Response body
     */
    public static String postRoomBodyResponse(String roomName) {
        return "{\n  \"id\": 2,\n  \"name\": \"" + roomName + "\",\n  "
                + "\"linkIdStudent\": \""
                + "f71b9850-a0b1-4ec6-b85e-ba4fffeaa7c9\",\n  "
                + "\"linkIdModerator\": \""
                + "bea8d9bc-a3fd-4070-9dae-d4ceadfad3a6\",\n  "
                + "\"isOpen\": true,\n  \"permission\": true,\n  \""
                + "createdAt\": \"2021-03-22T14:58:31.109+0000"
                + "\",\n  \"updatedAt\": \""
                + "2021-03-22T14:58:31.109+0000\"\n}";
    }

    /**Method to return check room response body example for tests.
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

    /**Method to return get questions response body example for tests.
     * @param questions List of questions
     * @param roomLink Link of the room
     * @param roomName Name of the room
     * @param userId Id of the user requesting
     * @return Response body
     */
    public static String getQuestionsBodyResponse(
            List<Question> questions, String roomLink, String roomName, String userId) {
        return "[{\n    \"id\": 1,\n    \"upVotes\": 0,"
                + "\n    \n\"room\": {\n      \"id\": 0,\n      \"name"
                + "\": \"\n" + roomName + "\",\n      \"linkIdStudent\": \""
                + roomLink + "\",\n      \n\"linkIdModerator\": \""
                + roomLink + "\",\n      \n\"isOpen\": true,\n      "
                + "\"permission\": true,\n\n      \"createdAt\": "
                + "\n\"2021-03-22T17:38:34.544+0000\",\n      "
                + "\"updatedAt\": \n\"2021-03-22T17:38:34.544+0000\""
                + "\n    },\n    \"user\": {\n      \n\"id\": "
                + userId + ",\n      \"name\": \"mod01\",\n      "
                + "\"isModerator\": true,\n      \n\"roomId\": {\n        "
                + "\"id\": 0,\n        \"name\": \"roomdo\","
                + "\n        \n\"linkIdStudent\": \""
                + roomLink + "\",\n        \n\"linkIdModerator\": \""
                + roomLink + "\",\n        \n\"isOpen\": true,\n        "
                + "\"permission\": true,\n        \"createdAt\": \n"
                + "\"2021-03-22T17:38:34.544+0000\",\n        "
                + "\"updatedAt\": \n\"2021-03-22T17:38:34.544+0000\""
                + "\n      },\n      \"createdAt\": \n\""
                + "2021-03-22T17:38:41.188+0000\",\n      \"updatedAt"
                + "\": \n\"2021-03-22T17:38:41.188+0000\"\n    },\n    "
                + "\"questionText\": \n\""
                + questions.get(0).getQuestionText()
                + "\",\n    \"createdAt\": \""
                + "2021-03-22T18:38:52.1034\",\n    \n\"updatedAt\": \""
                + "2021-03-22T18:38:52.10343\"\n  },\n{\n    \"id\": 1,"
                + "\n    \"upVotes\": 0,\n    \n\"room\": {\n      \"id\""
                + ": 0,\n      \"name\": \"" + roomName + "\",\n    "
                + "\"linkIdStudent\": \""
                + roomLink + "\",\n      \n\"linkIdModerator\": \""
                + roomLink + "\",\n      \n\"isOpen\": true,\n      "
                + "\"permission\": true,\n      \"createdAt\": \n\""
                + "2021-03-22T17:38:34.544+0000\",\n      \"updatedAt\": "
                + "\n\"2021-03-22T17:38:34.544+0000\"\n    },"
                + "\n    \"user\": {\n      \n\"id\": " + userId
                + ",\n      \"name\": \"mod01\",\n      \"isModerator\""
                + ": true,\n      \n\"roomId\": {\n        \"id"
                + "\": 0,\n        \"name\": \"roomdo\",\n        "
                + "\n\"linkIdStudent\": \""
                + roomLink + "\",\n        \n\"linkIdModerator\": \""
                + roomLink + "\",\n        \n\"isOpen\": true,\n        "
                + "\"permission\": true,\n        \"createdAt\": \n\""
                + "2021-03-22T17:38:34.544+0000\",\n        "
                + "\"updatedAt\": \n\"2021-03-22T17:38:34.544+0000\""
                + "\n      },\n      \"createdAt\": \n\""
                + "2021-03-22T17:38:41.188+0000\",\n      \"updatedAt\""
                + ": \n\"2021-03-22T17:38:41.188+0000\"\n    },\n    \""
                + "questionText\": \n\""
                + questions.get(1).getQuestionText()
                + "\",\n    \"createdAt\": \""
                + "2021-03-22T18:38:52.1034\",\n    \n\"updatedAt\": \""
                + "2021-03-22T18:38:52.10343\"\n  }]";
    }
}
