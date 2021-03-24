/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osfiledir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author USER
 */
public class FileDirTree extends JComponent implements Accessible {

    private Node mainRoot;

    private Node root;
    private String path;
    int totalFilist;
    private String abPath;

    private class Node {

        private String path;
        private ArrayList<Node> dir = new ArrayList<Node>();
        private File dirFile;

        Node(String path, File dirFile) {
            this.path = path;
            this.dirFile = dirFile;
        }
    }

    public FileDirTree() {
        this.path = "root";
        totalFilist = 0;
        File file;
        file = new File(path);
        root = new Node(path, file);
        if (!file.exists()) {
            file.mkdir();
        }
        abPath = file.getAbsolutePath();
        loadFiles(root);
        mainRoot = root;
    }

    public String getAbPath() {
        return abPath;
    }

    public String getPath(File f) {

        return f.getAbsolutePath();
    }

    public void mikDir(String path) {
        mikDir(root, path);
    }

    private void mikDir(Node h, String path, int lvl) {

        if (h.dirFile.isDirectory()) {
            String a[] = h.dirFile.list();
            String[] t = path.split("\\\\");
            String myPath = t[0];
            for (int i = 1; i < lvl; i++) {
                myPath = myPath + "\\" + t[i];
            }
//            System.out.println(myPath + " my path");
//            System.out.println(path + " the path");
            for (int i = 0; i < a.length; i++) {
                String rePath = h.path + "\\" + a[i];
                //System.out.println(rePath + "  rePAtrh");
                if (myPath.equals(rePath)) {
//                    System.out.println(path.split("\\\\").length);
//                    System.out.println(rePath.split("\\\\").length);
                    if (path.split("\\\\").length == (rePath.split("\\\\").length + 1)) {
//                        System.out.println("WE DID IT");
//                        File newFile = new File(rePath);
//                        newFile.mkdir();
//                        Node newDir = new Node(rePath, newFile);
//                        h.dir.add(newDir);
//                        totalFilist++;
                        String rePath1 = path;
                        File newFile = new File(rePath1);
                        // System.out.println(rePath1);
                        if (!newFile.exists()) {
                            newFile.mkdir();
                            Node newDir = new Node(rePath1, newFile);
                            h.dir.add(newDir);
                            totalFilist++;
                        }
                        break;
                    } else {
                        mikDir(h.dir.get(i), path, ++lvl);
                    }

                }
            }

        }
    }

    private void mikDir(Node h, String path) {

        String rootPatter = "root.*";

        if (!path.matches(rootPatter)) {

            String patter = ".*\\.txt";

            if (path.matches(patter)) {   // try to have regulare EX to what what path is is (apsulot or relitv) WriteFile for TXT fils

                String rePath = h.path + "\\" + path;
                File newFile = new File(rePath);
                try (PrintWriter output = new PrintWriter(newFile);) {

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileDirTree.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!newFile.exists()) {
                    Node newDir = new Node(rePath, newFile);
                    h.dir.add(newDir);
                    totalFilist++;
                } else {
                    System.out.println("the file "+ path + " is Exest");
                }
            } else {
                String rePath = h.path + "\\" + path;
                File newFile = new File(rePath);

                if (!newFile.exists()) {
                    newFile.mkdir();
                    Node newDir = new Node(rePath, newFile);
                    h.dir.add(newDir);
                    totalFilist++;
                } else {
                    System.out.println(path + " file is Exest");
                }
            }
        } else {

            boolean found = false;
            for (Node j : h.dir) {

                if (j.path.equals(path)) {
                    String rePath = h.path + "\\" + path;
                    File newFile = new File(rePath);

                    if (!newFile.exists()) {
                        newFile.mkdir();
                        Node newDir = new Node(rePath, newFile);
                        h.dir.add(newDir);

                        totalFilist++;
                    }
                }
            }
            if (!found) {
               
                mikDir(mainRoot, path, 2);
            } else {
                System.out.println("invalid path");
            }
        }

    }

    public void deleat(String path) {
        deleat(path, root);
    }

    private void deleat(String path, Node h) {// requsev for the deleat to find the folder

       
        String patter = "root.*";

        if (!path.matches(patter)) {
            for (Node i : h.dir) {
                String s = i.dirFile.getPath();
                String fullPath[] = s.split("\\\\");
                String newPath = "";
                for (int j = 0; j < fullPath.length - 1; j++) { // -1 so  we do not need the file neme for the path root\os\
                    newPath += fullPath[j] + "\\" + "";
                }
                newPath += path;
                if (i.path.equals(newPath)) {
                   
                    //  i.dirFile.delete();
                    boolean deleted = i.dirFile.delete();;

                    if (deleted) {
                        h.dir.remove(h.dir.indexOf(i));
                    } else {
                        System.out.println("the dirctory has files can not delet it");
                    }
                    break;
                }
            }
        } else {
           
            boolean found = false;
            for (Node j : h.dir) {
                if (j.path.equals(path)) {
                    
                    //System.out.println("WE GOT IT ur path is " + path + "  we got " + i.path + " we cheack with " + path);
                    j.dirFile.delete();
                    h.dir.remove(h.dir.indexOf(j));
                    found = true;
                    break;
                }
            }
            if (!found) {
                //String[] t = path.split("\\\\");
                deletByAbsoltPath(h, path, 2);
            }
        }

    }

    private void deletByAbsoltPath(Node h, String path, int lvl) {

        if (h.dirFile.isDirectory()) {

            String a[] = h.dirFile.list();
            String[] t = path.split("\\\\");

            String myPath = t[0];
            for (int i = 1; i < lvl; i++) {
                myPath = myPath + "\\" + t[i];
            }
            

            for (int i = 0; i < a.length; i++) {

                String rePath = h.path + "\\" + a[i];

                if (myPath.equals(rePath)) {
                    if (path.equals(rePath)) {

                        for (Node j : h.dir) {

                            j.dirFile.delete();
                            h.dir.remove(h.dir.indexOf(j));

                            break;
                        }

                    } else {
                        deletByAbsoltPath(h.dir.get(i), path, ++lvl);
                    }
                }
            }
        }

    }

    public String cdFile(String path) {

        return cdFile(path, root);
    }

    private String cdFileByAbsoltPath(Node h, String path, int lvl) {

        if (h.dirFile.isDirectory()) {
            String a[] = h.dirFile.list();

            String[] t = path.split("\\\\");

            String myPath = t[0];
            for (int i = 1; i < lvl; i++) {
                myPath = myPath + "\\" + t[i];
            }

            for (int i = 0; i < a.length; i++) {

                String rePath = h.path + "\\" + a[i];

                if (myPath.equals(rePath)) {
                    if (path.equals(rePath)) {
                        for (Node j : h.dir) {
                            root = j;

                            return j.dirFile.getAbsolutePath();
                        }

                    } else {
                        return cdFileByAbsoltPath(h.dir.get(i), path, ++lvl);
                    }
                }
            }
        }
        return "";
    }

    private String cdFile(String path, Node h) {

        String patter = "root.*";

        if (path.equals("")) {
            root = mainRoot;
            return root.dirFile.getAbsolutePath();
        }
        if (!path.matches(patter)) {
            String a[] = h.dirFile.list();
            for (Node i : h.dir) {
                String s = i.dirFile.getPath();
                String fullPath[] = s.split("\\\\");
                String newPath = "";
                for (int j = 0; j < fullPath.length - 1; j++) {
                    newPath += fullPath[j] + "\\" + "";
                }
                newPath += path;
                if (i.path.equals(newPath)) {
                    root = i;
                    //System.out.println(i.path + " --> is the new root");
                    return i.dirFile.getAbsolutePath();
                }
            }
        } else {

            boolean found = false;
            for (Node j : h.dir) {
                if (j.path.equals(path)) {

                    return j.dirFile.getAbsolutePath();
                }
            }
            if (!found) {
                return cdFileByAbsoltPath(h, path, 2);
            }
        }
        System.out.println("the file dose not exist");
        return "";
    }

    public void printFiles() {
        printFiles(root);
    }

    private void loadFiles(Node h) { // we  have to load all files loop for all files use the list().length to cheak if the directory is emty else load the files 

        String a[] = h.dirFile.list();

        for (int i = 0; i < a.length; i++) {
            totalFilist++;

            String rePath = h.path + "\\" + a[i];
            File newFile = new File(rePath);
            if (newFile.exists()) {
                Node newDir = new Node(rePath, newFile);
                h.dir.add(newDir);
                if (newFile.isDirectory()) {
                    if (newFile.list().length > 0) {
                        loadFiles(newDir);
                    }
                }
            } else {
                newFile.mkdir();
                Node newDir = new Node(rePath, newFile);
                h.dir.add(newDir);
                System.out.println(h.dir.size());
                System.out.println("file has been created");
            }
        }
    }

    public void printTree() {
        MyJTree tree = new MyJTree(root);
        tree.setSize(300, 300);
        tree.setVisible(true);
    }

    public void printWithDetiles() {
        printWithDetiles(root);
    }

    private void printWithDetiles(Node h) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        for (Node i : h.dir) {
            String ms = "";
            int supDir = 0;
            File file = i.dirFile;
            if (file.isDirectory()) {
                ms = "d";
                supDir = file.list().length;
            } else {
                ms = "-";
            }

            if (file.canRead()) {
                ms += "r";
            }

            if (file.canWrite()) {
                ms += "w";
            }

            if (file.canExecute()) {
                ms += "x";
            }
            double bytes = file.length();

            System.out.print(ms + " " + supDir + " " + bytes + " " + file.getName() + " " + sdf.format(file.lastModified()) + " " + file.getName());
            System.out.println("");

        }

    }

    public void printWindow() {

        JFileChooser chooser = new JFileChooser("root");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "", "txt", "gif");
        //chooser.setFileFilter(filter);

        // int returnVal = chooser.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            System.out.println("You chose to open this file: "
//                    + chooser.getSelectedFile().getName());
//        }
        chooser.setFileView(new FileView() {
            @Override
            public Boolean isTraversable(File f) {
                return (f.isDirectory() && f.getName().equals("SERVER"));
            }
        });
        chooser.showOpenDialog(this);
        

    }

    public void printNumberOfFils() {
        System.out.println(totalFilist);
    }

    private void printFiles(Node h) {

        for (Node i : h.dir) {
            System.out.print(i.dirFile.getName() + " ");
        }
        System.out.println("");
    }

    private class MyJTree extends javax.swing.JFrame {

        Node root;

        MyJTree(Node root) {
            this.root = root;
            DefaultMutableTreeNode rootDir = new DefaultMutableTreeNode("root");
            loadFiles(root, rootDir);
            JTree jt = new JTree(rootDir);
            add(jt);
        }

        private void loadFiles(Node h, DefaultMutableTreeNode root) {

            String a[] = h.dirFile.list();

            for (int i = 0; i < a.length; i++) {

                // System.out.println(a[i]);
                String rePath = h.path + "\\" + a[i];
                File newFile = new File(rePath);
                if (newFile.exists()) {
                    Node newDir = new Node(rePath, newFile);
                    //  h.dir.add(newDir);
                    DefaultMutableTreeNode rootDir = new DefaultMutableTreeNode(a[i]);
                    root.add(rootDir);
                    if (newFile.isDirectory()) {
                        // System.out.println(newFile + " is a dirctory");
                        if (newFile.list().length > 0) {
                            loadFiles(newDir, rootDir);
                        }
                    }

                } else {
                    newFile.mkdir();
                    Node newDir = new Node(rePath, newFile);
                    h.dir.add(newDir);
//                    System.out.println(h.dir.size());
//                    System.out.println("file has been created");
                }
            }
        }

    }

}
