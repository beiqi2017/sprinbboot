package com.example.demo.controller;

import java.util.Random;  
import java.util.concurrent.Callable;  
import java.util.concurrent.CompletionService;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.ExecutorCompletionService;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.Future;  
  
public class CallableAndFuture {  
  
    public static void main(String[] args) throws InterruptedException, ExecutionException {  
  
          
        ExecutorService threadPool = Executors.newSingleThreadExecutor();  
          
        Future future = threadPool.submit(  
                new Callable<String>(){  
  
                    public String call() throws Exception {  
                        Thread.sleep(2000);  
                        return "Hello";  
                    }  
                      
                }  
        );  
          
        System.out.println("等待结果......");  
          
        try {  
            System.out.println("拿到结果： " + future.get());  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (ExecutionException e) {  
            e.printStackTrace();  
        }  
          
          
        ExecutorService threadPool1 = Executors.newFixedThreadPool(10);  
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool1);  
        for(int i=1; i<=30; i++){  
            final int seq = i;  
            completionService.submit(  
                    new Callable<Integer>(){  
      
                        public Integer call() throws Exception {  
                            if(seq>5){
                            	Thread.sleep(seq*1000);  
                            	return seq*seq;
                            }
                            return seq;  
                        }  
                    }  
            );  
        }  
          
        for(int i=0; i<30; i++){  
            System.out.println(completionService.take().get());  
        }  
        threadPool1.shutdown();
    }  
  
}  
