package com.bonsainet.bonsai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
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


}
