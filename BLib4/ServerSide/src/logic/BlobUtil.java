package logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlobUtil
{
    /**
     * The method converts a Blob object to List(String[])
     *
     * @param blobData - the Blob object to convert
     * @return - the List(String[]) object
     */
    public static List<String[]> convertBlobToList(byte[] blobData)
    {
        List<String[]> rows = new ArrayList<>();
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(blobData), String.valueOf(StandardCharsets.UTF_8)))
        {
            while (scanner.hasNextLine())
            {
                rows.add(scanner.nextLine().split(",")); // Split rows into columns
            }
        }
        return rows;
    }

    /**
     * The method converts a List (String[]) to a Blob object
     *
     * @param rows - the List (String[]) object to convert
     * @return - the Blob object
     * @throws IOException - if an I/O error occurs
     */
    public static byte[] convertListToBlob(List<String[]> rows) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String[] row : rows)
        {
            String line = String.join(",", row); // Join columns with commas
            outputStream.write((line + "\n").getBytes(StandardCharsets.UTF_8)); // Add newline
        }
        return outputStream.toByteArray();
    }
}