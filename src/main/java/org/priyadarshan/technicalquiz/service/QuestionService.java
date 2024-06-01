package org.priyadarshan.technicalquiz.service;

import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dao.QuestionDao;
import org.priyadarshan.technicalquiz.pojo.Question;
import org.priyadarshan.technicalquiz.utility.KeyAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {


    private final QuestionDao questionDao;
    private final KeyAuthenticator keyAuthenticator;

    public ResponseEntity<String> addQuestion(String key, Question question) {
        keyAuthenticator.setKey(key);
        if (keyAuthenticator.isKeyValidAndAdmin()) {
            questionDao.save(question);
            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Invalid key", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> addQuestions(String key,List<Question> questions) {
        keyAuthenticator.setKey(key);
        if (keyAuthenticator.isKeyValidAndAdmin()) {
            int successfulAdds = 0;
            StringBuilder errorMessageBuilder = new StringBuilder();

            for (Question question : questions) {
                try {
                    questionDao.save(question);
                    successfulAdds++;
                } catch (Exception e) {
                    errorMessageBuilder.append("Error adding question: ").append(question.getQuestionTitle()).append(" - ").append(e.getMessage()).append("\n");
                }
            }

            String message;
            if (successfulAdds == questions.size()) {
                message = "All questions added successfully!";
            } else {
                message = String.format("%d questions added successfully. Errors encountered while adding %d questions:\n%s",
                        successfulAdds, questions.size() - successfulAdds, errorMessageBuilder);
            }

            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Invalid key", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<List<Question>> getRandomQuestionsByCategory(String category, int limit) {
        try {
            List<Question> questions = questionDao.findRandomByCategory(category, limit);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_GATEWAY);
        }
    }


    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_GATEWAY);
        }

    }
}
