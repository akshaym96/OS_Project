/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 * 
 * This class is used when files are created by the user to store the addresses to which
 * files points.There are only used by the files.
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class DataBlock extends Block {
   protected byte[] data;
   protected int numBytes;
   protected static int NUM_BYTES = 20;
/*
 * Changes the block into datablock
 */
   public DataBlock(int bNo)
   {
     super(bNo);
     data = new byte[NUM_BYTES];
     numBytes = 0;
   }
/*
 * Changes the block into datablock
 */
   public DataBlock(int bNo, boolean isFree)
   {
     super(bNo,isFree);
     data = new byte[NUM_BYTES];
     numBytes = 0;
   }
   /*
    * Checks if the dataBlock is full or not
    */
   public boolean isFull()
   {
      return (numBytes == NUM_BYTES);
   }
   /*
    * This functions returns the byte array requested by the 
    * reading of a file by the user
    */
   public byte[] getBytes()
   {
      if (numBytes == 0)
         return null;
      else 
      {   
         byte[] result = new byte[numBytes];
         for(int i=0; i<numBytes; i++)
            result[i] = data[i];
         return result;
      }      
   }
   /*
    * This functions sets the data block free by setting a attribute
    * isFree to true
    */
   public void setFree()
   {
     numBytes = 0;
     isFree = true;
   }
}