package com.bonsainet.bonsai.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PicTest {

  @Test
  public void setDimensionsFromImageTest() {

    BufferedImage bufferedImage = new BufferedImage(100, 150,
            BufferedImage.TYPE_INT_RGB); // create a BufferedImage object
        // here you can fill in some data so the image isn't blank

    Pic pic = new Pic();

    pic.setDimensionsFromImage(bufferedImage, false);

    assertEquals(pic.getDimX(), 100);
    assertEquals(pic.getDimY(), 150);
  }

  @Test
  public void setThumbDimensionsFromImageTest() {

    BufferedImage bufferedImage = new BufferedImage(100, 150,
        BufferedImage.TYPE_INT_RGB); // create a BufferedImage object
    // here you can fill in some data so the image isn't blank

    Pic pic = new Pic();

    pic.setDimensionsFromImage(bufferedImage, true);

    assertEquals(pic.getDimXThumb(), 100);
    assertEquals(pic.getDimYThumb(), 150);
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
      assertThat(bufferedImage)
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
}
