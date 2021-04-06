package nl.tudelft.oopp.app;


import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.QuestionsUpdateRepository;
import nl.tudelft.oopp.app.services.QuestionsUpdateService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionUpdateServiceTests {
    @InjectMocks
    private QuestionsUpdateService questionsUpdateService;

    @Mock
    private QuestionsUpdateRepository questionsUpdateRepository;

    private User user;

    private Room room;

    private List<QuestionsUpdate> list;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        room = new Room("My room");
        list = new ArrayList<>();
    }

    @Test
    public void testQuestionUpdateNull() {


        when(questionsUpdateRepository.findUpdate(user.getId(), room.getId())).thenReturn(list);
        Assertions.assertNull(questionsUpdateService.findUpdate(user.getId(), room.getId()));
    }

    @Test
    public void testQuestionUpdate() {
        QuestionsUpdate questionsUpdate = new QuestionsUpdate();

        list.add(questionsUpdate);
        when(questionsUpdateRepository.findUpdate(user.getId(), room.getId())).thenReturn(list);
        Assertions.assertEquals(questionsUpdate, questionsUpdateService.findUpdate(user.getId(), room.getId()));
    }

    @Test
    public void testDeleteUpdate() {
        questionsUpdateService.deleteUpdate(1L, user.getId(), room.getId());
        verify(questionsUpdateRepository).deleteUpdate(1L, user.getId(), room.getId());
    }
}
