import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CPU {

    public static void main(String[] args) throws Exception{    
        int ThreadCount=0;  
        System.out.println("**************CPU BENCHMARKING************");
        //Getting the number of threads as an input such as 1,2,4
        System.out.println("Enter the Thread Count: ");
        Scanner reader=new Scanner(System.in);
        ThreadCount=reader.nextInt();
        //Theoritical Performance
        System.out.println("*******THEORITICAL PERFORMANCE************");
        Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /proc/cpuinfo | grep processor | wc -l"});       //[1]
        Process q = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /proc/cpuinfo | grep 'GHz'"});                   //[1]
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String core = stdInput.readLine();
        int Totalcores = Integer.parseInt(core);
        stdInput = new BufferedReader(new InputStreamReader(q.getInputStream()));
        String speed = stdInput.readLine();                                                     //[3]
        speed = (String) speed.subSequence(speed.length()-7, speed.length()-3);
        float Ghz= Float.parseFloat(speed);
        int Ipc=4;
        System.out.println("Theoritical Performance :"+Ghz*Totalcores*Ipc+" GFLOPS"+"\n");
        System.out.println("**************************************");
        //Getting to make multiple threads execute concurrently
         ExecutorService executorService = null;
        for(int id=1;id<=ThreadCount;id++){
            executorService=Executors.newFixedThreadPool(ThreadCount);              //[2]
            executorService.submit(new ThreadRunnable(id));
        }
        executorService.shutdown();
    }
}


/*REFERENCES
    [1]. http://stackoverflow.com/questions/26830617/java-running-bash-commands
    [2]. http://tutorials.jenkov.com/java-util-concurrent/executorservice.html
    [3]. http://stackoverflow.com/questions/14411173/java-multiple-lines-from-stdin
*/
