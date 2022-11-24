package jw.multiThread;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tmp {

    private Long sum;

    public void plus(){
        this.sum++;
    }

    public Tmp() {
        this.sum = 0L;
    }
}
