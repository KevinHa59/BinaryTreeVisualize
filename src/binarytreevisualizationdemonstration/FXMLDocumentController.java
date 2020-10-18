/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binarytreevisualizationdemonstration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author KevinHa
 */
public class FXMLDocumentController implements Initializable {
    
    Vector<Label> NodeLabels;
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        NodeLabels = new Vector<Label>();
        
    }  
    
    
    @FXML
    private void OnAddNodeClicked(MouseEvent event) {
        addNode(Integer.parseInt(txt_key.getText()));
        txt_key.setText(null);

    }
    @FXML
    private void OnFindNodeClicked(MouseEvent event) {
        findNode(Integer.parseInt(txt_key.getText()), true);
    }
    @FXML
    private void OnResetStyleNodeClicked(MouseEvent event) {
        resetPreviousNodeStyle();
        previousKey = null;
        //TravesalTree(root);
    }
    
    
    @FXML
    private void OnFindLCANodeClicked(MouseEvent event) {
        findLowestCommonAncester(Integer.parseInt(txt_lca1.getText()), Integer.parseInt(txt_lca2.getText()));
    }

    
    void TravesalTree(Node focusNode){
        
        if(focusNode != null){
            TravesalTree(focusNode.leftChild);
            
            System.out.println(focusNode);
            focusNode.getLabelNode().getStyleClass().add("Node");
            
            TravesalTree(focusNode.rightChild);
        }
    }
    
    
    // Add Node
    public void addNode(int key){
        resetPreviousNodeStyle();
        
        Label newLabel = new Label(key+"");
        NodeLabels.add(newLabel);
        newLabel.setPrefSize(50, 50);
        newLabel.setAlignment(Pos.CENTER);
        newLabel.setId(key+"");
        newLabel.getStyleClass().add("Node_Hightline");
        previousKey = key+"";
        Node newNode = new Node(key);
        
        if(root == null){
            newNode.setX(500);
            newNode.setY(50);
            newNode.setDistance(200);
            newNode.setLabelNode(newLabel);
            root = newNode;
            
        }else{
            Node parent;
            Node focusNode = root;
            boolean cont = true;
            while(cont == true){
                parent = focusNode;
                //System.out.println(key + ": " + parent.getX() + " | " + parent.getY()  + " | " + parent.getDistance() );
                if(key < focusNode.key){
                    focusNode = focusNode.leftChild;
                    if(focusNode == null){
                        newNode.setX(parent.getX() - parent.getDistance());
                        newNode.setY(parent.getY() + 100);
                        newNode.setDistance(parent.getDistance()/2);
                        newNode.setLabelNode(newLabel);
                        parent.leftChild = newNode;
                        parent.DrawLine();
                        cont = false;
                    }
                }else{
                    focusNode = focusNode.rightChild;
                    if(focusNode == null){
                        newNode.setX(parent.getX() + parent.getDistance());
                        newNode.setY(parent.getY() + 100);
                        newNode.setDistance(parent.getDistance()/2);
                        newNode.setLabelNode(newLabel);
                        parent.rightChild = newNode;
                        parent.DrawLine();
                        cont = false;
                    }
                }
                
            }
            
        }
        newLabel.setLayoutX(newNode.getX());
        newLabel.setLayoutY(newNode.getY());
        
        pane_visual.getChildren().add(newLabel);
    }

    // Find Node
    String previousKey = null;
    public boolean findNode(int key, boolean highlight){
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
            return;
        }else{
        // case 1  key1 < focusNode < key2 or key1 > focusNode > key2
            while(true){
                System.out.println(key1 + " " + focusNode.key + " " + key2);
                System.out.println((focusNode.key < key1 && focusNode.key > key2) || (focusNode.key > key1 && focusNode.key < key2));
                if((focusNode.key <= key1 && focusNode.key >= key2) || (focusNode.key >= key1 && focusNode.key <= key2)){
                    findNode(focusNode.key, true);
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
    
    class Node {
        
        int key;
        int x;
        int y;
        int distance;
        
        Label labelNode;
        Line leftLine;
        Line rightLine;
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
        public Node() {

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
            if(leftChild != null && leftLine == null){
                leftLine = new Line();
                leftLine.setStroke(Color.AQUA);
                leftLine.toBack();
                leftLine.setStartX(this.labelNode.getPrefWidth()/2 + x);
                leftLine.setStartY(this.labelNode.getPrefHeight() + y);
                leftLine.setEndX(leftChild.x + this.labelNode.getPrefWidth()/2);
                leftLine.setEndY(leftChild.y);

                pane_visual.getChildren().add(leftLine);
            }
            if(rightChild != null && rightLine == null){
                rightLine = new Line();
                rightLine.setStroke(Color.AQUA);
                rightLine.toBack();
                rightLine.setStartX(this.labelNode.getPrefWidth()/2 + x);
                rightLine.setStartY(this.labelNode.getPrefHeight() + y);
                rightLine.setEndX(rightChild.x + this.labelNode.getPrefWidth()/2);
                rightLine.setEndY(rightChild.y);

                pane_visual.getChildren().add(rightLine);
            }
            
        }
        
        public String toString(){
            return key + " Loc X: " + x + " | Loc Y: " + y;
        }

        

    }
}