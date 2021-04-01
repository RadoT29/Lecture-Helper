package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.repositories.QuestionsUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsUpdateService {

    @Autowired
    private QuestionsUpdateRepository questionsUpdateRepository;

    /**
     * This method retrieves the questions updates of every user.
     * @param idUser - the user id
     * @param idRoom - the room id
     * @return - the first update on the list
     */
    public QuestionsUpdate findUpdate(Long idUser, Long idRoom) {
        List<QuestionsUpdate> list = questionsUpdateRepository.findUpdate(idUser, idRoom);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Deleting an update record by provided:.
     * @param updateId - the update id
     * @param idUser - user id
     * @param idRoom - room id
     */
    public void deleteUpdate(Long updateId, Long idUser, Long idRoom) {
        questionsUpdateRepository.deleteUpdate(updateId, idUser, idRoom);
    }
}
