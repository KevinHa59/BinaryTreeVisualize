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
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private CheckBox cbox_delLeft;
    @FXML
    private TextField txt_findKey;
    @FXML
    private TextField txt_deleteKey;
    @FXML
    private ToggleGroup deleteNode;
    @FXML
    private RadioButton chbx_deleteLeft;
    @FXML
    private RadioButton chbx_deleteRight;
    @FXML
    private ScrollPane scroll_output;
    
    
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
        BuildVisualTree();
    }
    @FXML
    private void OnFindNodeClicked(MouseEvent event) {
        if(txt_findKey.getText().length() == 0){
            AddMessage("> Find Node - null", "red");
            return;
        }
        boolean found = findNode(Integer.parseInt(txt_findKey.getText()), root, true, false);
        if(found == true){
            AddMessage("> Node ["+txt_findKey.getText()+"] - Found", "");
        }else{
            AddMessage("> Node ["+txt_findKey.getText()+"] - Not Found", "");
            resetPreviousNodeStyle(root);
        }
        
    }
    @FXML
    private void OnResetStyleNodeClicked(MouseEvent event) {
        resetPreviousNodeStyle(root);
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
        txt_lca1.setText(null);
        txt_lca2.setText(null);
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
        if(txt_deleteKey.getText().length() == 0){
            AddMessage("> Error - Cannot add empty node", "red");
            return;
        }
        deleteNode(Integer.parseInt(txt_deleteKey.getText()));
        txt_deleteKey.setText(null);
        
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
        

        for(int i = 0; i < 15; i++){

            Random rand = new Random();
            int newNode = rand.nextInt(50);
            NodeLabels.add(newNode);
            addNode(newNode);

        }
            
        AddMessage("> " + NodeLabels, "green");
        resetPreviousNodeStyle(root);
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
        resetPreviousNodeStyle(root);
    }
    
    // Add Node
    public void addNode(int key){
        
        if(root != null && findNode(key, root, false, false) == true){
            return;
        }
        resetPreviousNodeStyle(root);

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
    public boolean findNode(int key, Node startNode, boolean highlight, boolean isLMA){
        String message = "";
        boolean result = true;
        if(isLMA == false){
            resetPreviousNodeStyle(root);
        }
        Node focusNode = startNode;
        
        while(key != focusNode.key){
            if(highlight == true){
                focusNode.setHightLightPath();
                
            }
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

        }
        // case 2: Node with 2 child.
        else{

            Node replacedNode = delNode;

            if(chbx_deleteLeft.isSelected() == true){
                replacedNode = replacedNode.leftChild;
                while(replacedNode.rightChild != null){
                    replacedNode = replacedNode.rightChild;
                }
            }
            if(chbx_deleteRight.isSelected() == true){
                replacedNode = replacedNode.rightChild;
                while(replacedNode.leftChild != null){
                    replacedNode = replacedNode.leftChild;
                }
            }
            int tempNodeKey = NodeLabels.indexOf(key);
            int tempNodeMaxKey = NodeLabels.indexOf(replacedNode.key);
            System.out.println(tempNodeKey + " " + tempNodeMaxKey);
            Collections.swap(NodeLabels, tempNodeKey, tempNodeMaxKey);
            NodeLabels.removeElement(delNode.key);
            BuildVisualTree();
            
            AddMessage("> Node [" + key + "] has been deleted", "green");
        }
        
        
    }
    
    // Reset Node Style
    public void resetPreviousNodeStyle(Node focusNode){
        if(focusNode != null){
            resetPreviousNodeStyle(focusNode.leftChild);
            resetPreviousNodeStyle(focusNode.rightChild);
            focusNode.getLabelNode().getStyleClass().clear();
            focusNode.getLabelNode().getStyleClass().add("Node");
        }
        
    }

    // Find Lowest Common Ancester
    public void findLowestCommonAncester(int key1, int key2){
        Node focusNode = root;
        
        if(findNode(key1, root, false,false) == false || findNode(key2, root, false,false) == false){
            AddMessage("> Error: [" + key1 + "] and/or [" + key2 + "] is/are not existed in the tree.", "red");
            return;
        }else{
            while(true){
                if((focusNode.key <= key1 && focusNode.key >= key2) || (focusNode.key >= key1 && focusNode.key <= key2)){
                    findNode(key1, focusNode, true,true);
                    findNode(key2, focusNode, true,true);
                    focusNode.setHightLight();
                    
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

    // Add Message to message box
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

    Pane submenu;
    public void SubMenu(Node focusNode){

        if(submenu != null){
            pane_visual.getChildren().remove(submenu);
        }

        Pane menu = new Pane();
        menu.setPrefWidth(200);
        submenu = menu;
        menu.setId("SubMenu");
        menu.getStyleClass().add("subMenu");

        VBox list = new VBox();
        
        Label selection1 = new Label("Find path from root");
        selection1.getStyleClass().add("subMenuButton");
        Label selection2 = new Label("Delete node");
        selection2.getStyleClass().add("subMenuButton");
        Label selection3 = new Label("Close");
        selection3.getStyleClass().add("subMenuButton");
        
        selection1.prefWidthProperty().bind(menu.widthProperty());
        selection2.prefWidthProperty().bind(menu.widthProperty());
        selection3.prefWidthProperty().bind(menu.widthProperty());
        
        // Action Find Path
        selection1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetPreviousNodeStyle(root);
                findNode(focusNode.key, root, true, false);
                pane_visual.getChildren().remove(submenu);
            }
        });
        
        // Action Delete Node
        selection2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteNode(focusNode.key);
                txt_key.setText(null);
            }
        });
        // Close Sub menu
        selection3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pane_visual.getChildren().remove(submenu);
            }
        });
        
        
        list.getChildren().add(selection1);
        list.getChildren().add(selection2);
        list.getChildren().add(selection3);
        
        menu.getChildren().add(list);
     
        menu.setLayoutX(focusNode.getLabelNode().getLayoutX() + 50);
        menu.setLayoutY(focusNode.getLabelNode().getLayoutY());
        pane_visual.getChildren().add(menu);
    }
    // Add Label Node
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
            newNode.setDistance((int)newNode.parent.getDistance()/2);
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
        newNodeLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                SubMenu(newNode);
            }
        });
        
        return newNodeLabel;
    }
    
    // Add connect Line
    Line addLine(Node currentNode){
        
        Line newLine = new Line();
        newLine.getStyleClass().add("Line");
        newLine.setStartX(currentNode.getX()+currentNode.getLabelNode().getPrefWidth()/2);
        
        newLine.setStartY(currentNode.getY());

        newLine.setEndX(currentNode.parent.getX()+currentNode.getLabelNode().getPrefWidth()/2);
        newLine.setEndY(currentNode.parent.getY()+currentNode.getLabelNode().getPrefHeight());
            
        return newLine;
    }
    
    // Class node
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
        
        public void setHightLight(){
                labelNode.getStyleClass().add("Node_Hightline");
        }
        public void setHightLightPath(){
                labelNode.getStyleClass().add("Node_Path");
        }
    }
}