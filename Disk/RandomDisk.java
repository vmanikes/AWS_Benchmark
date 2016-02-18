import java.io.*;

public class RandomDisk {
    public static void main(String[] args) {
        int[] blockSize = {1,1024,1048576};
        //Passing blocksize as parameters for sequential read and write
        for(int i=0;i<blockSize.length;i++){
            System.out.println("***********BENCHMARK FOR BLOCK SIZE: "+blockSize[i]+" for 1-thread & 2-thread respectively************");
            RandomDiskThread.bufferLengthRandom = blockSize[i];
            RandomDiskThread.calculateReadAndWriteRandom();
            System.out.println("*****************************************************************************************");
        }
    }    
}

class RandomDiskThread extends Thread{
    public static long bufferLengthRandom,writeTimeRandom,readTimeRandom;
    public static float latencyRandom;
    public static void calculateReadAndWriteRandom(){
        RandomDiskThread[] randomThreads=new RandomDiskThread[4];
        //Passing threads ine by one to run the run() method
        int[] threadCount={1,2};
        for(int i=0;i<threadCount.length;i++){
            randomThreads[i]=new RandomDiskThread();
            randomThreads[i].start(); 
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void run(){
        try {
            randomWrite(RandomDiskThread.bufferLengthRandom);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void randomWrite(long bufferLength) throws Exception{
        //Creating a new file
        File file=new File("Disk_ExperimentRandom.txt");
        //By using the RandomAccessFile Class we any can read/write to a file at a random location using the seek() method
        RandomAccessFile randomAccessFile=new RandomAccessFile(file, "rw");     //[1]
        randomAccessFile.seek(file.length());
        //Writing some data to the file initially
        for(int k=0;k<bufferLength;k++){
            randomAccessFile.write('b');
        }
        //writing to a file ata a random location
        long start=System.nanoTime();
        for(int i=0;i<bufferLength;i++){
            randomAccessFile.seek(Math.abs(bufferLength-(file.length()+i)));    //[1]
            randomAccessFile.write('k');                                        //[3]
        }
        double end=System.nanoTime()-start;
        randomAccessFile.close();  
        //calculating the latency
        double writeLatency=(double)end/1000000;
        System.out.println("Latency for random write: "+writeLatency+"ms");
        //Throughput calculation in Mbps
        double writeTimeSeconds=(double)end/1000000000;
        double writeRanThroughput=bufferLength/(1048576*writeTimeSeconds);
        System.out.println("Random Write Throughput: "+writeRanThroughput+" Mbps");
        System.out.println("\n");
        randomRead(bufferLength);
    }
    
    public void randomRead(long bufferLength) throws Exception{
        //Creating a new file
        File file=new File("Disk_ExperimentRandom.txt");
        //As it is a read operation I am using only read mode for the file
        RandomAccessFile randomAccessFile=new RandomAccessFile(file, "r"); 
        //Using byte to read data bit by bit
        byte[] dataStorer=new byte[10];
        long start=System.nanoTime();
        //calculating the random read time
        for(long i=0;i<bufferLengthRandom;i++){
            randomAccessFile.seek(Math.abs(bufferLength-(file.length()+i)));    //[1]
            randomAccessFile.read(dataStorer);                                  //[4]
        }
        long end=System.nanoTime()-start;
        //calculating latency
        double readLatency=(double)end/1000000;
        randomAccessFile.close();
        System.out.println("Latency for random read: "+readLatency+"ms");
        //Throughput Calculation in Mbps
        double readTimeSeconds=(double)end/1000000000;
        double readRanThroughput=bufferLength/(1048576*readTimeSeconds);
        System.out.println("Random read Throughput: "+readRanThroughput+" Mbps");
        System.out.println("\n");
        
    }    
}



/*REFERENCES
    [1]. http://javarevisited.blogspot.com/2015/02/randomaccessfile-example-in-java-read-write-String.html
    [2]. https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html#write(byte[])
    [3]. https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html#read()
*/
