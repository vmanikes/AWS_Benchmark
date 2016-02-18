import java.io.*;

public class SequentialDisk {
    public static void main(String[] args) {
        //Passing blocksize as parameters for sequential read and write
        int[] blockSize = {1,1024,1048576};
        for(int i=0;i<blockSize.length;i++){
            System.out.println("***********BENCHMARK FOR BLOCK SIZE: "+blockSize[i]+" for 1-thread & 2-thread respectively************");
            SequentialThread.bufferLength = blockSize[i];
            SequentialThread.calculateReadAndWrite();
            System.out.println("*****************************************************************************************");
        }
        //Alos calling the Random read and write method
        RandomDisk.main(null);
    }
}
    
class SequentialThread extends Thread{
    public static long bufferLength,writeTime,readTime;
    public static float latencySequential;    
    public static void calculateReadAndWrite(){
        SequentialThread[] sequentialThreads=new SequentialThread[2];
        //Passing threads ine by one to run the run() method
        int[] threadCount={1,2};
            for(int i=0;i<threadCount.length;i++){
                sequentialThreads[i]=new SequentialThread();
                sequentialThreads[i].start();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
    
    @Override
    public void run() {
        try{
        sequentialWrite(SequentialThread.bufferLength);       
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Method to write sequentially to a file
    public void sequentialWrite(long bufferLength) throws Exception{
        try { 
            //I will send data  byte by byte into the file
            String appender="k";
            //String Buffer will hold the strea of data I will be writing to it 
            //I will be using StringBuffer instead of StringBuilder as String Buffer is thread safe
            StringBuffer stringBuffer=new StringBuffer();                   //[1]
            for(int i=0;i<bufferLength;i++){
                stringBuffer.append(appender);
            }
            //Creating a new file
            File file=new File("Disk_Experiment.txt");                                 //[2]
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
            long startTime=System.nanoTime();                                          //[5]
            bufferedWriter.write(stringBuffer.toString());
            long endTime=System.nanoTime()-startTime;
            SequentialThread.writeTime=endTime;
            double writeLatency=(double)endTime/1000000;
            System.out.println("Latency for sequential write: "+writeLatency+"ms");
            
            //Throughput
            double writeTimeSeconds=(double)endTime/1000000000;
            double writeSeqThroughput=bufferLength/(1048576*writeTimeSeconds);
            System.out.println("Sequential Write Throughput: "+writeSeqThroughput+" Mbps");
            System.out.println("\n");
            bufferedWriter.close();
            sequentialRead(); 
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    //Method to sequentially read the file
    public void sequentialRead() throws Exception{
        try {
            File file=new File("Disk_Experiment.txt");
            FileInputStream fileInputStream=new FileInputStream(file);      //[3]
            byte[] dataStorer=new byte[(int)file.length()];                 //[4]
            long startTime=System.nanoTime();
            fileInputStream.read(dataStorer);
            long endTime=System.nanoTime()-startTime;                       //[5]
            SequentialThread.readTime=endTime;
            double readLatency=(double)endTime/1000000;
            System.out.println("Latency for sequential read: "+ readLatency+"ms");
            
            //Throughput
            double readTimeSeconds=(double)endTime/1000000000;
            double readSeqThroughput=bufferLength/(1048576*readTimeSeconds);
            System.out.println("Sequential read Throughput: "+readSeqThroughput+" Mbps");
            System.out.println("\n");
            fileInputStream.close();            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}

/*REFERENCES
    [1]. http://www.tutorialspoint.com/java/java_string_buffer.htm
    [2]. http://stackoverflow.com/questions/2885173/how-to-create-a-file-and-write-to-a-file-in-java
    [3]. https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
    [4]. http://howtodoinjava.com/core-java/io/how-to-read-file-content-into-byte-array-in-java/
    [5]. http://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime
*/




