/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 * 
 * This is the class which is required by the directory creation.
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class DirectoryBlock extends Block {
      protected int[] inodeNo;
      protected String[] name;
      protected String[] type;
      protected int numEntries;
      public static int NUM_ENTRIES = 100;
      /*
       * This functions converts the block into directory block
       */
      public DirectoryBlock(int bNo)
      {
         super(bNo);
         numEntries = 0;
         inodeNo = new int[NUM_ENTRIES];
         name = new String[NUM_ENTRIES];
         type = new String[NUM_ENTRIES];
      }
      /*
       * This functions converts the block into directory block
       */
      public DirectoryBlock(int bNo, boolean isFree)
      {  
         super(bNo,isFree);
         numEntries = 0;
         inodeNo = new int[NUM_ENTRIES];
         name = new String[NUM_ENTRIES];
         type = new String[NUM_ENTRIES];
      }
       /*
        * This function adds an entry into directoyBLock
        * example if a file is created                  
        */
      public boolean addEntry(String name, int iNo, String type)
      {
         if (numEntries < NUM_ENTRIES)
         {
             inodeNo[numEntries] = iNo;
             this.name[numEntries] = name;
             this.type[numEntries] = type;
             numEntries++;
             return true;
         }  
         else return false;
      }                    
     /*
      * This function check if there is file with the same name which
      * is given as parameter
      *  
      */
      public boolean entryExists(String name, int iNo)
      {
         for(int i=0; i<numEntries; i++)
            if (inodeNo[i] == iNo && this.name[i].equals(name))
               return true;
         return false;      
      }               
      /*
       * REtunrs the inode of the given filename
       */
      public int getInode(String name)
      {
         for(int i=0; i<numEntries; i++)
            if (this.name[i].equals(name))
               return inodeNo[i];
         return -1;      
      } 
 /*
  * checks if the entries in block are full or not
  */
      public boolean isFull()
      {
        return (numEntries == NUM_ENTRIES);
      }
      /*
       * this function sets the directory block to "free"
       */
      public void setFree()
      {
        numEntries = 0;
        isFree = true;
      }
      /*
       * Removes the filename entry from the directory block
       */
      public void removeEntry(String file, int iNo)
      {

         int index = -1;
         for(int i=0; i<numEntries; i++)
         {
            if (name[i].equals(file) && iNo == inodeNo[i])
            {
               index = i;
               break;
            }   
         }
         if (index != -1)
         {
            for(int i=index+1; i<numEntries; i++)
            {
               name[i-1] = name[i];
               type[i-1] = type[i];
               inodeNo[i-1] = inodeNo[i];
            }
            numEntries--;   
         }
      }
      /*
       * This functions returns the names of the all the directories
       * from the root 
       * In simple the path is returned.
       */
      public String[] getNames()
      {
         String[] r = new String[numEntries];
         for(int i=0; i<numEntries; i++)
            r[i] = name[i];
         return r;   
      }
      /*
       * Returns if it is a driectory or regular file
       */
      public String getType(String file)
      {
         for(int i=0; i<numEntries; i++)
            if (this.name[i].equals(file))
               return type[i];
         return "error"; 
      }
      
      public int[] getInodes()
      {
         int[] r = new int[numEntries];
         for(int i=0; i<numEntries; i++)
            r[i] = inodeNo[i];
         return r;   
         
      }
      public void print()
      {
         for(int i=0; i<numEntries; i++)
         {
             System.out.println(name[i]+ " "+inodeNo[i]);
         }
      }
}