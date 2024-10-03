import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filePaths) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String filePath : filePaths) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    ZipEntry zipEntry = new ZipEntry(filePath.substring(filePath.lastIndexOf("/") + 1));
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Deleted: " + filePath);
            } else {
                System.out.println("Failed to delete: " + filePath);
            }
        }
    }

    public static void main(String[] args) {
        GameProgress gp1 = new GameProgress(100, 3, 1, 50.5);
        GameProgress gp2 = new GameProgress(80, 5, 2, 100.75);
        GameProgress gp3 = new GameProgress(60, 2, 3, 200.0);

        saveGame("C:\\Games\\savegames\\save1.dat", gp1);
        saveGame("C:\\Games\\savegames\\save2.dat", gp2);
        saveGame("C:\\Games\\savegames\\save3.dat", gp3);

        // Список файлов сохранений
        List<String> saveFiles = List.of(
                "C:\\Games\\savegames\\save1.dat",
                "C:\\Games\\savegames\\save2.dat",
                "C:\\Games\\savegames\\save3.dat"
        );

        zipFiles("C:\\Games\\savegames\\saves.zip", saveFiles);

        deleteFiles(saveFiles);
    }
}