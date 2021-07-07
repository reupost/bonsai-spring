package com.bonsainet.bonsai.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PicTest {

  File getTempImage(int imgHeight, int imgWidth) {
    BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight,
        BufferedImage.TYPE_3BYTE_BGR);

    File file = null;
    try {
      file = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", file);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return file;
  }

  void removeTempImage(File f, boolean andDeleteThumb) {
    Path pathThumb = null;
    try {
      if (f.isFile()) {
        f.setWritable(true);
        f.delete();
        if (andDeleteThumb)
          pathThumb = Paths
              .get(f.getParentFile().getCanonicalPath() + File.separatorChar + Pic.THUMB_DIR);
        File fThumb = new File(pathThumb.toString(), f.getName());
        if (fThumb.isFile()) {
          try {
            fThumb.setWritable(true);
            fThumb.delete();
          } catch (NullPointerException npe) {
            //do nothing, could be normal
          }
        }
        File thumbFolder = new File(fThumb.getParent());
        if (thumbFolder.isDirectory()) {
          thumbFolder.setWritable(true);
          thumbFolder.delete();
        }
      }
    } catch (Exception e) {
      // fail silently?!
    }
  }

  @Test
  public void setDimensionsTest() {
    Pic pic = new Pic();
    final int imgWidth = 100;
    final int imgHeight = 150;
    File img = getTempImage(imgHeight, imgWidth);
    try {
      pic.setRootFolder(img.getParentFile().getCanonicalPath());
      pic.setFileName(img.getName());
      pic.setDimensions();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      removeTempImage(img, false);
    }

    assertEquals(pic.getDimX(), imgWidth);
    assertEquals(pic.getDimY(), imgHeight);
  }

  @Test
  public void getThumbFromImageTest() {
    Pic pic = new Pic();
    BufferedImage bufferedImage = new BufferedImage(Pic.THUMB_DIM *4, Pic.THUMB_DIM *2,
        BufferedImage.TYPE_INT_RGB); // create a BufferedImage object

    BufferedImage thumb = pic.getThumbFromImage(bufferedImage);
    assertEquals(thumb.getWidth(), Pic.THUMB_DIM);
    assertEquals(thumb.getHeight(), Pic.THUMB_DIM * 2.0/4.0);
  }

  @Test
  public void setThumbTest() {
    Pic pic = new Pic();
    final int imgWidth = 200;
    final int imgHeight = 300;
    File img = getTempImage(imgHeight, imgWidth);

    try {
      pic.setRootFolder(img.getParentFile().getCanonicalPath());
      pic.setFileName(img.getName());
      pic.setThumb();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      removeTempImage(img, true);
    }

    assertThat(pic.getDimXThumb(), lessThanOrEqualTo(Pic.THUMB_DIM));
    assertThat(pic.getDimYThumb(), lessThanOrEqualTo(Pic.THUMB_DIM));
  }

  @Test
  public void setThumbUnchangedTest() {
    Pic pic = new Pic();
    final int imgWidth = 200;
    final int imgHeight = 300;
    File img = getTempImage(imgHeight, imgWidth);

    try {
      pic.setRootFolder(img.getParentFile().getCanonicalPath());
      pic.setFileName(img.getName());
      pic.setThumb();

      assertThat(pic.getImageHash(), equalTo(pic.getMD5HashFromFile(img)));

      // now delete thumb file and ensure it is not recreated (which it would be if re-thumbed)
      Path pathThumb = Paths.get(pic.getRootFolder() + File.separatorChar + Pic.THUMB_DIR);
      File fThumb = new File(pathThumb.toString(), pic.getFileNameThumb());
      if (fThumb.isFile()) {
        fThumb.delete();
      }
      File thumbFolder = new File(fThumb.getParent());
      if (thumbFolder.isDirectory()) {
        thumbFolder.delete();
      }

      pic.setThumb();

      assertThat(fThumb.exists(), is(false));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      removeTempImage(img, true);
    }

    assertThat(pic.getDimXThumb(), lessThanOrEqualTo(Pic.THUMB_DIM));
    assertThat(pic.getDimYThumb(), lessThanOrEqualTo(Pic.THUMB_DIM));
  }

  @Test
  public void settersDoNotExistTest() {
    String[] setMethodsShouldNotExist = {"setFileNameThumb", "setDimX", "setDimY", "setDimXThumb",
        "setDimYThumb", "setImageHash"};
    try {
      Class cls = Class.forName("com.bonsainet.bonsai.model.Pic");
      Method[] m = cls.getMethods();
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

    File img;
    try {
      img = File.createTempFile("tmp", ".jpg");
      ImageIO.write(bufferedImage, "jpg", img);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    try {
      BufferedImage readBufferedImage = pic.getImageFromFile(img);
      org.assertj.core.api.AssertionsForClassTypes.assertThat(bufferedImage)
          .usingRecursiveComparison()
          .isEqualTo(readBufferedImage);
    } finally {
      removeTempImage(img, false);
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
      removeTempImage(file, false);
    }
  }

  @Test
  public void getImageFromNonExistentFileTest() {
    Pic pic = new Pic();
    File img = getTempImage(1, 1);
    removeTempImage(img, false);
    try {
      BufferedImage bufferedImage = pic.getImageFromFile(img);
      assertNull(bufferedImage);
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
      removeTempImage(file, false);
    }

    Assert.assertArrayEquals(inBytes, outBytes);
  }

  @Test
  public void getImageThumbTest() {
    final int imgWidth = 200;
    final int imgHeight = 300;
    Pic pic = new Pic();
    File thumbFile;
    BufferedImage thumbImage;
    String thumbDirectory = "";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] inBytes;
    byte[] outBytes;

    File img = getTempImage(imgHeight, imgWidth);
    try {
      pic.setRootFolder(img.getParentFile().getCanonicalPath());
      pic.setFileName(img.getName());
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
      removeTempImage(img, true);
    }

    Assertions.assertArrayEquals(inBytes, outBytes);
  }

  @Test
  public void supplementWithTest() {
    Pic pic = new Pic();
    pic.setId(1);
    pic.setEntityType("test");

    Pic fromPic = new Pic();
    fromPic.setEntityId(1);
    fromPic.setId(2);
    fromPic.setView("front");
    File img = getTempImage(1, 1);
    try {
      fromPic.setRootFolder(img.getParentFile().getCanonicalPath());
      fromPic.setFileName(img.getName());
      fromPic.setThumb(); //sets thumbDimX and Y
    } catch (IOException ioe) {
      ioe.printStackTrace();
      assert false;
    }

    try {
      pic.supplementWith(fromPic);
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
      assert false;
    }
    assertEquals(pic.getId(), 1); //not overwritten
    assertEquals(pic.getEntityType(), "test"); //not replaced with null
    assertEquals(pic.getFileName(), fromPic.getFileName()); //supplemented
    assertEquals(pic.getEntityId(), fromPic.getEntityId()); //supplemented
    assertEquals(pic.getView(), fromPic.getView()); //supplemented
    assertNull(pic.getDateTaken()); //not set
    assertEquals(pic.getDimXThumb(), fromPic.getDimXThumb()); //set even though not settable outside of class

    removeTempImage(img, true);
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
    pic.setView("front");

    assertEquals(pic.getId(), 1);
    assertEquals(pic.getEntityId(), 2);
    assertEquals(pic.getEntityType(), "test");
    assertEquals(pic.getTitle(), "test title");
    assertEquals(pic.getDateTaken(), dte);
    assertEquals(pic.getRootFolder(), "folder");
    assertEquals(pic.getFileName(), "test.jpg");
    assertEquals(pic.getView(), "front");
  }
}
