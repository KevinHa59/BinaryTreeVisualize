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
import java.util.TimerTask;

import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 *
 * @author KevinHa
 */
public class FXMLDocumentController implements Initializable {
    
    Vector<Node> NodeLabels;
    Vector<Integer> travesalTreeList;
    
    @FXML
    private AnchorPane pane_root;
    @FXML
    private Pane pane_visual;
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
    
    Vector<String> NodeDetailTitle;
    
    Pane submenu;
    @FXML
    private ImageView img_customNode;
    @FXML
    private ToggleGroup Travesal;
    @FXML
    private RadioButton cbox_inorder;
    @FXML
    private RadioButton cbox_preorder;
    @FXML
    private RadioButton cbox_postorder;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        NodeLabels = new Vector<Node>();
        
        img_customNode.getStyleClass().add("imageButton");
        pane_visual.prefWidthProperty().bind(scroll_view.widthProperty());
        
        
    }  

    @FXML
    private void OnAddNodeClicked(MouseEvent event) {
        if(txt_key.getText().length() == 0){
            AddMessage("> Add Node - null", "red");
            return;
        }
        if(NodeDetailTitle == null){
            AddMessage("> Add Node - " + txt_key.getText(), "");
            Node newNode = addNode(Integer.parseInt(txt_key.getText()));
            NodeLabels.add(newNode);
            txt_key.setText("");
            BuildVisualTree();
            
        }else{
            AddMessage("> Add Node - " + txt_key.getText(), "");
            addNode(Integer.parseInt(txt_key.getText()), NodeDetailTitle, false);

        }
        
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
        //previousKey = null;
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
    
    @FXML
    private void OnCustomNodeClicked(MouseEvent event) {
        customNodeDetail();
    }
    
    @FXML
    private void OnShowTravesalTreeClicked(MouseEvent event) {
        if(root == null){
            AddMessage("> Show Travesal Tree - null", "green");
            return;
        }
        if(cbox_inorder.isSelected()){
            AddMessage("> Show In-order Travesal Tree - null", "green");
            ShowTravesalTreeList(1);
        }else if(cbox_preorder.isSelected()){
            AddMessage("> Show Pre-order Travesal Tree - null", "green");
            ShowTravesalTreeList(2);
        }else if(cbox_postorder.isSelected()){
            AddMessage("> Show Post-order Travesal Tree - null", "green");
            ShowTravesalTreeList(3);
        }
    }
    
    void newTree(){
        
        if(root == null){
            AddMessage("> New Tree - blank", "");
            return;
        }
        root = null;
        pane_visual.getChildren().clear();
        //previousKey = null;
        
    }
    
    void randomTree(){
        NodeLabels.clear();
        newTree();
        AddMessage("> Random Tree - Start generating new random binary tree", "green");
        
        String RandomNodeList = "Random Node: ";
        for(int i = 0; i < 15; i++){
            
            Random rand = new Random();
            int newKey = rand.nextInt(50);
            Node n = new Node(newKey);
            RandomNodeList += newKey + ", ";
            NodeLabels.add(n);
            addNode(newKey);

        }
            
        AddMessage("> " + RandomNodeList, "green");
        resetPreviousNodeStyle(root);
        AddMessage("> Random Tree - Finished generating new random binary tree", "green");

    }
    
    // Travesal Tree
    
    void inOrderTravesalTree(Node focusNode){
        
        if(focusNode != null){
            
            inOrderTravesalTree(focusNode.leftChild);
            
            travesalTreeList.add(focusNode.key);
            
            inOrderTravesalTree(focusNode.rightChild);
        }
    }
    
    void preOrderTravesalTree(Node focusNode){
        
        if(focusNode != null){
            
            travesalTreeList.add(focusNode.key);
            
            inOrderTravesalTree(focusNode.leftChild);
            
            inOrderTravesalTree(focusNode.rightChild);
        }
    }
    
    void postOrderTravesalTree(Node focusNode){
        
        if(focusNode != null){
                        
            inOrderTravesalTree(focusNode.leftChild);
            
            inOrderTravesalTree(focusNode.rightChild);
            
            travesalTreeList.add(focusNode.key);
        }
    }
    
    // Show Travesal Tree
    Pane TravesalPane;
    void ShowTravesalTreeList(int type){
        travesalTreeList = new Vector<>();
        String Listtype = "";
        switch (type){
            case 1:
                Listtype = "In-Order Travesal Tree";
                inOrderTravesalTree(root);
                break;
            case 2:
                Listtype = "Pre-Order Travesal Tree";
                preOrderTravesalTree(root);
                break;
            case 3:
                Listtype = "Post-Order Travesal Tree";
                postOrderTravesalTree(root);
                break;
        }
        if(TravesalPane != null){
            pane_visual.getChildren().remove(TravesalPane);
        }
        Pane pane = new Pane();
        TravesalPane = pane;
        pane.setLayoutX(10);
        pane.setLayoutY(10);
        pane.getStyleClass().add("subMenu");
        VBox v = new VBox();
        v.setSpacing(3);
        String list = travesalTreeList + "";
        
        Label header = new Label(Listtype);
        header.getStyleClass().add("NodeDetail");
        Label content = new Label(list);
        content.getStyleClass().add("NodeDetail");
        Button close = new Button("Close");

        close.getStyleClass().add("Button");
        
        close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pane_visual.getChildren().remove(pane);
            }
        });
        
        v.getChildren().add(header);
        v.getChildren().add(content);
        v.getChildren().add(close);
        
        pane.getChildren().add(v);
        
        pane_visual.getChildren().add(pane);

        
    }
    
    public void BuildVisualTree(){
                
        newTree();
        for(int i = 0; i < NodeLabels.size(); i++){
            if(NodeDetailTitle != null){
                
                addNode(NodeLabels.get(i), NodeDetailTitle, true);

            }else{
                addNode(NodeLabels.get(i).key);
            }
        }
        
    }
    // Custom Node Detail
    public void customNodeDetail(){
        Vector<TextField> v = new Vector<TextField>();
        
        Pane container = new Pane();
        container.setLayoutX(10);
        container.setLayoutY(10);
        container.getStyleClass().add("customNodePane");
        
        BorderPane br = new BorderPane();
        
        VBox list = new VBox();
        list.setPadding(new Insets(0, 3, 0, 3));
        list.setSpacing(1);
        
        Label title = new Label("Custom Node Detail");
        title.getStyleClass().add("customNodeHeader");
        
        HBox bottomContainer =new HBox();
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(3));
        Button addDetail = new Button("Add Field");
        Button apply = new Button("Apply");
        addDetail.getStyleClass().add("Button");
        apply.getStyleClass().add("Button");
        bottomContainer.getChildren().add(addDetail);
        bottomContainer.getChildren().add(apply);
        
        
        if(NodeDetailTitle != null){
            
            for(int i = 0 ; i < NodeDetailTitle.size(); i++){
                
                HBox detailContainer =  new HBox();
                detailContainer.setAlignment(Pos.CENTER_RIGHT);
                TextField txt_detail = new TextField();
                txt_detail.setText(NodeDetailTitle.get(i));
                txt_detail.getStyleClass().add("customNodeTextfield");
                Button btn_deleteDetail = new Button();
                btn_deleteDetail.getStyleClass().add("Button");
                txt_detail.setPrefWidth(200);
                btn_deleteDetail.setText("X");
                
                btn_deleteDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        list.getChildren().remove(detailContainer);
                        v.remove(txt_detail);
                    }
                });
                
                detailContainer.getChildren().add(txt_detail);
                detailContainer.getChildren().add(btn_deleteDetail);
                v.add(txt_detail);
                
                list.getChildren().add(detailContainer);
            }
        }
        
        addDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HBox detailContainer =  new HBox();
                detailContainer.setAlignment(Pos.CENTER_RIGHT);
                TextField txt_detail = new TextField();
                txt_detail.getStyleClass().add("customNodeTextfield");
                Button btn_deleteDetail = new Button();
                btn_deleteDetail.getStyleClass().add("Button");
                txt_detail.setPrefWidth(200);
                btn_deleteDetail.setText("X");
                
                btn_deleteDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        list.getChildren().remove(detailContainer);
                        v.remove(txt_detail);
                    }
                });
                
                detailContainer.getChildren().add(txt_detail);
                detailContainer.getChildren().add(btn_deleteDetail);
                v.add(txt_detail);
                
                list.getChildren().add(detailContainer);
            }
        });

        apply.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                NodeDetailTitle = new Vector<String>();
                for(int i = 0; i < v.size();i++){
                    NodeDetailTitle.add(v.get(i).getText());
                }
                if(v.size() == 0){
                    NodeDetailTitle = null;
                }
                pane_visual.getChildren().remove(container);
            }
        });
        br.setTop(title);
        br.setCenter(list);
        br.setBottom(bottomContainer);
        
        container.getChildren().add(br);
        pane_visual.getChildren().add(container);
    }

    // Add Node
    public Node addNode(int key){
        Node newN = null;
        if(root != null && findNode(key, root, false, false) == true){
            return newN;
        }else{
        resetPreviousNodeStyle(root);

        //previousKey = key+"";
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
            newN = newNode;
        }
        return newN;
    }
    
    // Add Node Custom
    public Node addNode(int key, Vector<String> detail, boolean rebuild){
        Node newN = null;
        if(root != null && findNode(key, root, false, false) == true){
            return newN;
        }else{
        resetPreviousNodeStyle(root);

        //previousKey = key+"";
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
        
        if(rebuild == false){
            addNodeCustomPane(detail, newNode);
        }else{
            //newNode.setNodeDetails();
            newNode.AddNodeLabel();
            if(root.key != key){
                newNode.DrawLine();
            }
        }
            newN = newNode;
        }
        return newN;
    }
    
    // Rebuild Node Custom
    public Node addNode(Node keyNode, Vector<String> detail, boolean rebuild){
        Node newN = null;
        if(root != null && findNode(keyNode.key, root, false, false) == true){
            return newN;
        }else{
        resetPreviousNodeStyle(root);

        //previousKey = keyNode.key+"";
        Node newNode = new Node(keyNode.key);
        if(root == null){
            root = newNode;
            newNode.setNodeDetails(keyNode.getNodeDetails());
            
        }else{
            Node parent;
            Node focusNode = root;
            boolean cont = true;
            while(cont == true){
                parent = focusNode;
                if(keyNode.key < focusNode.key){
                    focusNode = focusNode.leftChild;
                    if(focusNode == null){
                        newNode.parent = parent;
                        parent.leftChild = newNode;
                        newNode.setIsParentLeft(false);
                        newNode.setNodeDetails(keyNode.getNodeDetails());
                        cont = false;
                    }
                }else{
                    focusNode = focusNode.rightChild;
                    if(focusNode == null){
                        newNode.parent = parent;
                        parent.rightChild = newNode;
                        newNode.setIsParentLeft(true);
                        newNode.setNodeDetails(keyNode.getNodeDetails());
                        cont = false;
                    }
                }
                
            }
            
        }
        
        if(rebuild == false){
            addNodeCustomPane(detail, newNode);
        }else{
            //newNode.setNodeDetails();
            newNode.AddNodeLabel();
            if(root.key != keyNode.key){
                newNode.DrawLine();
            }
        }
            newN = newNode;
        }
        return newN;
    }
    
    // Node Custom Pane
    public void addNodeCustomPane(Vector<String> titleList, Node newNode){
        Vector<TextField> detail = new Vector<TextField>();
        Vector<String> detailText = new Vector<String>();
        Pane container = new Pane();
        container.setLayoutX(10);
        container.setLayoutY(10);
        container.getStyleClass().add("customNodePane");
        
        BorderPane br = new BorderPane();
        
        VBox list = new VBox();
        list.setPadding(new Insets(0, 3, 0, 3));
        list.setSpacing(1);
        
        Label header = new Label("Custome Node Detail");
        header.getStyleClass().add("customNodeHeader");

        for(int i =0; i < titleList.size(); i++){
            HBox detailItemContainer =new HBox();
            detailItemContainer.setAlignment(Pos.CENTER_RIGHT);
            Label title = new Label();
            title.setText(titleList.get(i).toString());
            title.getStyleClass().add("customNodeHeader");
            TextField txt_detail = new TextField();
            txt_detail.getStyleClass().add("customNodeTextfield");
            detailItemContainer.getChildren().add(title);
            detailItemContainer.getChildren().add(txt_detail);
            list.getChildren().add(detailItemContainer);
            detail.add(txt_detail);
            
            if(newNode.getNodeDetails()!=null){
                
            }
        }
        
        Button addDetail = new Button("Add");
        addDetail.getStyleClass().add("Button");

        addDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for(int i = 0; i < detail.size();i++){
                    detailText.add(i, detail.get(i).getText());
                }
                
                newNode.setNodeDetails(detailText);
                
                newNode.AddNodeLabel();
                
                if(root.key != newNode.key){
                    newNode.DrawLine();
                }
                NodeLabels.add(newNode);
                txt_key.setText("");
                pane_visual.getChildren().remove(container);

            }
        });
        
        br.setTop(header);
        br.setCenter(list);
        br.setBottom(addDetail);
        
        container.getChildren().add(br);
        pane_visual.getChildren().add(container);
        
    }
    // Find Node
    //String previousKey = null;
    public boolean findNode(int key, Node startNode, boolean highlight, boolean isLMA){
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
            //previousKey = key+"";   
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
            int dkey = delNode.key;
            NodeLabels.removeIf(n -> (n.key == dkey));
            //NodeLabels.removeElement(delNode.key == delNode.key);
            
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
            int dkey = delNode.key;
            NodeLabels.removeIf(n -> (n.key == dkey));
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
            int tempNodeKey = -1;
            int tempNodeMaxKey = -1;
            for(int i = 0; i < NodeLabels.size();i++){
                if(NodeLabels.get(i).key == delNode.key){
                    tempNodeKey = i;
                }
                if(NodeLabels.get(i).key == replacedNode.key){
                    tempNodeMaxKey = i;
                }
            }
            Collections.swap(NodeLabels, tempNodeKey, tempNodeMaxKey);
            int dkey = delNode.key;
            NodeLabels.removeIf(n -> (n.key == dkey));
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
                
                newNode.showSubMenu(); 
                //SubMenu(newNode);
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
        
        Vector<String> nodeDetails;
        
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
        
        public Vector<String> getNodeDetails() {
            return nodeDetails;
        }

        public void setNodeDetails(Vector<String> nodeDetails) {
            this.nodeDetails = nodeDetails;
            
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
        
        public void showSubMenu(){
            if(submenu != null){
                pane_visual.getChildren().remove(submenu);
            }

            Pane menu = new Pane();
            menu.setPrefWidth(200);
            submenu = menu;
            menu.setId("SubMenu");
            menu.getStyleClass().add("subMenu");

            VBox list = new VBox();

            Label selection0 = new Label("Node Detail");
            selection0.getStyleClass().add("subMenuButton");
            Label selection1 = new Label("Find path from root");
            selection1.getStyleClass().add("subMenuButton");
            Label selection2 = new Label("Delete node");
            selection2.getStyleClass().add("subMenuButton");
            Label selection3 = new Label("Close");
            selection3.getStyleClass().add("subMenuButton");


            selection0.prefWidthProperty().bind(menu.widthProperty());
            selection1.prefWidthProperty().bind(menu.widthProperty());
            selection2.prefWidthProperty().bind(menu.widthProperty());
            selection3.prefWidthProperty().bind(menu.widthProperty());

            // Show Detail
            selection0.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pane_visual.getChildren().remove(submenu);
                    showNodeInfo();
                }
            });
            
            // Action Find Path
            selection1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    resetPreviousNodeStyle(root);
                    findNode(key, root, true, false);
                    pane_visual.getChildren().remove(submenu);
                }
            });

            // Action Delete Node
            selection2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    deleteNode(key);
                    txt_key.setText("");
                }
            });
            // Close Sub menu
            selection3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pane_visual.getChildren().remove(submenu);
                }
            });

            if(this.nodeDetails != null){
                list.getChildren().add(selection0);
            }
            list.getChildren().add(selection1);
            list.getChildren().add(selection2);
            list.getChildren().add(selection3);

            menu.getChildren().add(list);

            menu.setLayoutX(getLabelNode().getLayoutX() + 50);
            menu.setLayoutY(getLabelNode().getLayoutY());
            pane_visual.getChildren().add(menu);
        }
        
        public void showNodeInfo(){
            if(submenu != null){
                pane_visual.getChildren().remove(submenu);
            }

            Pane menu = new Pane();
            menu.setMinWidth(200);
            //menu.setPrefWidth(200);
            submenu = menu;
            menu.setId("SubMenu");
            menu.getStyleClass().add("subMenu");

            VBox list = new VBox();

            Label selection0 = new Label("Close");
            selection0.getStyleClass().add("subMenuButton");

            selection0.prefWidthProperty().bind(menu.widthProperty());

            // Close Sub menu
            selection0.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pane_visual.getChildren().remove(submenu);
                }
            });
            
            for(int i = 0; i < NodeDetailTitle.size();i++){
                HBox b = new HBox();
                Label field = new Label(NodeDetailTitle.get(i) + ": ");
                field.getStyleClass().add("NodeDetail");
                Label fieldDetail = new Label(nodeDetails.get(i));
                fieldDetail.getStyleClass().add("NodeDetailLabel");
                b.getChildren().add(field);
                b.getChildren().add(fieldDetail);
                list.getChildren().add(b);
            }
            
            if(getNodeDetails() != null){
                Separator line = new Separator(Orientation.HORIZONTAL);
                list.getChildren().add(line);
                list.getChildren().add(selection0);
            }


            menu.getChildren().add(list);

            menu.setLayoutX(getLabelNode().getLayoutX() + 50);
            menu.setLayoutY(getLabelNode().getLayoutY());
            pane_visual.getChildren().add(menu);
        }
    }
}