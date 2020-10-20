/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binarytreevisualizationdemonstration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author KevinHa
 */
public class FXMLDocumentController implements Initializable {
    
    Vector<Integer> NodeLabels;
    @FXML
    private AnchorPane pane_root;
    @FXML
    private AnchorPane pane_visual;
    @FXML
    private TextField txt_key;
    
    Node root;
    @FXML
    private TextField txt_lca1;
    @FXML
    private TextField txt_lca2;
    @FXML
    private VBox vbox_actionNotification;
    @FXML
    private ScrollPane scroll_view;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        NodeLabels = new Vector<Integer>();

        
        pane_visual.prefWidthProperty().bind(scroll_view.widthProperty());
        pane_visual.prefHeightProperty().bind(scroll_view.heightProperty());
        
        
    }  
    
    
    @FXML
    private void OnAddNodeClicked(MouseEvent event) {
        if(txt_key.getText().length() == 0){
            AddMessage("> Add Node - null", "red");
            return;
        }
        AddMessage("> Add Node - " + txt_key.getText(), "");
        addNode(Integer.parseInt(txt_key.getText()));
        NodeLabels.add(Integer.parseInt(txt_key.getText().toString()));
        txt_key.setText(null);
        
        System.out.println("Lis: " + NodeLabels);
        BuildVisualTree();
    }
    @FXML
    private void OnFindNodeClicked(MouseEvent event) {
        if(txt_key.getText().length() == 0){
            AddMessage("> Find Node - null", "red");
            return;
        }
        boolean found = findNode(Integer.parseInt(txt_key.getText()), true);
        if(found == true){
            AddMessage("> Node ["+txt_key.getText()+"] - Found", "");
        }else{
            AddMessage("> Node ["+txt_key.getText()+"] - Not Found", "");
        }
    }
    @FXML
    private void OnResetStyleNodeClicked(MouseEvent event) {
        resetPreviousNodeStyle();
        previousKey = null;
        //TravesalTree(root);
        
    }
    @FXML
    private void OnFindLCANodeClicked(MouseEvent event) {

        if(txt_lca1.getText().length() == 0 || txt_lca2.getText().length() == 0){
            AddMessage("> Error: Node 1 or/and Node 2 is/are null", "red");
            return;
        }else{
            findLowestCommonAncester(Integer.parseInt(txt_lca1.getText()), Integer.parseInt(txt_lca2.getText()));
        }
        txt_key.setText(null);
    }
    @FXML
    private void OnNewTreeClicked(MouseEvent event) {
        AddMessage("> New Tree - Deleted Old Binary Tree", "");
        NodeLabels.clear();
        newTree();
    }
    @FXML
    private void OnRandomTreeClicked(MouseEvent event) {
        randomTree();
        //BuildVisualTree();
    }
    @FXML
    private void OnDeleteNodeClicked(MouseEvent event) {
        deleteNode(Integer.parseInt(txt_key.getText()));
        txt_key.setText(null);
    }
    
    void newTree(){
        
        if(root == null){
            AddMessage("> New Tree - blank", "");
            return;
        }
        root = null;
        pane_visual.getChildren().clear();
        previousKey = null;
        
    }
    
    void randomTree(){
        NodeLabels.clear();
        newTree();
        AddMessage("> Random Tree - Start generating new random binary tree", "green");
        Random rand = new Random();
        for(int i = 0; i < 15; i++){
            int newNode = rand.nextInt(50);
            NodeLabels.add(newNode);
            addNode(newNode);
        }
        AddMessage("> " + NodeLabels, "green");
        resetPreviousNodeStyle();
        AddMessage("> Random Tree - Finished generating new random binary tree", "green");

    }
       
    void TravesalTree(Node focusNode){
        
        if(focusNode != null){
            TravesalTree(focusNode.leftChild);
            
            focusNode.getLabelNode().getStyleClass().add("Node");
            
            TravesalTree(focusNode.rightChild);
        }
    }
    
    public void BuildVisualTree(){
                
        newTree();
        for(int i = 0; i < NodeLabels.size(); i++){
            addNode(NodeLabels.get(i));
        }
        resetPreviousNodeStyle();
    }
    
    // Add Node
    public void addNode(int key){
        
        if(root != null && findNode(key, false) == true){
            return;
        }
        resetPreviousNodeStyle();

        previousKey = key+"";
        Node newNode = new Node(key);
        if(root == null){
            root = newNode;
            
        }else{
            Node parent;
            Node focusNode = root;
            boolean cont = true;
            while(cont == true){
                parent = focusNode;
                if(key < focusNode.key){
                    focusNode = focusNode.leftChild;
                    if(focusNode == null){
                        newNode.parent = parent;
                        parent.leftChild = newNode;
                        newNode.setIsParentLeft(false);
                        cont = false;
                    }
                }else{
                    focusNode = focusNode.rightChild;
                    if(focusNode == null){
                        newNode.parent = parent;
                        parent.rightChild = newNode;
                        newNode.setIsParentLeft(true);
                        cont = false;
                    }
                }
                
            }
            
        }
        newNode.AddNodeLabel();
        if(root.key != key){
            newNode.DrawLine();
        }
    }

    // Find Node
    String previousKey = null;
    public boolean findNode(int key, boolean highlight){
        String message = "";
        boolean result = true;
        
        resetPreviousNodeStyle();

        Node focusNode = root;
        
        while(key != focusNode.key){
            
            if(key < focusNode.key){
                if(focusNode.leftChild != null){
                    focusNode = focusNode.leftChild;
                }else{
                    result = false;
                    focusNode = null;
                    break;
                }
            }
            else{
                if(focusNode.rightChild != null){
                    focusNode = focusNode.rightChild;
                }else{
                    result = false;
                    focusNode = null;
                    break;
                }
            }
        }
        if(focusNode != null){
            
            if(highlight == true){
                focusNode.getLabelNode().getStyleClass().add("Node_Hightline");
            }
            previousKey = key+"";   
            result = true;
        }
        
        return result;
    }

    // Delete Node
    public void deleteNode(int key){
        Node delNode = root;
        Node parent = root;
        // Find delete node & parent of delete Node
        
        while(key != delNode.key){
            if(key < delNode.key){
                parent = delNode;
                delNode = delNode.leftChild;
                
            }else{
                parent = delNode;
                delNode = delNode.rightChild;
            }
        }
        
        //case 1: node without child
        if(delNode.leftChild == null && delNode.rightChild == null){
            
            if(delNode.getIsParentLeft() == false){
                parent.leftChild = null;
            }else{
                parent.rightChild = null;
            }
            NodeLabels.removeElement(key);
            System.out.println("Lis: " + NodeLabels);
            BuildVisualTree();
            AddMessage("> Node [" + key + "] has been deleted", "green");
        }
        
        //case 2: node with one child
        else if(delNode.leftChild == null || delNode.rightChild == null){
            
            if(delNode.leftChild == null){
                
                if(delNode.getIsParentLeft() == false){
                    parent.leftChild = delNode.rightChild;
                }else{
                    parent.rightChild = delNode.rightChild;
                }
                delNode.rightChild = null;
                AddMessage("> Node [" + key + "] has been deleted.", "green");
            }else{
                if(delNode.getIsParentLeft() == false){
                    parent.leftChild = delNode.leftChild;
                }else{
                    parent.rightChild = delNode.leftChild;
                }
                delNode.leftChild = null;
                AddMessage("> Node [" + key + "] has been deleted." , "green");
            }
            NodeLabels.removeElement(key);
            System.out.println("Lis: " + NodeLabels);
            BuildVisualTree();
            
            //delNode.RemoveNodeLabel();
            
            //AddMessage("Parent Node: " + parent.key + " | Left: " + parent.leftChild.key  + " | Right: " + parent.rightChild.key , "green");
        }
        
        //case 3: node with 2 childs
//        else{
//            Node MaxLeftNode = delNode;
//            MaxLeftNode = MaxLeftNode.leftChild;
//            while(MaxLeftNode.rightChild != null){
//                MaxLeftNode = MaxLeftNode.rightChild;
//            }
//            System.out.println("Max Left Node: " + MaxLeftNode.key);
//            MaxLeftNode.parent.rightChild = MaxLeftNode.leftChild;
//            MaxLeftNode.leftChild = delNode.leftChild;
//            MaxLeftNode.rightChild = delNode.rightChild;
//            if(delNode.getIsParentLeft() == true){
//                delNode.parent.rightChild = MaxLeftNode;
//            }else{
//                delNode.parent.leftChild = MaxLeftNode;
//            }
//            AddMessage("Delete Node: " + delNode.key + " is replaced by: " + MaxLeftNode.key  + " | Right: " + MaxLeftNode.rightChild.key   + " | Left: " + MaxLeftNode.leftChild.key + " | Parent: " + MaxLeftNode.parent.key, "green");
//        }
        else{
            int DelKey = delNode.key;
            Node MaxLeftNode = delNode;
            MaxLeftNode = MaxLeftNode.leftChild;
            while(MaxLeftNode.rightChild != null){
                MaxLeftNode = MaxLeftNode.rightChild;
                
            }

            //Collections.swap(NodeLabels, key, MaxLeftNode.key);
            NodeLabels.removeElement(key);
            System.out.println("Lis: " + NodeLabels);
            BuildVisualTree();
            
            AddMessage("Delete Node: " + DelKey + " is replaced by: " + delNode.key  + " | Right: " + delNode.rightChild.key   + " | Left: " + delNode.leftChild.key + " | Parent: " + delNode.parent.key, "green");
        }
        
        
    }
    
    // Reset Node Style
    public void resetPreviousNodeStyle(){
        if(previousKey != null){
            int key = Integer.parseInt(previousKey);
            Node focusNode = root;

            while(key != focusNode.key){

                if(key < focusNode.key){
                    focusNode = focusNode.leftChild;

                }
                else{
                    focusNode = focusNode.rightChild;
                }
                if(focusNode == null){
                    break;
                }
            }
            if(focusNode != null){
                focusNode.getLabelNode().getStyleClass().clear();
                focusNode.getLabelNode().getStyleClass().add("Node");
            }
        }
        
    }

    // Find Lowest Common Ancester
    public void findLowestCommonAncester(int key1, int key2){
        Node focusNode = root;
        
        if(findNode(key1, false) == false || findNode(key2, false) == false){
            AddMessage("> Error: [" + key1 + "] and/or [" + key2 + "] is/are not existed in the tree.", "red");
            return;
        }else{
            while(true){
                if((focusNode.key <= key1 && focusNode.key >= key2) || (focusNode.key >= key1 && focusNode.key <= key2)){
                    findNode(focusNode.key, true);
                    AddMessage("> Lowest common ancester of [" + key1 + "] and [" + key2 + "] is [" + focusNode.key+"]", "green");
                    return;
                }else {
                    if(focusNode.key > key1 && focusNode.key > key2){
                        focusNode = focusNode.leftChild;
                    }else{
                        focusNode = focusNode.rightChild;
                    }
                }
            }
        }
        
    }

    public void AddMessage(String message, String color){
        Label l = new Label();
        l.setText(message);
        switch(color){
            case "red":
                l.getStyleClass().add("Pane_Message_Error");
                break;
            case "green":
                l.getStyleClass().add("Pane_Message_Green");
                break;   
            default:
                l.getStyleClass().add("Pane_Message");
                break; 
        }

        vbox_actionNotification.getChildren().add(0,l);
        
        
    }

    Label addNodeLabel(int newKey, Node newNode){
        Label newNodeLabel = new Label(newKey+"");
        newNodeLabel.setAlignment(Pos.CENTER);
        newNodeLabel.setId(newKey+"");
        newNodeLabel.setPrefSize(50, 50);
        newNodeLabel.getStyleClass().add("Node");
        
        
        if(root.key != newKey){
            
            if(newKey < newNode.parent.key){ // left
                newNode.setX(newNode.parent.getX() - newNode.parent.getDistance());
            }else{ // right
                newNode.setX(newNode.parent.getX() + newNode.parent.getDistance()); 
            }
            newNode.setY(newNode.parent.getY() + 100);
            newNode.setDistance(newNode.parent.getDistance()/2);
            newNode.setLabelNode(newNodeLabel);
            newNodeLabel.setLayoutX(newNode.getX());
            newNodeLabel.setLayoutY(newNode.getY());
        }else{
            newNode.setX((int)pane_visual.getWidth()/2 - (int)newNodeLabel.getPrefWidth()/2);
            newNode.setY(50);
            newNode.setDistance(200);
            newNode.setLabelNode(newNodeLabel);
            newNodeLabel.setLayoutX(newNode.getX());
            newNodeLabel.setLayoutY(newNode.getY());

        }
        return newNodeLabel;
    }
    
    Line addLine(Node currentNode){
        
        Line newLine = new Line();
        newLine.setStroke(Color.AQUA);
        newLine.setStartX(currentNode.getX()+currentNode.getLabelNode().getPrefWidth()/2);
        
        newLine.setStartY(currentNode.getY());

        newLine.setEndX(currentNode.parent.getX()+currentNode.getLabelNode().getPrefWidth()/2);
        newLine.setEndY(currentNode.parent.getY()+currentNode.getLabelNode().getPrefHeight());
            
        return newLine;
    }
    
    class Node {
        
        int key;
        int x;
        int y;
        int distance;

        Label labelNode;
        Line line;
        Node parent;
        boolean isParentLeft;

        public boolean getIsParentLeft() {
            return isParentLeft;
        }

        public void setIsParentLeft(boolean isParentLeft) {
            this.isParentLeft = isParentLeft;
        }
        Node leftChild;
        Node rightChild;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        
        public Label getLabelNode() {
            return labelNode;
        }

        public void setLabelNode(Label labelNode) {
            this.labelNode = labelNode;
        }
        public Node(int key) {
            this.key = key;
        }
        
        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
        
        public void DrawLine(){
            this.line = addLine(this);
            pane_visual.getChildren().add(this.line);
            
        }
        public void AddNodeLabel(){
            this.labelNode = addNodeLabel(this.key, this);
            pane_visual.getChildren().add(labelNode);
        }
        

    }
}