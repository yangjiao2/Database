package edu.uci.ics.cs122b.project5;

/**
 * Created by Kunal on 3/13/2015.
 */
public class Stats {


    private static Stats thisInstance = new Stats();
    private int totalNumberOfQuestions;
    private int numberCorrect;
    private int numberIncorrect;
    private int avgTimePerQuestion;
    public Stats(){

    }

    public void setTotalNumberOfQuestions(int a){
        this.totalNumberOfQuestions = a;
    }

    public int getTotalNumberOfQuestions(){
        return totalNumberOfQuestions;
    }

    public void setNumberCorrect(){
        numberCorrect++;
    }

    public int getNumberCorrect(){
        return numberCorrect;
    }

    public void setNumberIncorrect(){
        numberIncorrect++;
    }

    public int getNumberIncorrect(){
        return numberIncorrect;
    }

    public void setAvgTimePerQuestion(int a){
        avgTimePerQuestion = a;
    }

    public int getAvgTimePerQuestion(){
        return avgTimePerQuestion;
    }

    public Stats getThisInstance(){
        return thisInstance;
    }


}
