package org.manca.jakarta.project.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Application;

import java.io.*;

@ApplicationScoped
public class StartListSerializer {

    public boolean serialize(StartList startList, String fileName) throws IOException {
        File file=new File("C:\\MySoftwareapplications\\Race\\src\\main\\resources\\data\\"+fileName);
        file.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(file,true);
                ObjectOutputStream obs = new ObjectOutputStream(fos)) {

            obs.writeObject(startList);
            System.out.println("org.manca.jakarta.project.util.StartListSerializer.serialize:'StartList serialized.'"+ file.getAbsolutePath());
        }

        return true;
    }

    public StartList deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("C:\\MySoftwareapplications\\Race\\src\\main\\resources\\data\\"+fileName);
             ObjectInputStream obs = new ObjectInputStream(fis)) {
            System.out.println("org.manca.jakarta.project.util.StartListSerializer.deserialize:'StartList deserialized.'");
            return (StartList) obs.readObject();
        }
    }

}
