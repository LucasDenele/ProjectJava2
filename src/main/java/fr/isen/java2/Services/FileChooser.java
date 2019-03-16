package fr.isen.java2.Services;

import fr.isen.java2.db.entities.Person;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.LinkedList;

public class FileChooser {
    public static void  makeExport(LinkedList<Person> persons){
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new java.io.File("."));
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = jfc.showOpenDialog(null);
        jfc.setAcceptAllFileFilterUsed(false);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getCurrentDirectory());
        }
    }

    public static void makeImport(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            if(selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")).equals("vcf")){
                System.out.println(selectedFile.getAbsolutePath());
            }
        }
    }
}
