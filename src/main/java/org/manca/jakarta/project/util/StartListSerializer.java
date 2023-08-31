package org.manca.jakarta.project.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;

@ApplicationScoped
public class StartListSerializer {

    public boolean serialize(StartList startList, String fileName) throws IOException {
        File file = new File(this.getPathForStartList());
        file.mkdirs();
        file = new File(this.getPathForStartList() + fileName);
        file.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(file,true);
                ObjectOutputStream obs = new ObjectOutputStream(fos)) {

            obs.writeObject(startList);
            System.out.println("org.manca.jakarta.project.util.StartListSerializer.serialize:'StartList serialized.'"+ file.getAbsolutePath());
        }

        return true;
    }

    public StartList deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(this.getPathForStartList()+fileName);
             ObjectInputStream obs = new ObjectInputStream(fis)) {
            System.out.println("org.manca.jakarta.project.util.StartListSerializer.deserialize:'StartList deserialized.'");
            return (StartList) obs.readObject();
        }
    }

    //Get the path of the files in which serialize or deserialize StartList instance dynamically
    private String getPathForStartList() {
        final String TARGET_URL = "/AppData/Local/Dtrcs/data/";
        String path = System.getenv("USERPROFILE").replace("\\", "/") + TARGET_URL;
        return path;
    }

}
