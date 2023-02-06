package jw.application.batch.chunk.writer;

import jw.application.batch.domain.ApiRequestVO;
import jw.application.batch.domain.ApiResponseVO;
import jw.application.service.AbstractApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
public class ApiItemWriter3 extends FlatFileItemWriter<ApiRequestVO> {

    private AbstractApiService apiService;

    public ApiItemWriter3(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO response = apiService.service(items);

        items.forEach(item -> item.setApiResponseVO(response));

        super.setResource(new FileSystemResource("C:\\Users\\nolan\\OneDrive\\product3.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(items);
    }
}