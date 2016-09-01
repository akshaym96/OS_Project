/**
 * Created by M.Akshay and V.Shravani on 17th April 2016.
 * 
 * Inode implementation of file system
 *
 * This class initializes the creation of our Virtual File System.
 * This is the main class which takes input from the user processes the input
 * and then redirects each input to a particular function required by the user if the 
 * command entered by the user is valid or prompts as wrong.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

class FileSystem {
  
	
	/*We have created three kinds blocks
	 * 1.Data Block
	 * 2.Address Block
	 * 3.Directory Block 
	 * 4.Inode Block
	 * 
	 * Every kind of block which is mentioned above is
	 * inherited from Block class.
	 */
		
      static int max_levels = 50; 
    /* Indicates the max directory level you can enter
     * For example, if you take max_levels=5.
     * You can go upto >/a/b/c/d/, you can't go more than this.
     * 
     */
      
      static String[] currentWorkingDirectory;
      /*   
       * This array stores the whole till your current working 
       * directory.
       * 
       */
      static int numLevels; 
      /*This shows the current level you are working 
       * in.For example, >/a/b/c/d is 5.It is kind
       * of offset.
       */
      static Inode[] inodePath;
      /*This array stores all the addressess of data blocks or
       * directory blocks.
       */
      static Inode inodeOfCurrentWorkingDirectory;
      /*
       * This is current Inode of your working
       * directory.
       */
      static DirectoryBlock[] directoryBlockPath;
      /*
       * This is a directoryBLock path.
       * 
       */
      
      static DirectoryBlock currentWorkingDirectoryDirNode;
      /*
       * This is current DirectoryBlock of your working 
       * directory.
       * 
       */
      static RandomAccessFile myvirtualFS;
      /*
       * For creating a Virtual Disk.
       */
      public static void main(String[] args) throws IOException
      {
         String commandLine;
 
         System.out.println("Enter the size of the virtual disk you want to create(in MB):-");
         /*
          * Asking user for the size of virtual disk(in MB)
          */
         
         Scanner sc =new Scanner(System.in);
         int numMB=sc.nextInt();
         
         System.out.println("Number of MB's you have entered is "+numMB);
         
         
         byte[] data = new byte[numMB*1024*1024];
         /*
          * Creating a byte array to initialize the 
          * virtual disk with NULL values.
          * As the input is in bytes converting this into
          * MB multiply by 1024*1024
          */
                  
         myvirtualFS =new  RandomAccessFile("MYDISK", "rw");
         /*
          * Creating a file by name "MYDISK" with read,write access
          * which will act as our virtual disk.
          */
         myvirtualFS.write(data);  
         /*
          * Writing the byte array into the virtual disk
          */
                 
         try {
        	 /*
        	  * Initialising our VFS 
        	  */
           currentWorkingDirectory = new String[max_levels];
           /*
            * As mentioned above can't go more than max_levels.
            */
           numLevels = 0;
           System.out.print("Type ");
           System.out.print( "\"help\"");
           System.out.println(" for more commands");
           Block.init();
           /*
            * Initializes file system and creates the root directory
            */
           currentWorkingDirectoryDirNode = Block.rootDirectoryBlock; 
           inodePath = new Inode[max_levels+1];
           inodePath[0] = Block.rootInode;
           directoryBlockPath = new DirectoryBlock[max_levels+1];
           directoryBlockPath[0] = Block.rootDirectoryBlock; 
           /*
            * Creating a Root Inode and Root directoryBlock
            */
           
           BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
           while (true)
           {            
               /*
                * Prints the working directory
                */
               System.out.print("akshay@shravani");  
               printWorkingDirectory();
               System.out.print(">");
               commandLine = br.readLine();           
               processCommand(commandLine);               
          }
        }        
        catch(IOException e)
        {
           System.out.println(e);
           System.exit(1);
        }             
      }
      /*
       * This function tells the user about the commands we are providing to our
       * Virtual File System.
       */
      public static void help()
      {
    	 System.out.println("|=======================================================================================|"); 
         System.out.println("|Command                    Description                                                 |");
         System.out.println("========================================================================================|");
         System.out.println("|help                       Displays commands and their description                     |");       
         System.out.println("|touch filename             Create empty file filename in the current working directory |");      
         System.out.println("|cat filename               Display contents of file filename                           |");
         System.out.println("|rm filename                Delete file filename                                        |"); 
         System.out.println("|mkdir dirname              Create firectory dirname                                    |");
         System.out.println("|ls                         List files in the current working directory                 |");   
         System.out.println("|cd dirname                 Changes to subdirectory dirname                             |");
         System.out.println("|cd..                       Changes to parent directory                                 |");
         System.out.println("|exit                       Terminates the program                                      |");
         System.out.println("|=======================================================================================|");
         
      }
      /*
       * This directs the input to particular function required by the user
       */
      public static void processCommand(String line)
      {
         StringTokenizer st = new StringTokenizer(line);
         String [] parameters = new String[st.countTokens()-1];
         String commandName = st.nextToken();
         int count = 0;
         while (st.hasMoreElements()) 
         {
            parameters[count++] = st.nextToken();                
         } 
         if (commandName.equals("help"))              	  		//Redirects to help function
            help();                                       
        
         else if (commandName.equals("touch"))          		//Redirects to "touch" which creates a file
         {
            if (count == 1)
                createFile(parameters[0]);
            else System.out.println("Usage: touch filename");   //Error in the niptu   
         }   
         else if (commandName.equals("cat"))                   //To display contents of the file
         {
           if (count == 1)
        	   showFile(parameters[0]);
           else System.out.println("Usage: cat filename");     //Error in the input
         }
         else if (commandName.equals("rm"))						//Removing a file
        {
           if (count == 1)
              deleteFile(parameters[0]);
           else System.out.println("Usage: rm filename");        //Error in the input
         }
         else if (commandName.equals("mkdir"))					//Making a directory
         {
           if (count == 1)
              createDirectory(parameters[0]);
           else System.out.println("Usage: mkdir dirname");   	 //Error in the input
         }
         else if (commandName.equals("ls"))					     //Listing files in the present directory
         {
           if (count == 0)
              listDirectory();
           else System.out.println("Usage: ls");                  //Error in the input
         }
         else if (commandName.equals("cd"))                      //To enter into another directory
         {
           if (count == 1)
              changeWorkingDirectory(parameters[0]);
           else System.out.println("Usage: cd dirname");          //Error in input
         }                                 
         else if (commandName.equals("cd.."))                       //Moving up the directory
         {
           if (count == 0)
              changeWorkingDirectoryToParent();
           else System.out.println("Usage: cd..");                   //Error in the input
         }                                      
         else if (commandName.equals("exit"))                      //Exiting the command line
         {
           if (count == 0)
              System.exit(1);
           else System.out.println("Usage: exit");   			  //Error in the input
         }
         else 
            System.out.println("No such command please type help to know more");
         /*
          * Telling the user to enter a valid command or provided functionality
          */
            System.out.println();
      }
      
      
      /*
       * Prints the path from root
       */
      public static void printWorkingDirectory()
      {
         for(int i=0; i<numLevels; i++)         
            System.out.print("/"+currentWorkingDirectory[i]);            
      }
      /*
       * Function to create a file
       */
       public static void createFile(String filename)
      {
         if (currentWorkingDirectoryDirNode.getInode(filename) != -1)
         {
            System.out.println("File Already Exists!");
            return;
         }
         int i = Block.newInode();
         if (i != -1)
         {
            currentWorkingDirectoryDirNode.addEntry(filename,i, "Regular file");  
            Inode inode = (Inode)Block.getBlock(i);
            int j = Block.newDataBlock();
            if (j != -1)
               inode.addBlock(j);
            else System.out.println("Cannot create file: no available data block");
           // j is the block number -- offset
            try {
            	System.out.println(j);
            	myvirtualFS.seek(j);
            	System.out.println("Enter something to enter in to a file:");
            	Scanner sc= new Scanner(System.in);
            	String a=sc.nextLine();
            	
            	inode.setsize_of_block(a.length());
				myvirtualFS.writeChars(a);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
           
         }
         else {
            System.out.println("Cannot create file: Maximum file limit reached!");
         }
      }      
       /*
        * Function to move up the parent(cd..)
        */
       public static void changeWorkingDirectoryToParent()
       {
          if (numLevels > 0)
          {
              numLevels--;
              inodeOfCurrentWorkingDirectory = inodePath[numLevels];
              currentWorkingDirectoryDirNode = directoryBlockPath[numLevels];              
          }
       } 
      /*
       * Function to show content inside the file mentioned(more)
       */
       public static void showFile(String file) 
       {
          BufferedReader standardInput = new BufferedReader(new InputStreamReader(System.in));
          int i = currentWorkingDirectoryDirNode.getInode(file);
          if (i != -1)
          {
             Inode inode = (Inode)Block.getBlock(i);
             if (inode.isDirectory())
             {
                System.out.println(file+" is a directory!");
                return;
             }
             int[] blocks = inode.getBlockAddresses();
             int length=inode.getsize_of_block();
             for(int count = 0; count<blocks.length; count++)
             {
   
             try {
             		
 					myvirtualFS.seek(blocks[count]);
 				} catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				}
 		            try {
 		            	//System.out.println(b.length);
 		            	for(int c=0;c<length;c++) //I can use this loop if i know the block size corresponding to blocknumber blocks[count]
 						System.out.print("" + myvirtualFS.readChar());
 					} catch (IOException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
             }
              /* DataBlock datablock = (DataBlock)Block.getBlock(blocks[count]);
                byte[] b = datablock.getBytes();
                if (b != null)
                {
                   if(currentWorkingDirectoryDirNode.getType(file).equals("SymLink")){                     
                      showFile(new String(b));
                       
                     }
                     else{
                    System.out.print(new String(b));
                 }
                } 
             */
          }
          else System.out.println("No such file");
       }
      /*
       * Function to delete a file(rm)
       */
      public static void deleteFile(String file)
      {
        int i = currentWorkingDirectoryDirNode.getInode(file);
        if (i != -1)
        {
            Inode inode = (Inode)Block.getBlock(i);
            if (inode.isDirectory())
            {
               System.out.println(file+" is a directory!");
               return;
            }   
            if (inode.getLinkCount() <= 1){
            int[] blocks = inode.getBlockAddresses();
            for(int j=0; j<blocks.length; j++)
                  ((DataBlock)Block.getBlock(blocks[j])).setFree();
            int d = inode.getSingleIndirectBlockNo();   
            if (d != -1)
            {
               AddressBlock ab = ((AddressBlock)Block.getBlock(d));
               int[] addresses = ab.getAddresses();
               for(int j=0; j<addresses.length; j++)
                  ((DataBlock)Block.getBlock(addresses[j])).setFree();  
               ab.setFree();            
            }   
            inode.setFree();
            currentWorkingDirectoryDirNode.removeEntry(file,i);
           }
           
           else{
               inode.setLinkCount(inode.getLinkCount() -1);
               currentWorkingDirectoryDirNode.removeEntry(file,i);
            }
        }
        else System.out.println("No such file!");
      }
      
       /*
        * Function for creating a directory (mkdir)
        */
      public static void createDirectory(String file)
      {
         int i = currentWorkingDirectoryDirNode.getInode(file);
         if (i != -1)
             System.out.println("File exists!");
         else {
            i = Block.newInode();
            if (i == -1)
               System.out.println("Maximum file number reached!");
            else {
                currentWorkingDirectoryDirNode.addEntry(file,i, "Directory");
                Inode inode = (Inode)Block.getBlock(i);
                inode.setDirectory();
                int j = Block.newDirectoryBlock();
                if (j == -1)
                   System.out.println("Maximum file number reached!");
                else {
                   inode.addBlock(j);                   
                }   
            } 
         }    
      }
      /*
       * Function to files and directories in the current directory(ls)
       */
      public static void listDirectory() 
      {
         String[] names = currentWorkingDirectoryDirNode.getNames();
         for(int i=0; i<names.length; i++)
            System.out.println(names[i]);
      }
      /*
       * Function to change the working direvtory(cd )
       */
      public static void changeWorkingDirectory(String file)
      {
         int i = currentWorkingDirectoryDirNode.getInode(file);
         if(currentWorkingDirectoryDirNode.getType(file).equals("SymLink")){
               int s = currentWorkingDirectoryDirNode.getInode(file);
               Inode inode = (Inode)Block.getBlock(s);
               int[] blocks = inode.getBlockAddresses();
               byte[] b1 = null;
               for(int count = 0; count<blocks.length; count++)
            {
              DataBlock datablock1 = (DataBlock)Block.getBlock(blocks[count]);
              b1 = datablock1.getBytes();
               
            }
            changeWorkingDirectory(new String(b1));
            return;
            }
         if (i == -1)
            System.out.println("No such file !");
         else {
            Inode inode = (Inode)Block.getBlock(i);
            if (!inode.isDirectory())
               System.out.println("Not a directory!");
            else {
                inodeOfCurrentWorkingDirectory = inode;
                int[] b = inode.getBlockAddresses();
                currentWorkingDirectoryDirNode = (DirectoryBlock)Block.getBlock(b[0]); 
                currentWorkingDirectory[numLevels++] = file;
                inodePath[numLevels] = inodeOfCurrentWorkingDirectory;
                directoryBlockPath[numLevels] = currentWorkingDirectoryDirNode;
            }   
         }   
      }
    
         
}
