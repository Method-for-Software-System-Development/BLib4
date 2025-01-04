package logic;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

public class ImageUtil {
    public static Image convertBlobToImage(Blob blob) {
        Image image = null;
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blobAsBytes);
            image = new Image(inputStream);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }
}