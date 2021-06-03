package com.bonsainet.bonsai.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PicTest {

  @Test
  public void setDimensionsTest() {
    Pic pic = new Pic();
    final int imgWidth = 100;
    final int imgHeight = 150;
    BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);

      pic.setRootFolder(file.getParentFile().getCanonicalPath());
      pic.setFileName(file.getName());
      pic.setDimensions();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (file.isFile() && !file.delete()) {
        // failed to delete the (existing) file
      }
    }

    assertEquals(pic.getDimX(), imgWidth);
    assertEquals(pic.getDimY(), imgHeight);
  }

  @Test
  public void getThumbFromImageTest() {
    Pic pic = new Pic();
    BufferedImage bufferedImage = new BufferedImage(pic.THUMB_DIM*4, pic.THUMB_DIM*2,
        BufferedImage.TYPE_INT_RGB); // create a BufferedImage object

    BufferedImage thumb = pic.getThumbFromImage(bufferedImage);
    assertEquals(thumb.getWidth(), pic.THUMB_DIM);
    assertEquals(thumb.getHeight(), pic.THUMB_DIM * 2.0/4.0);
  }

  @Test
  public void setThumbTest() {
    Pic pic = new Pic();
    final int imgWidth = 200;
    final int imgHeight = 300;
    BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);

      pic.setRootFolder(file.getParentFile().getCanonicalPath());
      pic.setFileName(file.getName());
      pic.setThumb();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //clean up files and thumb folder
      if (file.isFile()) {
        file.delete();
        Path pathThumb = Paths.get(pic.getRootFolder() + File.separatorChar + pic.THUMB_DIR);
        File fThumb = new File(pathThumb.toString(), pic.getFileNameThumb());
        if (fThumb.isFile()) {
          fThumb.delete();
        }
        File thumbFolder = new File(fThumb.getParent());
        if (thumbFolder.isDirectory()) {
          thumbFolder.delete();
        }
      }
    }

    assertThat(pic.getDimXThumb(), lessThanOrEqualTo(Integer.valueOf(pic.THUMB_DIM)));
    assertThat(pic.getDimYThumb(), lessThanOrEqualTo(Integer.valueOf(pic.THUMB_DIM)));
  }

  @Test
  public void setBadThumbTest() {
    Pic pic = new Pic();
    final int imgWidth = 200;
    final int imgHeight = 300;
    Path pathThumb;
    File fThumb = null;
    File folderThumb = null;
    BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);

      pic.setRootFolder(file.getParentFile().getCanonicalPath());
      pic.setFileName(file.getName());

      //create read-only thumb file and folder
      pathThumb = Paths.get(pic.getRootFolder() + File.separatorChar + pic.THUMB_DIR);
      if (Files.notExists(pathThumb)) {
        Files.createDirectory(pathThumb);
      }
      fThumb = new File(pathThumb.toString(), pic.getFileName());
      fThumb.createNewFile();
      fThumb.setWritable(false, false);
      folderThumb = new File(fThumb.getParent());
      boolean success = folderThumb.setWritable(false, false);
      if (!success) {
        //ok, probably windows...
        AclFileAttributeView view = Files.getFileAttributeView(
                pathThumb, AclFileAttributeView.class);
        List<AclEntry> acls = view.getAcl();
        for (int i = 0; i < acls.size(); i++) {
          UserPrincipal principal = acls.get(i).principal();
          Set<AclEntryPermission> permissions = acls.get(i).permissions();
          // remove WRITE_DATA permission
          permissions.remove(AclEntryPermission.WRITE_DATA);

          // create a new ACL entry
          AclEntry entry = AclEntry.newBuilder()
                  .setType(AclEntryType.ALLOW)
                  .setPrincipal(principal)
                  .setPermissions(permissions)
                  .build();

          // replace the ACL entry for authenticated users
          acls.set(i, entry);
        }
        // set the updated list of ACLs
        view.setAcl(acls);
      }

      pic.setThumb();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //clean up files and thumb folder
      if (file.isFile()) {
        file.delete();
        if (fThumb.isFile()) {
          fThumb.setWritable(true);
          fThumb.delete();
        }
        if (folderThumb.isDirectory()) {
          folderThumb.setWritable(true);
          folderThumb.delete();
        }
      }
    }

    assertThat(pic.getDimXThumb(), equalTo(0));
    assertThat(pic.getDimYThumb(), equalTo(0));
  }

  @Test
  public void settersDoNotExistTest() {
    String[] setMethodsShouldNotExist = {"setFileNameThumb", "setDimX", "setDimY", "setDimXThumb",
        "setDimYThumb"};
    try {
      Class cls = Class.forName("com.bonsainet.bonsai.model.Pic");
      Method m[] = cls.getMethods();
      for(int i = 0; i < m.length; i++) {
        for (int j = 0; j < setMethodsShouldNotExist.length; j++) {
          Assertions.assertNotEquals(m[i].getName().toLowerCase(), setMethodsShouldNotExist[j].toLowerCase());
        }
      }
    } catch (Exception e) {
      System.out.println("Pic class not found: " + e);
    }
  }

  @Test
  public void getImageFromFileTest() {
    Pic pic = new Pic();
    BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

    File file;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    try {
      BufferedImage readBufferedImage = pic.getImageFromFile(file);
      org.assertj.core.api.AssertionsForClassTypes.assertThat(bufferedImage)
          .usingRecursiveComparison()
          .isEqualTo(readBufferedImage);
    } finally {
      if (file.isFile() && !file.delete()) {
        // failed to delete the (existing) file
      }
    }
  }

  @Test
  public void getImageFromNonImageFileTest() {
    Pic pic = new Pic();
    File file;
    try {
      file = File.createTempFile("bad", ".txt");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      bw.write("text file");
      bw.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    try {
      BufferedImage bufferedImage = pic.getImageFromFile(file);
      assertEquals(bufferedImage, null);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (file.isFile() && !file.delete()) {
        // failed to delete the (existing) file
      }
    }
  }

  @Test
  public void getImageFromNonExistentFileTest() {
    Pic pic = new Pic();
    File file = null;
    try {
      file = File.createTempFile("bad", ".txt");
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } finally {
      if (file.isFile() && !file.delete()) {
        // failed to delete the (existing) file
      }
    }
    try {
      BufferedImage bufferedImage = pic.getImageFromFile(file);
      assertEquals(bufferedImage, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void getImageTest() {
    Pic pic = new Pic();
    BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] inBytes;
    byte[] outBytes;

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);

      ImageIO.write(bufferedImage, "jpg", baos);
      inBytes = baos.toByteArray();

      pic.setRootFolder(file.getParentFile().getCanonicalPath());
      pic.setFileName(file.getName());
      outBytes = pic.getImage();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } finally {
      if (file.isFile() && !file.delete()) {
        // failed to delete the (existing) file
      }
    }

    Assert.assertArrayEquals(inBytes, outBytes);
  }

  @Test
  public void getImageThumbTest() {
    final int imgWidth = 200;
    final int imgHeight = 300;
    Pic pic = new Pic();
    BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);
    File thumbFile;
    BufferedImage thumbImage;
    String thumbDirectory = "";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] inBytes;
    byte[] outBytes;

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);
      pic.setRootFolder(file.getParentFile().getCanonicalPath());
      pic.setFileName(file.getName());
      thumbDirectory = pic.getRootFolder() + File.separatorChar + pic.THUMB_DIR;
      pic.setThumb();
      thumbFile = new File(thumbDirectory, pic.getFileNameThumb());
      thumbImage = pic.getImageFromFile(thumbFile);
      ImageIO.write(thumbImage, "jpg", baos);
      inBytes = baos.toByteArray();

      outBytes = pic.getImageThumb();

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } finally {
      //clean up files and thumb folder
      if (file.isFile()) {
        file.delete();
        Path pathThumb = Paths.get(thumbDirectory);
        File fThumb = new File(pathThumb.toString(), pic.getFileNameThumb());
        if (fThumb.isFile()) {
          fThumb.delete();
        }
        File thumbFolder = new File(fThumb.getParent());
        if (thumbFolder.isDirectory()) {
          thumbFolder.delete();
        }
      }
    }

    Assert.assertArrayEquals(inBytes, outBytes);
  }

  @Test
  public void gettersSettersTest() {
    Pic pic = new Pic();
    Date dte = new Date();

    pic.setId(1);
    pic.setEntityType("test");
    pic.setEntityId(2);
    pic.setTitle("test title");
    pic.setDateTaken(dte);
    pic.setRootFolder("folder");
    pic.setFileName("test.jpg");

    assertEquals(pic.getId(), 1);
    assertEquals(pic.getEntityId(), 2);
    assertEquals(pic.getEntityType(), "test");
    assertEquals(pic.getTitle(), "test title");
    assertEquals(pic.getDateTaken(), dte);
    assertEquals(pic.getRootFolder(), "folder");
    assertEquals(pic.getFileName(), "test.jpg");
  }
}
