package kr.swim.util.enc;


import kr.swim.util.io.CloseResourceHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <PRE>
 *     파일 암/복호화 저장
 * </PRE>
 */
@Slf4j
public class FileEncUtils {

    private static final int KEY = 1249723;

    /**
     * <PRE>
     * 파일 암호화하여 덮어쓰기
     * </PRE>
     *
     * @param FILE_PATH
     * @throws IOException
     */
    public static final void encrypt(final String FILE_PATH) throws IOException {
        encrypt(FILE_PATH, FILE_PATH);
    }

    /**
     * <PRE>
     * 파일 암호화하여 저장
     * </PRE>
     *
     * @param FILE_PATH
     * @param DEST_PATH
     * @throws IOException
     */
    public static final void encrypt(final String FILE_PATH, final String DEST_PATH) throws IOException {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // Selecting a Image for operation
            fis = new FileInputStream(FILE_PATH);

            // Converting Image into byte array, create a array of same size as Image size
            byte data[] = new byte[fis.available()];

            // Read the array
            fis.read(data);
            int i = 0;

            // Performing an XOR operation on each value of byte array due to which every value of Image will change.
            for (byte b : data) {
                data[i] = (byte) (b ^ KEY);
                i++;
            }
            // Opening a file for writing purpose
            fos = new FileOutputStream(DEST_PATH);

            // Writing new byte array value to image which will Encrypt it.
            fos.write(data);

        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            CloseResourceHelper.close(fos);
            CloseResourceHelper.close(fis);
        }

    }


    /**
     * <PRE>
     * 파일 복호화 - 덮어쓰기
     * </PRE>
     *
     * @param FILE_PATH  파일 경로
     * @throws IOException
     */
    public static final void decrypt(final String FILE_PATH) throws IOException {
        decrypt(FILE_PATH, FILE_PATH);
    }

    /**
     * <PRE>
     * 파일 복호화 - 복사하기
     * </PRE>
     *
     * @param FILE_PATH  암호화된 원본 파일 경로
     * @param DEST_PATH  복호화할 신규 파일 경로
     * @throws IOException
     */
    public static final void decrypt(final String FILE_PATH, final String DEST_PATH) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // Selecting a Image for Decryption.
            fis = new FileInputStream(FILE_PATH);

            // Converting image into byte array, it will Create a array of same size as image.
            byte data[] = new byte[fis.available()];

            // Read the array
            fis.read(data);
            int i = 0;

            // Performing an XOR operation on each value of byte array to Decrypt it.
            for (byte b : data) {
                data[i] = (byte) (b ^ KEY);
                i++;
            }

            // Opening file for writting purpose
            fos = new FileOutputStream(DEST_PATH);

            // Writting Decrypted data on Image
            fos.write(data);
            
        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            CloseResourceHelper.close(fos);
            CloseResourceHelper.close(fis);
        }
    }
}
