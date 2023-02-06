# Sprig Batch 학습 및 토이프로젝트

## ＃Version<br>
Spring boot 2.7.5<br>
Spring batch 4.3.7<br>
Quartz<br>
JPA, MySQL


## ＃간단한 토이 프로젝트 개발
- 모든 배치 작업은 스케줄러 및 병렬처리
  1) 날마다 파일로부터 데이터 읽어 Entity 변환 및 DB 저장
  2) 특정 주기로 저장된 데이터를 파일화 및 외부 서버에 REST 통신
     -  Processor: 데이터 타입에 따른 Processor 동작 분류
     -  Writer: 분류된 데이터에 따른 통신할 외부 서버 분류



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
