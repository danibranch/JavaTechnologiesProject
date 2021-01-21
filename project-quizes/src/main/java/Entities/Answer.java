
package Entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name="answers")
@Table(name="answers")
@NamedQuery(name="answer_getByQuestionId", query="FROM answers where question_id = :id")
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="id")
    private int id;
    
    @Column(name="text")
    private String text;
    
    @Column(name="is_good_answer")
    private boolean isGoodAnswer;
    
//    @Column(name="questionId")
//    private int questionId;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private Question question;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public boolean getIsGoodAnswer() {
        return isGoodAnswer;
    }
    
    public void setIsGoodAnswer(boolean isGoodAnswer) {
        this.isGoodAnswer = isGoodAnswer;
    }
    
//    public int getQuestionId() {
//        return questionId;
//    }
//    
//    public void setQuestionId(int questionId) {
//        this.questionId = questionId;
//    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setQuestion(Question question) {
        this.question = question;
    }
}
