package vn.uiza.utils.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static vn.uiza.utils.util.FileUtilsTest.TestConfig.FILE_SEP;
import static vn.uiza.utils.util.FileUtilsTest.TestConfig.PATH_FILE;
import static vn.uiza.utils.util.FileUtilsTest.TestConfig.PATH_TEMP;

public class FileUtilsTest {
    static class TestConfig {

        static final String FILE_SEP = File.separator;
        static final String TEST_PATH;

        static {
            String projectPath = System.getProperty("user.dir");
            TEST_PATH = projectPath + FILE_SEP + "src" + FILE_SEP + "test" + FILE_SEP + "res" + FILE_SEP;
        }

        static final String PATH_TEMP = TEST_PATH + "temp" + FILE_SEP;
        static final String PATH_FILE = TEST_PATH + "file" + FILE_SEP;
    }

    @Test
    public void getFileByPath() {
        assertNull(FileUtils.getFileByPath(" "));
        assertNotNull(FileUtils.getFileByPath(PATH_FILE));
    }

    @Test
    public void isFileExists() {
        assertTrue(FileUtils.isFileExists(PATH_FILE + "UTF8.txt"));
        assertFalse(FileUtils.isFileExists(PATH_FILE + "UTF8"));
    }

    @Test
    public void rename() {
        assertTrue(FileUtils.rename(PATH_FILE + "GBK.txt", "GBK1.txt"));
        assertTrue(FileUtils.rename(PATH_FILE + "GBK1.txt", "GBK.txt"));
    }

    @Test
    public void isDir() {
        assertFalse(FileUtils.isDir(PATH_FILE + "UTF8.txt"));
        assertTrue(FileUtils.isDir(PATH_FILE));
    }

    @Test
    public void isFile() {
        assertTrue(FileUtils.isFile(PATH_FILE + "UTF8.txt"));
        assertFalse(FileUtils.isFile(PATH_FILE));
    }

    @Test
    public void createOrExistsDir() {
        assertTrue(FileUtils.createOrExistsDir(PATH_FILE + "new Dir"));
        assertTrue(FileUtils.createOrExistsDir(PATH_FILE));
        assertTrue(FileUtils.deleteDir(PATH_FILE + "new Dir"));
    }

    @Test
    public void createOrExistsFile() {
        assertTrue(FileUtils.createOrExistsFile(PATH_FILE + "new File"));
        assertFalse(FileUtils.createOrExistsFile(PATH_FILE));
        assertTrue(FileUtils.deleteFile(PATH_FILE + "new File"));
    }

    @Test
    public void createFileByDeleteOldFile() {
        assertTrue(FileUtils.createFileByDeleteOldFile(PATH_FILE + "new File"));
        assertFalse(FileUtils.createFileByDeleteOldFile(PATH_FILE));
        assertTrue(FileUtils.deleteFile(PATH_FILE + "new File"));
    }

    @Test
    public void copyDir() {
        assertFalse(FileUtils.copyDir(PATH_FILE, PATH_FILE));
        assertFalse(FileUtils.copyDir(PATH_FILE, PATH_FILE + "new Dir"));
        assertTrue(FileUtils.copyDir(PATH_FILE, PATH_TEMP));
        assertTrue(FileUtils.deleteDir(PATH_TEMP));
    }

    @Test
    public void copyFile() {
        assertFalse(FileUtils.copyFile(PATH_FILE + "GBK.txt", PATH_FILE + "GBK.txt"));
        assertTrue(FileUtils.copyFile(PATH_FILE + "GBK.txt", PATH_FILE + "new Dir" + FILE_SEP + "GBK.txt"));
        assertTrue(FileUtils.copyFile(PATH_FILE + "GBK.txt", PATH_TEMP + "GBK.txt"));
        assertTrue(FileUtils.deleteDir(PATH_FILE + "new Dir"));
        assertTrue(FileUtils.deleteDir(PATH_TEMP));
    }

    @Test
    public void moveDir() {
        assertFalse(FileUtils.moveDir(PATH_FILE, PATH_FILE));
        assertFalse(FileUtils.moveDir(PATH_FILE, PATH_FILE + "new Dir"));
        assertTrue(FileUtils.moveDir(PATH_FILE, PATH_TEMP));
        assertTrue(FileUtils.moveDir(PATH_TEMP, PATH_FILE));
    }

    @Test
    public void moveFile() {
        assertFalse(FileUtils.moveFile(PATH_FILE + "GBK.txt", PATH_FILE + "GBK.txt"));
        assertTrue(FileUtils.moveFile(PATH_FILE + "GBK.txt", PATH_TEMP + "GBK.txt"));
        assertTrue(FileUtils.moveFile(PATH_TEMP + "GBK.txt", PATH_FILE + "GBK.txt"));
        FileUtils.deleteDir(PATH_TEMP);
    }

    @Test
    public void listFilesInDir() {
        System.out.println(FileUtils.listFilesInDir(PATH_FILE, false).toString());
        System.out.println(FileUtils.listFilesInDir(PATH_FILE, true).toString());
    }


    @Test
    public void getFileCharsetSimple() {
        assertEquals("UTF-8", FileUtils.getFileCharsetSimple(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getFileLines() {
        assertEquals(7, FileUtils.getFileLines(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getDirSize() {
        System.out.println(FileUtils.getDirSize(PATH_FILE));
    }

    @Test
    public void getFileSize() {
        System.out.println(FileUtils.getFileSize(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getDirLength() {
        System.out.println(FileUtils.getDirLength(PATH_FILE));
    }

    @Test
    public void getFileLength() {
        System.out.println(FileUtils.getFileLength(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getFileMD5ToString() {
        assertEquals("FDB8A37C0B454BD4EB3A43EFC93F36F5", FileUtils.getFileMD5ToString(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getDirName() {
        assertEquals(PATH_FILE, FileUtils.getDirName(new File(PATH_FILE + "UTF8.txt")));
        assertEquals(PATH_FILE, FileUtils.getDirName(PATH_FILE + "UTF8.txt"));
    }

    @Test
    public void getFileName() {
        assertEquals("UTF8.txt", FileUtils.getFileName(PATH_FILE + "UTF8.txt"));
        assertEquals("UTF8.txt", FileUtils.getFileName(new File(PATH_FILE + "UTF8.txt")));
    }

    @Test
    public void getFileNameNoExtension() {
        assertEquals("UTF8", FileUtils.getFileNameNoExtension(PATH_FILE + "UTF8.txt"));
        assertEquals("UTF8", FileUtils.getFileNameNoExtension(new File(PATH_FILE + "UTF8.txt")));
    }

    @Test
    public void getFileExtension() {
        assertEquals("txt", FileUtils.getFileExtension(new File(PATH_FILE + "UTF8.txt")));
        assertEquals("txt", FileUtils.getFileExtension(PATH_FILE + "UTF8.txt"));
    }
}
