package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RuleBasedLabelingMechanism extends LabelingMechanism {
    
    public RuleBasedLabelingMechanism() {

        this.assignedLabels = new ArrayList<Label>();
    }

    public List<Label> labelInstance(Instance instance, List<Label> labels, int maxLabel) {
        /*
            Assigns label/s to an instance based on on the similarity between the labels and the instance's 
            text. 
        */
        List<Label> sortedLabelsBySimilarities = sortLabelsBySimilarities(instance, labels);

        for (int i = 0; i < maxLabel; i++){
            this.assignedLabels.add(sortedLabelsBySimilarities.get(i)); // get the first N labels(N being maxLabel)
        }

        return this.assignedLabels;
    }

    public List<Label> sortLabelsBySimilarities(Instance instance, List<Label> labels){
        /*
            Given an instance, and list of labels, the method returns the list of labels
            which are sorted by their similarities.
        */
        Map<Label, Double> labelSimilarities = new HashMap<Label, Double>();
        List<Label> sortedLabels = new ArrayList<Label>();

        for (Label label: labels){
            double sim = calculateSimilarty(instance, label);
            labelSimilarities.put(label, sim);
        }

        labelSimilarities.entrySet().stream()
            .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
            .forEach(k -> sortedLabels.add(k.getKey())); // adding sortedLabels to the arrayList

        return sortedLabels;
    }

    public double calculateSimilarty(Instance instance, Label label){
        /*
            Given an instance and a label, calculte the similarity as the average of the occurrences
            of each letter of the label in the instance.
            E.g. Given an instance with the text: "I am an instance", and a label with the text: "label",
            The similarty should be calulated in the following steps: 
                - First get {a: 3, e: 1}
                - Sim = (3 + 1)/5
        */
        double similarity = 0.0;
        String instanceText = instance.getText().toLowerCase();
        String labelText = label.getText().toLowerCase();
        Map<Character, Integer> lettersOccurrences = new HashMap<Character, Integer>();

        for (int i = 0; i < instanceText.length(); i++){
            char c = instanceText.charAt(i);        
            if (labelText.contains(""+c)){ // converting the Char to a CharSequence
                if (i == 0 || !lettersOccurrences.containsKey(c)) lettersOccurrences.put(c,1);
                else lettersOccurrences.put(c,lettersOccurrences.get(c) + 1);
            }
        }

        for (Map.Entry<Character, Integer> entry : lettersOccurrences.entrySet()) {
            similarity = similarity + entry.getValue();
        }

        similarity = similarity/labelText.length();

        System.out.println(label.getText() + ":   " +   similarity);
        return similarity;
    }
}
