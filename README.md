## ＃Version<br>
Spring boot 2.7.5<br>
Spring batch 4.3.7


## ＃학습 키워드

1) Spring Batch DB schema 
   - DB schema 이해 및 DB 벤더별 적용

    
2) Job
   - SimpleJob
   - FlowJob(SimpleFlow), JobExecutionDecider
   - 자동 실행
   - 수동 실행 (JobLauncher 및 Runner Custom 구현)
   - JobExecutionListener
   - JobParamterValidator, JobParametersIncrementer, PreventRestart
   - 동기 및 비동기 실행 (SimpleAsyncTaskExecutor)


3) Step
   - TaskletStep (Tasklet 및 ChunkOrientedTasklet)
   - JobStep
   - FlowStep
   - PartitionStep
   - StepExecutionListener
   - JobScope, StepScope
   - 동기 및 비동기 실행 (TaskExecutor)



4) Chunk
   - ChunkOrientedTasklet
   - ItemStream
   - ItemReader (File, XML, Json, DB(Jdbc, JPA) / ItemReaderAdapter)
   - ItemProcessor (CompositeItemProcessor,  ClassifierCompositeItemProcessor)
   - ItemWriter (File, XML, Json, DB(Jdbc, JPA) / ItemWriterAdapter)
   - RepeatTemplete (CompletionPolicy, ExceptionHandler)
   - FaultTolerant (Skip, Retry)
   - Listener(Chunk, ItemReader, ItemWriter, ItemPrecessor, Skip, Retry)


5) 멀티 스레드
   - TaskExecutor (ThreadPoolTaskExecutor)
   - FlowJob (Split API로 병렬 실행)
   - Partitioner ( 다중 StepExecution 생성으로 병렬 처리)
   - SynchronizedItemStreamReader


6) 스케줄러 및 운영 특화 기능
   - Quartz ( Spring boot Quartz )
   - JobOperator
   - JobExplorer
   - JobRegistry


7) Test code

   - JobLauncherTestUtils
   - JobRepositoryTestUtils
   - JobScopeTestExecutionListener
   - StepScopeTestExecutionListener