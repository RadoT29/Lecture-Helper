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

    public QuestionsUpdate findUpdate(Long idUser, Long idRoom) {
        List<QuestionsUpdate> list = questionsUpdateRepository.findUpdate(idUser, idRoom);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void deleteUpdate(Long updateId, Long idUser, Long idRoom) {
        questionsUpdateRepository.deleteUpdate(updateId, idUser, idRoom);
    }
}
