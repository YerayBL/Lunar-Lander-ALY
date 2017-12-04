/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.util.List;
import model.Score;


/**
 *
 * @author tuno
 */
public class Scores {

   public List<Score> score = null;

    
    public Scores() {
    }

   
    public Scores(List<Score> score) {
        super();
        this.score = score;
    }

    public List<Score> getScore() {
        return score;
    }

    public void setScore(List<Score> Score) {
        this.score = score;
    }

    public Scores withScore(List<Score> configuracion) {
        this.score = score;
        return this;
    }

}
