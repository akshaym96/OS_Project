/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 * 
 * This class is used for converting a normal Block into a Address block.
 * This is mainly used when we require single indirect addresses to store large files or
 * if the direct block addresses or full.
 * This is accessed when we need to remove a file which user requires to delete.
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class AddressBlock extends Block {
   protected int[] addresses;
   protected int numAddresses;
   protected static int NUM_ADDRESSES = 32;
   
   /*    Single  Indirect Table.
    * 
    * -----------------------------
    * |             1             |
    * -----------------------------
    * -----------------------------
    * |             2             |
    * -----------------------------
    * -----------------------------
    * |             3             |
    * -----------------------------
    * -----------------------------
    * |             4             |
    * -----------------------------
    *              .
    *              .
    *              .
    *              .
    *              .
    * -----------------------------
    * |             32            |
    * -----------------------------
    */
   /*
    /*
       * This functions converts the block into address block
       */
   
   public AddressBlock(int bNo)
   {
      super(bNo);
      addresses= new int[NUM_ADDRESSES];
      numAddresses = 0;
   }
   /*
    * This functions converts the block into address block
    */
   public AddressBlock(int bNo, boolean isFree)
   {
      super(bNo,isFree);
      addresses= new int[NUM_ADDRESSES];
      numAddresses = 0;
   }
   /*
    * Add a address into the next free address row inside the table shown above
    */
   public boolean addAddress(int address)
   {
      if (numAddresses < NUM_ADDRESSES)
      {
         addresses[numAddresses++] = address;    
         return true;
      }
      return false;     
   }
   /*
    * Returns the addresses array 
    */
   public int[] getAddresses()
   {
      if (numAddresses == 0)
        return null;
      int[] result = new int[numAddresses];
      for(int i=0; i<result.length; i++)
         result[i] = addresses[i];
      return result;   
   }
   /*
    * Checks how many addresses are filled at present
    */
   public int getNumAddresses()
   {
     return numAddresses;
   }
   /*
    * Checks if the whole 32 addresses are full or not
    */
   public boolean isFull()
   {
      return (numAddresses == NUM_ADDRESSES);
   }
/*
 * Clearing the addresses block
 */
   public void setFree()
   {
      numAddresses = 0;
      isFree = true;
   }   
}