package jw.study.spring_batch.ex_chunk.itemReader.xmlItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

//@Configuration
public class XStreamConfiguration {

    public XStreamConfiguration(XStreamMarshaller marshaller) {
        XStream xstream = marshaller.getXStream();
        xstream.addPermission(AnyTypePermission.ANY);
    }
}