package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.communication.BanCommunication;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

public class StudentQuestionSceneController {


    /**
     * This method is constantly called by a thread and refreshes the page.
     *
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     * @throws NoStudentPermissionException - may be thrown.
     * @throws AccessDeniedException        - may be thrown.
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();


        //ServerCommunication.isTheRoomClosed(session.getRoomLink());

        //ServerCommunication.hasStudentPermission(session.getRoomLink());
        ServerCommunication.isRoomOpenStudents(session.getRoomLink());
        QuestionCommunication.updatesOnQuestions(session.getUserId(), session.getRoomLink());
        if (!session.isWarned()) {
            BanCommunication.isIpWarned(session.getRoomLink());
        } else {
            BanCommunication.isIpBanned(session.getRoomLink());
        }

    }
}
