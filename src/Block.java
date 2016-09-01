/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 * 
 * This a basic block implementation which has information of number of
 * blocks and number of inodes to be used.
 * This class is used to by AddressBlock,DataBlock,DirectoryBlock classes which
 * convert themselves into their respective blocks.
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class Block {
   protected int blockNo;
   protected boolean isFree;
   protected static Block blocks[];
   protected static int numBlocks;
   public static int NUM_BLOCKS = 7000;
   public static int NUM_INODES = 50; 
   public static DirectoryBlock rootDirectoryBlock;
   public static Inode rootInode;
        /*
         * This function initializes the first 50 blocks as simple free blocks as inodes
         * Then gets the first free block and makes it a rootInode.
         * Similarly the 51st block is made as a directoryBLock 
         * and is assigned as rootDrirectoryBlock
         */
   public static void init()
   {
      blocks = new Block[NUM_BLOCKS];
      for(int i=0; i< NUM_INODES; i++)
         blocks[i]= new Inode(i,i,true);
      numBlocks = NUM_INODES;
      int k = Block.newInode();
      rootInode = (Inode)Block.getBlock(k);
      int j = Block.newDirectoryBlock();
      rootDirectoryBlock = (DirectoryBlock)Block.getBlock(j);
      rootInode.addBlock(rootDirectoryBlock.getBlockNo());
   }  
     /*
      * THis function initializes the number to each block
      * and make it free , this is used while initializing the file system
      */
   public Block(int bNo)
   {
      blockNo = bNo;
      isFree = true;
   }   
   /*
    * THis function initializes the number to each block
    * and make it isFree (can be true or false depending on the input),
    * this is used while initializing the file system
    */
   public Block(int bNo, boolean isFree)
   {
      blockNo = bNo;
      this.isFree = isFree;
   }
/*
 * This function create a new addressBLock and returns the offset
 */
   public static int newAddressBlock()
   {
      if (numBlocks < NUM_BLOCKS)
      {
         blocks[numBlocks] = new AddressBlock(numBlocks,false);
         return numBlocks++;
      }
      else {
        for(int i=NUM_INODES; i<NUM_BLOCKS; i++)
           if (blocks[i].isFree())
           {
              blocks[i] = new AddressBlock(i,false);
              return i;
           }   
      }    
      return -1;
   }   
/*
 * THis functions is used to create a data block which is used the file created by the user
 */
   public static int newDataBlock()
   {
      if (numBlocks < NUM_BLOCKS)
      {
         blocks[numBlocks] = new DataBlock(numBlocks,false);
         return numBlocks++;
      }
      else {
        for(int i=NUM_INODES; i<NUM_BLOCKS; i++)
           if (blocks[i].isFree())
           {
              blocks[i] = new DataBlock(i,false);
              return i;       
           }   
      }   
      return -1;
   }   
/*
 * This function creates a INode and returns a block number
 */
   public static int newInode()
   {
     for(int i=0; i<NUM_INODES; i++)
        if (blocks[i].isFree())
        {
           blocks[i].setNotFree();
           return i;
        }        
     return -1;   
   }
  /*
   * This functions creates a directory block and returns
   */
   public static int newDirectoryBlock()
   {
      if (numBlocks < NUM_BLOCKS)
      {
         blocks[numBlocks] = new DirectoryBlock(numBlocks,false);
         return numBlocks++;
      }
      else {
        for(int i=NUM_INODES; i<NUM_BLOCKS; i++)
           if (blocks[i].isFree())
           {
              blocks[i] = new DirectoryBlock(i,false);
              return i;       
           }   
      }   
      return -1;
   }
   /*
    * returns a Block with given bNo
    */
   public static Block getBlock(int bNo)
   {
      if (bNo < numBlocks)
         return blocks[bNo];
      else return null;   
   }
     /*
      * This function returns a BLockNo 
      */
   public int getBlockNo()
   {
      return blockNo;
   }
   /*
    * This function sets a block no to bNo
    */
   public void setBlockNo(int bNo)
   {
      if (bNo >= 0 && bNo < NUM_BLOCKS)
         blockNo = bNo;
   }
   /*
    * This functions frees a block
    * by setting its isFree attribute to false
    */
   public void setNotFree()
   {
      isFree = false;
   }
   /*
    * This functions checks the attribute og block if it 
    * is free or not.
    */
   public boolean isFree()
   {
     return isFree;
   }  
}