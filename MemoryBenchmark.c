#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>
#include <math.h>
#include <string.h>
#include <pthread.h>
void *seqMemoryAccess(void *object);
void *randMemoryAccess(void *object);
int blockCheck(int);
int main() {
	srand((int)time(0));
	int threadCount = 0,i=0;
	struct timeval start, end,start1,end1;  
	for (threadCount = 1; threadCount <= 2; threadCount++) {
		int blockSize[]={1,1024,1048576};
		//Loop for assigning blocks of byte,kilo bytea and Mega byte
		for (i= 0; i< 3; i++) {
			int block=blockSize[i];
			int check=0,blockk=blockCheck(block);
			//Initialising the clocks
			gettimeofday(&start, NULL);			//[1]  
			pthread_t totalThreadsRandom[threadCount];
			//Starting the clock and creating the threads		
			for (check = 0; check < threadCount; check++)
				pthread_create(&totalThreadsRandom[check], NULL,randMemoryAccess, &block);	//[3]
			for (check = 0; check < threadCount; check++)
				pthread_join(totalThreadsRandom[check], NULL);					//[3]
			gettimeofday(&end, NULL);
			//calculating the latency and throughput                                                                          
			double executionTime = (1000.0*(end.tv_sec - start.tv_sec) + (end.tv_usec -start.tv_usec)/1000.0)/blockk;							
			double throughput = (threadCount * block / (1048576.0)) / (executionTime/1000.0);
			printf("*********************************************\n");
			printf("\nNumber of threads in action: %d",threadCount);
			printf("\nRandom Access on memory: %9d bytes",block);
 			printf("\nLatency : %9f ms\n", executionTime);
			printf("Throughput : %9f MB/sec\n",throughput);
			printf("*********************************************\n");
			
			//Initialising the clocks
			//Starting the clock and creating the threads
			pthread_t totalThreadsSequence[threadCount];
			gettimeofday(&start1, NULL);			//[1]
			//Starting the clock and creating the threads
			for (check = 0; check < threadCount; check++)
				pthread_create(&totalThreadsSequence[check], NULL, seqMemoryAccess, &block);	//[3]
			for (check = 0;check < threadCount;check++)
				pthread_join(totalThreadsSequence[check], NULL);				//[3]
			gettimeofday(&end1, NULL);
			//calculating the latency and throughput                            
			double executionTime1 = (1000.0*(end1.tv_sec - start1.tv_sec) + (end1.tv_usec -start1.tv_usec)/1000.0)/blockk; 
			double throughput1 = (threadCount * block / (1048576.0)) / (executionTime1/1000.0);
			printf("*********************************************\n");
			printf("Number of threads in action: %d",threadCount); 
			printf("\nSequential Access on memory: %9d bytes",block);
			printf("\nLatency : %9f ms",executionTime1);
			printf("\nThroughput : %9f MB/sec\n",throughput1);
			printf("*********************************************\n");
		}
	}
}
void *randMemoryAccess(void *object) {
	int memory=5242880;	//Declaring a 5mb 
	int *temp=(int*)object,i=0,loop=1000000;
	char *mem1=(char *)malloc(memory);	//[4]
	char *mem2=(char *)malloc(memory);	//[4]
	while(i<loop){
		long random=rand()%(memory- *temp);	//[7]
		memcpy(mem2+random,mem1+random,*temp);		//[6]
		i++;	
	}
	free(mem1);
	free(mem2);	
}

void *seqMemoryAccess(void *object) {
	int *memory=(int*)object,i=0,loop=1000000;
	char *mem1=(char *)malloc(*memory);	//[4]
	char *mem2=(char *)malloc(*memory);	//[4]
	while(i<loop){
		memcpy(mem2,mem1,*memory);	//[6]
		i++;
	}
	free(mem1);
	free(mem2);
}

int blockCheck(int block){
	int m=0;
	if (block > 1024) m = 2000;
	else m = 1000000;
	return m;
}


/*REFERENCES
	[1]. http://stackoverflow.com/questions/10192903/time-in-milliseconds
	[2]. http://www.tutorialspoint.com/c_standard_library/c_function_clock.htm
	[3]. https://computing.llnl.gov/tutorials/pthreads/
	[4]. http://www.codingunit.com/c-tutorial-the-functions-malloc-and-free
	[5]. http://www.java-samples.com/showtutorial.php?tutorialid=591
	[6]. http://www.tutorialspoint.com/c_standard_library/c_function_memcpy.htm
	[7]. http://www.tutorialspoint.com/c_standard_library/c_function_rand.htm
*/
