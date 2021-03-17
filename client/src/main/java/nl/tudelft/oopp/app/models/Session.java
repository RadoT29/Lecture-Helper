package nl.tudelft.oopp.app.models;

import java.util.ArrayList;
import java.util.List;

public final class Session {

    private static Session instance;

    private String roomLink;
    private String roomName;
    private boolean isModerator;
    private String userId;
    private List<String> upVotedQuestions;
    private List<String> questionsMade;

    /**Session constructor.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param roomName Name of the room
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     */
    public Session(String roomLink,
                   String roomName,
                   String userId,
                   boolean isModerator) {
        this.roomLink = roomLink;
        this.roomName = roomName;
        this.isModerator = isModerator;
        this.userId = userId;
        this.upVotedQuestions = new ArrayList<>();
        this.questionsMade = new ArrayList<>();

    }

    /**Session constructor.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param userId Id of the user on the local client
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     */
    public Session(String roomLink, String userId, boolean isModerator) {
        this.roomLink = roomLink;
        this.userId = userId;
        this.isModerator = isModerator;
        this.upVotedQuestions = new ArrayList<>();
        this.questionsMade = new ArrayList<>();

    }

    /**Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param roomName Name of the room
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     * @return singleton class instance
     */
    public static Session getInstance(String roomLink,
                                      String roomName,
                                      String userId,
                                      boolean isModerator
                                      ) {
        if (instance == null) {
            instance = new Session(roomLink, roomName, userId, isModerator);
        }
        return instance;
    }

    /**Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param userId Id of the user on the local client
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     * @return singleton class instance
     */
    public static Session getInstance(String roomLink,
                                      String userId,
                                      boolean isModerator) {
        if (instance == null) {
            instance = new Session(roomLink, userId, isModerator);
        }
        return instance;
    }

    /** Get session instance.
     * @return singleton class instance
     */
    public static Session getInstance() {
        return instance;
    }

    public String getRoomLink() {
        return roomLink;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean getIsModerator() {
        return isModerator;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public List<String> getQuestionsMade() {
        return this.questionsMade;
    }


    public List<String> getUpvotesList() {
        return this.upVotedQuestions;
    }

    /**
     * Method to add a question to the list of the upvoted questions by a specific user
     * (given that each user will have a different list initiated in their session).
     * @param questionId - Question Id from question that has been upvoted by user
     */
    public void incrementUpvotes(String questionId) {
        this.upVotedQuestions.add(questionId);
    }

    /**
     * Method to remove a question from the list of the upvoted questions by a specific user
     * (given that each user will have a different list initiated in their session).
     * @param questionId - Question Id of question to remove the upvote from
     */
    public void decrementUpvotes(String questionId) {
        this.upVotedQuestions.remove(questionId);
    }



    /**
     * Method to remove a user's question from the list of questions that they have made
     * (each user has their own list of questions made, in their correspondent session,
     * if they want to delete one question they have made this method will be called).
     * @param questionId - Id of the question to remove
     */
    public void questionDeleted(String questionId) {
        this.questionsMade.remove(questionId);
    }

    /**
     * This method resets the session by clearing all the session Data.
     */
    public void cleanUserSession() {
        isModerator = false;
        roomLink = null;
        roomName = null;
        userId = null;
    }
}

