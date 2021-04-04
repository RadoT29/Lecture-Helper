package nl.tudelft.oopp.app.models;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor

public final class Session {

    private static Session instance;

    private String roomLink;
    private String roomName;
    private boolean isModerator;
    private String userId;
    private List<String> upVotedQuestions = new ArrayList<>();
    private List<String> questionsMade = new ArrayList<>();
    private String timeZone;
    private Stage stage;
    //status of the student
    //if the student is warned,
    //that it will be banned
    private boolean isWarned;

    /**
     * Constructor for the session.
     * @param stage - Stage to be used
     */
    public Session(Stage stage) {
        this.stage = stage;
    }

    public static Session getInstance(Stage stage) {
        instance = new Session(stage);
        return instance;
    }

    /**
     * Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     *
     * @param roomLink    Link for the room that is going to be used by this client for requests
     * @param roomName    Name of the room
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
            instance = new Session();
        }
        instance.setRoomLink(roomLink);
        instance.setRoomName(roomName);
        instance.setUserId(userId);
        instance.setModerator(isModerator);
        return instance;
    }

    /**
     * Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     *
     * @param roomLink    Link for the room that is going to be used by this client for requests
     * @param userId      Id of the user on the local client
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     * @return singleton class instance
     */
    public static Session getInstance(String roomLink,
                                      String userId,
                                      boolean isModerator) {
        instance.setRoomLink(roomLink);
        instance.setUserId(userId);
        instance.setModerator(isModerator);
        return instance;
    }

    /**
     * Get session instance.
     *
     * @return singleton class instance
     */
    public static Session getInstance() {
        return instance;
    }

    /**
     * This method resets the session by clearing all the session Data.
     */
    public static void clearSession() {
        
        if (instance == null) {
            instance = new Session();
            return;
        }
        
        Stage stage = instance.getStage();
        instance = null;
        instance = new Session(stage);
    }

    /**
     * This method resets the session by making it null for tests.
     */
    public static void clearSessionTest() {
        instance = null;
    }


    /**
     * Method to add a question to the list of questions that the user in this session has made
     * (given that each user will have their own list of questions made in their session).
     * @param questionId - Question id of the question just made
     */
    public void questionAdded(String questionId) {
        this.questionsMade.add(questionId);
    }

    /**
     * Method to add a question to the list of the upvoted questions by a specific user
     * (given that each user will have a different list initiated in their session).
     *
     * @param questionId - Question Id from question that has been upvoted by user
     */
    public void incrementUpvotes(String questionId) {
        this.upVotedQuestions.add(questionId);
    }

    /**
     * Method to remove a question from the list of the upvoted questions by a specific user
     * (given that each user will have a different list initiated in their session).
     *
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

}

