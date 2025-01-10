package logic;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;

import com.opencsv.CSVReader;
import javafx.scene.image.Image;
import com.opencsv.CSVWriter;

public class BlobUtil
{
    /**
     * The method converts CSVWriter object to a Blob object to be stored in the DB
     * @param csvWriter - The CSVWriter object to be converted
     * @return - The Blob object
     */
    public static Blob convertCsvToBlob(CSVWriter csvWriter) {
        Blob blob = null;
        try {
            byte[] csvAsBytes = csvWriter.toString().getBytes();
            blob = new javax.sql.rowset.serial.SerialBlob(csvAsBytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blob;
    }

    /**
     * The method converts a Blob object to a CSVReader object
     * @param blob - The Blob object to be converted from the DB
     * @return - The CSVReader object
     */
    public static CSVReader convertBlobToCsv(Blob blob) {
        CSVReader csvReader = null;
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blobAsBytes);
            InputStreamReader reader = new InputStreamReader(inputStream);
            csvReader = new CSVReader(reader);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return csvReader;
    }
}