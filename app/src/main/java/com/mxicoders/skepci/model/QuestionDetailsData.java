package com.mxicoders.skepci.model;

/**
 * Created by mxicoders on 26/8/17.
 */

public class QuestionDetailsData {

    String option4;
    String timing;
    String task_id;
    String task_type;
    String question;
    String answer;
    String option1;
    String option2;
    String option3;
    String question_title_id;
    String question_id;
    String number_count;
    String is_answered;

    public String getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(String current_count) {
        this.current_count = current_count;
    }

    String current_count;

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    String remaining;

    public String getIs_answered() {
        return is_answered;
    }

    public void setIs_answered(String is_answered) {
        this.is_answered = is_answered;
    }



    public String getNumber_count() {
        return number_count;
    }

    public void setNumber_count(String number_count) {
        this.number_count = number_count;
    }



    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_title_id() {
        return question_title_id;
    }

    public void setQuestion_title_id(String question_title_id) {
        this.question_title_id = question_title_id;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }


}
