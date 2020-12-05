package main;

public class Instance {
    private int id;
    private String text ;
    
    public Instance(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId() {
    	return this.id;
    }
    
    public String getText() {
    	return this.text;
    }    
}
