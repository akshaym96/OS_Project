/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 * 
 * This is the class where inodes are created.
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Inode extends Block {
   protected int inodeNo;
   protected boolean isDirectory;
   protected int blockAddress[];
   protected int numBlockAddresses;
   public int size_of_block;
   protected int singleIndirectAddress;
   protected int linkCount;
   protected static int NUM_DIRECT_ADDRESSES = 5;
   
   /*      Direct Table entry
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
    * -----------------------------
    * |             5             |
    * -----------------------------
    * 
    */
   
   /*
    * This function which initializes attributes to the inode 
    * Block.
    */
   
   
   
   public Inode(int bNo, boolean isFree)
   {
     super(bNo,isFree);
     inodeNo = -1;
     numBlockAddresses = 0;
     blockAddress = new int[NUM_DIRECT_ADDRESSES];
     singleIndirectAddress = -1;
     linkCount = 1;
     isDirectory = false;
   }
   public void setsize_of_block(int sizeofblock)
   {
	   size_of_block=sizeofblock;
   }
   public int getsize_of_block()
   {
      return size_of_block;
   }
   /*
    * This function which initializes attributes to the inode 
    * Block.
    */     
   public Inode(int bNo, int inodeNo, boolean isFree)
   {
     super(bNo,isFree); 
     this.inodeNo = inodeNo;
     numBlockAddresses = 0;
     blockAddress = new int[NUM_DIRECT_ADDRESSES];
     singleIndirectAddress = -1;
     linkCount = 1;
     isDirectory = false;
   }
   /*
    * This function returns the link count of the inode.
    * 
    */
   public int getLinkCount()
   {
      return linkCount;
   }
   
   /*
    * This functions set the link count of Inode to linkCount.
    */
   public void setLinkCount(int linkCount)
   {
      this.linkCount = linkCount;
   }
   /*
    * This function returns the inodeNO.
    */
   public int getInodeNode()
   {
      return inodeNo;
   }
   /*
    * This functions sets a number to the inode
    * by inodeNo
    */
   public void setInodeNo(int inodeNo)
   {
      this.inodeNo = inodeNo;
   }
   /*
    * This function adds a block to the inode
    */
   public boolean addBlock(int bNo)
   {
      if (numBlockAddresses < NUM_DIRECT_ADDRESSES)
      {
         blockAddress[numBlockAddresses++] = bNo;
         return true; 
      }   
      else if (singleIndirectAddress !=-1)
      {
         AddressBlock a = (AddressBlock)Block.getBlock(singleIndirectAddress);
         return a.addAddress(bNo);                 
      }    
      else {
         singleIndirectAddress = Block.newAddressBlock();
         AddressBlock a = (AddressBlock)Block.getBlock(singleIndirectAddress);
         return a.addAddress(bNo);
      }
   }
   /*
    * Function returns the block addresses
    */
   public int[] getBlockAddresses()
   {
      if (numBlockAddresses == 0 && singleIndirectAddress == -1)
         return null;
      else if (singleIndirectAddress == -1)
      {
         int[] a = new int[numBlockAddresses];
         for(int i=0; i<numBlockAddresses; i++)
            a[i] = blockAddress[i];
         return a;
      }   
      else {
         AddressBlock a = (AddressBlock)Block.getBlock(singleIndirectAddress);
         int[] a1 = a.getAddresses(); 
         int[] addresses = new int[a1.length+numBlockAddresses];         
         int i,j;
         for(j=0; j<numBlockAddresses; j++)
            addresses[j] = blockAddress[j];    
         for(i=0; i<a1.length; i++)
            addresses[j++] = a1[i];
         return addresses;   
      }
         
   }  
   /* Returns number of blocks */   
   public int getSize()
   {
      if (singleIndirectAddress == -1)
         return numBlockAddresses;
      else {
         AddressBlock a = (AddressBlock)Block.getBlock(singleIndirectAddress);
         return numBlockAddresses + a.getNumAddresses();
      }    
   }
   /*
    * This function frees everything requested by the user to remove a file
    */
   public void setFree()
   {
      numBlockAddresses = 0;
      singleIndirectAddress = -1;
      isFree = true;
      isDirectory = false;
   }
      /*
       * This function returns the singleIndirectBlockNo
       */
   public int getSingleIndirectBlockNo()
   {
     return singleIndirectAddress;
   }
  /*
   * This function sets the current Inode to point 
   * to a directoryBlock requested from FileSystem.java
   */
   public void setDirectory()
   {
      isDirectory = true;
   }   
   /*
    * Checks if the current Inode points to a directoryBlock or not.
    */
   public boolean isDirectory()
   {
      return isDirectory;
   }
}