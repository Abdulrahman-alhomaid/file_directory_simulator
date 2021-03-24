/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osfiledir;

import java.util.Scanner;

/**
 *
 * @author USER
 */
public class OsFileDir {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        FileDirTree root = new FileDirTree();

        Scanner kb = new Scanner(System.in);
        String path = root.getAbPath();
        String path1 = root.getAbPath();
        boolean loopStatis = true;

        while (loopStatis) {
            System.out.print(path + "$");
            String n = kb.nextLine();
            String[] comand = n.split(" ");

            switch (comand[0]) {
                case "cd":
                    String cdResult;
                    if (comand.length == 1) {
                        cdResult = root.cdFile("");
                    } 
                    else {
                        cdResult = root.cdFile(comand[1]);
                    }
                    if (!cdResult.equals("")) {
                        path = cdResult;
                    }
                   
                    break;
                case "mikdir":
                    String pattern = "root.*";
                    if (comand[1].matches(pattern)) {
                        
                    }
                    root.mikDir(comand[1]);
                    break;
                case "ls":
                    if (comand.length == 1) {
                        root.printFiles();
                    } else if (comand[1].equals("-t")) {
                       
                        root.printTree();
                    } else if (comand[1].equals("-l")) {
                        root.printWithDetiles();
                    }
                    else 
                        System.out.println("in falid command");
                    break;
                case "rm":
                    if (comand.length == 1) {
                        root.deleat(path);
                    } else {
                        System.out.println(comand[1]);
                        root.deleat(comand[1]);
                    }
                    break;
                case "out":
                    loopStatis = false;
                    break;
                default:
                    System.out.println("in falid command");

            }
        }
    }

}
