package jw.spring_batch.ex_chunk.itemReader.DB.jpa;


import jw.spring_batch.ex_chunk.itemReader.Customer;
import jw.spring_batch.ex_chunk.itemReader.Customer3;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer3> {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Customer3 process(Customer item) throws Exception {
        Customer3 customer3 = modelMapper.map(item, Customer3.class);
        customer3.setFirstName(customer3.getFirstName()+"abcd");
        return customer3;
    }
}
