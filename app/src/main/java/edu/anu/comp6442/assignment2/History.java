package edu.anu.comp6442.assignment2;

/**
 * Created by Snigdha on 5/8/2016.
 */
public class History {

    int id;
    String expression;
    String values;

    //empty constructor
    public History(){}

    //constructor
    public History(int id, String expression, String values){
        this.id=id;
        this.expression=expression;
        this.values=values;
    }

    //constructor
    public History(String expression, String values){
        this.expression=expression;
        this.values=values;
    }

    //getting ID
    public int getId() {
        return this.id;
    }

    //setting ID
    public void setId(int id){
        this.id=id;
    }

    //getting expression
    public String getExp(){
        return this.expression;
    }

    //setting expression
    public void setExp(String exp){
        this.expression=exp;
    }
    //getting values
    public String getValues(){
        return this.values;
    }

    //setting values
    public void setValues(String values){
        this.values=values;
    }

}
