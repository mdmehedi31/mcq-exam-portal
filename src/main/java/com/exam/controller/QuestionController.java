package com.exam.controller;


import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.definition.QuestionService;
import com.exam.service.definition.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService  quizService;

    @PostMapping("/add-question")
    public ResponseEntity<Question>  addQuestion(@RequestBody Question question){
        return ResponseEntity.ok(this.questionService.addQuestion(question));
    }
    @PutMapping("/update_question")
    private ResponseEntity<Question> update(@RequestBody Question question){

        return ResponseEntity.ok(this.questionService.updateQuestion(question));
    }

    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuizQuestion(@PathVariable("qid") Long qid){

         Quiz quiz = this.quizService.getQuiz(qid);
         Set<Question> questionSet = quiz.getQuestion();
         List list = new ArrayList(questionSet);

        if(list.size()>Integer.parseInt(quiz.getNumberOfQuestion())){
            list=list.subList(0,Integer.parseInt(quiz.getNumberOfQuestion())+1);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/quiz/all/{qid}")
    public ResponseEntity<?> getQuestionAdmin(@PathVariable("qid") Long qid){

        Quiz quiz= new Quiz();
        quiz.setQId(qid);
        Set<Question> questionSet= this.questionService.getQuestionOfQuiz(quiz);
        return ResponseEntity.ok(questionSet);
    }
    // single question
    @GetMapping("/single-quiz/{quesId}")
    public Question getSingleQuestion(@PathVariable("quesId") Long quesId){
        return this.questionService.getQuestion(quesId);
    }

    //delete question
    @DeleteMapping("/delete/{quesId}")
    public void delete(@PathVariable("quesId") Long quesId){

        this.questionService.deleteQuestion(quesId);
    }


    @PostMapping("/submit-quiz")
    public ResponseEntity<?> submitQuiz(@RequestBody List<Question> questions){

       double  markObtained=0;
        int correctAnswer=0;
        int attempted=0;

        System.out.println(questions);
        for(Question q: questions){
            //single question

          Question  question=this.questionService.get(q.getQuesId());

          if(question.getAnsware().equals(q.getGivenAnswer())){

                correctAnswer++;
              double markOne=Double.parseDouble(questions.get(0).getQuiz().getMaxMarks())/questions.size();

              markObtained+=markOne;
          }
          if(q.getGivenAnswer()!=null){

              attempted++;
          }

        }

        Map<Object,Object> data= Map.of("marksObtained",markObtained,"correctAnswer",
                correctAnswer,"attemped",attempted);
        return ResponseEntity.ok(data);
    }
}
