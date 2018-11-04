package com.kerinb.IR_proj2_group14;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class ApplicationLibrary {

    static List<String> getFileNamesFromDirTree(String rootDir){
        List<String> files = new ArrayList<>();
        try {
            Files.walk(Paths.get(rootDir)).forEach(path ->{
                File file = new File(path.toString());
                if(file.isFile() && ! file.getName().contains("read")){
                    files.add(path.toString());
                }
            });
        } catch (IOException e) {
            System.out.println(String.format("ERROR: IOException occurred when walking file tree: %s", rootDir));
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
        return files;
    }
}
