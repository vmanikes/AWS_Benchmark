public class ThreadRunnable implements Runnable{

    int id;
    public ThreadRunnable(int id){
        this.id=id;
    }
    
    @Override
    public void run() {
         benchmarkCalculation(id);
    }
    
    public void benchmarkCalculation(int id){
        calculateFlops(id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        calculateIops(id);
    }
    
    public int intRandom(){                                     //[1]
        int integerRandom = (int) (Math.random() * (100 - 10)) + 10;
        return integerRandom;
    }

    public void calculateFlops(int id){
        System.out.println("\n");
        float a=236.54f;
        float b=121.22f;
        float c=34.67f;
        float d=45.67f;
        float e=56.43f;
        //Calculate the time for which the whole operation is run
        long start=System.currentTimeMillis();                  //[2]
        for(long i=0;i<Integer.MAX_VALUE;i++){
            a=b+c;
            c=d+e;
            b=c-d;
            
        }
        long end=System.currentTimeMillis()-start;
        //Calculating Gflops
        double duration=end/1000;
        double GFLOP=((Integer.MAX_VALUE)/duration)/1000000000;
        System.out.println("Calculating FLOPS benchmark in thread: "+id+".Time is:"+GFLOP*9.0);
    } 
    
    public void calculateIops(int id){
        System.out.println("\n");
        int a=455;
        int b=553;
        int c=242;
        int d=331;
        int e=442;
        //Calculate the time for which the whole operation is run
        long start1=System.currentTimeMillis();                 //[2]
        for(long i=0;i<Integer.MAX_VALUE;i++){
            a=b+c;
            c=d+e;
            b=c-d;
        }
        long end1=System.currentTimeMillis()-start1;
        //Calculating Giops
        double duration1=end1/1000;
        double GIOPS=((Integer.MAX_VALUE)/duration1)/1000000000;
        System.out.println("Calculating IOPS benchmark in thread:"+id+". Time is:"+GIOPS*9.0);
    }    
}

/*REFERENCES
    [1]. http://stackoverflow.com/questions/5271598/java-generate-random-number-between-two-given-values
    [2]. http://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime
*/
