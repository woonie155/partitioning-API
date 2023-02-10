# Sprig Batch 학습 및 토이프로젝트

## ＃Version<br>
Spring boot 2.7.5<br>
Spring batch 4.3.7<br>
Quartz<br>
JPA, MySQL


## ＃토이 프로젝트
1. File Job 
- 매일 새롭게 생성되는 제품목록 파일을 정해진 시간에 로드하고 DB에 저장한다.
    - 매일 30줄의 데이터가 담긴 csv 파일이 주어진다.
    - **Reader**: 파일을 로드하여 한 줄마다 VO객체로 매핑한다.
    - **Proccessor**: VO객체를 Entity로 변경한다.
    - **Writer**: JPA로 RDB에 저장한다.
2. API Job
- DB에 저장된 제품목록을 병렬처리하며 외부 서버에 전달한다.
    - Partitioning 기능을 통해 병렬처리한다. 제품 유형을 구분해 병렬처리한다.
    - Processor 및 Writer는 Classifier를 이용해 제품 유형에 따라 다른 처리를 한다.
    - **Reader**: VO 객체로 읽는다. 제품 유형에 따라 쓰레드가 할당된다.
    - **Processor**:  제품 유형에 따라 통신할 서버가 다르므로, 통신할 서버에 맞춰 아이템을 재구성한다.
    - **Writer**: 제품 유형에 따라 통신할 서버가 다르다. RestTemplate으로 통신하고, 응답 데이터를 txt 파일로 저장한다.
3. 스케줄링
- Spring Quartz를 이용해 File Job과 API Job을 실행

<!--

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
   - StepScopeTestExecutionListener -->
