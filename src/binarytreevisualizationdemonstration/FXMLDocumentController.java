/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binarytreevisualizationdemonstration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 *
 * @author KevinHa
 */
public class FXMLDocumentController implements Initializable {
    
    
    @FXML
    private AnchorPane pane_root;
    @FXML
    private AnchorPane pane_visual;
    @FXML
    private TextField txt_key;
    
    Node root;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }  
    
    public void AddNode(String key){
        Label node = new Label(key);
        node.getStyleClass().add("Node");
        pane_visual.getChildren().add(node);
        node.setLayoutX(500);
        node.setLayoutY(100);
    }
    
    @FXML
    private void OnAddNodeClicked(MouseEvent event) {
        Node node = new Node();
        addNode(Integer.parseInt(txt_key.getText()));
        //System.out.println("-------------");
        //TravesalTree(root);
    }
    
    void TravesalTree(Node focusNode){
        
        if(focusNode != null){
            TravesalTree(focusNode.leftChild);
            
            System.out.println(focusNode);
            
            TravesalTree(focusNode.rightChild);
        }
    }
    
    public void addNode(int key){
        
        Label newLabel = new Label(key+"");
        newLabel.toFront();
        newLabel.setPrefSize(50, 50);
        newLabel.setAlignment(Pos.CENTER);
        newLabel.setId(key+"");
        newLabel.getStyleClass().add("Node");
        
        Node newNode = new Node(key);
        
        if(root == null){
            newNode.setX(500);
            newNode.setY(50);
            newNode.setDistance(200);
            root = newNode;
            
        }else{
            Node parent;
            Node focusNode = root;
            boolean cont = true;
            while(cont == true){
                parent = focusNode;
                System.out.println(key + ": " + parent.getX() + " | " + parent.getY()  + " | " + parent.getDistance() );
                if(key < focusNode.key){
                    focusNode = focusNode.leftChild;
                    if(focusNode == null){
                        newNode.setX(parent.getX() - parent.getDistance());
                        newNode.setY(parent.getY() + 100);
                        newNode.setDistance(parent.getDistance()/2);
                        parent.setLabelNode(newLabel);
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
                        parent.setLabelNode(newLabel);
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
                leftLine.toBack();
                leftLine.setStartX(this.labelNode.getPrefWidth()/2 + x);
                leftLine.setStartY(this.labelNode.getPrefHeight() + y);
                leftLine.setEndX(leftChild.x + this.labelNode.getPrefWidth()/2);
                leftLine.setEndY(leftChild.y);

                pane_visual.getChildren().add(leftLine);
            }
            if(rightChild != null && rightLine == null){
                rightLine = new Line();
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