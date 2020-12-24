package main;

public class Label {
    private int id;
    private String text;
    private Dataset dataset;
  
    public Label(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;    
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Dataset getDataset() {
        return this.dataset;
    }
}
